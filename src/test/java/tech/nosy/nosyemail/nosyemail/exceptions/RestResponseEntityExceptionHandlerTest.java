package tech.nosy.nosyemail.nosyemail.exceptions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;

import javax.persistence.RollbackException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class RestResponseEntityExceptionHandlerTest {
    @InjectMocks
    RestResponseEntityExceptionHandler restResponseEntityExceptionHandler;

    @Mock
    RollbackException rollbackException;

    @Test
    public void emailTemplateNameInvalidException() {
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.emailTemplateNameInvalidException().getStatusCode());
    }

    @Test
    public void constraintViolationMustBeWellFormed() {
        Throwable throwable=mock(Throwable.class);
        when(rollbackException.getCause()).thenReturn(throwable);
        when(throwable.getLocalizedMessage()).thenReturn("must be a well-formed");
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.constraintViolation(rollbackException).getStatusCode());

    }

    @Test
    public void constraintViolationMustNotBeNull() {
        Throwable throwable=mock(Throwable.class);
        when(rollbackException.getCause()).thenReturn(throwable);
        when(throwable.getLocalizedMessage()).thenReturn("must not be null");
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.constraintViolation(rollbackException).getStatusCode());
    }

    @Test
    public void constraintViolationMustNotBeEmpty() {
        Throwable throwable=mock(Throwable.class);
        when(rollbackException.getCause()).thenReturn(throwable);
        when(throwable.getLocalizedMessage()).thenReturn("must not be empty");
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.constraintViolation(rollbackException).getStatusCode());
    }

    @Test
    public void constraintViolation() {
        Throwable throwable=mock(Throwable.class);
        when(rollbackException.getCause()).thenReturn(throwable);
        when(throwable.getLocalizedMessage()).thenReturn("some fields cannot be determined");
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.constraintViolation(rollbackException).getStatusCode());
    }

    @Test
    public void authorizationServerCannotPerformTheOperation() {
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,restResponseEntityExceptionHandler.authorizationServerCannotPerformTheOperation().getStatusCode());
    }

    @Test
    public void inputSystemExistAlreadyExistsException() {
        DataIntegrityViolationException dataIntegrityViolationException=mock(DataIntegrityViolationException.class);
        ResponseEntity<MessageError> messageErrorResponseEntity=restResponseEntityExceptionHandler.objectAlreadyExistsException(dataIntegrityViolationException);
        assertEquals(MessageError.RESOURCE_ALREADY_EXISTS.getMessage(),messageErrorResponseEntity.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT,messageErrorResponseEntity.getStatusCode());
    }


    @Test
    public void inputSystemNameIsMandatoryException() {
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.inputSystemNameIsMandatoryException().getStatusCode());

    }

    @Test
    public void usernameAndPasswordAreNotProvidedForNonDefaultException() {
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.usernameAndPasswordAreNotProvidedForNonDefaultException().getStatusCode());

    }

    @Test
    public void emailTemplateNotFoundException() {
        assertEquals(HttpStatus.NOT_FOUND,restResponseEntityExceptionHandler.emailTemplateNotFoundException().getStatusCode());
    }

    @Test
    public void inputSystemNotFoundException() {
        assertEquals(HttpStatus.NOT_FOUND,restResponseEntityExceptionHandler.inputSystemNotFoundException().getStatusCode());
    }

    @Test
    public void emailTemplateExistException() {
        assertEquals(HttpStatus.CONFLICT,restResponseEntityExceptionHandler.emailTemplateExistException().getStatusCode());
    }

    @Test
    public void notEnoughParametersForPlaceholdersException() {
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.notEnoughParametersForPlaceholdersException().getStatusCode());
    }

    @Test
    public void inputSystemHasChildrenException() {
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.inputSystemHasChildrenException().getStatusCode());

    }
    @Test
    public void sendExceptionMailSendException(){
        MailSendException mailSendException=mock(MailSendException.class);
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.sendException(mailSendException).getStatusCode());
    }
    @Test
    public void sendException(){
        MailSendException mailSendException=new MailSendException("No recipient addresses");
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.sendException(mailSendException).getStatusCode());
    }
}
