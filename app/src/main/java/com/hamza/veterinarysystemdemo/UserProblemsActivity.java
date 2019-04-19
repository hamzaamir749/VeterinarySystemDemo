package com.hamza.veterinarysystemdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.veterinarysystemdemo.adapters.diseasesAnimalsNamesAdapter;
import com.hamza.veterinarysystemdemo.adapters.userProblemsAdapter;
import com.hamza.veterinarysystemdemo.models.diseasesAnimalsNamesModel;
import com.hamza.veterinarysystemdemo.models.userProblemsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserProblemsActivity extends AppCompatActivity {

    private ImageView add_post_activity;
    private RecyclerView userPromblemsRecycler;
    private Toolbar mToolBar;
    private List<userProblemsModel> postList;
    private LinearLayoutManager linearLayoutManager;
    Context context;
    IPADDRESS ipaddress;
    private userProblemsAdapter uPAdapter;
    private String getPost_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_problems);

        userPromblemsRecycler=findViewById(R.id.userProblemRecycler);

        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        getPost_url="http://www.veterinarysystem.ga/getproblems.php";

        postList=new ArrayList<>();

        context=this;
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        userPromblemsRecycler.setLayoutManager(linearLayoutManager);
        userPromblemsRecycler.setHasFixedSize(true);


        mToolBar=findViewById(R.id.userProblemsToolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("User Problems");
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
        add_post_activity=findViewById(R.id.add_new_post_button);



        add_post_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToAddProblemsActivity();
            }
        });

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
                            uPAdapter=new userProblemsAdapter(postList,context);
                        }
                        Collections.reverse(postList);

                        userPromblemsRecycler.setAdapter(uPAdapter);

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Could not get post data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e)
                {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
