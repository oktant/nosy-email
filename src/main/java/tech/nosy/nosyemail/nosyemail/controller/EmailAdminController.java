package tech.nosy.nosyemail.nosyemail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.nosy.nosyemail.nosyemail.dto.EmailTemplateDto;
import tech.nosy.nosyemail.nosyemail.dto.InputSystemDto;
import tech.nosy.nosyemail.nosyemail.model.EmailProviderProperties;
import tech.nosy.nosyemail.nosyemail.model.EmailTemplate;
import tech.nosy.nosyemail.nosyemail.model.ReadyEmail;
import tech.nosy.nosyemail.nosyemail.service.EmailTemplateService;
import tech.nosy.nosyemail.nosyemail.service.InputSystemService;
import tech.nosy.nosyemail.nosyemail.utils.EmailTemplateMapper;
import tech.nosy.nosyemail.nosyemail.utils.InputSystemMapper;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class EmailAdminController {

  private EmailTemplateService emailTemplateService;
  private InputSystemService inputSystemService;

  @Autowired
  public EmailAdminController(
          EmailTemplateService emailTemplateService,
          InputSystemService inputSystemService
  ) {
    this.emailTemplateService = emailTemplateService;
    this.inputSystemService = inputSystemService;
  }

  @PostMapping(value="/input-systems")
  public ResponseEntity<InputSystemDto> newType(@Valid @RequestBody InputSystemDto inputSystemDto, Principal principal) {
    return new ResponseEntity<>( InputSystemMapper.INSTANCE.toInputSystemDto(inputSystemService.saveInputSystem(
            InputSystemMapper.INSTANCE.toInputSystem(inputSystemDto), principal.getName())), HttpStatus.CREATED);
  }

  @GetMapping(value = "/input-systems")
  public ResponseEntity<Set<InputSystemDto>> getInputSystems(Principal principal) {
    return new ResponseEntity<>(
            InputSystemMapper.INSTANCE.toInputSystemDtoSet(inputSystemService.getListOfInputSystems(principal.getName())), HttpStatus.OK);
  }

  @DeleteMapping(value = "/input-systems/{inputSystemId}")
  public ResponseEntity<String> deleteInputSystem(
          @PathVariable String inputSystemId, Principal principal) {
    inputSystemService.deleteInputSystem(inputSystemId, principal.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

  @GetMapping(value = "/email-providers")
  public ResponseEntity<List<String>> getEmailAllProviders(Principal principal) {
    return new ResponseEntity<>(emailTemplateService.getAllEmailProviders(), HttpStatus.OK);
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

  @GetMapping(value = "/input-systems/{inputSystemId}/email-templates")
  public ResponseEntity<List<EmailTemplateDto>> getEmailTemplates(
          @PathVariable String inputSystemId, Principal principal) {
    return new ResponseEntity<>(EmailTemplateMapper.INSTANCE.toEmailTemplateDtoList(
            emailTemplateService.getListOfEmailTemplates(inputSystemId, principal.getName())),
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
  /////////////////////////////////////////////////////FIXED

  @PostMapping(value = "/email/post")
  public ResponseEntity<EmailTemplateDto> emailPost(@RequestBody ReadyEmail readyEmail) {
      return new ResponseEntity<>(
              EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplateService.postEmail(readyEmail)), HttpStatus.OK);
  }

  @DeleteMapping(value = "/input-systems/{inputSystemId}/email-templates/{emailTemplateId}")
  public ResponseEntity<String> deleteEmailTemplate(
      @PathVariable String inputSystemId,
      @PathVariable String emailTemplateId,
      Principal principal) {
    emailTemplateService.deleteEmailTemplate(inputSystemId, emailTemplateId, principal.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
