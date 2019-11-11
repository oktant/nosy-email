package tech.nosy.nosyemail.nosyemail.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tech.nosy.nosyemail.nosyemail.exceptions.*;
import tech.nosy.nosyemail.nosyemail.model.EmailProviderProperties;
import tech.nosy.nosyemail.nosyemail.model.Feed;
import tech.nosy.nosyemail.nosyemail.model.InputSystem;
import tech.nosy.nosyemail.nosyemail.model.User;
import tech.nosy.nosyemail.nosyemail.repository.FeedRepository;
import tech.nosy.nosyemail.nosyemail.repository.InputSystemRepository;
import tech.nosy.nosyemail.nosyemail.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedServiceTest {

    @InjectMocks
    private FeedService feedServiceMock;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private InputSystemRepository inputSystemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;
    private Feed feed;
    private InputSystem inputSystem;
    private User user;

    private String email = "test@nosy.tech";
    private String feedId = "feedId";
    private String inputSystemId = "inputSystemId";

    @Before
    public void beforeFeed() {
        user = new User();
        user.setEmail(email);
        user.setFirstName("Test");
        user.setLastName("Nosy");
        user.setPassword("dajsndjasn");

        inputSystem = new InputSystem();
        inputSystem.setInputSystemName("testInputSystem");
        inputSystem.setInputSystemId(inputSystemId);
        inputSystem.setUser(user);

        feed = new Feed();
        feed.setFeedId(feedId);
        feed.setFeedName("testFeed");
        feed.setInputSystem(inputSystem);
        feed.setFeedSubscribers(new HashSet<>());
    }

    @Test
    public void newFeed() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.newFeed(inputSystemId, feed, email).getFeedId());
    }

    @Test(expected = NotAuthenticatedException.class)
    public void newFeedNotAuthenticated() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        Assert.assertEquals(feedId, feedServiceMock.newFeed(inputSystemId, feed, email).getFeedId());
    }

    @Test(expected = InputSystemNotFoundException.class)
    public void newFeedInputSystemNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        Assert.assertEquals(feedId, feedServiceMock.newFeed(inputSystemId, feed, email).getFeedId());
    }

    @Test(expected = FeedExistException.class)
    public void newFeedFeedExists() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(feed).when(feedRepository).findFeedByFeedNameAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.newFeed(inputSystemId, feed, email).getFeedId());
    }

    @Test
    public void updateFeedTest() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(feed).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.updateFeed(inputSystemId, feedId, feed, email).getFeedId());
    }

    @Test(expected = NotAuthenticatedException.class)
    public void updateFeedNotAuthenticated() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        Assert.assertEquals(feedId, feedServiceMock.updateFeed(inputSystemId, feedId, feed, email).getFeedId());
    }

    @Test(expected = FeedNotFoundException.class)
    public void updateFeedNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        Assert.assertEquals(feedId, feedServiceMock.updateFeed(inputSystemId, feedId, null, email).getFeedId());
    }

    @Test(expected = FeedNotFoundException.class)
    public void updateFeedNull() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(feed).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.updateFeed(inputSystemId, feedId, null, email).getFeedId());
    }

    @Test(expected = FeedNameInvalidException.class)
    public void updateFeedNameInvalid() {
        Feed feedInvalid = new Feed();
        feedInvalid.setFeedName("FeedName");

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(feed).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        doReturn(feed).when(feedRepository).findFeedByFeedNameAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.updateFeed(inputSystemId, feedId, feedInvalid, email).getFeedId());
    }

    @Test
    public void getListOfFeeds() {
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(Collections.singletonList(feed)).when(feedRepository).findFeedsByInputSystemId(anyString());
        Assert.assertEquals(feedId, feedServiceMock.getListOfFeeds(inputSystemId, email).get(0).getFeedId());
    }

    @Test(expected = Test.None.class)
    public void deleteFeed() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(feed).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        feedServiceMock.deleteFeed(inputSystemId, feedId, email);
    }

    @Test(expected = NotAuthenticatedException.class)
    public void deleteFeedNotAuthenticated() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        feedServiceMock.deleteFeed(inputSystemId, feedId, email);
    }

    @Test
    public void subscribeToFeed() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(feed).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.subscribeToFeed(inputSystemId, feedId, email).getFeedId());
    }

    @Test(expected = NotAuthenticatedException.class)
    public void subscribeToFeedNotAuthenticated() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        Assert.assertEquals(feedId, feedServiceMock.subscribeToFeed(inputSystemId, feedId, email).getFeedId());
    }

    @Test(expected = InputSystemNotFoundException.class)
    public void subscribeToFeedInputSystemNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        Assert.assertEquals(feedId, feedServiceMock.subscribeToFeed(inputSystemId, feedId, email).getFeedId());
    }

    @Test(expected = FeedNotFoundException.class)
    public void subscribeToFeedFeedNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.subscribeToFeed(inputSystemId, feedId, email).getFeedId());
    }

    @Test(expected = FeedAlreadySubscribedException.class)
    public void subscribeToFeedAlreadySubscribed() {
        feed.getFeedSubscribers().add(email);

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(feed).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.subscribeToFeed(inputSystemId, feedId, email).getFeedId());
    }

    @Test
    public void unsubscribeToFeed() {
        feed.getFeedSubscribers().add(email);

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(feed).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.unsubscribeToFeed(inputSystemId, feedId, email).getFeedId());
    }

    @Test(expected = NotAuthenticatedException.class)
    public void unsubscribeToFeedNotAuthenticated() {
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        Assert.assertEquals(feedId, feedServiceMock.unsubscribeToFeed(inputSystemId, feedId, email).getFeedId());
    }

    @Test(expected = InputSystemNotFoundException.class)
    public void unsubscribeToFeedInputSystemNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        Assert.assertEquals(feedId, feedServiceMock.unsubscribeToFeed(inputSystemId, feedId, email).getFeedId());
    }

    @Test(expected = FeedNotFoundException.class)
    public void unsubscribeToFeedNotFound() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.unsubscribeToFeed(inputSystemId, feedId, email).getFeedId());
    }

    @Test(expected = FeedNotSubscribedException.class)
    public void unsubscribeToFeedNotSubscribed() {
        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(feed).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.unsubscribeToFeed(inputSystemId, feedId, email).getFeedId());
    }

    @Test
    public void postFeed() {
        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setUsername("EmailProviderUsername");
        emailProviderProperties.setPassword("EmailProviderPassword");
        emailProviderProperties.setPlaceholders(Collections.emptyList());

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        doReturn(inputSystem).when(inputSystemRepository).findByInputSystemIdAndEmail(anyString(), anyString());
        doReturn(feed).when(feedRepository).findFeedByIdAndInputSystemId(anyString(), anyString());
        Assert.assertEquals(feedId, feedServiceMock.postFeed(inputSystemId, feedId, emailProviderProperties, email).getFeedId());
    }

    @Test(expected = NotAuthenticatedException.class)
    public void postFeedNotAuthenticated() {
        EmailProviderProperties emailProviderProperties = new EmailProviderProperties();
        emailProviderProperties.setUsername("EmailProviderUsername");
        emailProviderProperties.setPassword("EmailProviderPassword");
        emailProviderProperties.setPlaceholders(Collections.emptyList());

        when(userRepository.findById(email)).thenReturn(Optional.empty());
        Assert.assertEquals(feedId, feedServiceMock.postFeed(inputSystemId, feedId, emailProviderProperties, email).getFeedId());
    }

}
