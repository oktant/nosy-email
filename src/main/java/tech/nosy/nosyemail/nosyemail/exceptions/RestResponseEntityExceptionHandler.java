package tech.nosy.nosyemail.nosyemail.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = AuthorizationServerCannotPerformTheOperation.class)
  public ResponseEntity<MessageError> authorizationServerCannotPerformTheOperation() {
    return new ResponseEntity<>(MessageError.ACCESS_FORBIDDEN_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = DataIntegrityViolationException.class)
  public ResponseEntity<MessageError> objectAlreadyExistsException(DataIntegrityViolationException e) {
    return new ResponseEntity<>(MessageError.RESOURCE_ALREADY_EXISTS, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(value = EmailTemplateNameInvalidException.class)
  public ResponseEntity<MessageError> emailTemplateNameInvalidException() {
    return new ResponseEntity<>(MessageError.EMAIL_TEMPLATE_NAME_CANNOT_BE_NULL, HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(value = InputSystemNameIsMandatoryException.class)
  public ResponseEntity<MessageError> inputSystemNameIsMandatoryException() {
    return new ResponseEntity<>(MessageError.INPUT_SYSTEM_NAME_IS_MANDATORY, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = UsernameAndPasswordAreNotProvidedForNonDefaultException.class)
  public ResponseEntity<MessageError> usernameAndPasswordAreNotProvidedForNonDefaultException() {
    return new ResponseEntity<>(MessageError.USERNAME_AND_PASSWORD_ARE_REQUIRED_FOR_NON_DEFAULT, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = EmailTemplateNotFoundException.class)
  public ResponseEntity<MessageError> emailTemplateNotFoundException() {
    return new ResponseEntity<>(MessageError.NO_EMAIL_TEMPLATE_FOUND, HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(value = InputSystemNotFoundException.class)
  public ResponseEntity<MessageError> inputSystemNotFoundException() {
    return new ResponseEntity<>(MessageError.NO_INPUT_SYSTEM_FOUND, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = EmailTemplateExistException.class)
  public ResponseEntity<MessageError> emailTemplateExistException() {
    return new ResponseEntity<>(MessageError.EMAIL_TEMPLATE_EXIST, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(value = NotEnoughParametersForPlaceholdersException.class)
  public ResponseEntity<MessageError> notEnoughParametersForPlaceholdersException() {
    return new ResponseEntity<>(MessageError.NOT_ENOUGH_PARAMETERS_FOR_PLACEHOLDERS, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = InputSystemHasChildrenException.class)
  public ResponseEntity<MessageError> inputSystemHasChildrenException() {
    return new ResponseEntity<>(MessageError.INPUT_SYSTEM_HAS_CHILDREN, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {ConstraintViolationException.class, RollbackException.class})
  public ResponseEntity<MessageError> constraintViolation(RollbackException ex) {
    if (ex.getCause().getLocalizedMessage().contains("must be a well-formed")) {
      return new ResponseEntity<>(MessageError.EMAIL_FIELDS_SHOULD_BE_WELL_FORMED, HttpStatus.BAD_REQUEST);
    } else if (ex.getCause().getLocalizedMessage().contains("must not be null") ||
            ex.getCause().getLocalizedMessage().contains("must not be empty")) {

      return new ResponseEntity<>(MessageError.NOT_ALL_MANDATORY_FIELDS_SPECIFIED, HttpStatus.BAD_REQUEST);
    } else {
      return new ResponseEntity<>(MessageError.FIELDS_CANNOT_BE_DETERMINED, HttpStatus.BAD_REQUEST);
    }
  }

  @ExceptionHandler(value = MailSendException.class)
  public ResponseEntity<MessageError> sendException(MailSendException ex) {
    if(ex.getMessage()!=null && ex.getMessage().contains("No recipient addresses")){
      return new ResponseEntity<>(MessageError.TO_ADDRESS_IS_NOT_SPECIFIED, HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(MessageError.MAIL_CANNOT_BE_SENT, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = EmailCredentialProfileAlreadyExistException.class)
  public ResponseEntity<MessageError> emailCredentialProfileAlreadyExistException() {
    return new ResponseEntity<>(MessageError.EMAIL_CREDENTIALS_EXISTS, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = EmailCredentialNotExist.class)
  public ResponseEntity<MessageError> emailCredentialNotExist() {
    return new ResponseEntity<>(MessageError.EMAIL_CREDENTIALS_NOT_EXISTS, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = CustomEmailConfigShouldNotBeEmptyException.class)
  public ResponseEntity<MessageError> customEmailConfigShouldNotBeEmptyException() {
    return new ResponseEntity<>(MessageError.EMAIL_CONFIG_SHOULD_NOT_BE_EMPTY, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = CustomEmailConfigNotExists.class)
  public ResponseEntity<MessageError> customEmailConfigNotExists() {
    return new ResponseEntity<>(MessageError.EMAIL_CONFIG_SHOULD_EXIST, HttpStatus.NOT_FOUND);
  }
}
