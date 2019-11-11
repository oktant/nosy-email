package tech.nosy.nosyemail.nosyemail.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tech.nosy.nosyemail.nosyemail.config.KeycloakConfigBean;
import tech.nosy.nosyemail.nosyemail.config.security.TokenCollection;
import tech.nosy.nosyemail.nosyemail.exceptions.AuthorizationServerCannotPerformTheOperation;
import tech.nosy.nosyemail.nosyemail.exceptions.UserAlreadyExistException;
import tech.nosy.nosyemail.nosyemail.model.User;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KeycloakServiceTest {
    @InjectMocks
    KeycloakService keycloakService;

    @Mock
    KeycloakConfigBean keycloakConfigBean;


    private String email="test@nosy.tech";
    private User user;
    @Before
    public void setUser(){

        user=new User();
        user.setEmail(email);
        user.setFirstName("Test");
        user.setLastName("Nosy");
        user.setPassword("dajsndjasn");
    }

    @Test
    public void isAuthenticatedFalse() {
        String token="addsada";

        when(keycloakConfigBean.getPostForAuthentication(any())).thenReturn(false);

        assertFalse(keycloakService.isAuthenticated(token));
    }


    @Test
    public void isAuthenticatedTrue() {
        String token="addsada";
        when(keycloakConfigBean.getPostForAuthentication(any())).thenReturn(true);
        assertTrue(keycloakService.isAuthenticated(token));

    }
    @Test(expected = Test.None.class)
    public void logoutUserNotFound() {
        String username="dadas";
        RealmResource realmResource=mock(RealmResource.class);
        UsersResource usersResource=mock(UsersResource.class);
        when(keycloakConfigBean.getKeycloakUserResource()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        keycloakService.logoutUser(username);

    }
    @Test(expected = Test.None.class)
    public void logoutUserSuccess() {
        String username="dadas";
        RealmResource realmResource=mock(RealmResource.class);
        UsersResource usersResource=mock(UsersResource.class);
        when(keycloakConfigBean.getKeycloakUserResource()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        UserResource userResource=mock(UserResource.class);
        when(usersResource.get(any())).thenReturn(userResource);
        keycloakService.logoutUser(username);

    }
    @Test(expected = Test.None.class)
    public void deleteUsername() {
        String username="dadas";
        RealmResource realmResource=mock(RealmResource.class);
        UsersResource usersResource=mock(UsersResource.class);
        when(keycloakConfigBean.getKeycloakUserResource()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        keycloakService.deleteUsername(username);

    }

    @Test
    public void getUserInfo() {
        String username="dadas";
        UsersResource usersResource=mock(UsersResource.class);
        RealmResource realmResource=mock(RealmResource.class);
        when(keycloakConfigBean.getKeycloakUserResource()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        assertNotNull(keycloakService.getUserInfo(username));
    }



    @Test(expected = UserAlreadyExistException.class)
    public void registerNewUserFail() throws URISyntaxException {
        User user=new User();
        RealmResource realmResource=mock(RealmResource.class);
        UsersResource usersResource=mock(UsersResource.class);
        when(keycloakConfigBean.getKeycloakUserResource()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        URI uri=new URI("dasdas");
        Response response=Response.status(Response.Status.CONFLICT).
                location(uri).build();
        when(usersResource.create(any())).thenReturn(response);
        keycloakService.registerNewUser(user);
    }



    @Test(expected = Test.None.class)
    public void registerNewUserSuccessWithClient() throws URISyntaxException {
        User user=new User();
        RealmResource realmResource=mock(RealmResource.class);
        UsersResource usersResource=mock(UsersResource.class);
        when(keycloakConfigBean.getKeycloakUserResource()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        URI uri=new URI("dasdas");
        Response response=Response.status(Response.Status.CREATED).
                location(uri).build();
        when(usersResource.create(any())).thenReturn(response);
        ClientRepresentation clientRepresentation=mock(ClientRepresentation.class);
        List<ClientRepresentation> clientRepresentationList=new ArrayList<ClientRepresentation>();
        clientRepresentationList.add(clientRepresentation);
        ClientResource clientResource=mock(ClientResource.class);
        ClientsResource clientsResource=mock(ClientsResource.class);
        RoleMappingResource roleMappingResource=mock(RoleMappingResource.class);
        RolesResource rolesResource=mock(RolesResource.class);
        RoleResource roleResource=mock(RoleResource.class);
        RoleScopeResource roleScopeResource=mock(RoleScopeResource.class);
        RoleRepresentation roleRepresentation=mock(RoleRepresentation.class);
        UserResource userResource=mock(UserResource.class);
        when(realmResource.clients()).thenReturn(clientsResource);
        when(clientsResource.findByClientId(any())).thenReturn(clientRepresentationList);
        when(clientsResource.get(any())).thenReturn(clientResource);
        when(clientResource.roles()).thenReturn(rolesResource);
        when(rolesResource.get(anyString())).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenReturn(roleRepresentation);
        when(usersResource.get(anyString())).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.clientLevel(any())).thenReturn(roleScopeResource);
        doNothing().when(roleScopeResource).add(any());
        when(keycloakConfigBean.getNosyClientRole()).thenReturn("nosy-role");
        keycloakService.registerNewUser(user);
    }

    @Test(expected = AuthorizationServerCannotPerformTheOperation.class)
    public void registerNewUserClientErrorExceptionTest(){
        User user=new User();
        RealmResource realmResource=mock(RealmResource.class);
        UsersResource usersResource=mock(UsersResource.class);
        when(keycloakConfigBean.getKeycloakUserResource()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);

        when(usersResource.create(any())).thenThrow(ClientErrorException.class);
        keycloakService.registerNewUser(user);
    }
    @Test
    public void getTokens() throws IOException {
        TokenCollection tokenCollection=mock(TokenCollection.class);
        when(keycloakConfigBean.getTokens(user)).thenReturn(tokenCollection);
        assertNotNull(keycloakService.getTokens(user));
    }
}
