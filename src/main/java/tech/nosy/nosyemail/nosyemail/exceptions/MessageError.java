package tech.nosy.nosyemail.nosyemail.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MessageError {
  NOT_AUTHENTICATED("You are not authenticated. Please login first"),
  NO_INPUT_SYSTEM_FOUND(
      "No Input System with specified Id was found. Please create it before updating"),
  NO_EMAIL_TEMPLATE_FOUND(
      "No Template was found with specified request. Please correct your request"),
  EMAIL_TEMPLATE_EXIST("Email Template already exists. Please try another name"),
  EMAIL_TEMPLATE_NAME_CANNOT_BE_NULL("Email Template Name cannot be null or empty"),
  USERNAME_AND_PASSWORD_ARE_REQUIRED_FOR_NON_DEFAULT(
      "Username and password are required for non-Default Email Provider"),
  NOT_ENOUGH_PARAMETERS_FOR_PLACEHOLDERS(
      "Not enough paramaters to replace. Please add all placeholders"),
  INPUT_SYSTEM_HAS_CHILDREN(
      "Input System cannot be deleted. Because it has dependent children. Please delete Email Templates associated with this InputSystem to be able to delete it"),
  INPUT_SYSTEM_EXIST(
      "InputSystem with current name already exists in the system please. Please try another name"),
  USER_DOES_NOT_EXIST("User does not exists please register first"),
  INPUT_SYSTEM_NAME_IS_MANDATORY("Input System Name is mandatory field"),
  ACCESS_FORBIDDEN_EXCEPTION("Authorization server is not responding, please try again later"),
  USER_ALREADY_EXISTS_EXCEPTION("User already exists. Please try another email"),
  INVALID_USERNAME_OR_PASSWORD("Invalid username or password"),
  EMAIL_FIELDS_SHOULD_BE_WELL_FORMED("Email fields(Email To, Email From and Email CC) should be well-formed."),
  NOT_ALL_MANDATORY_FIELDS_SPECIFIED("Please specify all Mandatory fields."),
  FIELDS_CANNOT_BE_DETERMINED("Some fields cannot be determined. Please use appropriate format for all fields."),
  FEED_EXIST("Email Feed already exists. Please try another name."),
  NO_FEED_FOUND("No Feed was found with specified request. Please correct your request"),
  FEED_NAME_CANNOT_BE_NULL("Email Feed Name cannot be null or empty"),
  FEED_ALREADY_SUBSCRIBED_TO("Email Feed already subscribed to."),
  FEED_NOT_SUBSCRIBED_TO("Email Feed does not have an active subscription with the specified email.");


  private String message;

  MessageError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
