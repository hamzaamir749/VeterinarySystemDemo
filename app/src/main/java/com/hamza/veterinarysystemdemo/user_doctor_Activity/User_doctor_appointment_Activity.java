package com.hamza.veterinarysystemdemo.user_doctor_Activity;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User_doctor_appointment_Activity extends AppCompatActivity {
    Toolbar mToolbar;
    String getCities_URL, getTehseel_URL, getDoctors_URL;
    List<City> citynamesArraylist;
    List<tehseel> tehseelnamesArraylist;
    City selectedCity;
    tehseel selectedTehseel;
    Spinner spinnercity, spinnertehseel;
    IPADDRESS ipaddress;
    RecyclerView doctorsRecycler;

    List<User_DA_Model> docList;
    User_DA_Adapter user_da_adapter;
    User_DA_Model user_da_model;
    LinearLayoutManager linearLayoutManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_doctor_appointment_);

        spinnercity=findViewById(R.id.sp_city_doc_uda);
        spinnertehseel=findViewById(R.id.sp_tehsile_doc_uda);
        mToolbar = findViewById(R.id.user_doctor_appointment_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Doctors");
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
        getCities_URL = "http://" + ip + "/VeterinarySystem/getcities.php";
        getTehseel_URL = "http://" + ip + "/VeterinarySystem/gettehseel.php";
        getDoctors_URL = "http://" + ip + "/VeterinarySystem/getdoctors.php";


        citynamesArraylist = new ArrayList<>();
        tehseelnamesArraylist = new ArrayList<>();
        selectedCity = null;
        selectedTehseel = null;
        docList = new ArrayList<>();

        doctorsRecycler = findViewById(R.id.recycler_uda);
        linearLayoutManager = new LinearLayoutManager(this);
        doctorsRecycler.setHasFixedSize(true);
        doctorsRecycler.setLayoutManager(linearLayoutManager);
        context = this;

        getCities();
    }


    private void getCities() {

        StringRequest getcitiesRequest = new StringRequest(Request.Method.GET, getCities_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("citydata");
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        City newItem;
                        citynamesArraylist.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            newItem = new City();
                            newItem.setCid(object.getInt("id"));
                            newItem.setCname(object.getString("name"));
                            citynamesArraylist.add(newItem);
                        }
                        setCitySpinner();


                    } else {
                        Toast.makeText(getApplicationContext(), "Could not get City data", Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(this).add(getcitiesRequest);

    }

    private void setCitySpinner() {
        SpinnerAdapter spinnerAdapter;
        List<String> cities=getCitiesForSpinner();
        spinnerAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,cities);
        spinnercity.setAdapter(spinnerAdapter);

        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tvSelectedCity = parent.getItemAtPosition(position).toString();
                if(tvSelectedCity.equals("Please Select City")){
                    selectedCity = null;
                    spinnertehseel.setVisibility(View.GONE);
                    return;
                }
                else
                {
                    spinnertehseel.setVisibility(View.VISIBLE);
                    for (City item : citynamesArraylist) {
                        if (item.getCname().equals(tvSelectedCity)) {
                            selectedCity = item;
                        }
                    }
                    Toast.makeText(getApplicationContext(), String.valueOf(selectedCity.getCid()), Toast.LENGTH_SHORT).show();
                    getTehseel(String.valueOf(selectedCity.getCid()));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getTehseel(final String cityid) {
        StringRequest getTehseelRequest = new StringRequest(Request.Method.POST, getTehseel_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("tehseeldata");
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        tehseel newItem;
                        tehseelnamesArraylist.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            newItem = new tehseel();
                            newItem.setTname(object.getString("name"));
                            newItem.setTid(object.getInt("id"));
                            tehseelnamesArraylist.add(newItem);
                        }
                        setTehseelSpinner();
                    } else {
                        Toast.makeText(getApplicationContext(), "Could not get Tehseel data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "parsing exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> items = new HashMap<>();
                items.put("cityidd", cityid);
                return items;
            }
        };
        Volley.newRequestQueue(this).add(getTehseelRequest);


    }

    private void setTehseelSpinner() {

        List<String> tehseels=getTehseelNames();
        SpinnerAdapter adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,tehseels);
        spinnertehseel.setAdapter(adapter);

        spinnertehseel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tvSelectedTehseel = parent.getItemAtPosition(position).toString();
                if(tvSelectedTehseel.equals("Please Select Tehseel")){
                    selectedTehseel = null;
                    spinnertehseel.setVisibility(View.GONE);
                    return;
                }
                spinnertehseel.setVisibility(View.VISIBLE);
                for (tehseel item : tehseelnamesArraylist) {
                    if (item.getTname().equals(tvSelectedTehseel)) {
                        selectedTehseel = item;
                    }
                }
                getDoctors(String.valueOf(selectedTehseel.getTid()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getDoctors(String tehseelid) {
        Toast.makeText(getApplicationContext(), tehseelid, Toast.LENGTH_SHORT).show();
    }

    private List<String> getCitiesForSpinner() {
        List<String> cities = new ArrayList<>();
        cities.add("Please Select City");
        for (City item : citynamesArraylist) {
            cities.add(item.getCname());
        }
        return cities;
    }

    private List<String> getTehseelNames() {
        List<String> tehseelname = new ArrayList<>();
        tehseelname.add("Please Select Tehseel");
        for (tehseel item : tehseelnamesArraylist) {
            tehseelname.add(item.getTname());
        }
        return tehseelname;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}

