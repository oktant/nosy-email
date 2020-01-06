package tech.nosy.nosyemail.nosyemail.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tech.nosy.nosyemail.nosyemail.exceptions.InputSystemHasChildrenException;
import tech.nosy.nosyemail.nosyemail.exceptions.InputSystemNameIsMandatoryException;
import tech.nosy.nosyemail.nosyemail.exceptions.InputSystemNotFoundException;
import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;
import tech.nosy.nosyemail.nosyemail.model.InputSystem;
import tech.nosy.nosyemail.nosyemail.repository.InputSystemRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test(expected = Test.None.class)
    public void deleteInputSystem(){
        when(inputSystemRepository.findInputSystemByEmailAndInputSystemName(anyString(),anyString())).thenReturn(
                inputSystem);

        inputSystemServiceMock.deleteInputSystem(inputSystem.getInputSystemId(), email);
    }

    @Test
    public void getListOfInputSystemsTest() {
        assertEquals(new HashSet<InputSystemService>(), inputSystemServiceMock.getListOfInputSystems("",email));
    }
    @Test
    public void getListOfInputSystemsTestEmpty() {
        assertEquals(new HashSet<InputSystemService>(), inputSystemServiceMock.getListOfInputSystems(null,email));
    }

    @Test
    public void getListOfInputSystemsTestNotEmpty() {

        when(inputSystemRepository.findInputSystemByEmailAndInputSystemName(anyString(), anyString()))
                .thenReturn(inputSystem);
        Set<InputSystem> actual= inputSystemServiceMock.getListOfInputSystems("dasda",email);
        Set<InputSystem> expectedInputSystems=new HashSet<>();
        expectedInputSystems.add(inputSystem);
        assertEquals(expectedInputSystems.toArray()[0], actual.toArray()[0]);
    }

    @Test(expected = InputSystemNotFoundException.class)
    public void deleteInputSystemTest() {

        String email="test@nosy.tech";
        InputSystem inputSystem=new InputSystem();
        inputSystem.setInputSystemId("inputSystemId");
        inputSystem.setInputSystemName("inputSystemName");
        inputSystemServiceMock.deleteInputSystem(inputSystem.getInputSystemId(), email);
    }

    @Test(expected = InputSystemNotFoundException.class)
    public void deleteInputSystemEmailTemplateIsEmptyIsNotNullTest() {
        String email="test@nosy.tech";
        InputSystem inputSystem=new InputSystem();
        inputSystem.setInputSystemId("inputSystemId");
        inputSystem.setInputSystemName("inputSystemName");
        Set<EmailTemplate> emailTemplates=new HashSet<>();

        inputSystem.setEmailTemplate(emailTemplates);
        inputSystemServiceMock.deleteInputSystem(inputSystem.getInputSystemId(), email);

    }

    @Test(expected = InputSystemNotFoundException.class)
    public void deleteInputSystemNotFoundTest() {
        String email="test@nosy.tech";
        InputSystem inputSystem=new InputSystem();
        inputSystem.setInputSystemId("inputSystemId");
        inputSystem.setInputSystemName("inputSystemName");
        inputSystem.setEmail(email);
        inputSystemServiceMock.deleteInputSystem(inputSystem.getInputSystemName(), email);

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
        when(inputSystemRepository.findInputSystemByEmailAndInputSystemName(email, inputSystem.getInputSystemName())).
                thenReturn(inputSystem);
        inputSystemServiceMock.deleteInputSystem(inputSystem.getInputSystemName(), email);

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
