package com.hamza.veterinarysystemdemo.DoctorSection.DoctorAppointmentHistory;

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

public class DoctorAppointmentsHistoryActivity extends AppCompatActivity {
    Context context;
    RecyclerView historyRecycler;
    LinearLayoutManager linearLayoutManager;
    UserSessionManager userSessionManager;
    Toolbar mToolbar;
    IPADDRESS ipaddress;
    SessionDetails sessionDetails;
    String getDoctorAppointmentHistory_URL;
    String doctorID;
    DAHModel dahModel;
    DAHAdapter dahAdapter;
    List<DAHModel> list;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment_history);
        historyRecycler=findViewById(R.id.doctor_appointment_history_recycler);
        mToolbar = findViewById(R.id.doctor_appointment_history_toolbar);
        progressBar=findViewById(R.id.dah_loading);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.appointmentsHistory);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        context=this;
        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        getDoctorAppointmentHistory_URL = "http://" + ip + "/VeterinarySystem/getAppointsHistoryForDoctor.php";
        //getDoctorAppointmentHistory_URL="http://www.veterinarysystem.ga/getAppointsHistoryForDoctor.php";
        userSessionManager=new UserSessionManager(this);
        sessionDetails=userSessionManager.getSessionDetails();
        doctorID=String.valueOf(sessionDetails.getId());
        list=new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(this);
        historyRecycler.setHasFixedSize(true);
        historyRecycler.setLayoutManager(linearLayoutManager);
        progressBar.setIndeterminateDrawable(new Circle());
        getAppointmentHistroyForDoctor();

    }

    private void getAppointmentHistroyForDoctor() {
        StringRequest Appointments = new StringRequest(Request.Method.POST, getDoctorAppointmentHistory_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Approvedmedicines");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            int uid = obj.getInt("userid");
                            String uname = obj.getString("username");
                            String uimage = obj.getString("userimage");
                            String utime = obj.getString("time");
                            String udate = obj.getString("date");
                            String uphone = obj.getString("userphoneno");
                            String uaddress = obj.getString("useraddress");

                            dahModel=new DAHModel(uid,uimage,uaddress,uphone,uname,utime,udate);
                            list.add(dahModel);
                        }
                        progressBar.setVisibility(View.GONE);
                        dahAdapter=new DAHAdapter(context,list);
                        historyRecycler.setAdapter(dahAdapter);

                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                  //  Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                pendingMap.put("userid", doctorID);
                return pendingMap;
            }
        };
        Volley.newRequestQueue(this).add(Appointments);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
