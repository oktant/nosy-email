package tech.nosy.nosyemail.nosyemail.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tech.nosy.nosyemail.nosyemail.model.EmailFromProvider;
import tech.nosy.nosyemail.nosyemail.model.InputSystem;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;


public class EmailTemplateDto {
  @NotNull private String id;

  @NotNull private String name;
  @NotNull @Email private String fromAddress;
  @NotNull private String subject;

  @NotNull private EmailFromProvider fromProvider;

  private Set<@NotEmpty @Email String> to;

  private Set<@NotEmpty @Email String> cc;

  @NotNull private String text;

  @NotNull private int retryTimes;

  private int retryPeriod;

  private int priority;
  @JsonIgnore
  @NotNull private InputSystem inputSystem;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFromAddress() {
    return fromAddress;
  }

  public void setFromAddress(String fromAddress) {
    this.fromAddress = fromAddress;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public EmailFromProvider getFromProvider() {
    return fromProvider;
  }

  public void setFromProvider(EmailFromProvider fromProvider) {
    this.fromProvider = fromProvider;
  }

  public Set<String> getTo() {
    return to;
  }

  public void setTo(Set<String> to) {
    this.to = to;
  }

  public Set<String> getCc() {
    return cc;
  }

  public void setCc(Set<String> cc) {
    this.cc = cc;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getRetryTimes() {
    return retryTimes;
  }

  public void setRetryTimes(int retryTimes) {
    this.retryTimes = retryTimes;
  }

  public int getRetryPeriod() {
    return retryPeriod;
  }

  public void setRetryPeriod(int retryPeriod) {
    this.retryPeriod = retryPeriod;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public InputSystem getInputSystem() {
    return inputSystem;
  }

  public void setInputSystem(InputSystem inputSystem) {
    this.inputSystem = inputSystem;
  }
}
