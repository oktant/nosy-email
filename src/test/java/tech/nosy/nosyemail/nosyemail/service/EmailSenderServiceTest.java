package tech.nosy.nosyemail.nosyemail.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;
import tech.nosy.nosyemail.nosyemail.config.EmailConfigs;
import tech.nosy.nosyemail.nosyemail.model.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailSenderServiceTest {

    @InjectMocks
    private EmailSenderService emailSenderService;


    private ReadyEmail readyEmail;
    @Mock
    EmailConfigs emailConfigs;

    @Before
    public void setUp() {
        readyEmail = new ReadyEmail();
        PlaceHolder placeHolders = new PlaceHolder();
        placeHolders.setName("name");
        placeHolders.setValue("value");
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setEmailTemplateName("emailTemplateName");
        emailTemplate.setEmailTemplateFromAddress("test@nosy.tech");
        emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
        emailTemplate.setEmailTemplateId("emailTemplateId");
        emailTemplate.setEmailTemplateRetryTimes(2);

        Set<String> emailTemplateToSet = new HashSet<>();
        String emailTemplateTo = "nosyTo@email.to";
        String emailTemplateTo1 = "nosyTo1@email.to";

        emailTemplateToSet.add(emailTemplateTo);
        emailTemplateToSet.add(emailTemplateTo1);


        Set<String> emailTemplateCcSet = new HashSet<>();
        String emailTemplateCc = "nosyCc@email.to";
        String emailTemplateCc1 = "nosyCc1@email.to";

        emailTemplateCcSet.add(emailTemplateCc);
        emailTemplateCcSet.add(emailTemplateCc1);


        emailTemplate.setEmailTemplateCc(emailTemplateCcSet);
        emailTemplate.setEmailTemplateTo(emailTemplateToSet);


        emailTemplate.setEmailTemplatePriority(1);
        emailTemplate.setEmailTemplateRetryPeriod(1);
        emailTemplate.setEmailTemplateRetryTimes(1);
        emailTemplate.setEmailTemplateSubject("subject");
        emailTemplate.setEmailTemplateText("text");
        emailTemplate.setEmailTemplatePriority(1);
        readyEmail.setEmailTemplate(emailTemplate);
        ReflectionTestUtils.setField(emailSenderService, "emailDefaultUsername", "asdasd");
        ReflectionTestUtils.setField(emailSenderService, "emailDefaultPassword", "asdasd");

    }


    @Test(expected = org.junit.Test.None.class)
    public void send() throws MessagingException {
        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setPassword("dadad");
        emailProviderProperties.setUsername("dadasdas");
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(javaMailSender).send(mimeMessage);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);

        doNothing().when(mimeMessageHelper).setFrom(anyString());
        emailSenderService.send(readyEmail, javaMailSender);


    }

    @Test(expected = org.junit.Test.None.class)
    public void NonDefault() throws MessagingException {
        readyEmail.getEmailTemplate().setEmailTemplateFromProvider(EmailFromProvider.YANDEX);

        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setPassword("dadad");
        emailProviderProperties.setUsername("dadasdas");
        readyEmail.setEmailProviderProperties(emailProviderProperties);

        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(javaMailSender).send(mimeMessage);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);

        doNothing().when(mimeMessageHelper).setFrom(anyString());
        emailSenderService.send(readyEmail, javaMailSender);
    }

    @Test(expected = IllegalArgumentException.class)
    public void WithCcNull() throws MessagingException {
        readyEmail.getEmailTemplate().setEmailTemplateFromProvider(EmailFromProvider.YANDEX);

        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setPassword("dadad");
        emailProviderProperties.setUsername("dadasdas");
        readyEmail.setEmailProviderProperties(emailProviderProperties);
        Set<String> ccSet = new HashSet<>();
        ccSet.add(null);
        readyEmail.getEmailTemplate().setEmailTemplateCc(ccSet);
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);

        doThrow(IllegalArgumentException.class).when(mimeMessageHelper).setFrom(anyString());
        emailSenderService.send(readyEmail, javaMailSender);
    }

    @Test(expected = IllegalArgumentException.class)
    public void WithToNull() throws MessagingException {
        readyEmail.getEmailTemplate().setEmailTemplateFromProvider(EmailFromProvider.YANDEX);

        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setPassword("dadad");
        emailProviderProperties.setUsername("dadasdas");
        readyEmail.setEmailProviderProperties(emailProviderProperties);
        Set<String> toSet = new HashSet<>();
        toSet.add(null);
        readyEmail.getEmailTemplate().setEmailTemplateTo(toSet);
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);

        doThrow(IllegalArgumentException.class).when(mimeMessageHelper).setFrom(anyString());
        emailSenderService.send(readyEmail, javaMailSender);


    }

    @Test(expected = org.junit.Test.None.class)
    public void WithCcEmpty() throws MessagingException {
        readyEmail.getEmailTemplate().setEmailTemplateFromProvider(EmailFromProvider.YANDEX);

        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setPassword("dadad");
        emailProviderProperties.setUsername("dadasdas");
        readyEmail.setEmailProviderProperties(emailProviderProperties);
        Set<String> ccSet = new HashSet<>();
        readyEmail.getEmailTemplate().setEmailTemplateCc(ccSet);
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);

        doNothing().when(mimeMessageHelper).setFrom(anyString());
        emailSenderService.send(readyEmail, javaMailSender);
    }

    @Test(expected = IllegalArgumentException.class)
    public void FromAddressNull() throws MessagingException {
        readyEmail.getEmailTemplate().setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        readyEmail.getEmailTemplate().setEmailTemplateFromAddress(null);
        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setPassword("dadad");
        emailProviderProperties.setUsername("dadasdas");
        readyEmail.setEmailProviderProperties(emailProviderProperties);
        Set<String> toSet = new HashSet<>();
        toSet.add(null);
        readyEmail.getEmailTemplate().setEmailTemplateTo(toSet);
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);

        doThrow(IllegalArgumentException.class).when(mimeMessageHelper).setFrom(anyString());
        emailSenderService.send(readyEmail, javaMailSender);


    }

    @Test(expected = Test.None.class)
    public void messagingException() {
        readyEmail.getEmailTemplate().setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setPassword("dadad");
        emailProviderProperties.setUsername("dadasdas");
        readyEmail.setEmailProviderProperties(emailProviderProperties);
        Set<String> toSet = new HashSet<>();
        toSet.add("dadasda");
        readyEmail.getEmailTemplate().setEmailTemplateTo(toSet);
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);
        try {
            doThrow(MessagingException.class).when(mimeMessageHelper).setFrom(anyString());
            emailSenderService.send(readyEmail, javaMailSender);

        } catch (MessagingException e) {
            throw new RuntimeException();
        }


    }

    @Test(expected = Test.None.class)
    public void messagingExceptionFromAddToMethod() {
        readyEmail.getEmailTemplate().setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setPassword("dadad");
        emailProviderProperties.setUsername("dadasdas");
        readyEmail.setEmailProviderProperties(emailProviderProperties);
        Set<String> toSet = new HashSet<>();
        toSet.add("dadasda");
        readyEmail.getEmailTemplate().setEmailTemplateTo(toSet);
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);

        try {
            doNothing().when(mimeMessageHelper).setFrom(anyString());

            doThrow(MessagingException.class).when(mimeMessageHelper).addTo(anyString());

            emailSenderService.send(readyEmail, javaMailSender);

        } catch (MessagingException e) {
            throw new RuntimeException();
        }


    }

    @Test(expected = Test.None.class)
    public void messagingExceptionFromAddCcMethod() {
        readyEmail.getEmailTemplate().setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setPassword("dadad");
        emailProviderProperties.setUsername("dadasdas");
        readyEmail.setEmailProviderProperties(emailProviderProperties);
        Set<String> toSet = new HashSet<>();
        toSet.add("dadasda");
        readyEmail.getEmailTemplate().setEmailTemplateTo(toSet);
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);

        try {
            doNothing().when(mimeMessageHelper).setFrom(anyString());

            doThrow(MessagingException.class).when(mimeMessageHelper).addCc(anyString());

            emailSenderService.send(readyEmail, javaMailSender);

        } catch (MessagingException e) {
            throw new RuntimeException();
        }


    }


    @Test(expected = Test.None.class)
    public void testWithEmptyEmailProviderProperties() {
        readyEmail.getEmailTemplate().setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        readyEmail.setEmailProviderProperties(null);
        Set<String> toSet = new HashSet<>();
        toSet.add("dadasda");
        readyEmail.getEmailTemplate().setEmailTemplateTo(toSet);
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);

        try {
            doNothing().when(mimeMessageHelper).setFrom(anyString());

            doThrow(MessagingException.class).when(mimeMessageHelper).addCc(anyString());

            emailSenderService.send(readyEmail, javaMailSender);

        } catch (MessagingException e) {
            throw new RuntimeException();
        }

    }

    @Test(expected = Test.None.class)
    public void testWithNullUsernameEmailProviderProperties() {
        readyEmail.getEmailTemplate().setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);
        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setPassword("dadad");
        emailProviderProperties.setUsername(null);
        readyEmail.setEmailProviderProperties(emailProviderProperties);
        Set<String> toSet = new HashSet<>();
        toSet.add("dadasda");
        readyEmail.getEmailTemplate().setEmailTemplateTo(toSet);
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);

        try {
            doNothing().when(mimeMessageHelper).setFrom(anyString());

            doThrow(MessagingException.class).when(mimeMessageHelper).addCc(anyString());

            emailSenderService.send(readyEmail, javaMailSender);

        } catch (MessagingException e) {
            throw new RuntimeException();
        }

    }

    @Test(expected = Test.None.class)
    public void testWithNullPasswordEmailProviderProperties() {
        readyEmail.getEmailTemplate().setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);
        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setPassword(null);
        emailProviderProperties.setUsername("dasdasd");
        readyEmail.setEmailProviderProperties(emailProviderProperties);
        Set<String> toSet = new HashSet<>();
        toSet.add("dadasda");
        readyEmail.getEmailTemplate().setEmailTemplateTo(toSet);
        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(emailConfigs.mimeMessageHelper(mimeMessage)).thenReturn(mimeMessageHelper);

        try {
            doNothing().when(mimeMessageHelper).setFrom(anyString());

            doThrow(MessagingException.class).when(mimeMessageHelper).addCc(anyString());

            emailSenderService.send(readyEmail, javaMailSender);

        } catch (MessagingException e) {
            throw new RuntimeException();
        }

    }
}
