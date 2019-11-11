package tech.nosy.nosyemail.nosyemail.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tech.nosy.nosyemail.nosyemail.dto.EmailCollectionFileEncodedDto;
import tech.nosy.nosyemail.nosyemail.exceptions.EmailCollectionDoesNotExistException;
import tech.nosy.nosyemail.nosyemail.exceptions.UserNotExistsException;
import tech.nosy.nosyemail.nosyemail.model.EmailCollection;
import tech.nosy.nosyemail.nosyemail.model.User;
import tech.nosy.nosyemail.nosyemail.repository.EmailCollectionRepository;
import tech.nosy.nosyemail.nosyemail.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailCollectionServiceTest {

    @InjectMocks
    private EmailCollectionService emailCollectionService;
    @Mock
    private EmailCollectionRepository emailCollectionRepository;
    @Mock
    private UserRepository userRepository;
    private EmailCollection emailCollection;
    private String emailCollectionId;
    private String name;
    private List<String> emails = new ArrayList<>();
    private List<EmailCollection> result = new ArrayList<>();
    private User user;
    private String email;
    private EmailCollectionFileEncodedDto emailCollectionFileEncodedDto;
    private String base64;
    private List<String> parsedList = new ArrayList<>();

    private void setVariables() {
        emailCollectionId = "emailCollectionId";
        name = "NoSyGroup";
        email = "test@nosy.tech";
        emailCollection = new EmailCollection();
        emails.addAll(Arrays.asList("darth.vader@deathstar.com", "luke.skywalker@tatooine.com"));
        emailCollection.setEmailCollectionName(name);
        emailCollection.setEmailCollectionEmails(emails);
        emailCollection.setEmailCollectionId(emailCollectionId);
        result.clear();
        result.add(emailCollection);
        user=new User();
        user.setEmail(email);
        user.setFirstName("Test");
        user.setLastName("Nosy");
        user.setPassword("dajsndjasn");
        emailCollection.setUser(user);
        emailCollectionFileEncodedDto = new EmailCollectionFileEncodedDto();
        emailCollectionFileEncodedDto.setData("mocked data");
        emailCollectionFileEncodedDto.setName("email collection");
        base64 = "dGVzdDFAbWFpbC5jb20sdGVzdDJAbWFpbC5jb20=";
        parsedList.add("test1@mail.com");
        parsedList.add("test2@mail.com");
    }

    @Before
    public void beforeEmailCollection() {
        setVariables();
    }

    @Test
    public void addToEmailCollection() {
        when(emailCollectionRepository.findByEmailCollectionName(anyString())).thenReturn(emailCollection);
        doReturn(emailCollection).when(emailCollectionRepository).save(any());
        Assert.assertEquals(emailCollectionId, emailCollectionService.addToEmailCollection(emailCollectionFileEncodedDto).getEmailCollectionId());
    }

    @Test
    public void replaceEmailCollection() {
        when(emailCollectionRepository.findByEmailCollectionName(anyString())).thenReturn(emailCollection);
        doReturn(emailCollection).when(emailCollectionRepository).save(any());
        Assert.assertEquals(emailCollectionId, emailCollectionService.replaceEmailCollection(emailCollectionFileEncodedDto).getEmailCollectionId());
    }

    @Test
    public void parseBase64Data() {
        assertEquals(parsedList, emailCollectionService.parseBase64Data(base64));
    }

    @Test
    public void createEmailCollection() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(emailCollection).when(emailCollectionRepository).save(any());
        Assert.assertEquals(name, emailCollectionService.createEmailCollection(emailCollectionFileEncodedDto, user.getEmail()).getEmailCollectionName());
        Assert.assertEquals(user, emailCollectionService.createEmailCollection(emailCollectionFileEncodedDto, user.getEmail()).getUser());
    }

    @Test
    public void getAllEmailCollections() {
        doReturn(result).when(emailCollectionRepository).findAll();
        assertEquals(result, emailCollectionService.getAllEmailCollections());
    }

    @Test
    public void getEmailCollectionById() {
        doReturn(Optional.of(emailCollection)).when(emailCollectionRepository).findById(anyString());
        Assert.assertEquals(emailCollection, emailCollectionService.getEmailCollectionById(emailCollectionId));
    }

    @Test
    public void deleteEmailCollectionById() {
        doReturn(null).when(emailCollectionRepository).findById(anyString());
        emailCollectionService.deleteEmailCollectionById(emailCollectionId);
        assertNotEquals(emailCollection, emailCollectionRepository.findById(emailCollectionId));
    }

    @Test(expected = EmailCollectionDoesNotExistException.class)
    public void getEmailCollectionByIdDoesNotExist() {
        doReturn(Optional.empty()).when(emailCollectionRepository).findById(anyString());
        emailCollectionService.getEmailCollectionById(emailCollectionId);
    }

    @Test(expected = UserNotExistsException.class)
    public void createEmailCollectionUserNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        emailCollectionService.createEmailCollection(emailCollectionFileEncodedDto, user.getEmail());
    }
}
