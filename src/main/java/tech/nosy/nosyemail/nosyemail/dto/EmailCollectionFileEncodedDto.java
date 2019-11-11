package tech.nosy.nosyemail.nosyemail.dto;

import java.util.List;

public class EmailCollectionFileEncodedDto {

    private String name;
    private String data;
    private List<String> emails;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public void setData(String data) {
        this.data = data;
    }
}
