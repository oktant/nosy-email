package tech.nosy.nosyemail.nosyemail.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import tech.nosy.nosyemail.nosyemail.dto.EmailCollectionDto;
import tech.nosy.nosyemail.nosyemail.dto.EmailCollectionFileEncodedDto;
import tech.nosy.nosyemail.nosyemail.exceptions.EmailCollectionDoesNotExistException;
import tech.nosy.nosyemail.nosyemail.model.EmailCollection;
import tech.nosy.nosyemail.nosyemail.service.EmailCollectionService;

import java.security.Principal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailCollectionControllerTest {

    @InjectMocks
    EmailCollectionController emailCollectionController;
    @Mock
    EmailCollectionService emailCollectionService;
    private EmailCollectionFileEncodedDto emailCollectionFileEncodedDto = new EmailCollectionFileEncodedDto();
    private EmailCollectionDto emailCollectionDto = new EmailCollectionDto();
    private EmailCollection emailCollection = new EmailCollection();

    private void setVariables() {
        emailCollectionFileEncodedDto.setName("test");
        emailCollectionFileEncodedDto.setData("dGVzdDFAbWFpbC5jb20sdGVzdDJAbWFpbC5jb20=");
        emailCollectionDto.setEmails(Arrays.asList("test1@mail.com", "test2@mail.com"));
        emailCollectionDto.setName("test");
        emailCollectionDto.setId("emailCollectionId");

        emailCollection.setEmailCollectionEmails(Arrays.asList("test1@mail.com", "test2@mail.com"));
        emailCollection.setEmailCollectionName("test");
        emailCollection.setEmailCollectionId("emailCollectionId");
    }

    @Before
    public void beforeEmailCollectionController() {
        setVariables();
    }

    @Test
    public void emailCollectionUpload() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.CREATED, emailCollectionController.
                uploadEmailCollection(emailCollectionFileEncodedDto, principal).getStatusCode());
    }

    @Test
    public void emailCollectionCreate() {
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.CREATED, emailCollectionController.
                createEmailCollection(emailCollectionFileEncodedDto, principal).getStatusCode());
    }

    @Test
    public void emailCollectionAddTo() {
        assertEquals(HttpStatus.OK, emailCollectionController.
                addToEmailCollection(emailCollectionFileEncodedDto).getStatusCode());
    }

    @Test
    public void emailCollectionReplace() {
        assertEquals(HttpStatus.OK, emailCollectionController.
                replaceEmailCollection(emailCollectionFileEncodedDto).getStatusCode());
    }

    @Test
    public void getAllEmailCollections() {
        assertEquals(HttpStatus.OK, emailCollectionController.
                getAllEmailCollections().getStatusCode());
    }

    @Test
    public void getEmailCollectionById() {
        emailCollectionDto.setId("hej");
        assertEquals(HttpStatus.OK, emailCollectionController.getEmailCollectionById("emailCollectionId").getStatusCode());
        assertEquals("hej", emailCollectionDto.getId());
    }

    @Test
    public void getEmailCollectionByIdNotFound() {
        when(emailCollectionService.getEmailCollectionById(anyString())).thenThrow(EmailCollectionDoesNotExistException.class);
        assertEquals(HttpStatus.NOT_FOUND, emailCollectionController.
                getEmailCollectionById("1").getStatusCode());
    }

    @Test
    public void deleteEmailCollectionById() {
        assertEquals(HttpStatus.NO_CONTENT, emailCollectionController.
                deleteEmailCollectionById("emailCollectionId").getStatusCode());
    }
}
