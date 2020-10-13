package com.hamza.veterinarysystemdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hamza.veterinarysystemdemo.AboutUs.AboutUSActivity;
import com.hamza.veterinarysystemdemo.CartPackage.CartActivity;
import com.hamza.veterinarysystemdemo.MedicineActivity.MedicineListActivity;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.hamza.veterinarysystemdemo.SettingsActivity.SettingsActivity;
import com.hamza.veterinarysystemdemo.AdminPostsActivity.AdminPostsAdapterEnglish;
import com.hamza.veterinarysystemdemo.AdminPostsActivity.AdminPostsAdapterUrdu;
import com.hamza.veterinarysystemdemo.AdminPostsActivity.AdminPostsModel;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.UserSection.Growth.GrowthAnimalListActivity;
import com.hamza.veterinarysystemdemo.UserSection.HistoryActivity;
import com.hamza.veterinarysystemdemo.user_doctor_Activity.User_doctor_appointment_Activity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView HeaderName;
    private CircleImageView HeaderProfileImage;
    private UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    private String UserNameHeader, ProfileHeaderPic, getAdminPost_url, lang;
    //String ChangeLanguage;
    int userid;

    List<AdminPostsModel> adminPosts;
    AdminPostsAdapterUrdu adapterUrdu;
    AdminPostsAdapterEnglish adapterEnglish;
    LinearLayoutManager linearLayoutManager;
    IPADDRESS ipaddress;
    Context context;
    RecyclerView adminPostRecycler;
    PrettyDialog prettyDialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.main_loading);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.LatestUpdates);
        }


        //ChangeLanguage = String.valueOf(R.string.ChangeLanguage);
        userSessionManager = new UserSessionManager(this);
        sessionDetails = userSessionManager.getSessionDetails();
        prettyDialog = new PrettyDialog(this);

        HeaderName = findViewById(R.id.nav_main_header_name);
        HeaderProfileImage = findViewById(R.id.nav_main_header_profile);
        adminPostRecycler = findViewById(R.id.recycler_admin_posts_user);


        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        //getAdminPost_url = "http://www.veterinarysystem.ga/getadminpost.php";
        getAdminPost_url="http://" + ip + "/VeterinarySystem/getadminpost.php";

        adminPosts = new ArrayList<>();

        context = this;
        linearLayoutManager = new LinearLayoutManager(this);

        adminPostRecycler.setLayoutManager(linearLayoutManager);
        adminPostRecycler.setHasFixedSize(true);


        UserNameHeader = sessionDetails.getName();
        ProfileHeaderPic = sessionDetails.getProfilepicture();
        userid = sessionDetails.getId();

       // Toast.makeText(getApplicationContext(),lang,Toast.LENGTH_LONG).show();

        /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        HeaderName = headerView.findViewById(R.id.nav_main_header_name);
        HeaderProfileImage = headerView.findViewById(R.id.nav_main_header_profile);
        setNavHeader();
        progressBar.setIndeterminateDrawable(new Circle());
        getAllPosts();

    }


    private void setNavHeader() {
        HeaderName.setText(UserNameHeader);
        Picasso.get().load(ProfileHeaderPic).into(HeaderProfileImage);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "en");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.dots_mainUser_cLanguage) {
            prettyDialog
                    .setTitle(getResources().getString(R.string.ChangeLanguage))
                    .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.English), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                @Override
                public void onClick() {
                    setLocale("en");
                    recreate();
                    prettyDialog.dismiss();
                }
            }).addButton(getResources().getString(R.string.Urdu), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                @Override
                public void onClick() {
                    setLocale("ur");
                    recreate();
                    prettyDialog.dismiss();
                }
            }).show();

            return true;
        } else if (id == R.id.dots_mainUser_contacts) {
            SendUserToAboutUsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SendUserToAboutUsActivity() {
        Intent intent=new Intent(getApplicationContext(), AboutUSActivity.class);
        startActivity(intent);
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
       if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1)
       {
           config.setLocale(locale);
       }
       else
       {
           config.locale = locale;
       }
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("langSetting", MODE_PRIVATE).edit();
        editor.putString("my_lang", language);
        editor.apply();
    }

    private void loadLocale() {
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "en");
        setLocale(lang);

    }

    private void getAllPosts() {
        StringRequest getPostsRequest = new StringRequest(Request.Method.POST, getAdminPost_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int post_id = object.getInt("postid");
                            String adminname = object.getString("adminname");
                            String postdes = object.getString("description");
                            String postimage = object.getString("image");
                            String adminimage = object.getString("adminimage");
                            String posttime = object.getString("time");
                            String postdate = object.getString("date");
                            AdminPostsModel Modelobj = new AdminPostsModel(post_id, postdes, posttime, postdate, postimage, adminname, adminimage);
                            adminPosts.add(Modelobj);

                        }
                        progressBar.setVisibility(View.GONE);
                      if (lang.equals("ur"))
                      {
                          adapterUrdu = new AdminPostsAdapterUrdu(context, adminPosts);
                          adminPostRecycler.setAdapter(adapterUrdu);
                      }
                      else if (lang.equals("en"))
                      {
                          adapterEnglish = new AdminPostsAdapterEnglish(context, adminPosts);
                          adminPostRecycler.setAdapter(adapterEnglish);
                      }





                    } else {
                        progressBar.setVisibility(View.GONE);
                       // Toast.makeText(getApplicationContext(), "Could not get post data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                   // Log.d("exception",e.getMessage());
                   // Toast.makeText(getApplicationContext(),"parsing exception: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> adminMap=new HashMap<>();
                adminMap.put("languageType",lang);
                return adminMap;
            }
        };
        Volley.newRequestQueue(this).add(getPostsRequest);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_post) {
            sendUserToUsersProblemsActivity();
        } else if (id == R.id.nav_doctor) {

            SendUserToUserDoctorAppointmentActivity();

        } else if (id == R.id.nav_medicine) {

            SendUserToMedicineActivity();

        } else if (id == R.id.nav_diseases) {

            SendUserToDiseasesActivity();

        } else if (id == R.id.nav_growth) {
            SendUserToGrowthActivity();

        } else if (id == R.id.nav_cart) {

            SendUserToCartActivity();

        } else if (id == R.id.nav_Histroy) {
            SendUserToHistoryActivity();

        }  else if (id == R.id.nav_settings) {

            SendUserToSettingsActivity();

        } else if (id == R.id.nav_logout) {

            SendUserToLoginActivity();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void SendUserToGrowthActivity() {
        Intent growthIntent=new Intent(getApplicationContext(), GrowthAnimalListActivity.class);
        startActivity(growthIntent);
    }

    private void SendUserToHistoryActivity() {
        Intent historyIntent=new Intent(getApplicationContext(), HistoryActivity.class);
        startActivity(historyIntent);
    }

    private void SendUserToCartActivity() {

        Intent cartintent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(cartintent);
    }

    private void SendUserToUserDoctorAppointmentActivity() {
        Intent UDAIntent = new Intent(getApplicationContext(), User_doctor_appointment_Activity.class);
        startActivity(UDAIntent);
    }

    private void SendUserToMedicineActivity() {

        Intent medicineIntent = new Intent(getApplicationContext(), MedicineListActivity.class);
        startActivity(medicineIntent);
    }

    private void SendUserToSettingsActivity() {
        Intent settingsintent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(settingsintent);
    }

    private void sendUserToUsersProblemsActivity() {
        Intent usersProblemsIntent = new Intent(getApplicationContext(), UserProblemsActivity.class);
        startActivity(usersProblemsIntent);
    }

    private void SendUserToDiseasesActivity() {
        Intent diseasesIntent = new Intent(getApplicationContext(), diseasesCategoryActivity.class);
        startActivity(diseasesIntent);
    }

    private void SendUserToLoginActivity() {
        userSessionManager.setLoggedIn(false);
        userSessionManager.clearSessionData();
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }
}
