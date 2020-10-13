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
import com.hamza.veterinarysystemdemo.StoreSection.Adapter.StoreOrderAdapter;
import com.hamza.veterinarysystemdemo.StoreSection.Model.StoreOrderModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreMedicineOrderActivity extends AppCompatActivity {
    RecyclerView myOrderRecycler;
    LinearLayoutManager linearLayout;
    Toolbar mToolbar;
    List<StoreOrderModel> myClientsList;
    StoreOrderAdapter storeOrderAdapter;
    Context context;
    String getClients_URL,storeID;
    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    IPADDRESS ipaddress;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_medicine_order);
        mToolbar = findViewById(R.id.smo_toolbar);
        progressBar=findViewById(R.id.smo_loading);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.MyOrders);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        getClients_URL="http://"+ip+"/VeterinarySystem/getorderclients.php";
        //getClients_URL="http://www.veterinarysystem.ga/getorderclients.php";

        myOrderRecycler=findViewById(R.id.smo_recycler);
        linearLayout=new LinearLayoutManager(this);
        context=this;
        myOrderRecycler.setHasFixedSize(true);
        myOrderRecycler.setLayoutManager(linearLayout);
        userSessionManager=new UserSessionManager(this);
        sessionDetails=userSessionManager.getSessionDetails();
        storeID=String.valueOf(sessionDetails.getId());
        myClientsList=new ArrayList<>();
        myClientsList.clear();
        progressBar.setIndeterminateDrawable(new Circle());
        getMyCLients();
    }

    private void getMyCLients() {
        StringRequest getMyClientsRequest=new StringRequest(Request.Method.POST, getClients_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject jsonObject=new JSONObject(response);
                    boolean status=jsonObject.getBoolean("status");
                    JSONArray jsonArray=jsonObject.getJSONArray("myorderclients");
                    if (status)
                    {
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            int id=object.getInt("userid");
                            String name=object.getString("username");
                            String image=object.getString("userimage");
                            String time=object.getString("time");
                            String phone=object.getString("userphone");
                            String address=object.getString("useraddress");
                            StoreOrderModel storeOrderModel=new StoreOrderModel(id,name,image,time,phone,address);
                            myClientsList.add(storeOrderModel);
                        }
                        progressBar.setVisibility(View.GONE);
                        storeOrderAdapter=new StoreOrderAdapter(myClientsList,context);
                        myOrderRecycler.setAdapter(storeOrderAdapter);
                    }else
                    {
                        progressBar.setVisibility(View.GONE);
                     //   Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    progressBar.setVisibility(View.GONE);
                   // Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(context, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> getClientsMap=new HashMap<>();
                getClientsMap.put("storeIDD",storeID);
                return getClientsMap;
            }
        };
        Volley.newRequestQueue(context).add(getMyClientsRequest);
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
