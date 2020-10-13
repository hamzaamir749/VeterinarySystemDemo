package com.hamza.veterinarysystemdemo.StoreSection;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.hamza.veterinarysystemdemo.StoreSection.Adapter.StoreHistoryAdapter;
import com.hamza.veterinarysystemdemo.StoreSection.Model.StoreHistoryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreOrderHistoryActivity extends AppCompatActivity {
    Toolbar mToolBar;
    RecyclerView sohRecycler;
    LinearLayoutManager linearLayoutManager;
    Context context;
    List<StoreHistoryModel> historyList;
    StoreHistoryAdapter storeHistoryAdapter;
    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    String storeid,getUsers_url;
    IPADDRESS ipaddress;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_history);
        sohRecycler = findViewById(R.id.soh_recycler);
        mToolBar = findViewById(R.id.soh_toolbar);
        progressBar=findViewById(R.id.soh_loading);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.ClientHistory);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        userSessionManager = new UserSessionManager(this);
        sessionDetails = userSessionManager.getSessionDetails();
        storeid=String.valueOf(sessionDetails.getId());

        historyList = new ArrayList<>();
        context = this;
        linearLayoutManager = new LinearLayoutManager(this);
        sohRecycler.setLayoutManager(linearLayoutManager);
        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        getUsers_url="http://" + ip + "/VeterinarySystem/getMedicinesHistoryForStoreusers.php";
        //getUsers_url="http://www.veterinarysystem.ga/getMedicinesHistoryForStoreusers.php";
        progressBar.setIndeterminateDrawable(new Circle());
        getStoreHistoryData();
    }

    private void getStoreHistoryData() {
        StringRequest historyRequest = new StringRequest(Request.Method.POST, getUsers_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("users");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            int uid = obj.getInt("userid");
                            String uname = obj.getString("username");
                            String uaddress = obj.getString("useraddress");
                            String uphone = obj.getString("userphone");
                            String uimage = obj.getString("userimage");
                           StoreHistoryModel storeHistoryModel=new StoreHistoryModel(uid,uimage,uname,uphone,uaddress);
                           historyList.add(storeHistoryModel);
                        }
                        progressBar.setVisibility(View.GONE);
                        storeHistoryAdapter = new StoreHistoryAdapter(context, historyList);
                        sohRecycler.setAdapter(storeHistoryAdapter);
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                   // Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(context, "Volley: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hisMap = new HashMap<>();
                hisMap.put("userid", storeid);
                return hisMap;
            }
        };
        Volley.newRequestQueue(context).add(historyRequest);
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
