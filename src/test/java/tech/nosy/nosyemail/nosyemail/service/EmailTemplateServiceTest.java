package tech.nosy.nosyemail.nosyemail.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tech.nosy.nosyemail.nosyemail.exceptions.*;
import tech.nosy.nosyemail.nosyemail.model.*;
import tech.nosy.nosyemail.nosyemail.repository.EmailTemplateRepository;
import tech.nosy.nosyemail.nosyemail.repository.FeedRepository;
import tech.nosy.nosyemail.nosyemail.repository.InputSystemRepository;
import tech.nosy.nosyemail.nosyemail.repository.UserRepository;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailTemplateServiceTest {

    @InjectMocks
    private EmailTemplateService emailTemplateServiceMock;
    @Mock
    private EmailTemplateRepository emailTemplateRepositoryMock;
    @Mock
    private UserRepository userRepository;
    @Mock
    private InputSystemRepository inputSystemRepository;
    @Mock
    private FeedRepository feedRepository;
    @Mock
    EmailService emailService;

    @Mock
    ReadyEmail readyEmail;
    private EmailProviderProperties emailProviderProperties;
    private String emailTemplateId;
    private String inputSystemId;
    private String email;
    private EmailTemplate emailTemplate;
    private User user;
    private InputSystem inputSystem;
    private Feed feed;

    private void setVariables(){
        emailTemplateId="emailTemplateId";

        inputSystemId="inputSystemId";
        email="test@nosy.tech";
        emailTemplate=new EmailTemplate();
        emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
        Set<String> emailsCc=new HashSet<>();
        emailsCc.add("testCc@nosy.tech");
        emailTemplate.setEmailTemplateCc(emailsCc);
        Set<String> emailsTo=new HashSet<>();
        emailsTo.add("testTo@nosy.tech");
        emailTemplate.setEmailTemplateId(emailTemplateId);
        emailTemplate.setEmailTemplateTo(emailsTo);
        emailTemplate.setEmailTemplateText("Test Message");
        emailTemplate.setEmailTemplateSubject("Test Subject");
        emailTemplate.setEmailTemplateName("Test Email Template Name");
        emailTemplate.setEmailTemplateRetryPeriod(1);
        emailTemplate.setEmailTemplatePriority(1);
        emailTemplate.setEmailTemplateFromAddress("testFromAddress@nosy.tech");

        feed = new Feed();
        feed.setFeedId("feedId");
        feed.setFeedName("feedName");
        feed.setEmailTemplate(emailTemplate);
        feed.setInputSystem(inputSystem);
        feed.setFeedSubscribers(Collections.singleton("feedSubscriber"));

        inputSystem=new InputSystem();
        user=new User();
        user.setEmail(email);
        user.setFirstName("Test");
        user.setLastName("Nosy");
        user.setPassword("dajsndjasn");
        inputSystem.setInputSystemName("testInputSystem");
        inputSystem.setInputSystemId(inputSystemId);
        inputSystem.setUser(user);
        emailTemplate.setInputSystem(inputSystem);
        emailTemplate.setFeeds(Collections.emptySet());

        readyEmail=new ReadyEmail();
        readyEmail.setEmailTemplate(emailTemplate);
        emailProviderProperties=new EmailProviderProperties();
        emailProviderProperties.setPassword("TestPassword");
        List<PlaceHolder> placeHolders=new ArrayList<>();
        emailProviderProperties.setPlaceholders(placeHolders);
        emailProviderProperties.setUsername("Test");

        readyEmail.setEmailProviderProperties(emailProviderProperties);
    }

    @Before
    public void beforeEmailTemplate(){

        setVariables();

    }
    @Test(expected= InputSystemNotFoundException.class)
    public void getEmailTemplateByIdGeneralException() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        Assert.assertEquals(emailTemplateServiceMock.getEmailTemplateById(inputSystemId, emailTemplateId, email).getEmailTemplateId(),
                emailTemplateId);



    }
    @Test
    public void getEmailTemplateById() {
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());


        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        Assert.assertEquals(emailTemplateId, emailTemplateServiceMock.getEmailTemplateById(inputSystemId, emailTemplateId, email).getEmailTemplateId()
        );
        String emailTemplateString="EmailTemplate{emailTemplateId='emailTemplateId', emailTemplateName='Test Email Template Name', emailTemplateFromAddress='testFromAddress@nosy.tech', emailTemplateFromProvider=DEFAULT, emailTemplateTo=[testTo@nosy.tech], emailTemplateCc=[testCc@nosy.tech], emailTemplateText='Test Message', emailTemplateRetryTimes=0, emailTemplateRetryPeriod=1, emailTemplatePriority=1, emailTemplateSubject='Test Subject'}";
        assertEquals(emailTemplateString, emailTemplate.toString());

    }
    @Test(expected= EmailTemplateNotFoundException.class)
    public void getEmailTemplateByIdError() {
        doReturn(null).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());


        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        Assert.assertEquals(emailTemplateId, emailTemplateServiceMock.getEmailTemplateById(inputSystemId, emailTemplateId, email).getEmailTemplateId()
        );
        String emailTemplateString="EmailTemplate{emailTemplateId='emailTemplateId', emailTemplateName='Test Email Template Name', fromAddress='testFromAddress@nosy.tech', emailTemplateTo=[testTo@nosy.tech], emailTemplateCc=[testCc@nosy.tech], text='Test Message', retryTimes=0, retryPeriod=1, priority=1, subject='Test Subject'}";
        assertEquals(emailTemplateString, emailTemplate.toString());

    }

    @Test
    public void newEmailTemplate() {

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).save(any());
        Assert.assertEquals(emailTemplateId, emailTemplateServiceMock.newEmailTemplate(emailTemplate, inputSystemId, email).getEmailTemplateId());

    }
    @Test(expected = NotAuthenticatedException.class)
    public void newEmailCheckUserReturnsNull() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        Assert.assertEquals(emailTemplateId, emailTemplateServiceMock.newEmailTemplate(emailTemplate, inputSystemId, email).getEmailTemplateId());

    }

    @Test(expected = EmailTemplateExistException.class)
    public void newEmailTemplateEmailTemplateExists() {

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplateByEmailTemplateNameAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(emailTemplateId, emailTemplateServiceMock.newEmailTemplate(emailTemplate, inputSystemId, email).getEmailTemplateId());

    }
    @Test
    public void getAllEmailProviders() {
        ArrayList<String> providers=new ArrayList<>();
        providers.add("DEFAULT");
        providers.add("YANDEX");
        providers.add("GMAIL");

        assertEquals(providers, emailTemplateServiceMock.getAllEmailProviders());
    }

    @Test
    public void deleteEmailTemplate() {
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        emailTemplateServiceMock.deleteEmailTemplate(inputSystemId, emailTemplateId, email);
        assertNotNull(inputSystemId);
    }

    @Test
    public void getListOfEmailTemplates() {
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        List<EmailTemplate> listOfEmailTemplates=new ArrayList<>();
        listOfEmailTemplates.add(emailTemplate);
        when(emailTemplateRepositoryMock.findEmailTemplatesByInputSystemId(inputSystemId)).thenReturn(listOfEmailTemplates);
        assertEquals(listOfEmailTemplates,emailTemplateServiceMock.getListOfEmailTemplates(inputSystemId, email));
        assertEquals("testInputSystem", emailTemplate.getInputSystem().getInputSystemName());
        assertEquals("Test", emailProviderProperties.getUsername());

    }

    @Test(expected = InputSystemNotFoundException.class)
    public void getListOfEmailTemplatesInputSystemNotFound() {
        doReturn(null).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        emailTemplateServiceMock.getListOfEmailTemplates(inputSystemId, email);
    }

    @Test
    public void getListEmailTemplatesListNull() {
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(emailTemplateRepositoryMock.findEmailTemplatesByInputSystemId(inputSystemId)).thenReturn(null);
        assertNull(emailTemplateServiceMock.getListOfEmailTemplates(inputSystemId, email));
    }
    @Test
    public void getListEmailTemplatesListEmpty() {
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        List<EmailTemplate> listOfEmailTemplates=new ArrayList<>();
        when(emailTemplateRepositoryMock.findEmailTemplatesByInputSystemId(inputSystemId)).thenReturn(listOfEmailTemplates);
        assertEquals(listOfEmailTemplates,emailTemplateServiceMock.getListOfEmailTemplates(inputSystemId, email));
    }

