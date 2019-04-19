package com.hamza.veterinarysystemdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.CartPackage.Cart;

public class UserSessionManager {

    SharedPreferences userDB;
    public SharedPreferences.Editor editor;
    public Context context;
    int private_mode = 0;
    private static final String userDBName = "userData";
    public static Cart cart;
    public static TextView tvBadge;

    public UserSessionManager(Context context) {
        this.context = context;
        userDB = context.getSharedPreferences(userDBName, private_mode);
    }

    public void setStoreDataDetails(StoreSessionDetails storeSessionDetails) {

        SharedPreferences.Editor storeDataDetails=userDB.edit();
        storeDataDetails.putInt("id",storeSessionDetails.getId());
        storeDataDetails.putString("name",storeSessionDetails.getName());
        storeDataDetails.putString("profilepic",storeSessionDetails.getProfilepicture());
        storeDataDetails.putString("address",storeSessionDetails.getAddress());
        storeDataDetails.putString("phoneno",storeSessionDetails.getPhone());
        storeDataDetails.apply();
    }

    public StoreSessionDetails getStoreDataDetails() {
        int id=userDB.getInt("id",0);
        String name=userDB.getString("name","");
        String phoneno=userDB.getString("phoneno","");
        String address=userDB.getString("address","");
        String profilepic=userDB.getString("profilepic","");

        StoreSessionDetails storeSessionDetails=new StoreSessionDetails();
        storeSessionDetails.setId(id);
        storeSessionDetails.setAddress(address);
        storeSessionDetails.setName(name);
        storeSessionDetails.setPhone(phoneno);
        storeSessionDetails.setProfilepicture(profilepic);


        return storeSessionDetails;
    }

    public void setDoctorDataDetails(DoctorSessionDetails doctorSessionDetails) {


        SharedPreferences.Editor doctorDataDetails=userDB.edit();
        doctorDataDetails.putInt("id",doctorSessionDetails.getId());
        doctorDataDetails.putString("name",doctorSessionDetails.getName());
        doctorDataDetails.putString("profilepic",doctorSessionDetails.getProfilepicture());
        doctorDataDetails.putString("address",doctorSessionDetails.getAddress());
        doctorDataDetails.putString("phoneno",doctorSessionDetails.getPhone());
        doctorDataDetails.apply();



    }

    public DoctorSessionDetails getDoctorDataDetails() {
        int id=userDB.getInt("id",0);
        String name=userDB.getString("name","");
        String phoneno=userDB.getString("phoneno","");
        String address=userDB.getString("address","");
        String profilepic=userDB.getString("profilepic","");

        DoctorSessionDetails doctorSessionDetails=new DoctorSessionDetails();
        doctorSessionDetails.setId(id);
        doctorSessionDetails.setAddress(address);
        doctorSessionDetails.setName(name);
        doctorSessionDetails.setPhone(phoneno);
        doctorSessionDetails.setProfilepicture(profilepic);


        return doctorSessionDetails;
    }

    public void setClientDataDetails(ClientSessionDetails clientSessionDetails) {


        SharedPreferences.Editor clientDataDetails=userDB.edit();
        clientDataDetails.putInt("id",clientSessionDetails.getId());
        clientDataDetails.putString("name",clientSessionDetails.getName());
        clientDataDetails.putString("profilepic",clientSessionDetails.getProfilepicture());
        clientDataDetails.putString("address",clientSessionDetails.getAddress());
        clientDataDetails.putString("phoneno",clientSessionDetails.getPhone());
        clientDataDetails.apply();

    }

    public ClientSessionDetails getClientDataDetails() {

        int id=userDB.getInt("id",0);
        String name=userDB.getString("name","");
        String phoneno=userDB.getString("phoneno","");
        String address=userDB.getString("address","");
        String profilepic=userDB.getString("profilepic","");

        ClientSessionDetails clientSessionDetails=new ClientSessionDetails();
        clientSessionDetails.setId(id);
        clientSessionDetails.setAddress(address);
        clientSessionDetails.setName(name);
        clientSessionDetails.setPhone(phoneno);
        clientSessionDetails.setProfilepicture(profilepic);


        return clientSessionDetails;
    }

    public boolean isClientLoggedIn() {
        return userDB.getBoolean("loggedIn", false);
    }

    public boolean isDoctorLoggedIn() {
        return userDB.getBoolean("loggedIn", false);
    }

    public boolean isStoreLoggedIn() {
        return userDB.getBoolean("loggedIn", false);
    }


    public void clearClientData() {
        SharedPreferences.Editor clientSpEditor = userDB.edit();
        clientSpEditor.clear();
        clientSpEditor.apply();
    }

    public void clearDoctorData() {
        SharedPreferences.Editor doctorSpEditor = userDB.edit();
        doctorSpEditor.clear();
        doctorSpEditor.apply();
    }

    public void clearStoreData() {
        SharedPreferences.Editor storeSpEditor = userDB.edit();
        storeSpEditor.clear();
        storeSpEditor.apply();
    }

    public void setClientLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor riderSpEditor = userDB.edit();
        riderSpEditor.putBoolean("loggedIn", loggedIn);
        riderSpEditor.putString("user", "client");
        riderSpEditor.apply();
    }

    public void setDoctorLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor doctorSpEditor = userDB.edit();
        doctorSpEditor.putBoolean("loggedIn", loggedIn);
        doctorSpEditor.putString("user", "doctor");
        doctorSpEditor.apply();
    }
    public void setStoreLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor storeSpEditor = userDB.edit();
        storeSpEditor.putBoolean("loggedIn", loggedIn);
        storeSpEditor.putString("user", "store");
        storeSpEditor.apply();
    }
}