package com.hamza.veterinarysystemdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.hamza.veterinarysystemdemo.UserSection.UserProblemsActivity.UserProblemsAdapterUrdu;
import com.hamza.veterinarysystemdemo.UserSection.UserProblemsActivity.userProblemsAdapterEnglish;
import com.hamza.veterinarysystemdemo.models.userProblemsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserProblemsActivity extends AppCompatActivity {

    private ImageView add_post_activity;
    private RecyclerView userPromblemsRecycler;
    private Toolbar mToolBar;
    private List<userProblemsModel> postList;
    private LinearLayoutManager linearLayoutManager;
    Context context;
    IPADDRESS ipaddress;
    SearchView searchView;
    private userProblemsAdapterEnglish uPAdapter;
    private String getPost_url,lang;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_problems);
        searchView=findViewById(R.id.etuserproblemsSearchBox);
        userPromblemsRecycler=findViewById(R.id.userProblemRecycler);
        progressBar = findViewById(R.id.up_loading);
        mToolBar=findViewById(R.id.userProblemsToolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.UserProblems);
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
        //getPost_url="http://www.veterinarysystem.ga/getproblems.php";
        getPost_url="http://" + ip + "/VeterinarySystem/getproblems.php";

        postList=new ArrayList<>();

        context=this;
        linearLayoutManager=new LinearLayoutManager(this);
        userPromblemsRecycler.setLayoutManager(linearLayoutManager);
        userPromblemsRecycler.setHasFixedSize(true);



        add_post_activity=findViewById(R.id.add_new_post_button);



        add_post_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToAddProblemsActivity();
            }
        });
        progressBar.setIndeterminateDrawable(new Circle());
        getAllPosts();
    }

    private void getAllPosts() {
        StringRequest getPostsRequest=new StringRequest(Request.Method.GET, getPost_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArray=jsonObject.getJSONArray("postdata");
                    boolean status = jsonObject.getBoolean("status");
                    if (status)
                    {
                        for (int i=0; i<jsonArray.length();i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            int id=object.getInt("id");
                            String pname=object.getString("pname");
                            String pdes=object.getString("pdescription");
                            String pimage=object.getString("pimage");
                            String ppimage=object.getString("pprofileimage");
                            String ptime=object.getString("ptime");
                            String pdate=object.getString("pdate");
                            userProblemsModel userProblemsModelobj=new userProblemsModel(id,pdes,ptime,pdate,pimage,pname,ppimage);
                            postList.add(userProblemsModelobj);

                        }
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String s) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String s) {
                                uPAdapter.getFilter().filter(s);
                                return false;
                            }
                        });
                        Collections.reverse(postList);
                        progressBar.setVisibility(View.GONE);
                       // userPromblemsRecycler.setVisibility(View.VISIBLE);
                        if (lang.equals("en"))
                        {
                            uPAdapter=new userProblemsAdapterEnglish(postList,context);
                            userPromblemsRecycler.setAdapter(uPAdapter);
                        }
                        else if (lang.equals("ur"))
                        {
                            UserProblemsAdapterUrdu adapterUrdu=new UserProblemsAdapterUrdu(postList,context);
                            userPromblemsRecycler.setAdapter(adapterUrdu);
                        }


                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(getApplicationContext(), "Could not get post data", Toast.LENGTH_SHORT).show();
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
        });
        Volley.newRequestQueue(this).add(getPostsRequest);
    }

    private void SendUserToAddProblemsActivity() {
        Intent addPrombelsIntent=new Intent(getApplicationContext(),AddProblemsActivity.class);
        startActivity(addPrombelsIntent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "en");

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
