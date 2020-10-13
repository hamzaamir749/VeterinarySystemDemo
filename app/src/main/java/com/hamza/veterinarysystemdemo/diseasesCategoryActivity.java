package com.hamza.veterinarysystemdemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.hamza.veterinarysystemdemo.adapters.diseasesAnimalsNamesAdapter;
import com.hamza.veterinarysystemdemo.models.diseasesAnimalsNamesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class diseasesCategoryActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar mToolbar;
    private RecyclerView disesesRecycler;
    private LinearLayoutManager linearLayoutManager;
    private List<diseasesAnimalsNamesModel> animalsList;
    Context context;
    private diseasesAnimalsNamesAdapter dANAdapter;
    private String AnimalsNameList_URL;
    IPADDRESS ipaddress;
    String lang;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseases_category);
        progressBar=findViewById(R.id.dca_loading);
        mToolbar= findViewById(R.id.diseases_animal_name_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.DiseaseCategory);
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
        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        AnimalsNameList_URL="http://" + ip + "/VeterinarySystem/getanimalsname.php";
       //AnimalsNameList_URL="http://www.veterinarysystem.ga/getanimalsname.php";

        disesesRecycler=findViewById(R.id.recycler_disease_category_animal);
        animalsList=new ArrayList<>();
        context = this;
        linearLayoutManager=new LinearLayoutManager(this);

        disesesRecycler.setLayoutManager(linearLayoutManager);
        disesesRecycler.setHasFixedSize(true);

        //SharedPreferences sharedPreferences=getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        // lang=sharedPreferences.getString("my_lang","");
        progressBar.setIndeterminateDrawable(new Circle());
        getAnimalsList();



    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "en");

    }
    private void getAnimalsList() {
        final StringRequest getAnimalsListRequest=new StringRequest(Request.Method.POST, AnimalsNameList_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                   // new AlertDialog.Builder(context).setMessage(response).create().show();
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


                        }
                        progressBar.setVisibility(View.GONE);
                        dANAdapter=new diseasesAnimalsNamesAdapter(context,animalsList);
                        disesesRecycler.setAdapter(dANAdapter);

                    }else{
                        progressBar.setVisibility(View.GONE);
                       // Toast.makeText(getApplicationContext(), "Could not get data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e)
                {
                    progressBar.setVisibility(View.GONE);
                   // Toast.makeText(getApplicationContext(),"parsing exception: "+e.getMessage(),Toast.LENGTH_LONG).show();
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
