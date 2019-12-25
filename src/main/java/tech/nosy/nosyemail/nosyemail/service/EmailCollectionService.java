//package tech.nosy.nosyemail.nosyemail.service;
//
//import org.apache.commons.codec.binary.Base64;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import tech.nosy.nosyemail.nosyemail.dto.EmailCollectionFileEncodedDto;
//import tech.nosy.nosyemail.nosyemail.exceptions.EmailCollectionDoesNotExistException;
//import tech.nosy.nosyemail.nosyemail.exceptions.UserNotExistsException;
//import tech.nosy.nosyemail.nosyemail.model.EmailCollection;
//import tech.nosy.nosyemail.nosyemail.repository.EmailCollectionRepository;
//import tech.nosy.nosyemail.nosyemail.repository.UserRepository;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class EmailCollectionService {
//    private EmailCollectionRepository emailCollectionRepository;
//    private UserRepository userRepository;
//
//    @Autowired
//    public EmailCollectionService(
//            EmailCollectionRepository emailCollectionRepository,
//            UserRepository userRepository) {
//        this.emailCollectionRepository = emailCollectionRepository;
//        this.userRepository = userRepository;
//    }
//
//    public EmailCollection createEmailCollection(EmailCollectionFileEncodedDto emailCollectionFileEncodedDto, String email) {
//        EmailCollection emailCollection = new EmailCollection();
//        emailCollection.setEmailCollectionName(emailCollectionFileEncodedDto.getName());
//        emailCollection.setEmailCollectionEmails(emailCollectionFileEncodedDto.getEmails());
//
//        Optional<User> user = userRepository.findById(email);
//        if (!user.isPresent()) {
//            throw new UserNotExistsException();
//        }
//        emailCollection.setUser(user.get());
//
//        return emailCollectionRepository.save(emailCollection);
//    }
//
//    public EmailCollection replaceEmailCollection(EmailCollectionFileEncodedDto emailCollectionFileEncodedDto) {
//        EmailCollection emailCollection = emailCollectionRepository.findByEmailCollectionName(emailCollectionFileEncodedDto.getName());
//        emailCollection.getEmailCollectionEmails().clear();
//        List<String> emails = parseBase64Data(emailCollectionFileEncodedDto.getData());
//        emails.forEach(e -> emailCollection.getEmailCollectionEmails().add(e));
//
//        return emailCollectionRepository.save(emailCollection);
//    }
//
//    public EmailCollection addToEmailCollection(EmailCollectionFileEncodedDto emailCollectionFileEncodedDto) {
//        EmailCollection emailCollection = emailCollectionRepository.findByEmailCollectionName(emailCollectionFileEncodedDto.getName());
//        List<String> emails = parseBase64Data(emailCollectionFileEncodedDto.getData());
//        emails.forEach(e -> emailCollection.getEmailCollectionEmails().add(e));
//
//        return emailCollectionRepository.save(emailCollection);
//    }
//
//    public List<String> parseBase64Data(String data) {
//        byte[] bytes = Base64.decodeBase64(data);
//        String completeData = new String(bytes);
//        String[] emails = completeData.split(",");
//        for (int i = 0; i < emails.length; i++) {
//            emails[i] = emails[i].trim();
//        }
//        return Arrays.asList(emails);
//    }
//
//    public EmailCollection getEmailCollectionById(String emailCollectionId) {
//        Optional<EmailCollection> optionalEmailCollection = emailCollectionRepository.findById(emailCollectionId);
//        if(optionalEmailCollection.isPresent()) {
//            return optionalEmailCollection.get();
//        } else {
//            throw new EmailCollectionDoesNotExistException();
//        }
//    }
//
//    public List<EmailCollection> getAllEmailCollections() {
//        return emailCollectionRepository.findAll();
//    }
//
//    public void deleteEmailCollectionById(String emailCollectionId) {
//        emailCollectionRepository.deleteById(emailCollectionId);
//    }
//}
