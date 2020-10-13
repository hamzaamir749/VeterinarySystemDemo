package com.hamza.veterinarysystemdemo.StoreSection.Model;

public class StoreMyProductsModel {
    public int id;
    public String mImage,mName,mPrice;

    public StoreMyProductsModel(int id, String mImage, String mName, String mPrice) {
        this.id = id;
        this.mImage = mImage;
        this.mName = mName;
        this.mPrice = mPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }
}
