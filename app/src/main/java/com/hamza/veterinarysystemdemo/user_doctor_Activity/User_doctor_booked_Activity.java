package com.hamza.veterinarysystemdemo.user_doctor_Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class User_doctor_booked_Activity extends AppCompatActivity implements CallListener {
    Toolbar mToolbar;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    String latitude="72.8016119", longitude="32.9266836", doctorid, userid, getDoctorProfile_URL;

    String saveCurrentDate, saveCurrentTime, appointDoctor_URL, cancelAppiontDoctor_URL;

    String dname, dimage, dphone, daddress,type,name,mNo;

    TextView doctor_name, doctor_place, doctor_mobile, doctor_rating;
    CircleImageView doctor_img;
    Button doctor_appointment, doctor_appointment_cancel;
    IPADDRESS ipaddress;
    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    ProgressDialog progressDialog;
    PrettyDialog prettyDialog;
    Call call;
    Button callbtn;
    SinchClient sinchClient;
    CallClient callClient;
    String lang;
    private static final String APP_KEY = "0dbdb676-11a0-4762-b94f-7e00d792d830";
    private static final String APP_SECRET = "Z6sDniMeK0qvhXLLSutjmw==";
    private static final String ENVIRONMENT = "clientapi.sinch.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_doctor_booked_);
        mToolbar = findViewById(R.id.udb_toolbar);
        callbtn=findViewById(R.id.call_btn_client);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.Appointment);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }



        //Bind XML view
        doctor_img = findViewById(R.id.udb_doc_img);
        doctor_name = findViewById(R.id.udb_doc_name);
        doctor_mobile = findViewById(R.id.udb_doc_number);
        doctor_place = findViewById(R.id.udb_doc_place);
        doctor_rating = findViewById(R.id.udb_doc_rating);
        doctor_appointment = findViewById(R.id.udb_appoint_doctor);
        doctor_appointment_cancel = findViewById(R.id.udb_appoint_cancel);
        progressDialog = new ProgressDialog(this);
        prettyDialog = new PrettyDialog(User_doctor_booked_Activity.this);
        userSessionManager=new UserSessionManager(this);
        //Ip Address
        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        getDoctorProfile_URL = "http://" + ip + "/VeterinarySystem/getDoctorProfile.php";
        appointDoctor_URL="http://" + ip + "/VeterinarySystem/setappointment.php";
        cancelAppiontDoctor_URL="http://" + ip + "/VeterinarySystem/cancelAppointment.php";
        //getDoctorProfile_URL="http://www.veterinarysystem.ga/getDoctorProfile.php";
        //appointDoctor_URL="http://www.veterinarysystem.ga/setappointment.php";
        //cancelAppiontDoctor_URL="http://www.veterinarysystem.ga/cancelAppointment.php";
        //Location
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.RECORD_AUDIO,android.Manifest.permission.MODIFY_AUDIO_SETTINGS}, REQUEST_LOCATION);
        //Get doctor id from previous
        doctorid = getIntent().getExtras().get("doctorKey").toString();
        type=getIntent().getExtras().get("type").toString();
        if (type=="pending")
        {
            doctor_appointment_cancel.setVisibility(View.VISIBLE);
            doctor_appointment.setVisibility(View.GONE);
        }else if (type=="booked")
        {
            doctor_appointment_cancel.setVisibility(View.GONE);
            doctor_appointment.setVisibility(View.VISIBLE);
        }
        android.content.Context context = User_doctor_booked_Activity.this;

        sinchClient = Sinch.getSinchClientBuilder().context(context)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .userId("140441")
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.start();
       // callClient=sinchClient.getCallClient();

        sessionDetails = userSessionManager.getSessionDetails();

        userid = String.valueOf(sessionDetails.getId());

        //Getting Doctor Profile
        getDoctorDetail();

        //Appoint Doctor
        doctor_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppointDoctor();
            }
        });

        //Appointment Cancel
        doctor_appointment_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAppointDoctor();
            }
        });
        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (call == null) {
                   try {
                       call = sinchClient.getCallClient().callPhoneNumber(mNo);
                       // call=callClient.callPhoneNumber("+923488967050");
                       new TTFancyGifDialog.Builder(User_doctor_booked_Activity.this)
                               .setTitle(name)
                               .setMessage(mNo)
                               .setPositiveBtnText("End Call")
                               .setPositiveBtnBackground("#E0410F")
                               .setGifResource(R.drawable.callcelnew)      //pass your gif, png or jpg
                               .isCancellable(true)
                               .OnPositiveClicked(new TTFancyGifDialogListener() {
                                   @Override
                                   public void OnClick() {
                                       call.hangup();
                                       call=null;
                                   }
                               })
                               .build();
                   }
                   catch (Exception e)
                   {

                   }

                } else {
                    call.hangup();
                }
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "en");

    }



    private void cancelAppointDoctor() {

        progressDialog.setTitle(getResources().getString(R.string.cancelAppointment));
        progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest cancelappointDoctorRequest = new StringRequest(Request.Method.POST, cancelAppiontDoctor_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {

                    JSONObject object = new JSONObject(response);
                    boolean status = object.getBoolean("status");
                    if (status) {
                       prettyDialog.setTitle(getResources().getString(R.string.status)).setMessage(getResources().getString(R.string.DoctorAppointCancel))
                                .addButton(getResources().getString(R.string.oK), R.color.pdlg_color_white, R.color.loginBackgroundcolor,

                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                prettyDialog.dismiss();
                                                doctor_appointment_cancel.setFocusable(false);
                                                doctor_appointment_cancel.setVisibility(View.GONE);
                                                doctor_appointment.setVisibility(View.VISIBLE);
                                                doctor_appointment.setFocusable(true);

                                            }
                                        }).show();
                    }

                } catch (Exception e) {
                   // Toast.makeText(User_doctor_booked_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> cancelAppointmentMap=new HashMap<>();
                cancelAppointmentMap.put("userid",userid);
                cancelAppointmentMap.put("doctorid",doctorid);
                return cancelAppointmentMap;
            }
        };
        Volley.newRequestQueue(this).add(cancelappointDoctorRequest);
    }

    private void AppointDoctor() {

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:aa");
        saveCurrentTime = currentTime.format(calFordTime.getTime());

        locatinManagerFunction();

        if (TextUtils.isEmpty(latitude) && TextUtils.isEmpty(longitude)) {
            Toast.makeText(User_doctor_booked_Activity.this, "Please Wait... While getting Your Location... Make sure your GPS & Internet is on !!! ", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog.setTitle(getResources().getString(R.string.AppointingDoctor));
            progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            StringRequest appointDoctorRequest = new StringRequest(Request.Method.POST, appointDoctor_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {

                        JSONObject object = new JSONObject(response);
                        boolean status = object.getBoolean("status");
                        if (status) {
                            prettyDialog.setTitle(getResources().getString(R.string.status)).setMessage(getResources().getString(R.string.DoctorAppointSuccessfully))
                                    .addButton(getResources().getString(R.string.Done), R.color.pdlg_color_white, R.color.loginBackgroundcolor,

                                            new PrettyDialogCallback() {
                                                @Override
                                                public void onClick() {
                                                    prettyDialog.dismiss();
                                                    doctor_appointment_cancel.setFocusable(true);
                                                    doctor_appointment_cancel.setVisibility(View.VISIBLE);
                                                    doctor_appointment.setVisibility(View.GONE);
                                                    doctor_appointment.setFocusable(false);

                                                }
                                            }).show();
                        }

                    } catch (Exception e) {
                       // Toast.makeText(User_doctor_booked_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> appointDoctorMap = new HashMap<>();
                    appointDoctorMap.put("uid", userid);
                    appointDoctorMap.put("did", doctorid);
                    appointDoctorMap.put("uname", sessionDetails.getName());
                    appointDoctorMap.put("dname", dname);
                    appointDoctorMap.put("uphone", sessionDetails.getPhone());
                    appointDoctorMap.put("dphone", dphone);
                    appointDoctorMap.put("uimage", sessionDetails.getProfilepicture());
                    appointDoctorMap.put("dimage", dimage);
                    appointDoctorMap.put("uaddress", sessionDetails.getAddress());
                    appointDoctorMap.put("daddress", daddress);
                    appointDoctorMap.put("ulat", latitude);
                    appointDoctorMap.put("ulongi", longitude);
                    appointDoctorMap.put("uappointment", "pending");
                    appointDoctorMap.put("utime", saveCurrentTime);
                    appointDoctorMap.put("udate", saveCurrentDate);

                    return appointDoctorMap;

                }
            };
            Volley.newRequestQueue(this).add(appointDoctorRequest);
        }

    }

    private void getDoctorDetail() {

        StringRequest getDocProfileRequest = new StringRequest(Request.Method.POST, getDoctorProfile_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    boolean status = object.getBoolean("status");
                    if (status) {
                         name = object.getString("name");
                        String image = object.getString("profileimage");
                         mNo = object.getString("phone");
                        String place = object.getString("address");

                        Picasso.get().load(image).into(doctor_img);
                        doctor_name.setText(name);
                        doctor_mobile.setText(mNo);
                        doctor_place.setText(place);
                        doctor_rating.setText("5*");

                        giveValueToGlobal(name, image, mNo, place);
                        boolean appointmentStatus = object.getBoolean("appintment");
                        if (appointmentStatus) {
                            doctor_appointment.setVisibility(View.VISIBLE);
                            doctor_appointment.setFocusable(true);
                            doctor_appointment_cancel.setFocusable(false);
                            doctor_appointment_cancel.setVisibility(View.GONE);
                        } else {
                            doctor_appointment_cancel.setFocusable(true);
                            doctor_appointment_cancel.setVisibility(View.VISIBLE);
                            doctor_appointment.setVisibility(View.GONE);
                            doctor_appointment.setFocusable(false);
                        }
                    }

                } catch (Exception e) {
                   // Toast.makeText(User_doctor_booked_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> doctoruniqueid = new HashMap<>();
                doctoruniqueid.put("doctoruid", doctorid);
                doctoruniqueid.put("userid", userid);

                return doctoruniqueid;
            }
        };
        Volley.newRequestQueue(this).add(getDocProfileRequest);

    }

    private void giveValueToGlobal(String name, String image, String mNo, String place) {
        dname = name;
        dimage = image;
        dphone = mNo;
        daddress = place;
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
        if (ActivityCompat.checkSelfPermission(User_doctor_booked_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (User_doctor_booked_Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(User_doctor_booked_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


            } else {

                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCallProgressing(Call call) {

    }

    @Override
    public void onCallEstablished(Call call) {

    }

    @Override
    public void onCallEnded(Call call) {
        call.hangup();
        call=null;
    }

    @Override
    public void onShouldSendPushNotification(Call call, List<PushPair> list) {

    }
}
