package com.hamza.veterinarysystemdemo.models;

public class userProblemsModel {
    public int id;
    //int id;
    public String description, time, date, image, name, profileimagepost;

    public userProblemsModel(int id, String description, String time, String date, String image, String name, String profileimagepost) {
        this.id = id;
        this.description = description;
        this.time = time;
        this.date = date;
        this.image = image;
        this.name = name;
        this.profileimagepost = profileimagepost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileimagepost() {
        return profileimagepost;
    }

    public void setProfileimagepost(String profileimagepost) {
        this.profileimagepost = profileimagepost;
    }
}
