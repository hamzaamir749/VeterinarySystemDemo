package com.hamza.veterinarysystemdemo.DoctorSection.DoctorAppointmentHistory;

public class DAHModel {
    public String image,address,phone,name,time,date;
    public int id;

    public DAHModel(int id,String image, String address, String phone, String name,String time,String date) {
        this.image = image;
        this.address = address;
        this.phone = phone;
        this.name = name;
        this.time=time;
        this.date=date;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
