package tech.nosy.nosyemail.nosyemail.dto;

import java.util.List;

public class EmailConfigDto {
    private String name;
    private List<EmailServerPropertyDto> emailConfigs;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmailServerPropertyDto> getEmailConfigs() {
        return emailConfigs;
    }

    public void setEmailConfigs(List<EmailServerPropertyDto> emailConfigs) {
        this.emailConfigs = emailConfigs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EmailConfigDto{" +
                "name='" + name + '\'' +
                ", emailConfigs=" + emailConfigs +
                ", email='" + email + '\'' +
                '}';
    }
}
