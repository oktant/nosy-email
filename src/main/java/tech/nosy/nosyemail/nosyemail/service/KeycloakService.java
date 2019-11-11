package tech.nosy.nosyemail.nosyemail.service;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.config.KeycloakConfigBean;
import tech.nosy.nosyemail.nosyemail.config.security.TokenCollection;
import tech.nosy.nosyemail.nosyemail.exceptions.AuthorizationServerCannotPerformTheOperation;
import tech.nosy.nosyemail.nosyemail.exceptions.UserAlreadyExistException;
import tech.nosy.nosyemail.nosyemail.model.User;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class KeycloakService {
    private KeycloakConfigBean keycloakConfigBean;
    private static final Logger logger = LoggerFactory.getLogger(KeycloakService.class);

    @Autowired
    public KeycloakService(KeycloakConfigBean keycloakConfigBean){
        this.keycloakConfigBean=keycloakConfigBean;
    }

    public boolean isAuthenticated(String token)  {

       return keycloakConfigBean.getPostForAuthentication(token);

    }

    public void logoutUser(String username) {

        UsersResource usersResource = keycloakConfigBean.getKeycloakUserResource().users();

        if(usersResource.get(getUserGet(username).get())!=null){
            usersResource.get(getUserGet(username).get()).logout();
        }

    }
    private AtomicReference<String> getUserGet(String username) {
        UsersResource usersResource = keycloakConfigBean.getKeycloakUserResource().users();
        AtomicReference<String> userId = new AtomicReference<>("");
        usersResource
                .list()
                .forEach(
                        t -> {
                            if (username.equals(t.getUsername())) {
                                userId.set(t.getId());
                            }
                        });
        return userId;
    }

    public void registerNewUser(User user) {

        int statusId;
        try {
            RealmResource realmResource = keycloakConfigBean.getKeycloakUserResource();
            UsersResource usersResource = realmResource.users();

            UserRepresentation newUser = new UserRepresentation();
            newUser.setUsername(user.getEmail());
            newUser.setEmail(user.getEmail());
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setEnabled(true);
            Response result = usersResource.create(newUser);

            statusId = result.getStatus();

            if (statusId == 201) {

                String userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

                CredentialRepresentation passwordCred = new CredentialRepresentation();
                passwordCred.setTemporary(false);
                passwordCred.setType(CredentialRepresentation.PASSWORD);
                passwordCred.setValue(user.getPassword());

                usersResource.get(userId).resetPassword(passwordCred);

                ClientRepresentation clientRep = realmResource.clients().findByClientId(keycloakConfigBean.getClientId()).get(0);
                RoleRepresentation clientRoleRep =
                        realmResource
                                .clients()
                                .get(clientRep.getId())
                                .roles()
                                .get(keycloakConfigBean.getNosyClientRole())
                                .toRepresentation();
                realmResource
                        .users()
                        .get(userId)
                        .roles()
                        .clientLevel(clientRep.getId())
                        .add(Arrays.asList(clientRoleRep));

            } else {
                throw new UserAlreadyExistException();
            }

        } catch (ClientErrorException e) {
            logger.error(e.getLocalizedMessage());
            throw new AuthorizationServerCannotPerformTheOperation();
        }
    }

    public User getUserInfo(String username) {
        UsersResource userResource = keycloakConfigBean.getKeycloakUserResource().users();
        User user = new User();
        userResource
                .list()
                .forEach(
                        t -> {
                            if (username.equals(t.getUsername())) {
                                user.setEmail(t.getEmail());
                                user.setFirstName(t.getFirstName());
                                user.setLastName(t.getLastName());
                            }
                        });
        return user;
    }

    public TokenCollection getTokens(User user) throws IOException {
        return keycloakConfigBean.getTokens(user);
    }

    public void deleteUsername(String username) {
        UsersResource usersResource = keycloakConfigBean.getKeycloakUserResource().users();

        usersResource.delete(getUserGet(username).get());
    }
}
