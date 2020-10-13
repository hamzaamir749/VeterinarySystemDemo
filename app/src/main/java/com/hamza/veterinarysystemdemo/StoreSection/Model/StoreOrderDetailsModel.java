package com.hamza.veterinarysystemdemo.StoreSection.Model;

public class StoreOrderDetailsModel {
    public String medicineName,medicinePrice,medicineQuantity,medicineImage;

    public StoreOrderDetailsModel(String medicineName, String medicinePrice, String medicineQuantity, String medicineImage) {
        this.medicineName = medicineName;
        this.medicinePrice = medicinePrice;
        this.medicineQuantity = medicineQuantity;
        this.medicineImage = medicineImage;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicinePrice() {
        return medicinePrice;
    }

    public void setMedicinePrice(String medicinePrice) {
        this.medicinePrice = medicinePrice;
    }

    public String getMedicineQuantity() {
        return medicineQuantity;
    }

    public void setMedicineQuantity(String medicineQuantity) {
        this.medicineQuantity = medicineQuantity;
    }

    public String getMedicineImage() {
        return medicineImage;
    }

    public void setMedicineImage(String medicineImage) {
        this.medicineImage = medicineImage;
    }
}
