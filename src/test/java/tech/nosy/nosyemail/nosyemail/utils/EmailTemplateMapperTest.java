package tech.nosy.nosyemail.nosyemail.utils;


import org.junit.Before;
import org.junit.Test;
import tech.nosy.nosyemail.nosyemail.dto.EmailTemplateDto;
import tech.nosy.nosyemail.nosyemail.model.EmailFromProvider;
import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;
import tech.nosy.nosyemail.nosyemail.model.InputSystem;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;


public class EmailTemplateMapperTest {
    private EmailTemplateDto emailTemplateDto=new EmailTemplateDto();
    private EmailTemplate emailTemplate=new EmailTemplate();

    @Before
    public void setUp(){
        emailTemplate.setEmailTemplateName("EmailTemplateName");
        emailTemplate.setEmailTemplateText("EmailTemplateText");
        emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
        InputSystem inputSystem=new InputSystem();
        inputSystem.setInputSystemName("inputSystem");
        emailTemplate.setInputSystem(inputSystem);
        emailTemplate.setEmailTemplateFromAddress("test@nosy.tech");
        emailTemplate.setEmailTemplatePriority(1);
        emailTemplate.setEmailTemplateRetryPeriod(1);
        emailTemplate.setEmailTemplateSubject("EmailTemplateSubject");
        emailTemplate.setEmailTemplateRetryTimes(1);
        String emailTemplateTo="emailTemplateTo";
        String emailTemplateCc="emailTemplateCc";
        Set<String> emailTemplateToSet=new HashSet<>();
        Set<String> emailTemplateCcSet=new HashSet<>();
        emailTemplateCcSet.add(emailTemplateCc);
        emailTemplateToSet.add(emailTemplateTo);
        emailTemplate.setEmailTemplateTo(emailTemplateToSet);
        emailTemplate.setEmailTemplateCc(emailTemplateCcSet);


        emailTemplateDto.setName("EmailTemplateDtoName");
        emailTemplateDto.setText("EmailTemplateDtoText");
        emailTemplateDto.setFromProvider(EmailFromProvider.DEFAULT);
        emailTemplateDto.setFromAddress("testDto@nosy.tech");
        emailTemplateDto.setPriority(1);
        emailTemplateDto.setRetryPeriod(1);
        emailTemplateDto.setSubject("EmailTemplateSubjectDto");
        emailTemplateDto.setRetryPeriod(1);
        emailTemplateDto.setCc(emailTemplateCcSet);


    }
    @Test
    public void toEmailTemplateDtoTest(){

        assertEquals(emailTemplate.getEmailTemplateFromProvider(), EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate).getFromProvider());
        assertEquals(emailTemplate.getEmailTemplateCc(), EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate).getCc());
        assertEquals(emailTemplate.getEmailTemplateTo(), EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate).getTo());
        assertEquals(emailTemplate.getEmailTemplateName(), EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate).getName());
        assertEquals(emailTemplate.getEmailTemplateSubject(), EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate).getSubject());
        assertEquals(emailTemplate.getEmailTemplateFromAddress(), EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate).getFromAddress());
        assertEquals(emailTemplate.getEmailTemplatePriority(), EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate).getPriority());
        assertEquals(emailTemplate.getEmailTemplateRetryPeriod(), EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate).getRetryPeriod());
        assertEquals(emailTemplate.getEmailTemplateRetryTimes(), EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate).getRetryTimes());
        assertEquals(emailTemplate.getEmailTemplateText(), EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplate).getText());


    }

    @Test
    public void toEmailTemplateTest(){
        assertEquals(emailTemplateDto.getFromProvider(), EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto).getEmailTemplateFromProvider());
        assertEquals(emailTemplateDto.getCc(), EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto).getEmailTemplateCc());
        assertEquals(emailTemplateDto.getTo(), EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto).getEmailTemplateTo());
        assertEquals(emailTemplateDto.getName(), EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto).getEmailTemplateName());
        assertEquals(emailTemplateDto.getFromAddress(), EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto).getEmailTemplateFromAddress());
        assertEquals(emailTemplateDto.getPriority(), EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto).getEmailTemplatePriority());
        assertEquals(emailTemplateDto.getRetryPeriod(), EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto).getEmailTemplateRetryPeriod());
        assertEquals(emailTemplateDto.getRetryTimes(), EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto).getEmailTemplateRetryTimes());
        assertEquals(emailTemplateDto.getText(), EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto).getEmailTemplateText());
        assertEquals(emailTemplateDto.getSubject(), EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto).getEmailTemplateSubject());
    }

}
