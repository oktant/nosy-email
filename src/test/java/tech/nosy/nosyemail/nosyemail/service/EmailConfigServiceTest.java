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
    public void saveEmailConfig() {
        String email = "oktay@gmail.com";
        emailConfigService.saveEmailConfig(email, emailConfig);
    }

    @Test(expected = Test.None.class)
    public void getConfigs() {
        String email = "oktay@gmail.com";
        emailConfigService.getConfigs(email);
    }

    @Test(expected = CustomEmailConfigNotExists.class)
    public void getConfigCustomException() {
        String email = "oktay@gmail.com";
        String configName= "configName";
        emailConfigService.getConfig(email, configName);
    }

    @Test(expected = Test.None.class)
    public void getConfig() {
        String email = "oktay@gmail.com";
        String configName= "configName";
        when(emailConfigRepository.findAllByEmailConfigNameAndEmail(anyString(),anyString()))
                .thenReturn(emailConfig);
        emailConfigService.getConfig(email, configName);
    }

    //
//    @Test
//    void updateConfig() {
//    }
}