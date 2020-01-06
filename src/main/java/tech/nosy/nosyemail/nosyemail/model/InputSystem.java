package tech.nosy.nosyemail.nosyemail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

@DynamicInsert
@Entity
@Table(name = "inputSystem",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"inputSystemName", "email"})})
public class InputSystem {
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Id
  @JsonIgnore
  private String inputSystemId;

  @NotNull private String inputSystemName;

  @NotNull @Email
  private String email;

  @JsonManagedReference
  @OneToMany(mappedBy = "inputSystem", orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<EmailTemplate> emailTemplate;

  public String getInputSystemId() {
    return inputSystemId;
  }

  public void setInputSystemId(String inputSystemId) {
    this.inputSystemId = inputSystemId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getInputSystemName() {
    return inputSystemName;
  }

  public void setInputSystemName(String inputSystemName) {
    this.inputSystemName = inputSystemName.trim();
  }

  public Set<EmailTemplate> getEmailTemplate() {
    return emailTemplate;
  }

  public void setEmailTemplate(Set<EmailTemplate> emailTemplate) {
    this.emailTemplate = emailTemplate;
  }
}
