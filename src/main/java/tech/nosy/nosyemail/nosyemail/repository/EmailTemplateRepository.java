package tech.nosy.nosyemail.nosyemail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
@CrossOrigin
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, String> {
  @Query(
      "from EmailTemplate where email_template_name=:emailTemplateName and "
          + "input_system_id=:inputSystemId")
  EmailTemplate findEmailTemplateByEmailTemplateNameAndInputSystemId(
          @Param("emailTemplateName") String emailTemplateName,
          @Param("inputSystemId") String inputSystemId);

  @Query("from EmailTemplate where input_system_id=:inputSystemId")
  List<EmailTemplate> findEmailTemplatesByInputSystemId(
          @Param("inputSystemId") String inputSystemId);

  @Query(
      "from EmailTemplate where input_system_id=:inputSystemId and email_template_id=:emailTemplateId")
  EmailTemplate findEmailTemplatesByInputSystemIdAndEmailTemplateId(
          @Param("inputSystemId") String inputSystemId,
          @Param("emailTemplateId") String emailTemplateId);
}
