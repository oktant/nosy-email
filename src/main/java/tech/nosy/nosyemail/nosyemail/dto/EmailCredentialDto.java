package tech.nosy.nosyemail.nosyemail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class EmailCredentialDto {

    @NotNull
    private String profileName;

    @NotNull
    @Email
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    @Transient
    private String password;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setPassword(String password) {
        this.password = password;
    }
}
