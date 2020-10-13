package com.hamza.veterinarysystemdemo.CartPackage;

import android.content.Context;
import android.view.View;

import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.hamza.veterinarysystemdemo.models.userMedicineListModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cart {
    public  List<userMedicineListModel> productList;
    Context context;

    public Cart(Context context) {
        productList = new ArrayList<>();
        this.context = context;
    }

    public void addToCart(userMedicineListModel productModel) {
        Iterator iterator = productList.iterator();
        while (iterator.hasNext()) {
            userMedicineListModel item = (userMedicineListModel) iterator.next();
            if (item.getmID() == productModel.getmID()) {
                int quantity = item.getQuantity() + 1;
                productModel.setQuantity(quantity);
                iterator.remove();
                productList.add(productModel);
                return;
            }
        }
        productList.add(productModel);
    }

    public void removeFromCart(userMedicineListModel productModel) {
        Iterator iterator = productList.iterator();
        while (iterator.hasNext()) {
            userMedicineListModel item = (userMedicineListModel) iterator.next();
            if (item.getmID() == productModel.getmID()) {
                int quantity = item.getQuantity() - 1;
                productModel.setQuantity(quantity);
                iterator.remove();
                if (quantity > 0) {
                    productList.add(productModel);
                }
                return;
            }

        }
    }


    public void setBadge(String value){
        if(UserSessionManager.tvBadge!=null){
            //createBadge();
            UserSessionManager.tvBadge.setVisibility(View.VISIBLE);
            UserSessionManager.tvBadge.setText(value);
        }
    }

    public void removeBadge(){
        UserSessionManager.tvBadge.setVisibility(View.GONE);
    }


    public int getTotalItems() {
        int qty = 0;
        for (int i = 0; i < productList.size(); i++) {
            qty += productList.get(i).getQuantity();
        }
        return qty;
    }

    public int getItemQuantity(int productID) {
        Iterator itr = productList.iterator();
        while (itr.hasNext()) {
            userMedicineListModel item = (userMedicineListModel) itr.next();
            if (item.getmID() == productID) {
                return (int) item.getQuantity();
            }
        }
        return 0;
    }

    public double getTotalPrice() {
        double price = 0;
        for (int i = 0; i < productList.size(); i++) {
            double prc = Double.valueOf(productList.get(i).getPrice());
            price += (prc * Double.valueOf(productList.get(i).getQuantity()));
        }

        return price;
    }

    public void removeProduct(userMedicineListModel product) {
        Iterator itr = productList.iterator();
        while (itr.hasNext()) {
            userMedicineListModel item = (userMedicineListModel) itr.next();
            if (item.getmID() == product.getmID()) {
                itr.remove();
                return;
            }
        }
    }

}
