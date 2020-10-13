package com.hamza.veterinarysystemdemo.Session;

public class SessionDetails {
    public int id;
    public String name = null;
    public String address = null;
    public String phone = null;
    public String profilepicture = null;
    public String type=null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SessionDetails() {
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String EmailAddress;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

}
