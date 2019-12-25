//package tech.nosy.nosyemail.nosyemail.model;
//
//import org.hibernate.annotations.GenericGenerator;
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//public class EmailCollection {
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    @Id
//    @NotNull
//    private String emailCollectionId;
//
//    @Column(unique = true) @NotNull private String emailCollectionName;
//
//    @NotNull
//    private String username;
//
//    @ElementCollection
//    @JoinTable(name = "email_collection_emails", joinColumns = @JoinColumn(name = "email_collection_id"))
//    @NotNull private List<String> emailCollectionEmails = new ArrayList<>();
//
//    public String getEmailCollectionId() {
//        return emailCollectionId;
//    }
//
//    public void setEmailCollectionId(String emailCollectionId) {
//        this.emailCollectionId = emailCollectionId;
//    }
//
//    public String getEmailCollectionName() {
//        return emailCollectionName;
//    }
//
//    public void setEmailCollectionName(String emailCollectionName) {
//        this.emailCollectionName = emailCollectionName;
//    }
//
//    public List<String> getEmailCollectionEmails() {
//        return emailCollectionEmails;
//    }
//
//    public void setEmailCollectionEmails(List<String> emailCollectionEmails) {
//        this.emailCollectionEmails = emailCollectionEmails;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//}
