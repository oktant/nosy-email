//package tech.nosy.nosyemail.nosyemail.utils;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import tech.nosy.nosyemail.nosyemail.dto.FeedDto;
//import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;
//import tech.nosy.nosyemail.nosyemail.model.Feed;
//import tech.nosy.nosyemail.nosyemail.model.InputSystem;
//
//import java.util.Collections;
//
//import static org.junit.Assert.assertEquals;
//
//public class FeedMapperTest {
//
//    private Feed feed = new Feed();
//    private FeedDto feedDto = new FeedDto();
//
//    @Before
//    public void setUp(){
//        feed = new Feed();
//        feed.setFeedId("feedId");
//        feed.setFeedName("feedName");
//        feed.setFeedSubscribers(Collections.singleton("feedSubscriber"));
//        feed.setInputSystem(new InputSystem());
//        feed.setEmailTemplate(new EmailTemplate());
//
//        feedDto = new FeedDto();
//        feedDto.setId("feedDtoId");
//        feedDto.setName("feedDtoName");
//        feedDto.setSubscribers(Collections.singleton("feedDtoSubscriber"));
//        feedDto.setInputSystem(new InputSystem());
//        feedDto.setEmailTemplate(new EmailTemplate());
//    }
//
//    @Test
//    public void toFeedDto(){
//        Assert.assertEquals(feed.getFeedId(), FeedMapper.INSTANCE.toFeedDto(feed).getId());
//        Assert.assertEquals(feed.getFeedName(), FeedMapper.INSTANCE.toFeedDto(feed).getName());
//        Assert.assertEquals(feed.getFeedSubscribers(), FeedMapper.INSTANCE.toFeedDto(feed).getSubscribers());
//        Assert.assertEquals(feed.getInputSystem(), FeedMapper.INSTANCE.toFeedDto(feed).getInputSystem());
//        Assert.assertEquals(feed.getEmailTemplate(), FeedMapper.INSTANCE.toFeedDto(feed).getEmailTemplate());
//    }
//
//    @Test
//    public void toFeed(){
//        assertEquals(feedDto.getId(), FeedMapper.INSTANCE.toFeed(feedDto).getFeedId());
//        assertEquals(feedDto.getName(), FeedMapper.INSTANCE.toFeed(feedDto).getFeedName());
//        assertEquals(feedDto.getSubscribers(), FeedMapper.INSTANCE.toFeed(feedDto).getFeedSubscribers());
//        assertEquals(feedDto.getInputSystem(), FeedMapper.INSTANCE.toFeed(feedDto).getInputSystem());
//        assertEquals(feedDto.getEmailTemplate(), FeedMapper.INSTANCE.toFeed(feedDto).getEmailTemplate());
//    }
//
//}
