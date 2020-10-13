package com.hamza.veterinarysystemdemo.MedicineHistory;

import android.content.Context;
import android.os.Build;
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

public class ApprovedMedicineActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    Context context;
    RecyclerView approvedRecycler;
    List<ApprovedMedicineModelClass> list;
    LinearLayoutManager linearLayoutManager;
    IPADDRESS ipaddress;
    String getApproved_URL, userid;
    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    ApprovedMedicineAdapterClass approvedMedicineAdapterClass;
    ApprovedMedicineModelClass approvedMedicineModelClass;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_medicine);
        mToolBar = findViewById(R.id.maa_toolbar);
        approvedRecycler=findViewById(R.id.maa_recycler);
        progressBar=findViewById(R.id.maa_loading);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.approvedMedicines);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        getApproved_URL = "http://" + ip + "/VeterinarySystem/getapprovedmedicineforUD.php";
        // getApproved_URL="http://www.veterinarysystem.ga/addproblems.php";

        linearLayoutManager = new LinearLayoutManager(this);
        list = new ArrayList<>();
        list.clear();
        context = this;
        approvedRecycler.setLayoutManager(linearLayoutManager);
        userSessionManager = new UserSessionManager(this);
        sessionDetails = userSessionManager.getSessionDetails();
        userid = String.valueOf(sessionDetails.getId());
        progressBar.setIndeterminateDrawable(new Circle());
        getApprovedMedicines();
    }
    private void getApprovedMedicines() {
        StringRequest approvedMedicine = new StringRequest(Request.Method.POST, getApproved_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("approvedmedicines");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String mid = obj.getString("medicineid");
                            String sid = obj.getString("mStoreid");
                            String mname = obj.getString("medicinename");
                            String mimage = obj.getString("medicineimage");
                            String time = obj.getString("time");
                            String date = obj.getString("date");
                            String mquantity = obj.getString("medicinequantity");
                            String mprice = obj.getString("medicineprice");
                            approvedMedicineModelClass=new ApprovedMedicineModelClass(mid,sid,mname,mprice,mquantity,mimage,time,date);
                            list.add(approvedMedicineModelClass);

                        }
                        progressBar.setVisibility(View.GONE);
                        approvedMedicineAdapterClass=new ApprovedMedicineAdapterClass(context,list);
                        approvedRecycler.setAdapter(approvedMedicineAdapterClass);
                    }
                    else {
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
                pendingMap.put("userid", userid);
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
