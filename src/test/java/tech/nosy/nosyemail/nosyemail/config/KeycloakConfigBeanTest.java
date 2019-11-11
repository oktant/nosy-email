package tech.nosy.nosyemail.nosyemail.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class KeycloakConfigBeanTest {


    @InjectMocks
    private KeycloakConfigBean keycloakConfigBean;

    @Before
    public void setUp(){
        ReflectionTestUtils.setField(keycloakConfigBean, "keycloakUrl", "asdasd");
        ReflectionTestUtils.setField(keycloakConfigBean, "clientSecret", "test-client");
        ReflectionTestUtils.setField(keycloakConfigBean, "clientId", "dasda");
        ReflectionTestUtils.setField(keycloakConfigBean, "keycloakAdminUrl", "dasdadsdass");
        ReflectionTestUtils.setField(keycloakConfigBean, "keycloakAdminUser", "dasdadsdass");
        ReflectionTestUtils.setField(keycloakConfigBean, "keycloakAdminPassword", "dasdadsdass");
        ReflectionTestUtils.setField(keycloakConfigBean, "keycloakRealm", "dasdadsdass");
        ReflectionTestUtils.setField(keycloakConfigBean, "nosyClientRole", "nosy-role");


    }

    @Test(expected = Test.None.class)
    public void getKeycloakUserResource() {
        keycloakConfigBean.getKeycloakUserResource();
    }



    @Test
    public void getClientId() {
        assertEquals("dasda",keycloakConfigBean.getClientId());
    }

    @Test
    public void getNosyClientRole() {
        assertEquals("nosy-role",keycloakConfigBean.getNosyClientRole());
    }
}
