package com.sda.client;

import java.io.Serializable;
import java.util.List;

public class ContactList implements Serializable {

    private List<String > listContact;
    private String messageBody;

    public ContactList(List<String> listContact, String messageBody) {
        this.listContact = listContact;
        this.messageBody = messageBody;
    }

    public List<String> getListContact() {
        return listContact;
    }

    public void setListContact(List<String> listContact) {
        this.listContact = listContact;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
