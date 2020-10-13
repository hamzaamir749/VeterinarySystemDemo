package com.hamza.veterinarysystemdemo.Session;

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


    public void setSessionDetails(SessionDetails sessionDetails) {


        SharedPreferences.Editor DataDetails=userDB.edit();
        DataDetails.putInt("id", sessionDetails.getId());
        DataDetails.putString("name", sessionDetails.getName());
        DataDetails.putString("profilepic", sessionDetails.getProfilepicture());
        DataDetails.putString("address", sessionDetails.getAddress());
        DataDetails.putString("phoneno", sessionDetails.getPhone());
        DataDetails.putString("email", sessionDetails.getEmailAddress());
        DataDetails.putString("type",sessionDetails.getType());
        DataDetails.apply();

    }

    public SessionDetails getSessionDetails() {

        int id=userDB.getInt("id",0);
        String name=userDB.getString("name","");
        String phoneno=userDB.getString("phoneno","");
        String address=userDB.getString("address","");
        String profilepic=userDB.getString("profilepic","");
        String email=userDB.getString("email","");
        String type=userDB.getString("type","");
        SessionDetails sessionDetails =new SessionDetails();
        sessionDetails.setId(id);
        sessionDetails.setAddress(address);
        sessionDetails.setName(name);
        sessionDetails.setPhone(phoneno);
        sessionDetails.setProfilepicture(profilepic);
        sessionDetails.setEmailAddress(email);
        sessionDetails.setType(type);

        return sessionDetails;
    }

    public boolean isLoggedIn() {
        return userDB.getBoolean("loggedIn", false);
    }


    public void clearSessionData() {
        SharedPreferences.Editor clientSpEditor = userDB.edit();
        clientSpEditor.clear();
        clientSpEditor.apply();
    }



    public void setLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor riderSpEditor = userDB.edit();
        riderSpEditor.putBoolean("loggedIn", loggedIn);
        riderSpEditor.putString("user", "All");
        riderSpEditor.apply();
    }


}