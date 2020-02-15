package tech.nosy.nosyemail.nosyemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.exceptions.*;
import tech.nosy.nosyemail.nosyemail.model.*;
import tech.nosy.nosyemail.nosyemail.repository.EmailCredentialRepository;
import tech.nosy.nosyemail.nosyemail.repository.EmailTemplateRepository;
import tech.nosy.nosyemail.nosyemail.repository.InputSystemRepository;

import javax.validation.constraints.Email;
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
  private EmailCredentialRepository emailCredentialRepository;

  @Value("${default.nosy.from.address}")
  private String defaultNosyFromAddress;

  @Autowired
  public EmailTemplateService(
          EmailTemplateRepository emailTemplateRepository,
          InputSystemRepository inputSystemRepository,
          ReadyEmail readyEmail,
          EmailService emailServiceListener,
          EmailCredentialRepository emailCredentialRepository) {
    this.emailTemplateRepository = emailTemplateRepository;
    this.inputSystemRepository = inputSystemRepository;
    this.readyEmail = readyEmail;
    this.emailServiceListener = emailServiceListener;
    this.emailCredentialRepository=emailCredentialRepository;
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
      EmailTemplate emailTemplate, String inputSystemName, String email) {
    InputSystem inputSystem = getInputSystemForTemplate(inputSystemName, email);
    if (emailTemplateRepository.findEmailTemplateByEmailTemplateNameAndInputSystem(
            emailTemplate.getEmailTemplateName(), inputSystem)
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
    if(emailProviderProperties!=null && emailProviderProperties.getTo()!=null && !emailProviderProperties
            .getTo().isEmpty()){
      if(emailTemplate.getEmailTemplateTo()==null){
        emailTemplate.setEmailTemplateTo(new HashSet<String>());
      }
      emailTemplate.getEmailTemplateTo().addAll(emailProviderProperties.getTo());
    }

    readyEmailCurrent.setEmailTemplate(emailTemplate);
    readyEmailCurrent.setEmailProviderProperties(retrieveProfileFromEmailCredentials(emailProviderProperties, email));

    return postEmail(readyEmailCurrent);
  }

  public EmailProviderProperties retrieveProfileFromEmailCredentials(EmailProviderProperties providerProperties, String email){
    if (providerProperties!=null && providerProperties.getProfile()!=null
            && !providerProperties.getProfile().isEmpty()){
      EmailCredential emailCredential=emailCredentialRepository.findAllByEmailCredentialProfileNameAndEmail(providerProperties.getProfile(), email);
      if (emailCredential!=null){
        providerProperties.setUsername(emailCredential.getEmailCredentialUsername());
          providerProperties.setPassword(emailCredential.getEmailCredentialPassword());
      }
    }
    return providerProperties;
  }

  public EmailTemplate postEmail(ReadyEmail readyEmail) {
    EmailProviderProperties emailProviderProperties = readyEmail.getEmailProviderProperties();
    EmailTemplate emailTemplate = readyEmail.getEmailTemplate();



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
