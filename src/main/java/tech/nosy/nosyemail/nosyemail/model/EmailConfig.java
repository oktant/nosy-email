package tech.nosy.nosyemail.nosyemail.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
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

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<EmailServerProperty> emailConfigs;

    public List<EmailServerProperty> getEmailConfigs() {
        return emailConfigs;
    }

    public void setEmailConfigs(List<EmailServerProperty> emailConfigs) {
        this.emailConfigs = emailConfigs;
    }

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

//    public Map<String, String> getEmailConfigs() {
//        return emailConfigs;
//    }
//
//    public void setEmailConfigs(Map<String, String> emailConfigs) {
//        this.emailConfigs = emailConfigs;
//    }


}
