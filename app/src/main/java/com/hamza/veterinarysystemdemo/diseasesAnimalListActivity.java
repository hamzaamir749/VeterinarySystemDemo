package com.hamza.veterinarysystemdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.veterinarysystemdemo.adapters.diseasesAnimalsListAdapter;
import com.hamza.veterinarysystemdemo.adapters.diseasesAnimalsNamesAdapter;
import com.hamza.veterinarysystemdemo.models.diseasesAnimalsListModel;
import com.hamza.veterinarysystemdemo.models.diseasesAnimalsNamesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class diseasesAnimalListActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView DLRecycler;
    private LinearLayoutManager linearLayoutManager;
    private List<diseasesAnimalsListModel> animalDiseasesList;
    private diseasesAnimalsListAdapter DLadapter;
    private String Animalsdiseaseslist_URL;
    Context mContext;
    IPADDRESS ipaddress;
    private String animalsnameKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseases_animal_list);


        animalsnameKey=getIntent().getExtras().get("animalnameKey").toString();
        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();

        Animalsdiseaseslist_URL="http://www.veterinarysystem.ga/getanimalsdiseases.php";
        mContext=this;
        DLRecycler=findViewById(R.id.recycler_disease_list_animal);
        animalDiseasesList=new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DLRecycler.setLayoutManager(linearLayoutManager);
        DLRecycler.setHasFixedSize(true);
        mToolbar= findViewById(R.id.diseases_animal_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Disease List");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        else
        {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        SharedPreferences sharedPreferences=getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        String lang=sharedPreferences.getString("my_lang","");
        getDiseasesList(lang);
    }

    private void getDiseasesList(final String lang) {
        StringRequest getDiseasesListRequest=new StringRequest(Request.Method.POST, Animalsdiseaseslist_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    //new AlertDialog.Builder(mContext).setMessage(response).create().show();
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0; i<jsonArray.length();i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            int id=object.getInt("ad_id");
                            String name=object.getString("ad_name");
                            diseasesAnimalsListModel diseasesAnimalsListModels=new diseasesAnimalsListModel(id,name);
                            animalDiseasesList.add(diseasesAnimalsListModels);
                           DLadapter =new diseasesAnimalsListAdapter(mContext,animalDiseasesList);

                        }

                        DLRecycler.setAdapter(DLadapter);

                    }else{
                        Toast.makeText(getApplicationContext(), "Could not get data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"parsing exception: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> diseases=new HashMap<>();
                diseases.put("animalID",animalsnameKey);
                diseases.put("languageType",lang);
                return diseases;
            }
        };
        Volley.newRequestQueue(this).add(getDiseasesListRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
