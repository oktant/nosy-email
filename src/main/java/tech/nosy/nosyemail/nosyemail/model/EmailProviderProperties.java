package tech.nosy.nosyemail.nosyemail.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailProviderProperties {
  private String username;
  private String password;
  private List<PlaceHolder> placeholders;
  private List<String> to;

  public List<String> getTo() {
    return to;
  }

  public void setTo(List<String> to) {
    this.to = to;
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

  public void setPassword(String password) {
    this.password = password;
  }

  public List<PlaceHolder> getPlaceholders() {
    return placeholders;
  }

  public void setPlaceholders(List<PlaceHolder> placeholders) {
    this.placeholders = placeholders;
  }

  @Override
  public String toString() {
    return "EmailProviderProperties{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", placeholders=" + placeholders +
            ", to=" + to +
            '}';
  }
}
