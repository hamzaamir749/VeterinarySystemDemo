package com.hamza.veterinarysystemdemo.StoreSection;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.hamza.veterinarysystemdemo.StoreSection.Adapter.StoreHistoryDetailsAdapter;
import com.hamza.veterinarysystemdemo.StoreSection.Model.StoreHistoryDetailsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreHistoryDetailsActivity extends AppCompatActivity {
    String id,address,getmedicines_url,storeid;

    IPADDRESS ipaddress;
    RecyclerView shd_Recycler;
    Context context;
    GridLayoutManager gridLayoutManager;
    List<StoreHistoryDetailsModel> list;
    Toolbar mToolBar;
    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_history_details);
        mToolBar = findViewById(R.id.shd_toolbar);
        shd_Recycler = findViewById(R.id.shd_recycler);
        progressBar=findViewById(R.id.shd_loading);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.coh);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        id=getIntent().getExtras().get("userid").toString();
        address=getIntent().getExtras().get("useraddress").toString();

        userSessionManager = new UserSessionManager(this);
        sessionDetails = userSessionManager.getSessionDetails();
        storeid=String.valueOf(sessionDetails.getId());
        context = this;
        list = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(this, 3);
        shd_Recycler.setLayoutManager(gridLayoutManager);
        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        getmedicines_url="http://" + ip + "/VeterinarySystem/getMedicinesHistoryForStoremedicines.php";
        // getUsers_url="http://www.veterinarysystem.ga/getMedicinesHistoryForStoremedicines.php";
        progressBar.setIndeterminateDrawable(new Circle());
        getMedicineOrders();
    }

    private void getMedicineOrders() {
        StringRequest approvedMedicine = new StringRequest(Request.Method.POST, getmedicines_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("medicines");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String mname = obj.getString("medicinename");
                            String mimage = obj.getString("medicineimage");
                            String mquantity = obj.getString("medicinequantity");
                            StoreHistoryDetailsModel storeHistoryDetailsModel=new StoreHistoryDetailsModel(mimage,mname,mquantity);
                            list.add(storeHistoryDetailsModel);
                        }
                        progressBar.setVisibility(View.GONE);
                        StoreHistoryDetailsAdapter adapter=new StoreHistoryDetailsAdapter(context,list);
                        shd_Recycler.setAdapter(adapter);

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
               // Toast.makeText(context, "Volley: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pendingMap = new HashMap<>();
                pendingMap.put("userid", id);
                pendingMap.put("storeid",storeid);
                pendingMap.put("useraddress",address);
                return pendingMap;
            }
        };
        Volley.newRequestQueue(this).add(approvedMedicine);
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
