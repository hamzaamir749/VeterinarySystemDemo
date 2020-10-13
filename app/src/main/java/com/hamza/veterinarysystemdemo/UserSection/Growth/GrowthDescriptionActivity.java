package com.hamza.veterinarysystemdemo.UserSection.Growth;

import android.app.Activity;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
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
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.R;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GrowthDescriptionActivity extends AppCompatActivity {

    TextView name, quantity, usage;
    Toolbar mToolBar;
    String lang, type=null, id, getData_url;
    IPADDRESS ipaddress;
    TextView title;
    ScrollView scrollView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_description);
        name = findViewById(R.id.gd_foodname);
        quantity = findViewById(R.id.gd_quantity);
        usage = findViewById(R.id.gd_usage);
        mToolBar = findViewById(R.id.gda_toolbar);
        title = findViewById(R.id.gd_title);
        scrollView = findViewById(R.id.gda_scroll);
        progressBar = findViewById(R.id.gda_loading);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.Description);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        type = getIntent().getExtras().get("type").toString();
        id = getIntent().getExtras().get("id").toString();
        if (type.equals("milk")) {
            title.setText(getResources().getString(R.string.IncreaseMilk));
        } else if (type.equals("meet")) {
            title.setText(getResources().getString(R.string.IncreaseMeet));
        }

        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        getData_url = "http://" + ip + "/VeterinarySystem/getgrowthdescription.php";
        // getData_url="http://www.veterinarysystem.ga/getgrowthdescription.php";

//        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
  //      lang = sharedPreferences.getString("my_lang", "");
        progressBar.setIndeterminateDrawable(new Circle());
        getData();

    }

    private void getData() {
        StringRequest diseaseDescription = new StringRequest(Request.Method.POST, getData_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Boolean status = object.getBoolean("status");
                    if (status) {
                        String use = object.getString("usagefood");
                        String namee = object.getString("foodname");
                        String quan = object.getString("quantity");
                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        usage.setText(use);
                        name.setText(namee);
                        quantity.setText(quan);

                    } else {
                        progressBar.setVisibility(View.GONE);
                       // Toast.makeText(GrowthDescriptionActivity.this, "Status: False", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                   // Toast.makeText(GrowthDescriptionActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GrowthDescriptionActivity.this, "Volley: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("languageType", lang);
                map.put("type", type);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(diseaseDescription);
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
