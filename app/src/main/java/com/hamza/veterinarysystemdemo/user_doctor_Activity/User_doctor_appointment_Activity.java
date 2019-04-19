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
    ArrayList<String> citynamesArraylist;
    ArrayList<String> tehseelnamesArraylist;
    MaterialSpinner spinner, spinner1;
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
        docList = new ArrayList<>();
        spinner = (MaterialSpinner) findViewById(R.id.sp_city_doc_uda);
        spinner1 = (MaterialSpinner) findViewById(R.id.sp_tehsile_doc_uda);
        doctorsRecycler = findViewById(R.id.recycler_uda);
        linearLayoutManager = new LinearLayoutManager(this);
        doctorsRecycler.setHasFixedSize(true);
        doctorsRecycler.setLayoutManager(linearLayoutManager);
        context = this;

        getCities();
    }

    private void getCities() {
        StringRequest getcitiesRequest = new StringRequest(Request.Method.POST, getCities_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("citydata");
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String names = object.getString("city");
                            // citynamesArraylist.add(0,"Please Select City");
                            citynamesArraylist.add(names);
                        }
                        citynamesArraylist.add(0, "Please Select City");
                        spinner.setItems(citynamesArraylist);
                        spinner.getSelectedIndex();

                        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, String city) {
                                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                                if (city.equals("Please Select City")) {
                                    Snackbar.make(view, "Please Select Any City", Snackbar.LENGTH_LONG).show();
                                    spinner1.setVisibility(View.INVISIBLE);
                                } else {
                                    spinner1.setVisibility(View.VISIBLE);
                                    getTehseel(city);
                                    //Toast.makeText(User_doctor_appointment_Activity.this, item, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> user = new HashMap<>();
                user.put("usercatgory", "2");
                return user;
            }
        };
        Volley.newRequestQueue(this).add(getcitiesRequest);

    }

    public void getTehseel(final String city) {
        StringRequest getTehseelRequest = new StringRequest(Request.Method.POST, getTehseel_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("tehseeldata");
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String names = object.getString("tehseel");
                            tehseelnamesArraylist.add(names);
                        }
                        tehseelnamesArraylist.add(0, "Please Select Tehseel");
                        spinner1.setItems(tehseelnamesArraylist);

                        spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, String tehseel) {
                                if (tehseel.equals("Please Select Tehseel")) {
                                    Snackbar.make(view, "Please Select Any Tehseel", Snackbar.LENGTH_LONG).show();

                                } else {

                                    getDoctors(city, tehseel);
                                }
                            }
                        });


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
                items.put("cityname", city);
                items.put("usercatgory", "2");
                return items;
            }
        };
        Volley.newRequestQueue(this).add(getTehseelRequest);

    }

    private void getDoctors(final String city, final String tehseel) {

        StringRequest getDoctorsRequest = new StringRequest(Request.Method.POST, getDoctors_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("doctors");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String name = object.getString("name");
                            String image = object.getString("profileimage");
                            String tehseelplace = object.getString("tehseel");
                            User_DA_Model user_da_model = new User_DA_Model(id, name, image, tehseelplace);
                            docList.add(user_da_model);
                            user_da_adapter = new User_DA_Adapter(docList, context);

                        }

                        doctorsRecycler.setAdapter(user_da_adapter);

                    }

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> getDocotorMap = new HashMap<>();
                getDocotorMap.put("cityname", city);
                getDocotorMap.put("tehseelname", tehseel);
                getDocotorMap.put("usercategory", "2");
                return getDocotorMap;
            }
        };

        Volley.newRequestQueue(this).add(getDoctorsRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}

