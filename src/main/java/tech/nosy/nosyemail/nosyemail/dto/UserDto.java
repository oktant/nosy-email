package tech.nosy.nosyemail.nosyemail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Transient;


public class UserDto {
  private String email;
  @Transient
  private String password;

  private String firstName;
  private String lastName;


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
