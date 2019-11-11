package tech.nosy.nosyemail.nosyemail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.nosy.nosyemail.nosyemail.dto.EmailTemplateDto;
import tech.nosy.nosyemail.nosyemail.dto.FeedDto;
import tech.nosy.nosyemail.nosyemail.dto.InputSystemDto;
import tech.nosy.nosyemail.nosyemail.model.EmailProviderProperties;
import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;
import tech.nosy.nosyemail.nosyemail.model.ReadyEmail;
import tech.nosy.nosyemail.nosyemail.service.EmailTemplateService;
import tech.nosy.nosyemail.nosyemail.service.FeedService;
import tech.nosy.nosyemail.nosyemail.service.InputSystemService;
import tech.nosy.nosyemail.nosyemail.utils.EmailTemplateMapper;
import tech.nosy.nosyemail.nosyemail.utils.FeedMapper;
import tech.nosy.nosyemail.nosyemail.utils.InputSystemMapper;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class EmailAdminController {

  private EmailTemplateService emailTemplateService;
  private InputSystemService inputSystemService;
  private FeedService feedService;

  @Autowired
  public EmailAdminController(
          EmailTemplateService emailTemplateService,
          InputSystemService inputSystemService,
          FeedService feedService
  ) {
    this.emailTemplateService = emailTemplateService;
    this.inputSystemService = inputSystemService;
    this.feedService = feedService;
  }

  @PostMapping(value = "/input-systems/{inputSystemId}/email-templates/{emailTemplateId}/post")
  public ResponseEntity<EmailTemplateDto> emailTemplatePost(
      @PathVariable String inputSystemId,
      @PathVariable String emailTemplateId,
      @RequestBody EmailProviderProperties emailProviderProperties,
      Principal principal) {

    return new ResponseEntity<>(
        EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplateService.postEmailTemplate(
            inputSystemId, emailTemplateId, emailProviderProperties, principal.getName())),
        HttpStatus.OK);
  }

  @PostMapping(value = "/email/post")
  public ResponseEntity<EmailTemplateDto> emailPost(@RequestBody ReadyEmail readyEmail) {
      return new ResponseEntity<>(
              EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplateService.postEmail(readyEmail)), HttpStatus.OK);
  }

  @PostMapping(value = "/input-systems", consumes = "application/json")
  public ResponseEntity<InputSystemDto> newType(@RequestBody InputSystemDto inputSystemDto, Principal principal) {
    return new ResponseEntity<>( InputSystemMapper.INSTANCE.toInputSystemDto(inputSystemService.saveInputSystem(
        InputSystemMapper.INSTANCE.toInputSystem(inputSystemDto), principal.getName())), HttpStatus.CREATED);
  }

  @GetMapping(value = "/input-systems")
  public ResponseEntity<Set<InputSystemDto>> getInputSystems(Principal principal) {
    return new ResponseEntity<>(
            InputSystemMapper.INSTANCE.toInputSystemDtoSet(inputSystemService.getListOfInputSystems(principal.getName())), HttpStatus.OK);
  }

  @GetMapping(value = "/input-systems/email-providers")
  public ResponseEntity<List<String>> getEmailAllProviders(Principal principal) {
    return new ResponseEntity<>(emailTemplateService.getAllEmailProviders(), HttpStatus.OK);
  }

  @PutMapping(value = "/input-systems/{inputSystemId}")
  public ResponseEntity<InputSystemDto> updateInputSystemName(
      @PathVariable String inputSystemId,
      @RequestBody InputSystemDto inputSystemDto,
      Principal principal) {

    return new ResponseEntity<>(
            InputSystemMapper.INSTANCE.toInputSystemDto(inputSystemService.updateInputSystemStatus(
            inputSystemId, InputSystemMapper.INSTANCE.toInputSystem(inputSystemDto), principal.getName())),
        HttpStatus.OK);
  }

  @PostMapping(value = "/input-systems/{inputSystemId}/email-templates")
  public ResponseEntity<EmailTemplateDto> newEmailTemplate(Principal principal,
                                                           @PathVariable String inputSystemId,
                                                           @RequestBody EmailTemplateDto emailTemplateDto
      ) {
    return new ResponseEntity<>( EmailTemplateMapper.INSTANCE.toEmailTemplateDto(
        emailTemplateService.newEmailTemplate(
                EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto),
            inputSystemId,
            principal.getName())),
        HttpStatus.CREATED);
  }

  @GetMapping(value = "/input-systems/{inputSystemId}/email-templates/{emailTemplateId}")
  public ResponseEntity<EmailTemplate> getEmailTemplateByInputSystemAndEmailTemplateId(
      @PathVariable String inputSystemId,
      @PathVariable String emailTemplateId,
      Principal principal) {
    return new ResponseEntity<>(
        emailTemplateService.getEmailTemplateById(
            emailTemplateId, inputSystemId, principal.getName()),
        HttpStatus.OK);
  }

  @PutMapping(value = "/input-systems/{inputSystemId}/email-templates/{emailTemplateId}")
  public ResponseEntity<EmailTemplateDto> updateEmailTemplate(
      @PathVariable String inputSystemId,
      @PathVariable String emailTemplateId,
      @RequestBody EmailTemplateDto emailTemplateDto,
      Principal principal) {
    return new ResponseEntity<>(  EmailTemplateMapper.INSTANCE.toEmailTemplateDto(
        emailTemplateService.updateEmailTemplate(
                EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto),
            inputSystemId,
            emailTemplateId,
            principal.getName())),
        HttpStatus.OK);
  }

  @GetMapping(value = "/input-systems/{inputSystemId}/email-templates")
  public ResponseEntity<List<EmailTemplateDto>> getEmailTemplates(
          @PathVariable String inputSystemId, Principal principal) {
    return new ResponseEntity<>(EmailTemplateMapper.INSTANCE.toEmailTemplateDtoList(
        emailTemplateService.getListOfEmailTemplates(inputSystemId, principal.getName())),
        HttpStatus.OK);
  }

  @DeleteMapping(value = "/input-systems/{inputSystemId}/email-templates/{emailTemplateId}")
  public ResponseEntity<String> deleteEmailTemplate(
      @PathVariable String inputSystemId,
      @PathVariable String emailTemplateId,
      Principal principal) {
    emailTemplateService.deleteEmailTemplate(inputSystemId, emailTemplateId, principal.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(value = "/input-systems/{inputSystemId}")
  public ResponseEntity<String> deleteInputSystem(
          @PathVariable String inputSystemId, Principal principal) {
    inputSystemService.deleteInputSystem(inputSystemId, principal.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping(value = "/input-systems/{inputSystemId}/email-templates/{emailTemplateId}/feeds/{feedId}")
  public ResponseEntity<EmailTemplateDto> addFeedToEmailTemplate(
          @PathVariable String inputSystemId,
          @PathVariable String emailTemplateId,
          @PathVariable String feedId,
          Principal principal
  ) {
    return new ResponseEntity<>(EmailTemplateMapper.INSTANCE.toEmailTemplateDto(
            emailTemplateService.addFeedToEmailTemplate(
                    inputSystemId,
                    emailTemplateId,
                    feedId,
                    principal.getName()
            )), HttpStatus.OK);
  }

  @PostMapping(value = "/input-systems/{inputSystemId}/feeds")
  public ResponseEntity<FeedDto> newFeed(
          @PathVariable String inputSystemId,
          @RequestBody FeedDto feedDto,
          Principal principal
  ) {
      return new ResponseEntity<>(
              FeedMapper.INSTANCE.toFeedDto(feedService.newFeed(
                      inputSystemId,
                      FeedMapper.INSTANCE.toFeed(feedDto),
                      principal.getName()
              )), HttpStatus.CREATED
      );
  }

  @GetMapping(value = "/input-systems/{inputSystemId}/feeds")
  public ResponseEntity<List<FeedDto>> getFeeds(@PathVariable String inputSystemId, Principal principal) {
    return new ResponseEntity<>(
            FeedMapper.INSTANCE.toFeedDtoList(feedService.getListOfFeeds(
                    inputSystemId, principal.getName()
            )), HttpStatus.OK
    );
  }

  @GetMapping(value = "/input-systems/{inputSystemId}/feeds/{feedId}")
  public ResponseEntity<FeedDto> getFeedByInputSystemIdAndFeedId(
          @PathVariable String inputSystemId,
          @PathVariable String feedId
  ) {
    return new ResponseEntity<>(
            FeedMapper.INSTANCE.toFeedDto(feedService.getFeedByInputSystemIdAndFeedId(
                    inputSystemId,
                    feedId
            )), HttpStatus.OK
    );
  }

  @PutMapping(value = "/input-systems/{inputSystemId}/feeds/{feedId}")
  public ResponseEntity<FeedDto> updateFeed(
          @PathVariable String inputSystemId,
          @PathVariable String feedId,
          @RequestBody FeedDto feedDto,
          Principal principal
  ) {
    return new ResponseEntity<>(
            FeedMapper.INSTANCE.toFeedDto(feedService.updateFeed(
                    inputSystemId,
                    feedId,
                    FeedMapper.INSTANCE.toFeed(feedDto),
                    principal.getName()
            )), HttpStatus.OK
    );
  }

  @DeleteMapping(value = "/input-systems/{inputSystemId}/feeds/{feedId}")
  public ResponseEntity<String> deleteFeed(
          @PathVariable String inputSystemId,
          @PathVariable String feedId,
          Principal principal
  ) {
    feedService.deleteFeed(inputSystemId, feedId, principal.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping(value = "/input-systems/{inputSystemId}/feeds/{feedId}/subscriptions")
  public ResponseEntity<FeedDto> subscribeToFeed(
          @PathVariable String inputSystemId,
          @PathVariable String feedId,
          Principal principal
  ) {
      return new ResponseEntity<>(
              FeedMapper.INSTANCE.toFeedDto(feedService.subscribeToFeed(
                      inputSystemId,
                      feedId,
                      principal.getName()
              )), HttpStatus.OK
      );
  }

  @DeleteMapping(value = "/input-systems/{inputSystemId}/feeds/{feedId}/subscriptions")
  public ResponseEntity<FeedDto> unsubscribeToFeed(
          @PathVariable String inputSystemId,
          @PathVariable String feedId,
          Principal principal
  ) {
    return new ResponseEntity<>(
            FeedMapper.INSTANCE.toFeedDto(feedService.unsubscribeToFeed(
                    inputSystemId,
                    feedId,
                    principal.getName()
            )), HttpStatus.NO_CONTENT
    );
  }

  @PostMapping(value = "/input-systems/{inputSystemId}/feeds/{feedId}/post")
  public ResponseEntity<FeedDto> postFeed(
          @PathVariable String inputSystemId,
          @PathVariable String feedId,
          @RequestBody EmailProviderProperties emailProviderProperties,
          Principal principal
  ) {
    return new ResponseEntity<>(
            FeedMapper.INSTANCE.toFeedDto(feedService.postFeed(
                    inputSystemId,
                    feedId,
                    emailProviderProperties,
                    principal.getName()
            )), HttpStatus.OK
    );
  }

}
