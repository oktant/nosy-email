package tech.nosy.nosyemail.nosyemail.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import tech.nosy.nosyemail.nosyemail.exceptions.FeedNameInvalidException;

@RunWith(MockitoJUnitRunner.class)
public class FeedTest {

    @Test(expected = FeedNameInvalidException.class)
    public void onCreate() {
        Feed feed = new Feed();
        feed.onCreate();
    }

    @Test(expected = FeedNameInvalidException.class)
    public void onCreateEmpty() {
        Feed feed = new Feed();
        feed.setFeedName("");
        feed.onCreate();
    }

    @Test(expected= Test.None.class)
    public void onCreateSuccess() {
        Feed feed = new Feed();
        feed.setFeedName("feedName");
        feed.onCreate();
    }

}
