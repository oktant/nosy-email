package tech.nosy.nosyemail.nosyemail.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import tech.nosy.nosyemail.nosyemail.exceptions.EmailTemplateNameInvalidException;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@DynamicInsert
@Table(
    name = "emailTemplate",
    uniqueConstraints = @UniqueConstraint(columnNames = {"emailTemplateName", "input_system_id"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmailTemplate {
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Id
  @NotNull
  private String emailTemplateId;

  @NotNull private String emailTemplateName;
  @NotNull @Email private String emailTemplateFromAddress;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "text default 'Default'")
  @NotNull
  private EmailFromProvider emailTemplateFromProvider;

  @ElementCollection
  @JoinTable(name = "email_template_to", joinColumns = @JoinColumn(name = "email_template_id"))
  private Set<@Email String> emailTemplateTo;

  @ElementCollection
  @JoinTable(name = "email_template_cc", joinColumns = @JoinColumn(name = "email_template_id"))
  private Set<@NotEmpty @Email String> emailTemplateCc;

  @NotNull private String emailTemplateText;

  @NotNull
  @Column(columnDefinition = "int default 0")
  private int emailTemplateRetryTimes;

  private int emailTemplateRetryPeriod;

  private int emailTemplatePriority;

  @NotNull
  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "input_system_id")
  private InputSystem inputSystem;

  @NotNull private String emailTemplateSubject;

  @PrePersist
  protected void onCreate() {
    if (emailTemplateName == null || emailTemplateName.isEmpty()) {
      throw new EmailTemplateNameInvalidException();

    }
  }

  public String getEmailTemplateId() {
    return emailTemplateId;
  }

  public void setEmailTemplateId(String emailTemplateId) {
    this.emailTemplateId = emailTemplateId;
  }

  public String getEmailTemplateName() {
    return emailTemplateName;
  }

  public void setEmailTemplateName(String emailTemplateName) {
    this.emailTemplateName = emailTemplateName;
  }

  public String getEmailTemplateFromAddress() {
    return emailTemplateFromAddress;
  }

  public void setEmailTemplateFromAddress(String emailTemplateFromAddress) {
    this.emailTemplateFromAddress = emailTemplateFromAddress;
  }

  public EmailFromProvider getEmailTemplateFromProvider() {
    return emailTemplateFromProvider;
  }

  public void setEmailTemplateFromProvider(EmailFromProvider emailTemplateFromProvider) {
    this.emailTemplateFromProvider = emailTemplateFromProvider;
  }

  public Set<String> getEmailTemplateTo() {
    return emailTemplateTo;
  }

  public void setEmailTemplateTo(Set<String> emailTemplateTo) {
    this.emailTemplateTo = emailTemplateTo;
  }

  public Set<String> getEmailTemplateCc() {
    return emailTemplateCc;
  }

  public void setEmailTemplateCc(Set<String> emailTemplateCc) {
    this.emailTemplateCc = emailTemplateCc;
  }

  public String getEmailTemplateText() {
    return emailTemplateText;
  }

  public void setEmailTemplateText(String emailTemplateText) {
    this.emailTemplateText = emailTemplateText;
  }

  public int getEmailTemplateRetryTimes() {
    return emailTemplateRetryTimes;
  }

  public void setEmailTemplateRetryTimes(int emailTemplateRetryTimes) {
    this.emailTemplateRetryTimes = emailTemplateRetryTimes;
  }

  public int getEmailTemplateRetryPeriod() {
    return emailTemplateRetryPeriod;
  }

  public void setEmailTemplateRetryPeriod(int emailTemplateRetryPeriod) {
    this.emailTemplateRetryPeriod = emailTemplateRetryPeriod;
  }

  public int getEmailTemplatePriority() {
    return emailTemplatePriority;
  }

  public void setEmailTemplatePriority(int emailTemplatePriority) {
    this.emailTemplatePriority = emailTemplatePriority;
  }

  public InputSystem getInputSystem() {
    return inputSystem;
  }

  public void setInputSystem(InputSystem inputSystem) {
    this.inputSystem = inputSystem;
  }

  public String getEmailTemplateSubject() {
    return emailTemplateSubject;
  }

  public void setEmailTemplateSubject(String emailTemplateSubject) {
    this.emailTemplateSubject = emailTemplateSubject;
  }

  @Override
  public String toString() {
    return "EmailTemplate{" +
            "emailTemplateId='" + emailTemplateId + '\'' +
            ", emailTemplateName='" + emailTemplateName + '\'' +
            ", emailTemplateFromAddress='" + emailTemplateFromAddress + '\'' +
            ", emailTemplateFromProvider=" + emailTemplateFromProvider +
            ", emailTemplateTo=" + emailTemplateTo +
            ", emailTemplateCc=" + emailTemplateCc +
            ", emailTemplateText='" + emailTemplateText + '\'' +
            ", emailTemplateRetryTimes=" + emailTemplateRetryTimes +
            ", emailTemplateRetryPeriod=" + emailTemplateRetryPeriod +
            ", emailTemplatePriority=" + emailTemplatePriority +
            ", emailTemplateSubject='" + emailTemplateSubject + '\'' +
            '}';
  }
}
