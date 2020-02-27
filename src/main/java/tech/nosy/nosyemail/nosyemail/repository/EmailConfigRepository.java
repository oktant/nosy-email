package tech.nosy.nosyemail.nosyemail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tech.nosy.nosyemail.nosyemail.model.EmailConfig;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
@CrossOrigin
public interface EmailConfigRepository extends JpaRepository<EmailConfig, String> {
    public List<EmailConfig> findAllByEmail(String email);
    public EmailConfig findAllByEmailConfigNameAndEmail(String configName, String email);
}
