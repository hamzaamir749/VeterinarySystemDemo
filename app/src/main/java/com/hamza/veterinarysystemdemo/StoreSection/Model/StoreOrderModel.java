package com.hamza.veterinarysystemdemo.StoreSection.Model;

public class StoreOrderModel {
    public int id;
    public String name,image,time,phone,address;

    public StoreOrderModel(int id, String name, String image, String time, String phone, String address) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.time = time;
        this.phone=phone;
        this.address=address;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
