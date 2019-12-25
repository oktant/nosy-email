//package tech.nosy.nosyemail.nosyemail.utils;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import tech.nosy.nosyemail.nosyemail.dto.InputSystemDto;
//import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;
//import tech.nosy.nosyemail.nosyemail.model.InputSystem;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.junit.Assert.assertEquals;
//
//public class InputSystemMapperTest {
//    private InputSystemDto inputSystemDto=new InputSystemDto();
//    private InputSystem inputSystem=new InputSystem();
//    @Before
//    public void setUp(){
//        InputSystem inputSystem=new InputSystem();
//        inputSystem.setInputSystemId("inputSystemId");
//        inputSystem.setInputSystemName("inputSystemName");
//        User user=new User();
//        user.setEmail("testUser");
//        inputSystem.setUser(user);
//        inputSystem.setInputSystemName("inputSystemName");
//        EmailTemplate emailTemplate=new EmailTemplate();
//        emailTemplate.setEmailTemplateName("dadasd");
//        Set<EmailTemplate> emailTemplateSet=new HashSet<>();
//        emailTemplateSet.add(emailTemplate);
//        inputSystem.setInputSystemId("dadasdsa");
//        inputSystem.setEmailTemplate(emailTemplateSet);
//
//        InputSystemDto inputSystemDto=new InputSystemDto();
//        inputSystemDto.setId("inputSystemDtoId");
//        inputSystemDto.setName("inputSystemDtoName");
//    }
//
//
//    @Test
//    public void toInputSystemDto(){
//        Assert.assertEquals(inputSystem.getInputSystemId(),InputSystemMapper.INSTANCE.toInputSystemDto(inputSystem).getId());
//        Assert.assertEquals(inputSystem.getInputSystemName(),InputSystemMapper.INSTANCE.toInputSystemDto(inputSystem).getName());
//    }
//
//    @Test
//    public void toInputSystem(){
//        Assert.assertEquals(inputSystemDto.getId(),InputSystemMapper.INSTANCE.toInputSystem(inputSystemDto).getInputSystemId());
//        Assert.assertEquals(inputSystemDto.getName(),InputSystemMapper.INSTANCE.toInputSystem(inputSystemDto).getInputSystemName());
//    }
//}
