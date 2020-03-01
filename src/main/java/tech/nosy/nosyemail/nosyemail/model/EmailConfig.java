package tech.nosy.nosyemail.nosyemail.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(
        name = "emailConfig",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email", "emailConfigName"}))
public class EmailConfig {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @NotNull
    private String emailConfigId;
    @NotNull
    @NotEmpty
    private String emailConfigName;
    @NotEmpty
    @NotNull
    private int port;
    @NotEmpty
    @NotNull
    private String host;

    @OneToMany(mappedBy = "emailConfig")
    public Set<EmailTemplate> emailTemplateSet;

    @NotNull
    @NotEmpty
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailConfigId() {
        return emailConfigId;
    }

    public void setEmailConfigId(String emailConfigId) {
        this.emailConfigId = emailConfigId;
    }

    public String getEmailConfigName() {
        return emailConfigName;
    }

    public void setEmailConfigName(String emailConfigName) {
        this.emailConfigName = emailConfigName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Set<EmailTemplate> getEmailTemplateSet() {
        return emailTemplateSet;
    }

    public void setEmailTemplateSet(Set<EmailTemplate> emailTemplateSet) {
        this.emailTemplateSet = emailTemplateSet;
    }
}
