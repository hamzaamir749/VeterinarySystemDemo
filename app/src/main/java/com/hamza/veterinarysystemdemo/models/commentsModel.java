package com.hamza.veterinarysystemdemo.models;

public class commentsModel {
    String name,date,des,time,profilepiccomment;

    public commentsModel(String name, String date, String des, String time, String profilepiccomment) {
        this.name = name;
        this.date = date;
        this.des = des;
        this.time = time;
        this.profilepiccomment = profilepiccomment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfilepiccomment() {
        return profilepiccomment;
    }

    public void setProfilepiccomment(String profilepiccomment) {
        this.profilepiccomment = profilepiccomment;
    }
}
