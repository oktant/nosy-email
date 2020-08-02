package tech.nosy.nosyemail.nosyemail.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import tech.nosy.nosyemail.nosyemail.dto.EmailConfigDto;
import tech.nosy.nosyemail.nosyemail.dto.EmailTemplateDto;
import tech.nosy.nosyemail.nosyemail.dto.InputSystemDto;
import tech.nosy.nosyemail.nosyemail.model.EmailConfig;
import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;
import tech.nosy.nosyemail.nosyemail.model.ReadyEmail;
import tech.nosy.nosyemail.nosyemail.service.EmailConfigService;
import tech.nosy.nosyemail.nosyemail.service.EmailTemplateService;
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
    EmailConfigService emailConfigService;

    @Test
    public void emailTemplatePostTest() {
        EmailTemplateDto emailTemplateDto=new EmailTemplateDto();
        emailTemplateDto.setSubject("TestSubject");
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                emailTemplatePost("dasda", "dasdas", null,
                        principal).getStatusCode());
        assertEquals("TestSubject",emailTemplateDto.getSubject());
    }

    @Test
    public void emailPostTest() {
        ReadyEmail readyEmail = new ReadyEmail();
        readyEmail.setEmailTemplate(new EmailTemplate());
        readyEmail.getEmailTemplate().setEmailTemplateSubject("TestSubject");

        assertEquals(HttpStatus.OK, emailAdminController.emailPost(readyEmail).getStatusCode());
    }

    @Test
    public void newTypeTest() {
        InputSystemDto inputSystemDto=new InputSystemDto();
        inputSystemDto.setName("dasda");
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.CREATED, emailAdminController.newType(inputSystemDto,principal).getStatusCode());

    }

    @Test
    public void getInputSystemsTest() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.getInputSystems("", principal).getStatusCode());
    }

    @Test
    public void getEmailAllProvidersTest() {
        Principal principal=mock(Principal.class);
        when(emailTemplateService.getAllEmailProviders()).thenReturn(null);
        assertEquals(HttpStatus.OK, emailAdminController.getEmailAllProviders().getStatusCode());
    }

    @Test
    public void newEmailTemplateTest() {

        EmailTemplateDto emailTemplate=new EmailTemplateDto();
        emailTemplate.setConfigName("dasd");

        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.CREATED, emailAdminController.
                newEmailTemplate(principal, "inputSystemId",
                        emailTemplate).getStatusCode());
    }
    @Test
    public void getEmailTemplateByInputSystemAndEmailTemplateIdTest() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.
                getEmailTemplateByInputSystemAndEmailTemplateName("emailTemplateId","inputSystemId",principal).getStatusCode());
    }

    @Test
    public void updateEmailTemplateTest() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.OK, emailAdminController.
                updateEmailTemplate( "inputSystemId","emailTemplateId", null, principal)
                .getStatusCode());
    }


    @Test
    public void getEmailTemplatesTest() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.OK, emailAdminController.
                getEmailTemplates("inputSystemId", principal).getStatusCode());
    }

    @Test
    public void deleteEmailTemplateTest() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.NO_CONTENT, emailAdminController.
                deleteEmailTemplate("inputSystemId", "emailTemplateId",principal).getStatusCode());
    }

    @Test
    public void deleteInputSystemTest() {
        Principal principal=mock(Principal.class);

        assertEquals(HttpStatus.NO_CONTENT, emailAdminController.
                deleteInputSystem("inputSystemId", principal).getStatusCode());
    }


    @Test
    public void getConfigs() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.OK, emailAdminController.getConfigs(principal).getStatusCode());
    }

    @Test
    public void getConfigTest() {
        Principal principal=mock(Principal.class);
        String name= "configName";
        assertEquals(HttpStatus.OK, emailAdminController.getConfig(principal,name).getStatusCode());

    }

    @Test
    public void updateConfigTest() {
        Principal principal=mock(Principal.class);
        String name= "configName";
        assertEquals(HttpStatus.OK, emailAdminController.getConfig(principal,name).getStatusCode());
    }

    @Test
    public void postConfigTest() {
        Principal principal=mock(Principal.class);
        EmailConfigDto emailConfigDto=new EmailConfigDto();
        emailConfigDto.setHost("dsfsd");
        emailConfigDto.setPort(22);
        emailConfigDto.setName("dadas");
        assertEquals(HttpStatus.CREATED,
                emailAdminController.postEmailConfig(principal, emailConfigDto).getStatusCode());
    }
}


