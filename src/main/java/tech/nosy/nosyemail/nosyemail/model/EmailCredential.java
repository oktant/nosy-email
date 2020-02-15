package tech.nosy.nosyemail.nosyemail.model;

import com.sun.istack.NotNull;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Entity
public class EmailCredential {
    @Id
    @NotNull
    @NotEmpty
    private String emailCredentialProfileName;

    @NotNull
    @Email
    @NotEmpty
    private String emailCredentialUsername;
    @NotNull
    @NotEmpty

    private String emailCredentialPassword;
    @NotNull
    @Email
    private String email;
    public String getEmailCredentialProfileName() {
        return emailCredentialProfileName;
    }

    public void setEmailCredentialProfileName(String emailCredentialProfileName) {
        this.emailCredentialProfileName = emailCredentialProfileName;
    }

    public String getEmailCredentialUsername() {
        return emailCredentialUsername;
    }

    public void setEmailCredentialUsername(String emailCredentialUsername) {
        this.emailCredentialUsername = emailCredentialUsername;
    }

    public String getEmailCredentialPassword() {
        return emailCredentialPassword;
    }

    public void setEmailCredentialPassword(String emailCredentialPassword) {
        this.emailCredentialPassword = emailCredentialPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

