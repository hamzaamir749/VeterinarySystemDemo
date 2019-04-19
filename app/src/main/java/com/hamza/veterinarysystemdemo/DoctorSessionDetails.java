package com.hamza.veterinarysystemdemo;

public class DoctorSessionDetails {
    private int id;
    private String name = null,address = null,phone = null, profilepicture = null,email = null;

    public DoctorSessionDetails() {

    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
