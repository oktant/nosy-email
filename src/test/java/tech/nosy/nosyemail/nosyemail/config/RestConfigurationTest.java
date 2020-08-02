package tech.nosy.nosyemail.nosyemail.config;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestConfigurationTest {

    @InjectMocks
    private RestConfiguration restConfiguration;

    @Test(expected = Test.None.class)
    public void corsFilterTest() {
        restConfiguration.corsFilter();
    }

    @Test(expected = Test.None.class)
    public void apiDocketTest() {
        restConfiguration.apiDocket();

    }
}