package com.hamza.veterinarysystemdemo.UserSection.AppointmentsDoctorHistory;

public class AppointmentModelClass {
    public int id;
    public String name,image,phone,address,time,date,activity;

    public AppointmentModelClass(int id, String name, String image, String phone, String address, String time, String date) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.address = address;
        this.time = time;
        this.date = date;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
