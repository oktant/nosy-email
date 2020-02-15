package tech.nosy.nosyemail.nosyemail.service;

import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.exceptions.EmailCredentialNotExist;
import tech.nosy.nosyemail.nosyemail.exceptions.EmailCredentialProfileAlreadyExistException;
import tech.nosy.nosyemail.nosyemail.model.EmailCredential;
import tech.nosy.nosyemail.nosyemail.repository.EmailCredentialRepository;
import java.util.Set;

@Service
public class EmailCredentialService {
    private EmailCredentialRepository emailCredentialRepository;

    public EmailCredentialService(EmailCredentialRepository emailCredentialRepository){
        this.emailCredentialRepository=emailCredentialRepository;
    }

    public EmailCredential saveEmailCredentials(EmailCredential emailCredential, String nosyRegistrationEmail){

        if(emailCredentialRepository.
                findAllByEmailCredentialProfileNameAndEmail(emailCredential.getEmailCredentialProfileName(), nosyRegistrationEmail)
        !=null)
            throw new EmailCredentialProfileAlreadyExistException();

        emailCredential.setEmail(nosyRegistrationEmail);
        return emailCredentialRepository.save(emailCredential);
    }
    public Set<EmailCredential> getEmailCredentials(String email){
        return emailCredentialRepository.findAllByEmail(email);
    }

    public EmailCredential getEmailCredential(String profileName, String email){
        EmailCredential emailCredential=emailCredentialRepository.
                findAllByEmailCredentialProfileNameAndEmail(profileName, email);
        if(emailCredential==null){
            throw new EmailCredentialNotExist();
        }
        return emailCredential;
    }
}
