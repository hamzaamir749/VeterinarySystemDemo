package com.hamza.veterinarysystemdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hamza.veterinarysystemdemo.CartPackage.CartActivity;
import com.hamza.veterinarysystemdemo.SettingsActivity.SettingsActivity;
import com.hamza.veterinarysystemdemo.adapters.AdminPostsAdapterEnglish;
import com.hamza.veterinarysystemdemo.adapters.AdminPostsAdapterUrdu;
import com.hamza.veterinarysystemdemo.models.AdminPostsModel;
import com.hamza.veterinarysystemdemo.user_doctor_Activity.User_doctor_appointment_Activity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView HeaderName;
    private CircleImageView HeaderProfileImage;
    private UserSessionManager userSessionManager;
    ClientSessionDetails clientSessionDetails;
    private String UserNameHeader, ProfileHeaderPic, getAdminPost_url, lang;
    //String ChangeLanguage;
    private FirebaseAuth auth;
    int userid;

    List<AdminPostsModel> adminPosts;
    AdminPostsAdapterUrdu adapterUrdu;
    AdminPostsAdapterEnglish adapterEnglish;
    LinearLayoutManager linearLayoutManager;
    IPADDRESS ipaddress;
    Context context;
    RecyclerView adminPostRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        FirebAseLogIn();

        //ChangeLanguage = String.valueOf(R.string.ChangeLanguage);
        userSessionManager = new UserSessionManager(this);
        clientSessionDetails = userSessionManager.getClientDataDetails();

        HeaderName = findViewById(R.id.nav_main_header_name);
        HeaderProfileImage = findViewById(R.id.nav_main_header_profile);
        adminPostRecycler = findViewById(R.id.recycler_admin_posts_user);


        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        getAdminPost_url = "http://www.veterinarysystem.ga/getproblems.php";

        adminPosts = new ArrayList<>();

        context = this;
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adminPostRecycler.setLayoutManager(linearLayoutManager);
        adminPostRecycler.setHasFixedSize(true);


        UserNameHeader = clientSessionDetails.getName();
        ProfileHeaderPic = clientSessionDetails.getProfilepicture();
        userid = clientSessionDetails.getId();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.LatestUpdates);
        }

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
        getAllPosts();

    }

    private void FirebAseLogIn() {
        auth.signInWithEmailAndPassword("hamzaamir749@gmail.com", "123456").addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                } else {
                    Toast.makeText(MainActivity.this, "FireBaseNotLogggedIn: " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.dots_mainUser_cLanguage) {
            new PrettyDialog(MainActivity.this)
                    .setTitle(getResources().getString(R.string.ChangeLanguage))
                    .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.English), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                @Override
                public void onClick() {
                    setLocale("en");
                    recreate();
                }
            }).addButton(getResources().getString(R.string.Urdu), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                @Override
                public void onClick() {
                    setLocale("ur");
                    recreate();
                }
            }).show();
            return true;
        } else if (id == R.id.dots_mainUser_contacts) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("langSetting", MODE_PRIVATE).edit();
        editor.putString("my_lang", language);
        editor.apply();
    }

    private void loadLocale() {
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "");
        setLocale(lang);

    }

    private void getAllPosts() {
        StringRequest getPostsRequest = new StringRequest(Request.Method.GET, getAdminPost_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("postdata");
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String pname = object.getString("pname");
                            String pdes = object.getString("pdescription");
                            String pimage = object.getString("pimage");
                            String ppimage = object.getString("pprofileimage");
                            String ptime = object.getString("ptime");
                            String pdate = object.getString("pdate");
                            AdminPostsModel Modelobj = new AdminPostsModel(id, pdes, ptime, pdate, pimage, pname, ppimage);
                            adminPosts.add(Modelobj);
                            adapterUrdu = new AdminPostsAdapterUrdu(context, adminPosts);
                            adapterEnglish = new AdminPostsAdapterEnglish(context, adminPosts);
                        }
                        Collections.reverse(adminPosts);

                        if (lang.equals("en")) {
                            adminPostRecycler.setAdapter(adapterEnglish);

                        } else if (lang.equals("ur")) {
                            adminPostRecycler.setAdapter(adapterUrdu);

                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Could not get post data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
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

        } else if (id == R.id.nav_cart) {

            SendUserToCartActivity();

        } else if (id == R.id.nav_Histroy) {

        } else if (id == R.id.nav_manual) {

        } else if (id == R.id.nav_settings) {

            SendUserToSettingsActivity();

        } else if (id == R.id.nav_logout) {

            SendUserToLoginActivity();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void SendUserToCartActivity() {

        Intent cartintent=new Intent(getApplicationContext(), CartActivity.class);
        startActivity(cartintent);
    }

    private void SendUserToUserDoctorAppointmentActivity() {
        Intent UDAIntent=new Intent(getApplicationContext(), User_doctor_appointment_Activity.class);
        startActivity(UDAIntent);
    }

    private void SendUserToMedicineActivity() {

        Intent medicineIntent = new Intent(getApplicationContext(), userMedicineListActivity.class);
        startActivity(medicineIntent);
    }

    private void SendUserToSettingsActivity() {
        Intent settingsintent = new Intent(getApplicationContext(), SettingsActivity.class);
        settingsintent.putExtra("profileid", userid);
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
        userSessionManager.setClientLoggedIn(false);
        userSessionManager.clearClientData();
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }
}