//    @Test
//    public void postEmailTemplate() {
//        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
//        when(userRepository.findById(email)).thenReturn(Optional.of(user));
//        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
//                (anyString(), anyString());
//        assertEquals(emailTemplateId, emailTemplateServiceMock.postEmailTemplate(inputSystemId, emailTemplateId, emailProviderProperties, email).getEmailTemplateId());
//        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
//        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());
//
//    }

    @Test
    public void postEmail() {
        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());
    }

    @Test(expected = UsernameAndPasswordAreNotProvidedForNonDefaultException.class)
    public void postEmailTemplateNonDefaultWithoutPassword() {

        EmailTemplate emailTemplateForYandex=new EmailTemplate();
        emailTemplateForYandex.setEmailTemplateName("Test");
        emailTemplateForYandex.setEmailTemplateFromProvider(EmailFromProvider.YANDEX);

        EmailProviderProperties emailProviderPropertiesTest=new EmailProviderProperties();
        emailProviderPropertiesTest.setUsername("tett");
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplateForYandex).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());

        emailTemplateServiceMock.postEmailTemplate(inputSystemId, emailTemplateId, emailProviderPropertiesTest, email);
        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());

    }
    @Test(expected = UsernameAndPasswordAreNotProvidedForNonDefaultException.class)
    public void postEmailTemplateNonDefaultWithoutUsername() {

        EmailTemplate emailTemplateForYandex=new EmailTemplate();
        emailTemplateForYandex.setEmailTemplateName("Test");
        emailTemplateForYandex.setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        List<String> emailTo=new ArrayList<>();
        emailTo.add("oktay@gmail.com");
        EmailProviderProperties emailProviderPropertiesTest=new EmailProviderProperties();
        emailProviderPropertiesTest.setPassword("dasdadsadad");
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplateForYandex).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());


        emailTemplateServiceMock.postEmailTemplate(inputSystemId, emailTemplateId, emailProviderPropertiesTest, email);
        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());

    }

    @Test(expected = UsernameAndPasswordAreNotProvidedForNonDefaultException.class)
    public void postEmailTemplateNonDefaultWithoutPasswordWithoutUsername() {

        EmailTemplate emailTemplateForYandex=new EmailTemplate();
        emailTemplateForYandex.setEmailTemplateName("Test");
        emailTemplateForYandex.setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        EmailProviderProperties emailProviderPropertiesTest=new EmailProviderProperties();
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplateForYandex).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());


        emailTemplateServiceMock.postEmailTemplate(inputSystemId, emailTemplateId, emailProviderPropertiesTest, email);
        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());

    }
    @Test(expected = UsernameAndPasswordAreNotProvidedForNonDefaultException.class)
    public void postEmailTemplateNonDefaultWithPasswordNull() {

        EmailTemplate emailTemplateForYandex=new EmailTemplate();
        emailTemplateForYandex.setEmailTemplateName("Test");

        emailTemplateForYandex.setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        EmailProviderProperties emailProviderPropertiesTest=new EmailProviderProperties();
        emailProviderPropertiesTest.setPassword("");
        emailProviderPropertiesTest.setUsername("dafsaf");

        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplateForYandex).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());


        Assert.assertEquals(emailTemplateId, emailTemplateServiceMock.postEmailTemplate(inputSystemId, emailTemplateId, emailProviderPropertiesTest, email).getEmailTemplateId());
        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());
    }

    @Test(expected = UsernameAndPasswordAreNotProvidedForNonDefaultException.class)
    public void postEmailTemplateNonDefaultWithEmptyUsername() {

        EmailTemplate emailTemplateForYandex=new EmailTemplate();
        emailTemplateForYandex.setEmailTemplateName("Test");

        emailTemplateForYandex.setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        EmailProviderProperties emailProviderPropertiesTest=new EmailProviderProperties();
        emailProviderPropertiesTest.setPassword("dafsaf");
        emailProviderPropertiesTest.setUsername("");

        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplateForYandex).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());


        emailTemplateServiceMock.postEmailTemplate(inputSystemId, emailTemplateId, emailProviderPropertiesTest, email);
        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());

    }


    @Test
    public void postEmailTemplateNonDefaultWithUsernameAndPassword() {
        EmailTemplate emailTemplateForYandex=new EmailTemplate();
        emailTemplateForYandex.setEmailTemplateName("Test");
        emailTemplateForYandex.setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        EmailProviderProperties emailProviderPropertiesTest=new EmailProviderProperties();
        emailProviderPropertiesTest.setPassword("dafsaf");
        emailProviderPropertiesTest.setUsername("fasdfad");
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplateForYandex).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        emailTemplateServiceMock.postEmailTemplate(inputSystemId, emailTemplateId, emailProviderPropertiesTest, email);
        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());
    }


    @Test
    public void postEmailTemplateNonDefaultWithParameterProvider() {
        EmailTemplate emailTemplateForYandex=new EmailTemplate();
        emailTemplateForYandex.setEmailTemplateName("Test");
        emailTemplateForYandex.setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        EmailProviderProperties emailProviderPropertiesTest=new EmailProviderProperties();
        emailProviderPropertiesTest.setPassword("dafsaf");
        emailProviderPropertiesTest.setUsername("fasdfad");
        PlaceHolder placeHolder=new PlaceHolder();

        emailTemplateForYandex.setEmailTemplateText("Hi #{name}#, #{body}#");
        placeHolder.setName("name");
        placeHolder.setValue("Testoktay");
        PlaceHolder placeHolder1=new PlaceHolder();
        placeHolder1.setName("body");
        placeHolder1.setValue("Hi");
        List<PlaceHolder> placeholdersList=new ArrayList<>();
        placeholdersList.add(placeHolder);
        placeholdersList.add(placeHolder1);
        emailProviderPropertiesTest.setPlaceholders(placeholdersList);
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplateForYandex).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());

        Assert.assertEquals("Hi Testoktay, Hi", emailTemplateServiceMock.postEmailTemplate(inputSystemId, emailTemplateId,
                emailProviderPropertiesTest, email).getEmailTemplateText());

        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());
    }

    @Test(expected = NotEnoughParametersForPlaceholdersException.class)
    public void postEmailTemplateNonDefaultWithParameterProviderButNotEnoughReplacements() {
        EmailTemplate emailTemplateForYandex=new EmailTemplate();
        emailTemplateForYandex.setEmailTemplateName("Test");
        emailTemplateForYandex.setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        EmailProviderProperties emailProviderPropertiesTest=new EmailProviderProperties();
        emailProviderPropertiesTest.setPassword("dafsaf");
        emailProviderPropertiesTest.setUsername("fasdfad");
        PlaceHolder placeHolder=new PlaceHolder();

        emailTemplateForYandex.setEmailTemplateText("Hi #{name}# #{test}#, #{body}#");
        placeHolder.setName("name");
        placeHolder.setValue("Testoktay");
        PlaceHolder placeHolder1=new PlaceHolder();
        placeHolder1.setName("body");
        placeHolder1.setValue("Hi");
        List<PlaceHolder> placeholdersList=new ArrayList<>();
        placeholdersList.add(placeHolder);
        placeholdersList.add(placeHolder1);
        emailProviderPropertiesTest.setPlaceholders(placeholdersList);
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplateForYandex).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        emailTemplateServiceMock.postEmailTemplate(inputSystemId, emailTemplateId, emailProviderPropertiesTest, email);

        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());
    }


    @Test(expected = NotEnoughParametersForPlaceholdersException.class)
    public void postEmailTemplateNonDefaultWithParameterProviderButNotEnoughReplacementsClosingTag() {
        EmailTemplate emailTemplateForYandex=new EmailTemplate();
        emailTemplateForYandex.setEmailTemplateName("Test");
        emailTemplateForYandex.setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        EmailProviderProperties emailProviderPropertiesTest=new EmailProviderProperties();
        emailProviderPropertiesTest.setPassword("dafsaf");
        emailProviderPropertiesTest.setUsername("fasdfad");
        PlaceHolder placeHolder=new PlaceHolder();

        emailTemplateForYandex.setEmailTemplateText("Hi #{name}# }#, #{body}#");
        placeHolder.setName("name");
        placeHolder.setValue("Testoktay");
        PlaceHolder placeHolder1=new PlaceHolder();
        placeHolder1.setName("body");
        placeHolder1.setValue("Hi");
        List<PlaceHolder> placeholdersList=new ArrayList<>();
        placeholdersList.add(placeHolder);
        placeholdersList.add(placeHolder1);
        emailProviderPropertiesTest.setPlaceholders(placeholdersList);
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplateForYandex).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        emailTemplateServiceMock.postEmailTemplate(inputSystemId, emailTemplateId, emailProviderPropertiesTest, email);

        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());
    }
    @Test(expected = UsernameAndPasswordAreNotProvidedForNonDefaultException.class)
    public void postEmailTemplateNonDefaultWithUsernameNull() {

        EmailTemplate emailTemplateForYandex=new EmailTemplate();
        emailTemplateForYandex.setEmailTemplateName("Test");

        emailTemplateForYandex.setEmailTemplateFromProvider(EmailFromProvider.YANDEX);
        EmailProviderProperties emailProviderPropertiesTest=new EmailProviderProperties();
        emailProviderPropertiesTest.setPassword("");

        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplateForYandex).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());


        emailTemplateServiceMock.postEmailTemplate(inputSystemId, emailTemplateId, emailProviderPropertiesTest, email);
        assertEquals("Test", readyEmail.getEmailProviderProperties().getUsername());
        assertEquals("Test Email Template Name", readyEmail.getEmailTemplate().getEmailTemplateName());

    }



    @Test
    public void updateEmailTemplate() {
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        Assert.assertEquals(emailTemplate,emailTemplateServiceMock.updateEmailTemplate(emailTemplate, inputSystemId, emailTemplateId, email));
    }
    @Test
    public void updateEmailTemplateEmptyTemplateFromAddress() {
        emailTemplate.setEmailTemplateFromAddress("");

        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        Assert.assertEquals(emailTemplate,emailTemplateServiceMock.updateEmailTemplate(emailTemplate, inputSystemId, emailTemplateId, email));
    }
    @Test
    public void updateEmailTemplateNullTemplateFromAddress() {
        emailTemplate.setEmailTemplateFromAddress(null);

        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        Assert.assertEquals(emailTemplate,emailTemplateServiceMock.updateEmailTemplate(emailTemplate, inputSystemId, emailTemplateId, email));
    }


    @Test(expected = EmailTemplateNameInvalidException.class)
    public void updateEmailTemplateNullTemplateName() {
        emailTemplate.setEmailTemplateName(null);
        emailTemplate.setEmailTemplateFromAddress(null);

        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        Assert.assertEquals(emailTemplate,emailTemplateServiceMock.updateEmailTemplate(emailTemplate, inputSystemId, emailTemplateId, email));
    }

    @Test(expected = EmailTemplateNameInvalidException.class)
    public void updateEmailTemplateEmptyTemplateName() {
        emailTemplate.setEmailTemplateName("");
        emailTemplate.setEmailTemplateFromAddress(null);

        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        Assert.assertEquals(emailTemplate,emailTemplateServiceMock.updateEmailTemplate(emailTemplate, inputSystemId, emailTemplateId, email));
    }
    @Test
    public void updateEmailTemplateNotEqualsToCurrent() {
        EmailTemplate emailTemplate1=new EmailTemplate();

        emailTemplate1.setEmailTemplateName("dadasdas");

        String currentEmailTemplateId="dasd";
        EmailTemplate currentEmailTemplate=new EmailTemplate();
        currentEmailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
        Set<String> emailsCc=new HashSet<>();
        emailsCc.add("testCc@nosy.tech");
        currentEmailTemplate.setEmailTemplateCc(emailsCc);
        Set<String> emailsTo=new HashSet<>();
        emailsTo.add("testTo@nosy.tech");
        currentEmailTemplate.setEmailTemplateId(currentEmailTemplateId);
        currentEmailTemplate.setEmailTemplateTo(emailsTo);
        currentEmailTemplate.setEmailTemplateText("Test Message");
        currentEmailTemplate.setEmailTemplateSubject("Test Subject");
        currentEmailTemplate.setEmailTemplateName("TestName");
        currentEmailTemplate.setEmailTemplateRetryPeriod(1);
        currentEmailTemplate.setEmailTemplatePriority(1);
        currentEmailTemplate.setEmailTemplateFromAddress("testFromAddress@nosy.tech");
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(currentEmailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        Assert.assertEquals(emailTemplate1.getEmailTemplateName(),emailTemplateServiceMock.
                updateEmailTemplate(emailTemplate1, inputSystemId, emailTemplateId, email).getEmailTemplateName());
    }
    @Test(expected = EmailTemplateNameInvalidException.class)
    public void updateEmailTemplateCurrentIsEmpty() {
        EmailTemplate emailTemplate1=new EmailTemplate();
        emailTemplate.setEmailTemplateName("");
        String currentEmailTemplateId="dasd";
        EmailTemplate currentEmailTemplate=new EmailTemplate();
        currentEmailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
        Set<String> emailsCc=new HashSet<>();
        emailsCc.add("testCc@nosy.tech");
        currentEmailTemplate.setEmailTemplateCc(emailsCc);
        Set<String> emailsTo=new HashSet<>();
        emailsTo.add("testTo@nosy.tech");
        currentEmailTemplate.setEmailTemplateId(currentEmailTemplateId);
        currentEmailTemplate.setEmailTemplateTo(emailsTo);
        currentEmailTemplate.setEmailTemplateText("Test Message");
        currentEmailTemplate.setEmailTemplateSubject("Test Subject");
        currentEmailTemplate.setEmailTemplateName("TestName");
        currentEmailTemplate.setEmailTemplateRetryPeriod(1);
        currentEmailTemplate.setEmailTemplatePriority(1);
        currentEmailTemplate.setEmailTemplateFromAddress("testFromAddress@nosy.tech");
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(currentEmailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        Assert.assertEquals(emailTemplate,emailTemplateServiceMock.updateEmailTemplate(emailTemplate1, inputSystemId, emailTemplateId, email));
    }
    @Test(expected = EmailTemplateNotFoundException.class)
    public void updateEmailTemplateNull() {
        EmailTemplate emailTemplate1=null;

        String currentEmailTemplateId="dasd";
        EmailTemplate currentEmailTemplate=new EmailTemplate();
        currentEmailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
        Set<String> emailsCc=new HashSet<>();
        emailsCc.add("testCc@nosy.tech");
        currentEmailTemplate.setEmailTemplateCc(emailsCc);
        Set<String> emailsTo=new HashSet<>();
        emailsTo.add("testTo@nosy.tech");
        currentEmailTemplate.setEmailTemplateId(currentEmailTemplateId);
        currentEmailTemplate.setEmailTemplateTo(emailsTo);
        currentEmailTemplate.setEmailTemplateText("Test Message");
        currentEmailTemplate.setEmailTemplateSubject("Test Subject");
        currentEmailTemplate.setEmailTemplateName("TestName");
        currentEmailTemplate.setEmailTemplateRetryPeriod(1);
        currentEmailTemplate.setEmailTemplatePriority(1);
        currentEmailTemplate.setEmailTemplateFromAddress("testFromAddress@nosy.tech");
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(currentEmailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId
                (anyString(), anyString());
        Assert.assertEquals(emailTemplate,emailTemplateServiceMock.updateEmailTemplate(emailTemplate1, inputSystemId, emailTemplateId, email));
    }

    @Test
    public void addFeedToEmailTemplate() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId(anyString(), anyString());
        doReturn(feed).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(emailTemplate.getEmailTemplateId(), emailTemplateServiceMock.addFeedToEmailTemplate(inputSystemId, emailTemplateId, "feedId", email).getEmailTemplateId());
    }

    @Test(expected = FeedNotFoundException.class)
    public void addFeedToEmailTemplateFeedNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId(anyString(), anyString());
        doReturn(null).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(emailTemplate.getEmailTemplateId(), emailTemplateServiceMock.addFeedToEmailTemplate(inputSystemId, emailTemplateId, "feedId", email).getEmailTemplateId());
    }

    @Test(expected = FeedExistException.class)
    public void AddFeedToEmailTemplateFeedExsists() {
        emailTemplate.setFeeds(Collections.singleton(feed));

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(emailTemplate).when(emailTemplateRepositoryMock).findEmailTemplatesByInputSystemIdAndEmailTemplateId(anyString(), anyString());
        doReturn(feed).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(emailTemplate.getEmailTemplateId(), emailTemplateServiceMock.addFeedToEmailTemplate(inputSystemId, emailTemplateId, feed.getFeedId(), email).getEmailTemplateId());
    }

}
