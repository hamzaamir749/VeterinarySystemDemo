package com.hamza.veterinarysystemdemo.MedicineHistory;

public class PendingMedicineModelClass {
    public String medicineid,storeid,medicinename,medicineprice,medicinequantity,medicineimage,time,date;

    public PendingMedicineModelClass(String medicineid, String storeid, String medicinename, String medicineprice, String medicinequantity, String medicineimage, String time, String date) {
        this.medicineid = medicineid;
        this.storeid = storeid;
        this.medicinename = medicinename;
        this.medicineprice = medicineprice;
        this.medicinequantity = medicinequantity;
        this.medicineimage = medicineimage;
        this.time = time;
        this.date = date;
    }

    public String getMedicineid() {
        return medicineid;
    }

    public void setMedicineid(String medicineid) {
        this.medicineid = medicineid;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getMedicinename() {
        return medicinename;
    }

    public void setMedicinename(String medicinename) {
        this.medicinename = medicinename;
    }

    public String getMedicineprice() {
        return medicineprice;
    }

    public void setMedicineprice(String medicineprice) {
        this.medicineprice = medicineprice;
    }

    public String getMedicinequantity() {
        return medicinequantity;
    }

    public void setMedicinequantity(String medicinequantity) {
        this.medicinequantity = medicinequantity;
    }

    public String getMedicineimage() {
        return medicineimage;
    }

    public void setMedicineimage(String medicineimage) {
        this.medicineimage = medicineimage;
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
