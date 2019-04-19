package com.hamza.veterinarysystemdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.veterinarysystemdemo.CartPackage.CartActivity;
import com.hamza.veterinarysystemdemo.adapters.commentsAdapter;
import com.hamza.veterinarysystemdemo.adapters.userMedicineListAdapter;
import com.hamza.veterinarysystemdemo.models.commentsModel;
import com.hamza.veterinarysystemdemo.models.userMedicineListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class userMedicineListActivity extends AppCompatActivity {

    private RecyclerView uml_recycler;
    private Toolbar mToolbar;
    private GridLayoutManager linearLayoutManager;
    private List<userMedicineListModel> mList;
    Context context;
    private userMedicineListAdapter mAdapter;
    String getMedicine_URL;
    IPADDRESS ipaddress;
    ImageView carticonbtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_medicine_list);
        uml_recycler=findViewById(R.id.user_medicine_list_recycler);
        carticonbtn=findViewById(R.id.carticonbtn);
        UserSessionManager.tvBadge=findViewById(R.id.tvBadge);
        setBadge();

        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        getMedicine_URL="http://www.veterinarysystem.ga/getmedicineslist.php";


        carticonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartintent=new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cartintent);
            }
        });
        context = this;
        mList = new ArrayList<>();
        //linearLayoutManager = new GridLayoutManager();
        RecyclerView.LayoutManager linearLayoutManager=new GridLayoutManager(this,2);
        uml_recycler.setLayoutManager(linearLayoutManager);
        uml_recycler.setHasFixedSize(true);
        mToolbar = findViewById(R.id.user_medicine_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Medicines");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }


        getMedicinesList();
    }

    private void setBadge() {

        if(UserSessionManager.cart!=null){
            UserSessionManager.cart.setBadge(String.valueOf(UserSessionManager.cart.getTotalItems()));
        }

    }

    private void getMedicinesList() {

        StringRequest getMedicineRequest = new StringRequest(Request.Method.GET, getMedicine_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("medicinedata");
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String storeid = object.getString("storeid");
                            String medicineName = object.getString("itemname");
                            String medicinePrice = object.getString("itemprice");
                            String storeName = object.getString("storename");
                            String medicineImage = object.getString("itemimage");
                            userMedicineListModel userMedicineListModelobj = new userMedicineListModel(id, medicineName, storeid, storeName, medicinePrice, medicineImage);
                            mList.add(userMedicineListModelobj);

                            mAdapter = new userMedicineListAdapter(mList, context);

                        }
                        uml_recycler.setAdapter(mAdapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "Could not get medicine data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "parsing exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(getMedicineRequest);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
