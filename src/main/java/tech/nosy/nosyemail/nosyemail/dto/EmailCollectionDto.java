package tech.nosy.nosyemail.nosyemail.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class EmailCollectionDto {
    @NotNull private String id;
    @NotNull private String name;
    private List<String> emails = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
