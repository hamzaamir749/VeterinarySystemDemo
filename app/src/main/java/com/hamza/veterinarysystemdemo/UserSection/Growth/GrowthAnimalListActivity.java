package com.hamza.veterinarysystemdemo.UserSection.Growth;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;

public class GrowthAnimalListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Context context;
    List<GrowthModelClass> list;
    GrowthAdapterClass adapterClass;
    GrowthModelClass modelClass;
    ProgressBar progressBar;
    String getDataUrl, lang;
    Toolbar mToolBar;
    IPADDRESS ipaddress;
    PrettyDialog prettyDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_animal_list);
        progressBar = findViewById(R.id.gal_spinkit);
        recyclerView = findViewById(R.id.growth_animal_list_recycler);
        mToolBar = findViewById(R.id.growth_animal_list_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.AnimalsList);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }

        prettyDialog=new PrettyDialog(this);
        linearLayoutManager = new LinearLayoutManager(this);
        context = this;
        list = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
       // SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        //lang = sharedPreferences.getString("my_lang", "");

        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        // getDataUrl="http://www.veterinarysystem.ga/getanimalsname.php";
        getDataUrl = "http://" + ip + "/VeterinarySystem/getanimalsname.php";
        progressBar.setIndeterminateDrawable(new Circle());
        getAnimalsList();


    }

    private void getAnimalsList() {
        final StringRequest getAnimalsListRequest = new StringRequest(Request.Method.POST, getDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                  //  new AlertDialog.Builder(context).setMessage(response).create().show();
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String name = object.getString("name");
                            int typea = object.getInt("type");
                            modelClass = new GrowthModelClass(id, typea, name);
                            list.add(modelClass);


                        }
                        adapterClass = new GrowthAdapterClass(context, list);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(adapterClass);

                    } else {
                        progressBar.setVisibility(View.GONE);
                       // Toast.makeText(getApplicationContext(), "Could not get data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                   // Toast.makeText(getApplicationContext(), "parsing exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> langaugeMap = new HashMap<>();
                langaugeMap.put("languageType", lang);
                return langaugeMap;
            }
        };
        Volley.newRequestQueue(this).add(getAnimalsListRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "en");

    }
}
