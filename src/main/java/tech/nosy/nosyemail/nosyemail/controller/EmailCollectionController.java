package tech.nosy.nosyemail.nosyemail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.nosy.nosyemail.nosyemail.dto.EmailCollectionDto;
import tech.nosy.nosyemail.nosyemail.dto.EmailCollectionFileEncodedDto;
import tech.nosy.nosyemail.nosyemail.exceptions.EmailCollectionDoesNotExistException;
import tech.nosy.nosyemail.nosyemail.service.EmailCollectionService;
import tech.nosy.nosyemail.nosyemail.utils.EmailCollectionMapper;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/email-groups")
public class EmailCollectionController {
    private EmailCollectionService emailCollectionService;

    @Autowired
    public EmailCollectionController(EmailCollectionService emailCollectionService) {
        this.emailCollectionService = emailCollectionService;
    }

    @PostMapping(value="/files")
    public ResponseEntity<EmailCollectionDto> uploadEmailCollection(
            @RequestBody EmailCollectionFileEncodedDto emailCollectionFileEncodedDto,
            Principal principal) {
        emailCollectionFileEncodedDto.setEmails(emailCollectionService.parseBase64Data(emailCollectionFileEncodedDto.getData()));
        return new ResponseEntity<>(EmailCollectionMapper.INSTANCE.toEmailCollectionDto(emailCollectionService
                .createEmailCollection(emailCollectionFileEncodedDto, principal.getName())), HttpStatus.CREATED);
    }
    @PostMapping
    public ResponseEntity<EmailCollectionDto> createEmailCollection(
            @RequestBody EmailCollectionFileEncodedDto emailCollectionFileEncodedDto,
            Principal principal) {
        return new ResponseEntity<>(EmailCollectionMapper.INSTANCE.toEmailCollectionDto(emailCollectionService
                .createEmailCollection(emailCollectionFileEncodedDto, principal.getName())), HttpStatus.CREATED);    }


    @PutMapping
    public ResponseEntity<EmailCollectionDto> replaceEmailCollection(
            @RequestBody EmailCollectionFileEncodedDto emailCollectionFileEncodedDto) {
        return new ResponseEntity<>(EmailCollectionMapper.INSTANCE.toEmailCollectionDto(emailCollectionService
        .replaceEmailCollection(emailCollectionFileEncodedDto)), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<EmailCollectionDto> addToEmailCollection(
            @RequestBody EmailCollectionFileEncodedDto emailCollectionFileEncodedDto) {
        return new ResponseEntity<>(EmailCollectionMapper.INSTANCE.toEmailCollectionDto(emailCollectionService
                .addToEmailCollection(emailCollectionFileEncodedDto)), HttpStatus.OK);
    }



    @GetMapping(value = "/{emailCollectionId}")
    public ResponseEntity<EmailCollectionDto> getEmailCollectionById(
            @PathVariable String emailCollectionId) {
        try {
            return new ResponseEntity<>(EmailCollectionMapper.INSTANCE.toEmailCollectionDto(emailCollectionService
                    .getEmailCollectionById(emailCollectionId)), HttpStatus.OK);
        } catch (EmailCollectionDoesNotExistException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<EmailCollectionDto>> getAllEmailCollections() {
        return new ResponseEntity<>(EmailCollectionMapper.INSTANCE.toEmailCollectionDtoList(emailCollectionService
                .getAllEmailCollections()), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{emailCollectionId}")
    public ResponseEntity<String> deleteEmailCollectionById(
            @PathVariable String emailCollectionId) {
        emailCollectionService.deleteEmailCollectionById(emailCollectionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
