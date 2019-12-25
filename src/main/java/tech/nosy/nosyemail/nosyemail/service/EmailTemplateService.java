package tech.nosy.nosyemail.nosyemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.exceptions.*;
import tech.nosy.nosyemail.nosyemail.model.*;
import tech.nosy.nosyemail.nosyemail.repository.EmailTemplateRepository;
import tech.nosy.nosyemail.nosyemail.repository.InputSystemRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmailTemplateService {

  private EmailTemplateRepository emailTemplateRepository;
  private InputSystemRepository inputSystemRepository;
  private ReadyEmail readyEmail;
  private EmailService emailServiceListener;

  @Value("${default.nosy.from.address}")
  private String defaultNosyFromAddress;

  @Autowired
  public EmailTemplateService(
          EmailTemplateRepository emailTemplateRepository,
          InputSystemRepository inputSystemRepository,
          ReadyEmail readyEmail,
          EmailService emailServiceListener) {
    this.emailTemplateRepository = emailTemplateRepository;
    this.inputSystemRepository = inputSystemRepository;
    this.readyEmail = readyEmail;
    this.emailServiceListener = emailServiceListener;
  }

  public EmailTemplate getEmailTemplateById(
      String emailTemplateId, String inputSystemId, String email) {
    getInputSystemForTemplate(inputSystemId, email);
    EmailTemplate emailTemplate =
        emailTemplateRepository.findEmailTemplatesByInputSystemIdAndEmailTemplateId(
            inputSystemId, emailTemplateId);
    if (emailTemplate == null) {
      throw new EmailTemplateNotFoundException();
    }

    return emailTemplate;
  }

  private InputSystem getInputSystemForTemplate(String inputSystemId, String email) {
    InputSystem inputSystem = inputSystemRepository.findByInputSystemIdAndEmail(email, inputSystemId);
    if (inputSystem == null) {
      throw new InputSystemNotFoundException();
    }
    return inputSystem;
  }

  public EmailTemplate newEmailTemplate(
      EmailTemplate emailTemplate, String inputSystemId, String email) {
    InputSystem inputSystem = getInputSystemForTemplate(inputSystemId, email);
    if (emailTemplateRepository.findEmailTemplateByEmailTemplateNameAndInputSystemId(
            emailTemplate.getEmailTemplateName(), inputSystemId)
        != null) {
      throw new EmailTemplateExistException();
    }

    emailTemplate.setInputSystem(inputSystem);
    emailTemplateRepository.save(emailTemplate);

    return emailTemplate;
  }

  public List<String> getAllEmailProviders() {
    return Stream.of(EmailFromProvider.values())
        .map(EmailFromProvider::name)
        .collect(Collectors.toList());
  }

  public void deleteEmailTemplate(String inputSystemId, String emailTemplateId, String email) {
    EmailTemplate emailTemplate = getEmailTemplateById(emailTemplateId, inputSystemId, email);
    emailTemplateRepository.deleteById(emailTemplate.getEmailTemplateId());
  }

  public List<EmailTemplate> getListOfEmailTemplates(String inputSystemId, String email) {

    InputSystem inputSystem = inputSystemRepository.findByInputSystemIdAndEmail(email, inputSystemId);
    if (inputSystem == null) {
      throw new InputSystemNotFoundException();
    }

    return emailTemplateRepository.findEmailTemplatesByInputSystemId(inputSystemId);
  }

  public EmailTemplate postEmailTemplate(
      String inputSystemId,
      String emailTemplateId,
      EmailProviderProperties emailProviderProperties,
      String email) {

    ReadyEmail readyEmailCurrent = new ReadyEmail();
    EmailTemplate emailTemplate=getEmailTemplateById(emailTemplateId, inputSystemId, email);
    if(emailProviderProperties!=null && emailProviderProperties.getTo()!=null && !emailProviderProperties.getTo().isEmpty()){
      if(emailTemplate.getEmailTemplateTo()==null){
        emailTemplate.setEmailTemplateTo(new HashSet<String>());
      }
      emailTemplate.getEmailTemplateTo().addAll(emailProviderProperties.getTo());
    }
    readyEmailCurrent.setEmailTemplate(emailTemplate);
    readyEmailCurrent.setEmailProviderProperties(emailProviderProperties);

    return postEmail(readyEmailCurrent);
  }

  public EmailTemplate postEmail(ReadyEmail readyEmailDto) {
    EmailProviderProperties emailProviderProperties = readyEmailDto.getEmailProviderProperties();
    EmailTemplate emailTemplate = readyEmailDto.getEmailTemplate();

    boolean auth =
            (emailProviderProperties.getUsername() == null
                    || emailProviderProperties.getUsername().equals("")
                    || emailProviderProperties.getPassword() == null
                    || emailProviderProperties.getPassword().equals(""));

    if (!emailTemplate.getEmailTemplateFromProvider().equals(EmailFromProvider.DEFAULT) && auth) {
      throw new UsernameAndPasswordAreNotProvidedForNonDefaultException();
    }

    String text = emailTemplate.getEmailTemplateText();
    if(emailProviderProperties.getPlaceholders()!=null) {
      for (PlaceHolder placeholder : emailProviderProperties.getPlaceholders()) {
        text = text.replace("#{" + placeholder.getName() + "}#", placeholder.getValue());
      }

      if (text.contains("#{") || text.contains("}#")) {
        throw new NotEnoughParametersForPlaceholdersException();
      }
    }
    emailTemplate.setEmailTemplateText(text);
    readyEmail.setEmailTemplate(emailTemplate);
    readyEmail.setEmailProviderProperties(emailProviderProperties);

    emailServiceListener.handleReadyEmail(readyEmail);

    return emailTemplate;
  }

  public EmailTemplate updateEmailTemplate(
      EmailTemplate emailTemplate, String inputSystemId, String emailTemplateId, String email) {

    EmailTemplate currentEmailTemplate =
        getEmailTemplateById(emailTemplateId, inputSystemId, email);

    if (emailTemplate==null ) {

      throw new EmailTemplateNotFoundException();
    }
    if (emailTemplate.getEmailTemplateName() == null
        || emailTemplate.getEmailTemplateName().isEmpty() ||  (!currentEmailTemplate.getEmailTemplateName().equals(emailTemplate.getEmailTemplateName())
            && emailTemplateRepository.findEmailTemplateByEmailTemplateNameAndInputSystemId(
            emailTemplate.getEmailTemplateName(), inputSystemId)
            != null)) {
      throw new EmailTemplateNameInvalidException();
    }

    if (emailTemplate.getEmailTemplateFromAddress() == null || emailTemplate.getEmailTemplateFromAddress().isEmpty()) {

      currentEmailTemplate.setEmailTemplateFromAddress(defaultNosyFromAddress);
    }

    currentEmailTemplate.setEmailTemplateName(emailTemplate.getEmailTemplateName());

    currentEmailTemplate.setEmailTemplateTo(emailTemplate.getEmailTemplateTo());
    currentEmailTemplate.setEmailTemplateFromAddress(emailTemplate.getEmailTemplateFromAddress());
    currentEmailTemplate.setEmailTemplateText(emailTemplate.getEmailTemplateText());
    currentEmailTemplate.setEmailTemplateFromProvider(emailTemplate.getEmailTemplateFromProvider());
    currentEmailTemplate.setEmailTemplateCc(emailTemplate.getEmailTemplateCc());
    currentEmailTemplate.setEmailTemplatePriority(emailTemplate.getEmailTemplatePriority());
    currentEmailTemplate.setEmailTemplateRetryPeriod(emailTemplate.getEmailTemplateRetryPeriod());
    currentEmailTemplate.setEmailTemplateRetryTimes(emailTemplate.getEmailTemplateRetryTimes());
    currentEmailTemplate.setEmailTemplateSubject(emailTemplate.getEmailTemplateSubject());
    emailTemplateRepository.save(currentEmailTemplate);
    return currentEmailTemplate;
  }
}
