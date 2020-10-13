package com.hamza.veterinarysystemdemo.UserSection.AppointmentsDoctorHistory;

import android.app.ProgressDialog;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingAppointmentsActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    Context context;
    RecyclerView pendingRecycler;
    List<PendingAppointmentModelClass> list;
    LinearLayoutManager linearLayoutManager;
    PendingAppointmentModelClass pendingAppointmentModelClass;
    PendingAppointmentAdapterClass pendingAppointmentAdapterClass;
    IPADDRESS ipaddress;
    String getPending_URL, userid;
    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_appointments);
        mToolBar = findViewById(R.id.upa_toolbar);
        pendingRecycler=findViewById(R.id.upa_recycler);
        progressBar=findViewById(R.id.upa_loading);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.PendingAppointments);
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
        getPending_URL = "http://" + ip + "/VeterinarySystem/getpendingappointmentsforusers.php";
        // getApproved_URL="http://www.veterinarysystem.ga/addproblems.php";

        linearLayoutManager = new LinearLayoutManager(this);
        list = new ArrayList<>();
        list.clear();
        context = this;
        pendingRecycler.setLayoutManager(linearLayoutManager);
        userSessionManager = new UserSessionManager(this);
        sessionDetails = userSessionManager.getSessionDetails();
        userid = String.valueOf(sessionDetails.getId());
        progressBar.setIndeterminateDrawable(new Circle());
        getPendingAppointments();

    }

    private void getPendingAppointments() {
        StringRequest pendingAppointments = new StringRequest(Request.Method.POST, getPending_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("pendingappointments");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            int did = obj.getInt("doctorid");
                            String dname = obj.getString("doctorname");
                            String dimage = obj.getString("doctorimage");
                            String dtime = obj.getString("time");
                            String ddate = obj.getString("date");
                            String dphone = obj.getString("doctorphoneno");
                            String daddress = obj.getString("doctoraddress");
                            pendingAppointmentModelClass = new PendingAppointmentModelClass(did, dname, dimage, dphone, daddress, dtime, ddate);
                            list.add(pendingAppointmentModelClass);
                        }
                        progressBar.setVisibility(View.GONE);
                        pendingAppointmentAdapterClass = new PendingAppointmentAdapterClass(context, list);
                        pendingRecycler.setAdapter(pendingAppointmentAdapterClass);
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
              //  Toast.makeText(context, "Volley: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pendingMap = new HashMap<>();
                pendingMap.put("userid", userid);
                return pendingMap;
            }
        };
        Volley.newRequestQueue(this).add(pendingAppointments);
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
