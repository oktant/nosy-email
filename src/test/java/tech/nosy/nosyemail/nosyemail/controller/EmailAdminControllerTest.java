package tech.nosy.nosyemail.nosyemail.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import tech.nosy.nosyemail.nosyemail.dto.EmailTemplateDto;
import tech.nosy.nosyemail.nosyemail.dto.FeedDto;
import tech.nosy.nosyemail.nosyemail.dto.InputSystemDto;
import tech.nosy.nosyemail.nosyemail.model.EmailProviderProperties;
import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;
import tech.nosy.nosyemail.nosyemail.model.ReadyEmail;
import tech.nosy.nosyemail.nosyemail.service.EmailTemplateService;
import tech.nosy.nosyemail.nosyemail.service.FeedService;
import tech.nosy.nosyemail.nosyemail.service.InputSystemService;

import java.security.Principal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailAdminControllerTest {

    @InjectMocks
    EmailAdminController emailAdminController;

    @Mock
    EmailTemplateService emailTemplateService;

    @Mock
    InputSystemService inputSystemService;

    @Mock
    FeedService feedService;

    @Test
    public void emailTemplatePost() {
        EmailTemplateDto emailTemplateDto=new EmailTemplateDto();
        emailTemplateDto.setSubject("TestSubject");
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                emailTemplatePost("dasda", "dasdas", null,
                        principal).getStatusCode());
        assertEquals("TestSubject",emailTemplateDto.getSubject());
    }

    @Test
    public void emailPost() {
        ReadyEmail readyEmail = new ReadyEmail();
        readyEmail.setEmailTemplate(new EmailTemplate());
        readyEmail.getEmailTemplate().setEmailTemplateSubject("TestSubject");

        assertEquals(HttpStatus.OK, emailAdminController.emailPost(readyEmail).getStatusCode());
    }

    @Test
    public void newType() {
        InputSystemDto inputSystemDto=new InputSystemDto();
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.CREATED, emailAdminController.newType(inputSystemDto,principal).getStatusCode());

    }

    @Test
    public void getInputSystems() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.getInputSystems(principal).getStatusCode());
    }

    @Test
    public void getEmailAllProviders() {
        Principal principal=mock(Principal.class);
        when(emailTemplateService.getAllEmailProviders()).thenReturn(null);
        assertEquals(HttpStatus.OK, emailAdminController.getEmailAllProviders(principal).getStatusCode());
    }

    @Test
    public void updateInputSystemName() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.updateInputSystemName("inputSystemId", null, principal).getStatusCode());
    }

    @Test
    public void newEmailTemplate() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.CREATED, emailAdminController.
                newEmailTemplate(principal, "inputSystemId",null).getStatusCode());
    }

    @Test
    public void getEmailTemplateByInputSystemAndEmailTemplateId() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                getEmailTemplateByInputSystemAndEmailTemplateId("emailTemplateId","inputSystemId",principal).getStatusCode());
    }

    @Test
    public void updateEmailTemplate() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.OK, emailAdminController.
                updateEmailTemplate( "inputSystemId","emailTemplateId", null, principal)
                .getStatusCode());
    }


    @Test
    public void getEmailTemplates() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.OK, emailAdminController.
                getEmailTemplates("inputSystemId", principal).getStatusCode());
    }

    @Test
    public void deleteEmailTemplate() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.NO_CONTENT, emailAdminController.
                deleteEmailTemplate("inputSystemId", "emailTemplateId",principal).getStatusCode());
    }

    @Test
    public void deleteInputSystem() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.NO_CONTENT, emailAdminController.
                deleteInputSystem("inputSystemId", principal).getStatusCode());
    }

    @Test
    public void newFeed() {
        FeedDto feedDto = new FeedDto();
        feedDto.setId("feedId");
        feedDto.setName("feedName");
        Principal principal = mock(Principal.class);

        assertEquals(HttpStatus.CREATED, emailAdminController.
                newFeed("inputSystemId", feedDto, principal).getStatusCode());
    }

    @Test
    public void updateFeedTest() {
        FeedDto feedDto = new FeedDto();
        Principal principal = mock(Principal.class);

        assertEquals(HttpStatus.OK, emailAdminController
                .updateFeed("inputSystemId", "feedId", feedDto, principal).getStatusCode());
    }

    @Test
    public void deleteFeed() {
        Principal principal = mock(Principal.class);
        assertEquals(HttpStatus.NO_CONTENT, emailAdminController.
                deleteFeed("inputSystemId", "feedId", principal).getStatusCode());
    }

    @Test
    public void subscribeToFeed() {
        Principal principal = mock(Principal.class);

        assertEquals(HttpStatus.OK, emailAdminController.
                subscribeToFeed("inputSystemId", "feedId", principal).getStatusCode());
    }

    @Test
    public void unsubscribeToFeed() {
        Principal principal = mock(Principal.class);
        assertEquals(HttpStatus.NO_CONTENT, emailAdminController.
                unsubscribeToFeed("inputSystemId", "feedId", principal).getStatusCode());
    }

    @Test
    public void addFeedToEmailTemplate() {
        Principal principal = mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                addFeedToEmailTemplate("inputSystemId", "emailTemplateId", "feedId", principal).getStatusCode());
    }

    @Test
    public void getFeeds() {
        Principal principal = mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                getFeeds("inputSystemId", principal).getStatusCode());
    }

    @Test
    public void getFeedByInputSystemIdAndFeedId() {
        assertEquals(HttpStatus.OK, emailAdminController.
                getFeedByInputSystemIdAndFeedId("inputSystemId", "feedId").getStatusCode());
    }

    @Test
    public void postFeed() {
        Principal principal = mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                postFeed("inputSystemId", "feedId", new EmailProviderProperties(), principal).getStatusCode());
    }

}


