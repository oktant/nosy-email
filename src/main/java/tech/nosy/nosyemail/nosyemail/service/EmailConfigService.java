package tech.nosy.nosyemail.nosyemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.model.EmailConfig;
import tech.nosy.nosyemail.nosyemail.repository.EmailConfigRepository;

import java.util.List;

@Service
public class EmailConfigService {

    private EmailConfigRepository emailConfigRepository;

    @Autowired
    public EmailConfigService(EmailConfigRepository emailConfigRepository){
        this.emailConfigRepository=emailConfigRepository;
    }
    public EmailConfig saveEmailConfig(String email, EmailConfig emailConfig){
        emailConfig.setEmail(email);
        return emailConfigRepository.save(emailConfig);
    }
    public List<EmailConfig> getConfigs(String email){
        return emailConfigRepository.findAllByEmail(email);
    }
    public EmailConfig getConfig(String email, String configName){
        return emailConfigRepository.findAllByEmailConfigNameAndEmail(configName, email);
    }
    public EmailConfig updateConfig(String email, String name, EmailConfig emailConfig){
        EmailConfig emailConfigUpdate =
                emailConfigRepository.findAllByEmailConfigNameAndEmail(name, email);
        emailConfigUpdate.setEmailConfigName(name);
        emailConfigUpdate.setEmail(email);
        emailConfigUpdate.setEmailConfigs(emailConfig.getEmailConfigs());
        return emailConfigRepository.save(emailConfigUpdate);
    }


}
