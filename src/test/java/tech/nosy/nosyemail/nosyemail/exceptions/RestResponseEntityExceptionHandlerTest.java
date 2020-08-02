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
import org.springframework.web.bind.annotation.ExceptionHandler;

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
    public void emailTemplateNameInvalidExceptionTest() {
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.emailTemplateNameInvalidException().getStatusCode());
    }

    @Test
    public void constraintViolationMustBeWellFormedTest() {
        Throwable throwable=mock(Throwable.class);
        when(rollbackException.getCause()).thenReturn(throwable);
        when(throwable.getLocalizedMessage()).thenReturn("must be a well-formed");
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.constraintViolation(rollbackException).getStatusCode());

    }

    @Test
    public void constraintViolationMustNotBeNullTest() {
        Throwable throwable=mock(Throwable.class);
        when(rollbackException.getCause()).thenReturn(throwable);
        when(throwable.getLocalizedMessage()).thenReturn("must not be null");
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.constraintViolation(rollbackException).getStatusCode());
    }

    @Test
    public void constraintViolationMustNotBeEmptyTest() {
        Throwable throwable=mock(Throwable.class);
        when(rollbackException.getCause()).thenReturn(throwable);
        when(throwable.getLocalizedMessage()).thenReturn("must not be empty");
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.constraintViolation(rollbackException).getStatusCode());
    }

    @Test
    public void constraintViolationTest() {
        Throwable throwable=mock(Throwable.class);
        when(rollbackException.getCause()).thenReturn(throwable);
        when(throwable.getLocalizedMessage()).thenReturn("some fields cannot be determined");
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.constraintViolation(rollbackException).getStatusCode());
    }

    @Test
    public void authorizationServerCannotPerformTheOperationTest() {
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,restResponseEntityExceptionHandler.authorizationServerCannotPerformTheOperation().getStatusCode());
    }

    @Test
    public void inputSystemExistAlreadyExistsExceptionTest() {
        DataIntegrityViolationException dataIntegrityViolationException=mock(DataIntegrityViolationException.class);
        ResponseEntity<MessageError> messageErrorResponseEntity=restResponseEntityExceptionHandler.objectAlreadyExistsException(dataIntegrityViolationException);
        assertEquals(MessageError.RESOURCE_ALREADY_EXISTS.getMessage(),messageErrorResponseEntity.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT,messageErrorResponseEntity.getStatusCode());
    }


    @Test
    public void inputSystemNameIsMandatoryExceptionTest() {
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.inputSystemNameIsMandatoryException().getStatusCode());

    }

    @Test
    public void usernameAndPasswordAreNotProvidedForNonDefaultExceptionTest() {
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.usernameAndPasswordAreNotProvidedForNonDefaultException().getStatusCode());

    }

    @Test
    public void emailTemplateNotFoundExceptionTest() {
        assertEquals(HttpStatus.NOT_FOUND,restResponseEntityExceptionHandler.emailTemplateNotFoundException().getStatusCode());
    }

    @Test
    public void inputSystemNotFoundExceptionTest() {
        assertEquals(HttpStatus.NOT_FOUND,restResponseEntityExceptionHandler.inputSystemNotFoundException().getStatusCode());
    }

    @Test
    public void emailTemplateExistExceptionTest() {
        assertEquals(HttpStatus.CONFLICT,restResponseEntityExceptionHandler.emailTemplateExistException().getStatusCode());
    }

    @Test
    public void notEnoughParametersForPlaceholdersExceptionTest() {
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.notEnoughParametersForPlaceholdersException().getStatusCode());
    }

    @Test
    public void inputSystemHasChildrenExceptionTest() {
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.inputSystemHasChildrenException().getStatusCode());

    }
    @Test
    public void sendExceptionMailSendExceptionTest(){
        MailSendException mailSendException=mock(MailSendException.class);
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.sendException(mailSendException).getStatusCode());
    }
    @Test
    public void sendExceptionTest(){
        MailSendException mailSendException=new MailSendException("No recipient addresses");
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.sendException(mailSendException).getStatusCode());
    }

    @Test
    public void customEmailConfigShouldNotBeEmptyExceptionTest() {
        assertEquals(HttpStatus.BAD_REQUEST,restResponseEntityExceptionHandler.customEmailConfigShouldNotBeEmptyException().getStatusCode());
    }

    @Test
    public void customEmailConfigNotExistsTest() {
        assertEquals(HttpStatus.NOT_FOUND,restResponseEntityExceptionHandler.customEmailConfigNotExists().getStatusCode());
    }
}
