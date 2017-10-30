package com.bhojnalya.vikas.app.model;

import java.io.Serializable;

/**
 * Created by RaviAgrawal on 17-08-2017.
 */

public class ContactPerson implements Serializable {
    public String name;
    public String contact;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
