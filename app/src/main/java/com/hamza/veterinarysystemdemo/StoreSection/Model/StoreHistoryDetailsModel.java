package com.hamza.veterinarysystemdemo.StoreSection.Model;

public class StoreHistoryDetailsModel {

    String image,name,quantity;

    public StoreHistoryDetailsModel(String image, String name, String quantity) {
        this.image = image;
        this.name = name;
        this.quantity = quantity;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
