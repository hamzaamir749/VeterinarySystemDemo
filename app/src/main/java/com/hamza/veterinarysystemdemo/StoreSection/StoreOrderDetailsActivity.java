package com.hamza.veterinarysystemdemo.StoreSection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.hamza.veterinarysystemdemo.StoreSection.Adapter.StoreOrderDetailsAdapter;
import com.hamza.veterinarysystemdemo.StoreSection.Model.StoreOrderDetailsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class StoreOrderDetailsActivity extends AppCompatActivity {
    String storeID, clientID, getOrderDetails_URL, acceptOrder_URL, cancelOrder_URL,saveCurrentDate,saveCurrentTime;
    RecyclerView myOrderDetailsRecycler;
    Toolbar mToolbar;
    LinearLayoutManager linearLayoutManager;
    List<StoreOrderDetailsModel> myOrderList;
    StoreOrderDetailsAdapter storeOrderDetailsAdapter;
    StoreOrderDetailsModel storeOrderDetailsModel;
    Button accept_order, cancel_order;
    PrettyDialog prettyDialog;
    Context context;
    IPADDRESS ipaddress;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_details);
        accept_order = findViewById(R.id.sod_accept);
        cancel_order = findViewById(R.id.sod_cancel);
        progressBar=findViewById(R.id.sod_loading);
        myOrderDetailsRecycler = findViewById(R.id.sod_recycler);
        progressDialog=new ProgressDialog(this);
        mToolbar = findViewById(R.id.sod_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.MyOrderDetails);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }

        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        getOrderDetails_URL = "http://"+ip+"/VeterinarySystem/getorderdetails.php";
        acceptOrder_URL = "http://"+ip+"/VeterinarySystem/acceptorder.php";
        cancelOrder_URL = "http://"+ip+"/VeterinarySystem/deleteorder.php";
        //getOrderDetails_URL="http://www.veterinarysystem.ga/getorderdetails.php";
       // acceptOrder_URL="http://www.veterinarysystem.ga/acceptorder.php";
       // cancelOrder_URL="http://www.veterinarysystem.ga/deleteorder.php";
        //Get clientID and storeID
        clientID = getIntent().getExtras().get("clientID").toString();
        storeID = getIntent().getExtras().get("storeID").toString();
        //Set Recycler
        myOrderList=new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        myOrderDetailsRecycler.setHasFixedSize(true);
        myOrderDetailsRecycler.setLayoutManager(linearLayoutManager);
        prettyDialog = new PrettyDialog(this);
        context=this;
        //get Time and Date
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());
        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:aa");
        saveCurrentTime = currentTime.format(calFordTime.getTime());
        //get Data
        progressBar.setIndeterminateDrawable(new Circle());
        getOrderDetails();
        //Set Onclick events
        accept_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptorderFunction();
            }
        });
        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelorderFunction();
            }
        });
    }


    private void getOrderDetails() {

        StringRequest getOrderDetailsRequest = new StringRequest(Request.Method.POST, getOrderDetails_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("orderdetails");
                    if (status) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String name = jsonObject1.getString("medicinename");
                            String price = jsonObject1.getString("medicineprice");
                            String quantity = jsonObject1.getString("medicinequantity");
                            String image = jsonObject1.getString("medicineimage");
                            storeOrderDetailsModel=new StoreOrderDetailsModel(name,price,quantity,image);
                            myOrderList.add(storeOrderDetailsModel);

                        }
                        progressBar.setVisibility(View.GONE);
                        storeOrderDetailsAdapter=new StoreOrderDetailsAdapter(myOrderList,context);
                        myOrderDetailsRecycler.setAdapter(storeOrderDetailsAdapter);

                    } else {
                        progressBar.setVisibility(View.GONE);
                //        Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
              //      Toast.makeText(StoreOrderDetailsActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //   Toast.makeText(StoreOrderDetailsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> getDetailMap=new HashMap<>();
                getDetailMap.put("storeID",storeID);
                getDetailMap.put("clientID",clientID);
                return getDetailMap;
            }
        };
        Volley.newRequestQueue(this).add(getOrderDetailsRequest);
    }

    private void acceptorderFunction() {
        progressDialog.setMessage(getResources().getString(R.string.DeleteOrder));
        progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest acceptOrderRequest = new StringRequest(Request.Method.POST, acceptOrder_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        prettyDialog.setTitle(getResources().getString(R.string.status))
                                .setMessage(getResources().getString(R.string.OrderAccepted))
                                .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.oK), R.color.design_default_color_primary_dark, R.color.loginBackgroundcolor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), StoreMedicineOrderActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).show();

                    } else {
                      //  Toast.makeText(StoreOrderDetailsActivity.this, "Not Accepted", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                   // Toast.makeText(StoreOrderDetailsActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
              //  Toast.makeText(StoreOrderDetailsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> acceptMap = new HashMap<>();
                acceptMap.put("storeID", storeID);
                acceptMap.put("clientID", clientID);
                acceptMap.put("timeAccept",saveCurrentTime);
                acceptMap.put("dateAccept",saveCurrentDate);
                return acceptMap;
            }
        };
        Volley.newRequestQueue(this).add(acceptOrderRequest);
    }

    private void cancelorderFunction() {
        progressDialog.setMessage(getResources().getString(R.string.DeleteOrder));
        progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest cancelOrderRequest = new StringRequest(Request.Method.POST, cancelOrder_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        prettyDialog.setTitle(getResources().getString(R.string.status))
                                .setMessage(getResources().getString(R.string.OrderCancel))
                                .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.oK), R.color.design_default_color_primary_dark, R.color.loginBackgroundcolor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), StoreMedicineOrderActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).show();
                    } else {
                        //Toast.makeText(StoreOrderDetailsActivity.this, "Not Canceled", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                   // Toast.makeText(StoreOrderDetailsActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Toast.makeText(StoreOrderDetailsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> cancelMap = new HashMap<>();
                cancelMap.put("storeID", storeID);
                cancelMap.put("clientID", clientID);
                return cancelMap;
            }
        };
        Volley.newRequestQueue(this).add(cancelOrderRequest);
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
