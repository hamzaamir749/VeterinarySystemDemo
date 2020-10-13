package com.hamza.veterinarysystemdemo.user_doctor_Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
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
    List<String> citynamesArraylist;
    List<String> tehseelnamesArraylist;

    MaterialSpinner spinnercity, spinnertehseel;
    IPADDRESS ipaddress;
    RecyclerView doctorsRecycler;

    List<User_DA_Model> docList;
    User_DA_Adapter user_da_adapter;
    LinearLayoutManager linearLayoutManager;
    Context context;
    String lang;
    String citynames;
    String Tehseelnames;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_doctor_appointment_);
        progressBar=findViewById(R.id.uda_loading);
        spinnercity = findViewById(R.id.sp_city_doc_uda);
        spinnertehseel = findViewById(R.id.sp_tehsile_doc_uda);
        mToolbar = findViewById(R.id.user_doctor_appointment_toolbar);
        doctorsRecycler = findViewById(R.id.recycler_uda);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.Doctors);
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
        //getCities_URL="http://www.veterinarysystem.ga/getcities.php";
        //getDoctors_URL="http://www.veterinarysystem.ga/getdoctors.php";
        //getTehseel_URL="http://www.veterinarysystem.ga/gettehseel.php";


        citynamesArraylist = new ArrayList<>();
        String en="en";
        String ur="ur";

        tehseelnamesArraylist = new ArrayList<>();


        docList = new ArrayList<>();


        linearLayoutManager = new LinearLayoutManager(this);
        doctorsRecycler.setHasFixedSize(true);
        doctorsRecycler.setLayoutManager(linearLayoutManager);
        context = this;

        getCities();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "en");

    }

    private void getCities() {

        StringRequest getCitiesRequest = new StringRequest(Request.Method.POST, getCities_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("citydata");
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        tehseelnamesArraylist.clear();
                        if (lang.equals("en")) {
                            citynamesArraylist.add(0, "Please Choose City");
                        } else if (lang.equals("ur")) {
                            citynamesArraylist.add(0, "شہر کا انتخاب کریں");
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            if (lang.equals("en")) {
                                citynames = object.getString("name");
                            } else if (lang.equals("ur")) {
                                citynames = object.getString("urdu_name");
                            }
                            citynamesArraylist.add(citynames);
                        }
                        spinnercity.setItems(citynamesArraylist);
                        spinnercity.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                               // Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                                if (item.equals("Please Choose City") || item.equals("شہر کا انتخاب کریں")) {
                                    tehseelnamesArraylist.clear();
                                    docList.clear();
                                    spinnertehseel.setVisibility(View.INVISIBLE);
                                    doctorsRecycler.setVisibility(View.INVISIBLE);
                                } else {
                                    spinnertehseel.setVisibility(View.VISIBLE);
                                    doctorsRecycler.setVisibility(View.INVISIBLE);
                                    getTehseel(item);
                                }
                            }
                        });
                    } else {
                      //  Toast.makeText(getApplicationContext(), "Could not get Tehseel data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                   // Toast.makeText(getApplicationContext(), "parsing exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("language", lang);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(getCitiesRequest);

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

                        tehseelnamesArraylist.clear();
                        if (lang.equals("en"))
                        {
                            tehseelnamesArraylist.add(0,"Please Choose Tehseel");
                        }
                        else if (lang.equals("ur"))
                        {
                            tehseelnamesArraylist.add(0,"تحصیل کا انتخاب کریں");
                        }

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);


                            if (lang.equals("en")) {

                                Tehseelnames = object.getString("name");
                            } else if (lang.equals("ur")) {
                                Tehseelnames = object.getString("urdu_name");
                            }
                            tehseelnamesArraylist.add(Tehseelnames);
                        }

                        spinnertehseel.setItems(tehseelnamesArraylist);
                        spinnertehseel.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                                if (item.equals("Please Choose Tehseel") || item.equals("تحصیل کا انتخاب کریں")) {
                                    docList.clear();
                                    doctorsRecycler.setVisibility(View.INVISIBLE);
                                } else {
                                    docList.clear();
                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.setIndeterminateDrawable(new Circle());
                                    getDoctors(item);
                                    doctorsRecycler.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    } else {
                       // Toast.makeText(getApplicationContext(), "Could not get Tehseel data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                   // Toast.makeText(getApplicationContext(), "parsing exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                items.put("city", cityid);
                items.put("language", lang);
                return items;
            }
        };
        Volley.newRequestQueue(this).add(getTehseelRequest);


    }


    private void getDoctors(String doc) {
        StringRequest getDoctorRequest = new StringRequest(Request.Method.POST, getDoctors_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("doctors");
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String name = object.getString("name");
                            String image = object.getString("profileimage");
                            String tName = object.getString("address");
                            User_DA_Model user_da_model = new User_DA_Model(id, name, image, tName);
                            docList.add(user_da_model);


                        }
                        progressBar.setVisibility(View.GONE);
                        user_da_adapter = new User_DA_Adapter(docList, context);
                        doctorsRecycler.setAdapter(user_da_adapter);


                    } else {
                        progressBar.setVisibility(View.GONE);
                      //  Toast.makeText(getApplicationContext(), "Could not get Doctor data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(), "parsing exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                items.put("tehseelid", doc);
                items.put("language", lang);
                return items;
            }
        };
        Volley.newRequestQueue(this).add(getDoctorRequest);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}

