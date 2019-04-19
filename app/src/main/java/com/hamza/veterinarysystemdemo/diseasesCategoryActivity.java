package com.hamza.veterinarysystemdemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.veterinarysystemdemo.adapters.diseasesAnimalsNamesAdapter;
import com.hamza.veterinarysystemdemo.models.diseasesAnimalsNamesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class diseasesCategoryActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView disesesRecycler;
    private LinearLayoutManager linearLayoutManager;
    private List<diseasesAnimalsNamesModel> animalsList;
    Context context;
    private diseasesAnimalsNamesAdapter dANAdapter;
    private String AnimalsNameList_URL;
    IPADDRESS ipaddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseases_category);

        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        AnimalsNameList_URL="http://www.veterinarysystem.ga/getanimalsname.php";

        disesesRecycler=findViewById(R.id.recycler_disease_category_animal);
        animalsList=new ArrayList<>();
        context = this;
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        disesesRecycler.setLayoutManager(linearLayoutManager);
        disesesRecycler.setHasFixedSize(true);

        mToolbar= findViewById(R.id.diseases_animal_name_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Disease Category");
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
        getAnimalsList(lang);



    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getAnimalsList(final String lang) {
        final StringRequest getAnimalsListRequest=new StringRequest(Request.Method.POST, AnimalsNameList_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    //new AlertDialog.Builder(context).setMessage(response).create().show();
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0; i<jsonArray.length();i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            int id=object.getInt("id");
                            String name=object.getString("name");
                            diseasesAnimalsNamesModel diseasesAnimalsNamesModels=new diseasesAnimalsNamesModel(id,name);
                            animalsList.add(diseasesAnimalsNamesModels);
                            dANAdapter=new diseasesAnimalsNamesAdapter(context,animalsList);

                        }

                        disesesRecycler.setAdapter(dANAdapter);

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
                Map<String,String> langaugeMap=new HashMap<>();
                langaugeMap.put("languageType",lang);
                return langaugeMap;
            }
        };
        Volley.newRequestQueue(this).add(getAnimalsListRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
