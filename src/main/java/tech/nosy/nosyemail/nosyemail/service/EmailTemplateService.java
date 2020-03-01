package tech.nosy.nosyemail.nosyemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.exceptions.*;
import tech.nosy.nosyemail.nosyemail.model.*;
import tech.nosy.nosyemail.nosyemail.repository.EmailConfigRepository;
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
  private EmailConfigService emailConfigService;
  private EmailService emailServiceListener;

  @Value("${default.nosy.from.address}")
  private String defaultNosyFromAddress;

  @Autowired
  public EmailTemplateService(
          EmailTemplateRepository emailTemplateRepository,
          InputSystemRepository inputSystemRepository,
          ReadyEmail readyEmail,
          EmailConfigService emailConfigService,
          EmailService emailServiceListener) {
    this.emailTemplateRepository = emailTemplateRepository;
    this.inputSystemRepository = inputSystemRepository;
    this.readyEmail = readyEmail;
    this.emailServiceListener = emailServiceListener;
    this.emailConfigService=emailConfigService;
  }

  public EmailTemplate getEmailTemplateByName(
      String emailTemplateName, String inputSystemName, String email) {
    InputSystem inputSystem=inputSystemRepository.findInputSystemByEmailAndInputSystemName(email, inputSystemName);
    EmailTemplate emailTemplate =
        emailTemplateRepository.findEmailTemplateByEmailTemplateNameAndInputSystem(
                emailTemplateName, inputSystem);
    if (emailTemplate == null) {
      throw new EmailTemplateNotFoundException();
    }

    return emailTemplate;
  }

  private InputSystem getInputSystemForTemplate(String inputSystemName, String email) {
    InputSystem inputSystem = inputSystemRepository.findInputSystemByEmailAndInputSystemName(email, inputSystemName);
    if (inputSystem == null) {
      throw new InputSystemNotFoundException();
    }
    return inputSystem;
  }

  public EmailTemplate newEmailTemplate(
      EmailTemplate emailTemplate, String inputSystemName, String email, String emailConfigName) {
    InputSystem inputSystem = getInputSystemForTemplate(inputSystemName, email);
    if (emailTemplateRepository.findEmailTemplateByEmailTemplateNameAndInputSystem(
            emailTemplate.getEmailTemplateName(), inputSystem)
        != null) {
      throw new EmailTemplateExistException();
    }
    emailTemplate=setEmailConfig(emailTemplate, email, emailConfigName);
    emailTemplate.setInputSystem(inputSystem);
    emailTemplateRepository.save(emailTemplate);
    return emailTemplate;
  }
  public EmailTemplate setEmailConfig(EmailTemplate emailTemplate, String email, String emailConfigName){
    if (emailTemplate.getEmailTemplateFromProvider().equals(EmailFromProvider.CUSTOM)){
      if (emailConfigName!=null && !emailConfigName.isEmpty()){
          emailTemplate.setEmailConfig(emailConfigService.getConfig(email, emailConfigName));
      } else
        throw new CustomEmailConfigShouldNotBeEmptyException();
    }
    return emailTemplate;
  }

  public List<String> getAllEmailProviders() {
    return Stream.of(EmailFromProvider.values())
        .map(EmailFromProvider::name)
        .collect(Collectors.toList());
  }

  public void deleteEmailTemplate(String inputSystemName, String emailTemplateName, String email) {
    EmailTemplate emailTemplate = getEmailTemplateByName(emailTemplateName, inputSystemName, email);
    emailTemplateRepository.deleteById(emailTemplate.getEmailTemplateId());
  }

  public List<EmailTemplate> getListOfEmailTemplates(String inputSystemName, String email) {
    InputSystem inputSystem = inputSystemRepository.findInputSystemByEmailAndInputSystemName(email, inputSystemName);
    if (inputSystem == null) {
      throw new InputSystemNotFoundException();
    }
    return emailTemplateRepository.findEmailTemplatesByInputSystem(inputSystem);
  }

  public EmailTemplate postEmailTemplate(
      String inputSystemName,
      String emailTemplateName,
      EmailProviderProperties emailProviderProperties,
      String email) {

    ReadyEmail readyEmailCurrent = new ReadyEmail();
    EmailTemplate emailTemplate=getEmailTemplateByName(emailTemplateName, inputSystemName, email);
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
      EmailTemplate emailTemplate, String inputSystemName, String emailTemplateName, String email) {

    EmailTemplate currentEmailTemplate =
        getEmailTemplateByName(emailTemplateName, inputSystemName, email);

    if (emailTemplate==null ) {

      throw new EmailTemplateNotFoundException();
    }
    if (emailTemplate.getEmailTemplateName() == null
        || emailTemplate.getEmailTemplateName().isEmpty() ||  (
                !currentEmailTemplate.getEmailTemplateName().equals(emailTemplate.getEmailTemplateName())
            && emailTemplateRepository.findEmailTemplateByEmailTemplateNameAndInputSystem(
            emailTemplate.getEmailTemplateName(), emailTemplate.getInputSystem())
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
