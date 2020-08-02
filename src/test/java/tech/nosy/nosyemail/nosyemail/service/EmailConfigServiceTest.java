package tech.nosy.nosyemail.nosyemail.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tech.nosy.nosyemail.nosyemail.exceptions.CustomEmailConfigNotExists;
import tech.nosy.nosyemail.nosyemail.model.EmailConfig;
import tech.nosy.nosyemail.nosyemail.repository.EmailConfigRepository;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class EmailConfigServiceTest {
    EmailConfig emailConfig;

    @InjectMocks
    private EmailConfigService emailConfigService;

    @Mock
    private EmailConfigRepository emailConfigRepository;

    @Before
    public void setUp() {
        emailConfig = new EmailConfig();
        emailConfig.setEmail("oktay@gmail.com");
        emailConfig.setEmailConfigName("dasdasd");
        emailConfig.setHost("dasda");
        emailConfig.setPort(3);
        emailConfig.setEmailConfigId("dasfds");
    }

    @Test(expected = Test.None.class)
    public void saveEmailConfigTest() {
        String email = "oktay@gmail.com";
        emailConfigService.saveEmailConfig(email, emailConfig);
    }

    @Test(expected = Test.None.class)
    public void getConfigsTest() {
        String email = "oktay@gmail.com";
        emailConfigService.getConfigs(email);
    }

    @Test(expected = CustomEmailConfigNotExists.class)
    public void getConfigCustomExceptionTest() {
        String email = "oktay@gmail.com";
        String configName= "configName";
        emailConfigService.getConfig(email, configName);
    }

    @Test(expected = Test.None.class)
    public void getConfigTest() {
        String email = "oktay@gmail.com";
        String configName= "configName";
        when(emailConfigRepository.findAllByEmailConfigNameAndEmail(anyString(),anyString()))
                .thenReturn(emailConfig);
        emailConfigService.getConfig(email, configName);
    }


    @Test(expected = Test.None.class)
    public void updateConfigTest() {
        String email = "oktay@gmail.com";
        String configName= "configName";
        when(emailConfigRepository.findAllByEmailConfigNameAndEmail(anyString(),anyString()))
                .thenReturn(emailConfig);
        emailConfigService.updateConfig(email, configName, emailConfig);

    }
    @Test(expected = CustomEmailConfigNotExists.class)
    public void updateConfigNullTest() {
        String email = "oktay@gmail.com";
        String configName= "configName";
        emailConfigService.updateConfig(email, configName, emailConfig);

    }
}