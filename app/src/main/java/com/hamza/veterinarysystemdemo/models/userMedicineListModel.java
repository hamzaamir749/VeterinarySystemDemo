package com.hamza.veterinarysystemdemo.models;

public class userMedicineListModel {

    public int mID;
    public String mName, mStoreID, mStoreName, mPrice, mImage;
    public int quantity;
    public double price;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public userMedicineListModel(int mID, String mName, String mStoreID, String mStoreName, String mPrice, String mImage) {
        this.mID = mID;
        this.mName = mName;
        this.mStoreID = mStoreID;
        this.mStoreName = mStoreName;
        this.mPrice = mPrice;
        this.mImage = mImage;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmStoreID() {
        return mStoreID;
    }

    public void setmStoreID(String mStoreID) {
        this.mStoreID = mStoreID;
    }

    public String getmStoreName() {
        return mStoreName;
    }

    public void setmStoreName(String mStoreName) {
        this.mStoreName = mStoreName;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
