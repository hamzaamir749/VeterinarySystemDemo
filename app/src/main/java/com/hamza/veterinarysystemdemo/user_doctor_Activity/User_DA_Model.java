package com.hamza.veterinarysystemdemo.user_doctor_Activity;

public class User_DA_Model {
    public int id;
    public String name,image,tehseelplace;

    public User_DA_Model(int id, String name, String image, String tehseelplace) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.tehseelplace = tehseelplace;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTehseelplace() {
        return tehseelplace;
    }

    public void setTehseelplace(String tehseelplace) {
        this.tehseelplace = tehseelplace;
    }
}
