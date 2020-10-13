package com.hamza.veterinarysystemdemo;

import android.app.Activity;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class diseasesDescriptionActivity extends AppCompatActivity {
    ImageView dImage;
    TextView intro, sysptoms, causes, treat;
    Toolbar mToolBar;
    String diseaseID, lang,getData_url;
    IPADDRESS ipaddress;
    ProgressBar progressBar;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseases_description);
        mToolBar = findViewById(R.id.dda_toolbar);
        intro = findViewById(R.id.dda_intro);
        sysptoms = findViewById(R.id.dda_symptoms);
        causes = findViewById(R.id.dda_causes);
        treat = findViewById(R.id.dda_treatement);
        dImage = findViewById(R.id.dda_image);
        scrollView=findViewById(R.id.dda_scroll);
        progressBar=findViewById(R.id.dda_loading);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.DiseasesDescription);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        diseaseID = getIntent().getExtras().get("did").toString();
       // SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        //lang = sharedPreferences.getString("my_lang", "");
        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        getData_url="http://" + ip + "/VeterinarySystem/getdiseasesdiscription.php";
        // getData_url="http://www.veterinarysystem.ga/getdiseasesdiscription.php";
        progressBar.setIndeterminateDrawable(new Circle());
        getData();
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "en");

    }

    private void getData() {
        StringRequest diseaseDescription = new StringRequest(Request.Method.POST, getData_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Boolean status = object.getBoolean("status");
                    if (status) {
                        String in = object.getString("introduction");
                        String s = object.getString("symptoms");
                        String c = object.getString("casuses");
                        String t = object.getString("treatment");
                        String im = object.getString("image");
                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        intro.setText(in);
                        sysptoms.setText(s);
                        causes.setText(c);
                        treat.setText(t);
                        if (!TextUtils.isEmpty(im)) {
                            Picasso.get().load(im).into(dImage);
                        } else {
                            dImage.setVisibility(View.GONE);
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                       // Toast.makeText(diseasesDescriptionActivity.this, "Status: False", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                   // Toast.makeText(diseasesDescriptionActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(diseasesDescriptionActivity.this, "Volley: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("did", diseaseID);
                map.put("languageType", lang);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(diseaseDescription);
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
