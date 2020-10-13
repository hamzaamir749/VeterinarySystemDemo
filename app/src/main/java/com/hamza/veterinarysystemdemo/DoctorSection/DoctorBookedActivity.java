package com.hamza.veterinarysystemdemo.DoctorSection;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class DoctorBookedActivity extends AppCompatActivity {

    Toolbar mToolbar;
    IPADDRESS ipaddress;
    LocationManager locationManager;
    String cancelAppointment_URL,acceptAppointment_URL,latituteDoctor="72.860740",longitudeDoctor="32.935549",saveAppointmentAcceptTime;
    private static final int REQUEST_LOCATION = 1;
    TextView client_name,client_mobile_no,client_place;
    Button appoint_Accept,appoint_Cancel,start_Navigation;
    CircleImageView client_image;

    ProgressDialog progressDialog;
    PrettyDialog prettyDialog;
    String clientname,latitudeclient, longitudeclient,clientphone,clientaddress,clientimage,clientID;
    int doctorID;
    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_booked);
        mToolbar = findViewById(R.id.db_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.clients);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        client_image=findViewById(R.id.db_client_img);
        client_name=findViewById(R.id.db_client_name);
        client_mobile_no=findViewById(R.id.db_client_number);
        client_place=findViewById(R.id.db_client_place);
        appoint_Accept=findViewById(R.id.db_appoint_accept);
        appoint_Cancel=findViewById(R.id.db_appoint_cancel);
        start_Navigation=findViewById(R.id.db_start_navigation);
        //Getting Values
        clientID=getIntent().getExtras().get("clientKey").toString();
        clientname=getIntent().getExtras().get("clientname").toString();
        clientimage=getIntent().getExtras().get("clientimage").toString();
        clientaddress=getIntent().getExtras().get("clientaddress").toString();
        latitudeclient=getIntent().getExtras().get("clientlati").toString();
        longitudeclient=getIntent().getExtras().get("clientlongi").toString();
        clientphone=getIntent().getExtras().get("clientphone").toString();


        progressDialog=new ProgressDialog(this);
        prettyDialog=new PrettyDialog(this);
        userSessionManager=new UserSessionManager(this);
        builder=new AlertDialog.Builder(this);
        sessionDetails=userSessionManager.getSessionDetails();
        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        acceptAppointment_URL="http://"+ip+"/VeterinarySystem/acceptClientAppointment.php";
        cancelAppointment_URL="http://"+ip+"/VeterinarySystem/cancelClientAppointment.php";
        //acceptAppointment_URL="http://www.veterinarysystem.ga/acceptClientAppointment.php";
        //cancelAppointment_URL="http://www.veterinarysystem.ga/cancelClientAppointment.php";
        //Set Fields
        client_name.setText(clientname);
        client_mobile_no.setText(clientphone);
        client_place.setText(clientaddress);
        Picasso.get().load(clientimage).into(client_image);
        appoint_Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appoint_ClientFunction();
            }
        });
        appoint_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appoint_CancelFunction();
            }
        });
    }

    private void appoint_CancelFunction() {

        progressDialog.setTitle(getResources().getString(R.string.cancelAppointment));
        progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        StringRequest cancelAppointmentRequest=new StringRequest(Request.Method.POST, cancelAppointment_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        prettyDialog.setTitle(getResources().getString(R.string.status))
                                .setMessage(getResources().getString(R.string.appointmentCancel))
                                .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.oK), R.color.design_default_color_primary_dark, R.color.loginBackgroundcolor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), DoctorAppointmentActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).show();

                    } else {
                        //Toast.makeText(DoctorBookedActivity.this, getResources().getString(R.string.notAccepted), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                   // Toast.makeText(DoctorBookedActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> cancelMap=new HashMap<>();
                cancelMap.put("doctorid",String.valueOf(sessionDetails.getId()));
                cancelMap.put("userid",clientID);
                return cancelMap;
            }
        };
        Volley.newRequestQueue(this).add(cancelAppointmentRequest);

    }

    private void appoint_ClientFunction() {
        locatinManagerFunction();
        if (TextUtils.isEmpty(latituteDoctor) && TextUtils.isEmpty(longitudeDoctor)) {
            Toast.makeText(DoctorBookedActivity.this,  getResources().getString(R.string.waitLocation), Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setTitle(getResources().getString(R.string.acceptAppointment));
            progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            StringRequest AppointmentRequest=new StringRequest(Request.Method.POST, acceptAppointment_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("status");
                        boolean delete=jsonObject.getBoolean("delete");
                        boolean move=jsonObject.getBoolean("move");
                        if (status && delete && move) {
                            builder.setMessage(getResources().getString(R.string.appointmentAccepted))
                                    .setCancelable(false)
                                    .setPositiveButton(getResources().getString(R.string.startNavigation), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                            Intent navigationIntent=new Intent(DoctorBookedActivity.this,EmbeddedNavigationActivity.class);
                                            navigationIntent.putExtra("docLat","32.935549");
                                            navigationIntent.putExtra("docLang","72.860740");
                                            navigationIntent.putExtra("userLat","32.9266836");
                                            navigationIntent.putExtra("userLang","72.8016119");
                                            startActivity(navigationIntent);
                                            finish();
                                        }
                                    });
                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle(getResources().getString(R.string.navigationMessage));
                            alert.show();

                        } else {
                            Toast.makeText(DoctorBookedActivity.this, getResources().getString(R.string.notAccepted), Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        Toast.makeText(DoctorBookedActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> acceptMap=new HashMap<>();
                    acceptMap.put("doctoruid",String.valueOf(sessionDetails.getId()));
                    acceptMap.put("userid",clientID);
                    return acceptMap;
                }
            };
            Volley.newRequestQueue(this).add(AppointmentRequest);
        }


    }


    public void locatinManagerFunction() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(DoctorBookedActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (DoctorBookedActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DoctorBookedActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                latituteDoctor = String.valueOf(latti);
                longitudeDoctor = String.valueOf(longi);


            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                latituteDoctor = String.valueOf(latti);
                longitudeDoctor = String.valueOf(longi);


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                latituteDoctor = String.valueOf(latti);
                longitudeDoctor = String.valueOf(longi);


            } else {

                Toast.makeText(this, getResources().getString(R.string.unbletoTraceyourlocation), Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.pleaseTurnONyourGPSConnection))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
