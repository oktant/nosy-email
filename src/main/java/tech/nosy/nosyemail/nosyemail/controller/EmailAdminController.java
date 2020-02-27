package tech.nosy.nosyemail.nosyemail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.nosy.nosyemail.nosyemail.dto.EmailConfigDto;
import tech.nosy.nosyemail.nosyemail.dto.EmailTemplateDto;
import tech.nosy.nosyemail.nosyemail.dto.InputSystemDto;
import tech.nosy.nosyemail.nosyemail.model.EmailProviderProperties;
import tech.nosy.nosyemail.nosyemail.model.ReadyEmail;
import tech.nosy.nosyemail.nosyemail.service.EmailConfigService;
import tech.nosy.nosyemail.nosyemail.service.EmailTemplateService;
import tech.nosy.nosyemail.nosyemail.service.InputSystemService;
import tech.nosy.nosyemail.nosyemail.utils.EmailConfigMapper;
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
    private EmailConfigService emailConfigService;

    @Autowired
    public EmailAdminController(
            EmailTemplateService emailTemplateService,
            InputSystemService inputSystemService,
            EmailConfigService emailConfigService
    ) {
        this.emailTemplateService = emailTemplateService;
        this.inputSystemService = inputSystemService;
        this.emailConfigService = emailConfigService;
    }

    @PostMapping(value = "/input-systems")
    public ResponseEntity<InputSystemDto> newType(@Valid @RequestBody InputSystemDto inputSystemDto, Principal principal) {
        return new ResponseEntity<>(InputSystemMapper.INSTANCE.toInputSystemDto(inputSystemService.saveInputSystem(
                InputSystemMapper.INSTANCE.toInputSystem(inputSystemDto), principal.getName())), HttpStatus.CREATED);
    }

    @GetMapping(value = "/input-systems")
    public ResponseEntity<Set<InputSystemDto>> getInputSystems(@RequestParam(required = false) String name, Principal principal) {
        return new ResponseEntity<>(
                InputSystemMapper.INSTANCE.toInputSystemDtoSet(inputSystemService.getListOfInputSystems(name, principal.getName())), HttpStatus.OK);
    }

    @DeleteMapping(value = "/input-systems/{inputSystemName}")
    public ResponseEntity<String> deleteInputSystem(
            @PathVariable String inputSystemName, Principal principal) {
        inputSystemService.deleteInputSystem(inputSystemName, principal.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/input-systems/{inputSystemName}/email-templates")
    public ResponseEntity<EmailTemplateDto> newEmailTemplate(Principal principal,
                                                             @PathVariable String inputSystemName,
                                                             @Valid @RequestBody EmailTemplateDto emailTemplateDto
    ) {
        return new ResponseEntity<>(EmailTemplateMapper.INSTANCE.toEmailTemplateDto(
                emailTemplateService.newEmailTemplate(
                        EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto),
                        inputSystemName,
                        principal.getName())),
                HttpStatus.CREATED);
    }

    @GetMapping(value = "/email-providers")
    public ResponseEntity<List<String>> getEmailAllProviders() {
        return new ResponseEntity<>(emailTemplateService.getAllEmailProviders(), HttpStatus.OK);
    }

    @GetMapping(value = "/input-systems/{inputSystemName}/email-templates/{emailTemplateName}")
    public ResponseEntity<EmailTemplateDto> getEmailTemplateByInputSystemAndEmailTemplateName(
            @PathVariable String inputSystemName,
            @PathVariable String emailTemplateName,
            Principal principal) {
        return new ResponseEntity<>(EmailTemplateMapper.INSTANCE.toEmailTemplateDto(
                emailTemplateService.getEmailTemplateByName(
                        emailTemplateName, inputSystemName, principal.getName())),
                HttpStatus.OK);
    }

    @GetMapping(value = "/input-systems/{inputSystemName}/email-templates")
    public ResponseEntity<List<EmailTemplateDto>> getEmailTemplates(
            @PathVariable String inputSystemName, Principal principal) {
        return new ResponseEntity<>(EmailTemplateMapper.INSTANCE.toEmailTemplateDtoList(
                emailTemplateService.getListOfEmailTemplates(inputSystemName, principal.getName())),
                HttpStatus.OK);
    }

    @PutMapping(value = "/input-systems/{inputSystemName}/email-templates/{emailTemplateName}")
    public ResponseEntity<EmailTemplateDto> updateEmailTemplate(
            @PathVariable String inputSystemName,
            @PathVariable String emailTemplateName,
            @Valid @RequestBody EmailTemplateDto emailTemplateDto,
            Principal principal) {
        return new ResponseEntity<>(EmailTemplateMapper.INSTANCE.toEmailTemplateDto(
                emailTemplateService.updateEmailTemplate(
                        EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto),
                        inputSystemName,
                        emailTemplateName,
                        principal.getName())),
                HttpStatus.OK);
    }

    @PostMapping(value = "/email/post")
    public ResponseEntity<EmailTemplateDto> emailPost(@RequestBody ReadyEmail readyEmail) {
        return new ResponseEntity<>(
                EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplateService.postEmail(readyEmail)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/input-systems/{inputSystemName}/email-templates/{emailTemplateName}")
    public ResponseEntity<String> deleteEmailTemplate(
            @PathVariable String inputSystemName,
            @PathVariable String emailTemplateName,
            Principal principal) {
        emailTemplateService.deleteEmailTemplate(inputSystemName, emailTemplateName, principal.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/input-systems/{inputSystemName}/email-templates/{emailTemplateName}/post")
    public ResponseEntity<EmailTemplateDto> emailTemplatePost(
            @PathVariable String inputSystemName,
            @PathVariable String emailTemplateName,
            @RequestBody EmailProviderProperties emailProviderProperties,
            Principal principal) {

        return new ResponseEntity<>(
                EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplateService.postEmailTemplate(
                        inputSystemName, emailTemplateName, emailProviderProperties, principal.getName())),
                HttpStatus.OK);
    }


    @PostMapping(value="/email-configs")
    public ResponseEntity<EmailConfigDto> postEmailConfig(Principal principal, @RequestBody EmailConfigDto emailConfigDto ){
        return  new ResponseEntity<>(EmailConfigMapper.INSTANCE.toEmailConfigDto(emailConfigService.
                saveEmailConfig(principal.getName(), EmailConfigMapper.INSTANCE.toEmailConfig(emailConfigDto)))
                , HttpStatus.CREATED);

    }
    @GetMapping(value="/email-configs")
    public ResponseEntity<List<EmailConfigDto>> getConfigs(Principal principal){
        return new ResponseEntity<>(EmailConfigMapper.INSTANCE.toEmailConfigDtoList
                (emailConfigService.getConfigs(principal.getName())), HttpStatus.OK);
    }
    @GetMapping(value="/email-configs/{name}")
    public ResponseEntity<EmailConfigDto> getConfig(Principal principal,
                                                    @PathVariable("name") String name){
        return new ResponseEntity<>(EmailConfigMapper.INSTANCE.toEmailConfigDto
                (emailConfigService.getConfig(principal.getName(), name)), HttpStatus.OK);
    }

    @PutMapping(value="/email-configs/{name}")
    public ResponseEntity<EmailConfigDto> updateConfig(Principal principal,
                                                    @PathVariable("name") String name, @RequestBody EmailConfigDto emailConfigDto){

        return new ResponseEntity<>(EmailConfigMapper.INSTANCE.toEmailConfigDto
                (emailConfigService.updateConfig(principal.getName(),name, EmailConfigMapper.INSTANCE.toEmailConfig(emailConfigDto))), HttpStatus.OK);
    }
}
