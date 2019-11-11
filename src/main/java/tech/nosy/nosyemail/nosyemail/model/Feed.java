package tech.nosy.nosyemail.nosyemail.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import tech.nosy.nosyemail.nosyemail.exceptions.FeedNameInvalidException;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@DynamicInsert
@Table(name = "feed", uniqueConstraints = @UniqueConstraint(columnNames = {"feedName", "input_system_id"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Feed {

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @NotNull
    private String feedId;

    @NotNull
    private String feedName;

    @NotNull
    @ElementCollection
    @JoinTable(name = "feed_subscribers", joinColumns = @JoinColumn(name = "feed_id"))
    private Set<@NotEmpty @Email String> feedSubscribers;

    @NotNull
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "input_system_id")
    private InputSystem inputSystem;

    @ManyToOne
    @JoinColumn(name = "email_template_id")
    private EmailTemplate emailTemplate;

    @PrePersist
    protected void onCreate() {
        if (feedName == null || feedName.isEmpty()) {
            throw new FeedNameInvalidException();
        }
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public Set<String> getFeedSubscribers() {
        return feedSubscribers;
    }

    public void setFeedSubscribers(Set<String> feedSubscribers) {
        this.feedSubscribers = feedSubscribers;
    }

    public InputSystem getInputSystem() {
        return inputSystem;
    }

    public void setInputSystem(InputSystem inputSystem) {
        this.inputSystem = inputSystem;
    }

    public EmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(EmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }
}
