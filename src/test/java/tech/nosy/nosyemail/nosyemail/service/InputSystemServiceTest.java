package tech.nosy.nosyemail.nosyemail.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tech.nosy.nosyemail.nosyemail.exceptions.*;
import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;
import tech.nosy.nosyemail.nosyemail.model.InputSystem;
import tech.nosy.nosyemail.nosyemail.repository.InputSystemRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InputSystemServiceTest {
    @InjectMocks
    private InputSystemService inputSystemServiceMock;
    @Mock
    private InputSystemRepository inputSystemRepository;


    private String email;
    private InputSystem inputSystem;
    private Set<InputSystem> inputSystemList=new HashSet<>();

    @Before
    public void setUp(){
        email="test@nosy.tech";

        inputSystem=new InputSystem();
        inputSystem.setInputSystemId("inputSystemId");
        inputSystem.setInputSystemName("inputSystemName");
        inputSystemList.add(inputSystem);
    }
    //Fixme

//    @Test
//    public void getListOfInputSystemsTest() {
//        assertEquals(new HashSet<InputSystemService>(), inputSystemServiceMock.getListOfInputSystems(email));
//    }

    @Test(expected = Test.None.class)
    public void deleteInputSystemTest() {

        String email="test@nosy.tech";
        InputSystem inputSystem=new InputSystem();
        inputSystem.setInputSystemId("inputSystemId");
        inputSystem.setInputSystemName("inputSystemName");
        when(inputSystemRepository.findInputSystemByInputSystemIdAndEmail(inputSystem.getInputSystemId(), email)).
                thenReturn(inputSystem);
        inputSystemServiceMock.deleteInputSystem(inputSystem.getInputSystemId(), email);
    }

    @Test(expected = Test.None.class)
    public void deleteInputSystemEmailTemplateIsEmptyIsNotNullTest() {
        String email="test@nosy.tech";
        InputSystem inputSystem=new InputSystem();
        inputSystem.setInputSystemId("inputSystemId");
        inputSystem.setInputSystemName("inputSystemName");
        Set<EmailTemplate> emailTemplates=new HashSet<>();

        inputSystem.setEmailTemplate(emailTemplates);
        when(inputSystemRepository.findInputSystemByInputSystemIdAndEmail(inputSystem.getInputSystemId(), email)).
                thenReturn(inputSystem);
        inputSystemServiceMock.deleteInputSystem(inputSystem.getInputSystemId(), email);

    }

    @Test(expected = InputSystemNotFoundException.class)
    public void deleteInputSystemNotFoundTest() {
        String email="test@nosy.tech";
        InputSystem inputSystem=new InputSystem();
        inputSystem.setInputSystemId("inputSystemId");
        inputSystem.setInputSystemName("inputSystemName");
        inputSystem.setEmail(email);
        when(inputSystemRepository.findInputSystemByInputSystemIdAndEmail(inputSystem.getInputSystemId(), email)).
                thenReturn(null);
        inputSystemServiceMock.deleteInputSystem(inputSystem.getInputSystemId(), email);

    }

    @Test(expected = InputSystemHasChildrenException.class)
    public void deleteInputSystemEmailTemplateIsNotEmptyIsNotNullTest() {
        String email="test@nosy.tech";
        InputSystem inputSystem=new InputSystem();
        inputSystem.setInputSystemId("inputSystemId");
        inputSystem.setInputSystemName("inputSystemName");
        Set<EmailTemplate> emailTemplates=new HashSet<>();
        EmailTemplate emailTemplate=new EmailTemplate();
        emailTemplates.add(emailTemplate);
        inputSystem.setEmailTemplate(emailTemplates);
        when(inputSystemRepository.findInputSystemByInputSystemIdAndEmail(inputSystem.getInputSystemId(), email)).
                thenReturn(inputSystem);
        inputSystemServiceMock.deleteInputSystem(inputSystem.getInputSystemId(), email);

    }


    @Test(expected = InputSystemNameIsMandatoryException.class)
    public void saveInputSystemWithNullEmailTemplateTest() {
        InputSystem inputSystem=new InputSystem();
        String email="test@nosy.tech";
        inputSystemServiceMock.saveInputSystem(inputSystem, email);
    }

    @Test(expected = InputSystemNameIsMandatoryException.class)
    public void saveInputSystemEmptyEmailTemplateTest() {
        InputSystem inputSystem=new InputSystem();
        inputSystem.setInputSystemName(" ");
        String email="test@nosy.tech";
        inputSystemServiceMock.saveInputSystem(inputSystem, email);
    }

//    @Test(expected = InputSystemAlreadyExistsException.class)
//    public void saveInputSystemWithValidInputSystemNameAlreadyExistsTest() {
//        String email="test@nosy.tech";
//        InputSystem inputSystem=new InputSystem();
//        inputSystem.setInputSystemName("inputSystemName");
//        when(inputSystemRepository.findByInputSystemNameAndEmail(email,inputSystem.getInputSystemName())).
//                thenReturn(inputSystem);
//        inputSystemServiceMock.saveInputSystem(inputSystem, email);
//    }

    @Test
    public void saveInputSystemWithValidInputSystemNameButUserExistsTest() {
        String email="test@nosy.tech";
        InputSystem inputSystem=new InputSystem();
        inputSystem.setInputSystemName("inputSystemName");
        inputSystem.setEmail(email);
        when(inputSystemRepository.save(inputSystem)).thenReturn(inputSystem);
        assertEquals(email, inputSystem.getEmail());
        Assert.assertEquals(inputSystem.getInputSystemName(),inputSystemServiceMock.saveInputSystem(inputSystem, email).getInputSystemName());
    }
}
