package tech.nosy.nosyemail.nosyemail.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import tech.nosy.nosyemail.nosyemail.dto.TokenCollectionDto;
import tech.nosy.nosyemail.nosyemail.dto.UserDto;
import tech.nosy.nosyemail.nosyemail.model.User;
import tech.nosy.nosyemail.nosyemail.service.KeycloakService;
import tech.nosy.nosyemail.nosyemail.service.UserService;
import tech.nosy.nosyemail.nosyemail.utils.UserMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)

public class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    KeycloakService keycloakService;

    UserDto userDto=new UserDto();

    @Before
    public void setUp(){
        userDto.setFirstName("testFirstName");
        userDto.setLastName("testLastName");
        userDto.setPassword("testPassword");
        userDto.setEmail("test@nosy.tech");
    }

    @Test
    public void logout() {
        HttpServletRequest httpServletRequest= Mockito.mock(HttpServletRequest.class);
        doNothing().when(userService).logoutUser(httpServletRequest);
        assertEquals(HttpStatus.NO_CONTENT, authController.logout(httpServletRequest).getStatusCode());
    }

    @Test
    public void isAuthenticated()  {
        TokenCollectionDto tokenCollectionDto=new TokenCollectionDto();
        tokenCollectionDto.setAccessToken("testToken");
        doReturn(true).when(keycloakService).isAuthenticated(tokenCollectionDto.getAccessToken());
        assertEquals(HttpStatus.OK, authController.isAuthenticated(tokenCollectionDto).getStatusCode());


    }

    @Test
    public void getToken() throws IOException {
        assertEquals(HttpStatus.OK, authController.getToken(userDto).getStatusCode());
    }

    @Test
    public void newUser() {
        assertEquals(HttpStatus.CREATED, authController.newUser(userDto).getStatusCode());
    }

    @Test
    public void deleteUsername() {
        HttpServletRequest httpServletRequest= Mockito.mock(HttpServletRequest.class);
        doNothing().when(userService).deleteUser(httpServletRequest);
        assertEquals(HttpStatus.NO_CONTENT, authController.deleteUsername(httpServletRequest).getStatusCode());


    }

    @Test
    public void getUserProfile() {
        User user= UserMapper.INSTANCE.toUser(userDto);
        HttpServletRequest httpServletRequest= Mockito.mock(HttpServletRequest.class);
        doReturn(user).when(userService).getUserInfo(httpServletRequest);
        assertEquals(HttpStatus.OK, authController.getUserProfile(httpServletRequest).getStatusCode());
    }

}
