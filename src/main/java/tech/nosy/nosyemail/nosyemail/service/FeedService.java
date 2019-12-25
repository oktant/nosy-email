package tech.nosy.nosyemail.nosyemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.exceptions.*;
import tech.nosy.nosyemail.nosyemail.model.*;
import tech.nosy.nosyemail.nosyemail.repository.FeedRepository;
import tech.nosy.nosyemail.nosyemail.repository.InputSystemRepository;

import java.util.Collections;
import java.util.List;

@Service
public class FeedService {

    private FeedRepository feedRepository;
    private InputSystemRepository inputSystemRepository;
    private EmailService emailService;

    @Value("${default.nosy.from.address}")
    private String defaultNosyFromAddress;

    @Autowired
    public FeedService(
            FeedRepository feedRepository,
            InputSystemRepository inputSystemRepository,
            EmailService emailService
    ) {
        this.feedRepository = feedRepository;
        this.inputSystemRepository = inputSystemRepository;
        this.emailService=emailService;
    }

//    public Feed newFeed(String inputSystemId, Feed feed, String email) {
//        if (!userRepository.findById(email).isPresent()) {
//            throw new NotAuthenticatedException();
//        }
//
//        InputSystem inputSystem = getInputSystemByEmailAndInputSystemId(email, inputSystemId);
//
//        if (feedRepository.findFeedByFeedNameAndInputSystemId(feed.getFeedName(), inputSystemId) != null) {
//            throw new FeedExistException();
//        }
//
//        feed.setInputSystem(inputSystem);
//        feedRepository.save(feed);
//
//        return feed;
//    }
//
//    public List<Feed> getListOfFeeds(String inputSystemId, String email) {
//        getInputSystemByEmailAndInputSystemId(email, inputSystemId);
//        return feedRepository.findFeedsByInputSystemId(inputSystemId);
//    }
//
//    public Feed getFeedByInputSystemIdAndFeedId(String inputSystemId, String feedId) {
//        Feed feed = feedRepository.findFeedByIdAndInputSystemId(feedId, inputSystemId);
//        if (feed == null) {
//            throw new FeedNotFoundException();
//        }
//        return feed;
//    }
//
//    public Feed updateFeed(String inputSystemId, String feedId, Feed feed, String email) {
//        if (!userRepository.findById(email).isPresent()) {
//            throw new NotAuthenticatedException();
//        }
//
//        Feed currentFeed = getFeedByInputSystemIdAndFeedId(inputSystemId, feedId);
//
//        if (feed == null) {
//            throw new FeedNotFoundException();
//        }
//
//        if (
//            feed.getFeedName() == null || feed.getFeedName().isEmpty() ||
//            (!currentFeed.getFeedName().equals(feed.getFeedName()) &&
//            feedRepository.findFeedByFeedNameAndInputSystemId(feed.getFeedName(), inputSystemId) != null)
//        ) {
//            throw new FeedNameInvalidException();
//        }
//
//        currentFeed.setFeedName(feed.getFeedName());
//        currentFeed.setFeedSubscribers(feed.getFeedSubscribers());
//        currentFeed.setEmailTemplate(feed.getEmailTemplate());
//        feedRepository.save(currentFeed);
//
//        return currentFeed;
//    }
//
//    public void deleteFeed(String inputSystemId, String feedId, String email) {
//        if (!userRepository.findById(email).isPresent()) {
//            throw new NotAuthenticatedException();
//        }
//
//        getInputSystemByEmailAndInputSystemId(email, inputSystemId);
//
//        Feed feed = getFeedByInputSystemIdAndFeedId(inputSystemId, feedId);
//        feedRepository.deleteById(feed.getFeedId());
//    }
//
//    public Feed subscribeToFeed(String inputSystemId, String feedId, String email) {
//        if (!userRepository.findById(email).isPresent()) {
//            throw new NotAuthenticatedException();
//        }
//
//        getInputSystemByEmailAndInputSystemId(email, inputSystemId);
//
//        Feed feed = getFeedByInputSystemIdAndFeedId(inputSystemId, feedId);
//        if (feed.getFeedSubscribers().contains(email)) {
//            throw new FeedAlreadySubscribedException();
//        }
//
//        feed.getFeedSubscribers().add(email);
//        feedRepository.save(feed);
//
//        return feed;
//    }
//
//    public Feed unsubscribeToFeed(String inputSystemId, String feedId, String email) {
//        if (!userRepository.findById(email).isPresent()) {
//            throw new NotAuthenticatedException();
//        }
//
//        getInputSystemByEmailAndInputSystemId(email, inputSystemId);
//
//        Feed feed = getFeedByInputSystemIdAndFeedId(inputSystemId, feedId);
//
//        if (!feed.getFeedSubscribers().contains(email)) {
//            throw new FeedNotSubscribedException();
//        }
//
//        feed.getFeedSubscribers().remove(email);
//        feedRepository.save(feed);
//
//        return feed;
//    }
//
//    public Feed postFeed(String inputSystemId, String feedId, EmailProviderProperties emailProviderProperties, String email) {
//        if (!userRepository.findById(email).isPresent()) {
//            throw new NotAuthenticatedException();
//        }
//
//        getInputSystemByEmailAndInputSystemId(email, inputSystemId);
//
//        Feed feed = getFeedByInputSystemIdAndFeedId(inputSystemId, feedId);
//
//        EmailTemplate emailTemplate = feed.getEmailTemplate();
//        if(emailTemplate == null) {
//            feed.setEmailTemplate(createEmailTemplateFromFeed(feed, email));
//        }
//
//        ReadyEmail readyEmail = createReadyEmailFromFeed(feed, emailProviderProperties, email);
//
//        emailService.handleReadyEmail(readyEmail);
//
//        return feed;
//    }
//
//    private ReadyEmail createReadyEmailFromFeed(Feed feed, EmailProviderProperties emailProviderProperties, String email) {
//        EmailTemplate emailTemplate = feed.getEmailTemplate();
//        emailTemplate.setEmailTemplateTo(Collections.singleton(email));
//        emailTemplate.setInputSystem(feed.getInputSystem());
//
//        ReadyEmail readyEmail = new ReadyEmail();
//        readyEmail.setEmailProviderProperties(emailProviderProperties);
//        readyEmail.setEmailTemplate(emailTemplate);
//
//        return readyEmail;
//    }
//
//    private EmailTemplate createEmailTemplateFromFeed(Feed feed, String email) {
//        EmailTemplate emailTemplate = new EmailTemplate();
//
//        emailTemplate.setEmailTemplateName(feed.getFeedName());
//        emailTemplate.setEmailTemplateSubject(feed.getFeedName());
//        emailTemplate.setEmailTemplateText(feed.getFeedName());
//        emailTemplate.setEmailTemplateFromAddress(defaultNosyFromAddress);
//        emailTemplate.setEmailTemplateFromProvider(EmailFromProvider.DEFAULT);
//        emailTemplate.setEmailTemplateTo(Collections.singleton(email));
//        emailTemplate.setFeeds(Collections.singleton(feed));
//        emailTemplate.setInputSystem(feed.getInputSystem());
//
//        return emailTemplate;
//    }

    private InputSystem getInputSystemByEmailAndInputSystemId(String email, String inputSystemId) {
        InputSystem inputSystem = inputSystemRepository.findByInputSystemIdAndEmail(email, inputSystemId);
        if (inputSystem == null) {
            throw new InputSystemNotFoundException();
        }
        return inputSystem;
    }

}
