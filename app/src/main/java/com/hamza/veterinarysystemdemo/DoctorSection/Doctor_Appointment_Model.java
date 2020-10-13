package com.hamza.veterinarysystemdemo.DoctorSection;

public class Doctor_Appointment_Model {
    public int id;
    public String name,image,tehseelplace,lat,lang,phone;



    public Doctor_Appointment_Model(int id, String name, String image, String tehseelplace, String lat, String lang, String phone) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.tehseelplace = tehseelplace;
        this.lat = lat;
        this.lang = lang;
        this.phone=phone;
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
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
