package tech.nosy.nosyemail.nosyemail.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tech.nosy.nosyemail.nosyemail.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

  @InjectMocks
  EmailService emailServiceListener;

  @Mock
  EmailSenderService emailService;


  private ReadyEmail readyEmail;
  private EmailTemplate emailTemplate;
  private List<PlaceHolder> placeHoldersList=new ArrayList<>();
  @Before
  public void setUp(){
    emailTemplate=new EmailTemplate();
    PlaceHolder placeHolders = new PlaceHolder();
    placeHolders.setName("name");
    placeHolders.setValue("value");
    placeHoldersList.add(placeHolders);
    emailTemplate.setEmailTemplateName("emailTemplateName");
    emailTemplate.setEmailTemplateFromAddress("test@nosy.tech");
    emailTemplate.setEmailTemplateId("emailTemplateId");
    Set<String> emailTemplateToSet=new HashSet<>();
    String emailTemplateTo="nosy@email.to";
    String emailTemplateTo1="test@email.to";
    emailTemplateToSet.add(emailTemplateTo);
    emailTemplateToSet.add(emailTemplateTo1);



    Set<String> emailTemplateCcSet=new HashSet<>();
    String emailTemplateCc="nosy@email.to";
    emailTemplateCcSet.add(emailTemplateCc);



    emailTemplate.setEmailTemplateCc(emailTemplateCcSet);
    emailTemplate.setEmailTemplateTo(emailTemplateToSet);
    emailTemplate.setEmailTemplatePriority(1);
    emailTemplate.setEmailTemplateRetryPeriod(1);
    emailTemplate.setEmailTemplateRetryTimes(1);
    emailTemplate.setEmailTemplateSubject("subject");
    emailTemplate.setEmailTemplateText("text");


  }


  @org.junit.Test(expected = org.junit.Test.None.class)
  public void handleReadyEmail() {
    readyEmail=new ReadyEmail();
    emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
    readyEmail.setEmailTemplate(emailTemplate);
    doNothing().when(emailService).send(any(), any());
    emailServiceListener.handleReadyEmail(readyEmail);
  }

  @org.junit.Test(expected = org.junit.Test.None.class)
  public void handleReadyEmailYandex() {
    readyEmail=new ReadyEmail();
    emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
    readyEmail.setEmailTemplate(emailTemplate);
    emailServiceListener.handleReadyEmail(readyEmail);
  }

  @org.junit.Test(expected = org.junit.Test.None.class)
  public void handleReadyEmailGmail() {
    readyEmail=new ReadyEmail();
    emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.GMAIL);
    readyEmail.setEmailTemplate(emailTemplate);
    doNothing().when(emailService).send(any(), any());
    emailServiceListener.handleReadyEmail(readyEmail);
  }
  @org.junit.Test(expected = Test.None.class)
  public void checkEmailTemplate() {
    EmailProviderProperties emailProviderProperties=new EmailProviderProperties();
    emailProviderProperties.setUsername("username");
    emailProviderProperties.setPassword("password");
    readyEmail=new ReadyEmail();
    readyEmail.setEmailTemplate(emailTemplate);
    emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.GMAIL);

    emailProviderProperties.setPlaceholders(placeHoldersList);

    readyEmail.setEmailProviderProperties(emailProviderProperties);
    assertEquals("emailTemplateId", emailTemplate.getEmailTemplateId());
    assertEquals("emailTemplateName", emailTemplate.getEmailTemplateName());
    assertEquals(1, emailTemplate.getEmailTemplateRetryTimes());
    assertEquals(1, emailTemplate.getEmailTemplateRetryPeriod());
    assertEquals(1, emailTemplate.getEmailTemplatePriority());
    assertEquals("name", emailProviderProperties.getPlaceholders().get(0).getName());
    assertEquals("value", emailProviderProperties.getPlaceholders().get(0).getValue());

  }

}
