package com.hamza.veterinarysystemdemo.StoreSection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.hamza.veterinarysystemdemo.AdminPostsActivity.AdminPostsAdapterEnglish;
import com.hamza.veterinarysystemdemo.AdminPostsActivity.AdminPostsAdapterUrdu;
import com.hamza.veterinarysystemdemo.AdminPostsActivity.AdminPostsModel;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.LoginActivity;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.hamza.veterinarysystemdemo.SettingsActivity.SettingsActivity;
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

public class StoreDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    private TextView HeaderName;
    private CircleImageView HeaderProfileImage;
    private String UserNameHeader, ProfileHeaderPic, getAdminPost_url, lang;
    int storeID;
    PrettyDialog prettyDialog;
    List<AdminPostsModel> adminPosts;
    AdminPostsAdapterUrdu adapterUrdu;
    AdminPostsAdapterEnglish adapterEnglish;
    LinearLayoutManager linearLayoutManager;
    IPADDRESS ipaddress;
    Context context;
    RecyclerView adminPostRecycler;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar=findViewById(R.id.smd_loading);
        adminPostRecycler = findViewById(R.id.recycler_admin_posts_store);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.LatestUpdates);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        HeaderName = headerView.findViewById(R.id.nav_header_store_name);
        HeaderProfileImage = headerView.findViewById(R.id.nav_header_store_image);

        userSessionManager = new UserSessionManager(this);
        prettyDialog = new PrettyDialog(this);
        sessionDetails = userSessionManager.getSessionDetails();
        UserNameHeader = sessionDetails.getName();
        ProfileHeaderPic = sessionDetails.getProfilepicture();
        storeID = sessionDetails.getId();

        setNavHeader();


        //Recycler Start
        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        //getAdminPost_url = "http://www.veterinarysystem.ga/getproblems.php";
        getAdminPost_url = "http://" + ip + "/VeterinarySystem/getadminpost.php";

        adminPosts = new ArrayList<>();

        context = this;
        linearLayoutManager = new LinearLayoutManager(this);
        adminPostRecycler.setLayoutManager(linearLayoutManager);
        adminPostRecycler.setHasFixedSize(true);


        //RecyclerEnd

        progressBar.setIndeterminateDrawable(new Circle());
        getAllPosts();
    }

    private void setNavHeader() {
        HeaderName.setText(UserNameHeader);
        Picasso.get().load(ProfileHeaderPic).into(HeaderProfileImage);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "en");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.store_drawer, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.dots_store_cLanguage) {
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
        } else if (id == R.id.dots_store_contacts) {
            Intent intent=new Intent(getApplicationContext(), AboutUSActivity.class);
            startActivity(intent);
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
                        if (lang.equals("ur")) {
                           adapterUrdu = new AdminPostsAdapterUrdu(context, adminPosts);
                          adminPostRecycler.setAdapter(adapterUrdu);
                        } else if (lang.equals("en")){
                        adapterEnglish = new AdminPostsAdapterEnglish(context, adminPosts);
                        adminPostRecycler.setAdapter(adapterEnglish);
                         }


                    } else {
                        progressBar.setVisibility(View.GONE);
                       // Toast.makeText(getApplicationContext(), "Could not get post data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
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
                Map<String, String> adminMap = new HashMap<>();
                adminMap.put("languageType", lang);
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

        if (id == R.id.nav_store_addItems) {
            SendUserToAddItemActivity();
        } else if (id == R.id.nav_store_myproducts) {
            sendStoreToMyProductsActivity();

        } else if (id == R.id.nav_store_orders) {
            sendStoreToMyOrdersActivity();

        } else if (id == R.id.nav_store_orderHistory) {
            sendStoreToHistoryActivity();

        } else if (id == R.id.nav_store_settings) {
            SendStoreToSettingsActivity();

        } else if (id == R.id.nav_store_logout) {
            sendStoreToLoginActivity();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendStoreToHistoryActivity() {

        Intent historyIntent = new Intent(getApplicationContext(), StoreOrderHistoryActivity.class);
        startActivity(historyIntent);
    }

    private void sendStoreToMyOrdersActivity() {
        Intent orderIntent = new Intent(getApplicationContext(), StoreMedicineOrderActivity.class);
        startActivity(orderIntent);
    }

    private void sendStoreToMyProductsActivity() {
        Intent myProductsIntent = new Intent(getApplicationContext(), StoreMyProductsActivity.class);
        startActivity(myProductsIntent);
    }

    private void SendUserToAddItemActivity() {
        Intent additemintent = new Intent(getApplicationContext(), StoreAddItemsActivity.class);
        startActivity(additemintent);
    }

    private void sendStoreToLoginActivity() {
        userSessionManager.setLoggedIn(false);
        userSessionManager.clearSessionData();
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    private void SendStoreToSettingsActivity() {
        Intent settingsintent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(settingsintent);
    }
}
