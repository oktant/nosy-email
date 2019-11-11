package tech.nosy.nosyemail.nosyemail.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.nosy.nosyemail.nosyemail.config.security.TokenCollection;
import tech.nosy.nosyemail.nosyemail.exceptions.InvalidUsernameAndPasswordException;
import tech.nosy.nosyemail.nosyemail.model.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Component
public class    KeycloakConfigBean {
    private static final String GRANT_TYPE_STRING = "grant_type";
    private static final String CLIENT_ID_STRING = "client_id";
    private static final String CLIENT_SECRET_STRING = "client_secret";
    @Value("${nosy.client.grantType}")
    private String grantType;
    @Value("${nosy.client.keycloak.url}")
    private String keycloakUrl;

    @Value("${nosy.client.clientSecret}")
    private String clientSecret;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.auth-server-url}")
    private String keycloakAdminUrl;

    @Value("${nosy.keycloak.admin.user}")
    private String keycloakAdminUser;

    @Value("${nosy.keycloak.admin.password}")
    private String keycloakAdminPassword;

    @Value("${keycloak.realm}")
    private String keycloakRealm;
    @Value("${nosy.client.role}")
    private String nosyClientRole;


    private TokenCollection tokenCollection;
    @Autowired
    public KeycloakConfigBean(TokenCollection tokenCollection){
        this.tokenCollection=tokenCollection;
    }

    public RealmResource getKeycloakUserResource() {

        Keycloak kc =
                KeycloakBuilder.builder()
                        .serverUrl(keycloakAdminUrl)
                        .realm(keycloakRealm)
                        .username(keycloakAdminUser)
                        .password(keycloakAdminPassword)
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                        .build();

        return kc.realm(keycloakRealm);
    }

    public boolean requestInterceptor(HttpPost post) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                return httpclient.execute(
                    post,
                    response -> {
                        ObjectMapper mapper = new ObjectMapper();

                        Map stringObjectMap =
                                mapper.readValue(response.getEntity().getContent(), Map.class);

                        return (boolean) stringObjectMap.get("active");
                    });
        } catch (IOException e) {
            return false;
        }
    }
    public TokenCollection getTokens(User user) throws IOException {
        HttpPost post=getPost(user);
        TokenCollection tokenCollectionCurrent= getTokenCollection(post);
        if (tokenCollectionCurrent == null || tokenCollectionCurrent.getAccessToken() == null) {
            throw new InvalidUsernameAndPasswordException();
        }
        return getTokenCollection(post);
    }
    public TokenCollection getTokenCollection(HttpPost post) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            return httpclient.execute(
                    post,
                    response -> {
                        ObjectMapper mapper = new ObjectMapper();
                        int status = response.getStatusLine().getStatusCode();

                        if (status >= 200 && status < 300) {
                            tokenCollection =
                                    mapper.readValue(response.getEntity().getContent(), TokenCollection.class);

                            return tokenCollection;

                        } else {
                            return null;
                        }
                    });
        }
    }

    private HttpPost getPost(User user) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(keycloakUrl);
        List<NameValuePair> params =
                asList(
                        new BasicNameValuePair(GRANT_TYPE_STRING, grantType),
                        new BasicNameValuePair(CLIENT_ID_STRING, clientId),
                        new BasicNameValuePair("username", user.getEmail()),
                        new BasicNameValuePair("password", user.getPassword()),
                        new BasicNameValuePair(CLIENT_SECRET_STRING, clientSecret));

        post.setEntity(new UrlEncodedFormEntity(params));
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        return post;
    }


    public boolean getPostForAuthentication(String token)  {
        HttpPost post = new HttpPost(keycloakUrl + "/introspect");
        String tokenString = "token";
        List<NameValuePair> params =
                asList(
                        new BasicNameValuePair(CLIENT_ID_STRING, clientId),
                        new BasicNameValuePair(tokenString, token),
                        new BasicNameValuePair(CLIENT_SECRET_STRING, clientSecret));
        try {
            post.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        return requestInterceptor(post);
    }

    public String getClientId(){
        return clientId;
    }

    public String getNosyClientRole() {
        return nosyClientRole;
    }
}
