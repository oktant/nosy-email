package tech.nosy.nosyemail.nosyemail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;
import tech.nosy.nosyemail.nosyemail.model.InputSystem;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
@CrossOrigin
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, String> {
  List<EmailTemplate> findEmailTemplatesByInputSystem(InputSystem inputSystem);
  EmailTemplate findEmailTemplateByEmailTemplateNameAndInputSystem(String emailTemplateName, InputSystem inputSystem);
}
