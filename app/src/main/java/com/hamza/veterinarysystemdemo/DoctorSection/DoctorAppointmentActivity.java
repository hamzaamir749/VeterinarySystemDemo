package com.hamza.veterinarysystemdemo.DoctorSection;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorAppointmentActivity extends AppCompatActivity {
    Toolbar mToolbar;
    IPADDRESS ipaddress;

    List<Doctor_Appointment_Model> clientsList;
    Doctor_Appointment_Adapter doctor_appointment_adapter;
    Context context;
    LinearLayoutManager linearLayoutManager;
    RecyclerView clientsRecycler;
    String getClients_URL;
    UserSessionManager  userSessionManager;
    SessionDetails sessionDetails;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment);
        clientsRecycler = findViewById(R.id.da_recycler);
        progressBar=findViewById(R.id.da_loading);
        mToolbar = findViewById(R.id.da_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.clients);
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
        getClients_URL = "http://" + ip + "/VeterinarySystem/getClients.php";
        //getClients_URL="http://www.veterinarysystem.ga/getClients.php";
        userSessionManager=new UserSessionManager(this);
        sessionDetails=userSessionManager.getSessionDetails();


        context = this;
        clientsList = new ArrayList<>();
        clientsList.clear();
        linearLayoutManager = new LinearLayoutManager(this);
        clientsRecycler.setHasFixedSize(true);
        clientsRecycler.setLayoutManager(linearLayoutManager);
        progressBar.setIndeterminateDrawable(new Circle());
        getClients();

    }

    private void getClients() {
        StringRequest getCLientRequest = new StringRequest(Request.Method.POST, getClients_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("clientsData");
                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("userid");
                            String name = object.getString("username");
                            String image = object.getString("userimage");
                            String tName = object.getString("useraddress");
                            String lati = object.getString("latitude");
                            String longi = object.getString("langtitude");
                            String mobile = object.getString("userphoneno");
                            Doctor_Appointment_Model doctor_appointment_model = new Doctor_Appointment_Model(id, name, image, tName, lati, longi, mobile);
                            clientsList.add(doctor_appointment_model);
                        }
                        progressBar.setVisibility(View.GONE);
                        doctor_appointment_adapter = new Doctor_Appointment_Adapter(clientsList, context);
                        clientsRecycler.setAdapter(doctor_appointment_adapter);
                    } else {
                        progressBar.setVisibility(View.GONE);
                       // Toast.makeText(getApplicationContext(), "Could not get Client data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                 //   Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

              //  Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> getClientMap=new HashMap<>();
                getClientMap.put("doctorID",String.valueOf(sessionDetails.getId()));
                return getClientMap;
            }
        };
        Volley.newRequestQueue(this).add(getCLientRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),DoctorDrawerActivity.class);
        startActivity(intent);
        finish();
    }
}
