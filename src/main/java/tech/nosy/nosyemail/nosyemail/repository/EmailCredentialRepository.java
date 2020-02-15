package tech.nosy.nosyemail.nosyemail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tech.nosy.nosyemail.nosyemail.model.EmailCredential;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Transactional
@Repository
@CrossOrigin
public interface EmailCredentialRepository extends JpaRepository<EmailCredential, String> {
    Set<EmailCredential> findAllByEmail(String email);
    EmailCredential findAllByEmailCredentialProfileNameAndEmail(String profileName, String email);
}
