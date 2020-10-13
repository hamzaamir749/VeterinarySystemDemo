package com.hamza.veterinarysystemdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.veterinarysystemdemo.DoctorSection.DoctorAppointmentHistory.DoctorAppointmentsHistoryActivity;
import com.hamza.veterinarysystemdemo.MedicineHistory.ApprovedMedicineActivity;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.hamza.veterinarysystemdemo.UserSection.AppointmentsDoctorHistory.ApprovedAppointmentsActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class FeedBackActivity extends AppCompatActivity {
    Toolbar mToolBar;
    String againstID, id;
    EditText text;
    Button submit;
    ProgressDialog progressDialog;
    PrettyDialog prettyDialog;
    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    String section,putfeedback_url;
    IPADDRESS ipaddress;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        mToolBar = findViewById(R.id.feedback_all_toolbar);
        text = findViewById(R.id.edt_FeedBack);
        submit = findViewById(R.id.btn_feedback);
        ratingBar=findViewById(R.id.feedback_stars_rating);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.FeedBack);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }

        againstID = getIntent().getExtras().get("Key").toString();
        section = getIntent().getExtras().get("section").toString();
        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        putfeedback_url = "http://" + ip + "/VeterinarySystem/feedbackall.php";
        // putfeedback_url="http://www.veterinarysystem.ga/feedbackall.php";
        progressDialog = new ProgressDialog(this);
        prettyDialog = new PrettyDialog(this);
        userSessionManager = new UserSessionManager(this);
        sessionDetails = userSessionManager.getSessionDetails();
        id = String.valueOf(sessionDetails.getId());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiveFeedBack();
            }
        });
    }

    private void GiveFeedBack() {

      String rate= String.valueOf(ratingBar.getRating());
        String feedback = text.getText().toString();
        if (TextUtils.isEmpty(feedback)) {
            text.setError(getResources().getString(R.string.givefeedbackhere));
        } else {
            progressDialog.setTitle(getResources().getString(R.string.SubmittingFeedback));
            progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            StringRequest feedbackRequest = new StringRequest(Request.Method.POST, putfeedback_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            prettyDialog
                                    .setTitle(getResources().getString(R.string.status))
                                    .setMessage(getResources().getString(R.string.ssf))
                                    .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.oK), R.color.design_default_color_primary_dark, R.color.loginBackgroundcolor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    if (section.equals("medicinesapprovedappointments")) {
                                        Intent intent = new Intent(getApplicationContext(), ApprovedMedicineActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (section.equals("doctorsapprovedappointments")) {
                                        Intent dintent = new Intent(getApplicationContext(), DoctorAppointmentsHistoryActivity.class);
                                        startActivity(dintent);
                                        finish();
                                    } else if (section.equals("userspprovedappointments")) {
                                        Intent uintent = new Intent(getApplicationContext(), ApprovedAppointmentsActivity.class);
                                        startActivity(uintent);
                                        finish();
                                    }
                                }
                            }).show();
                        }
                    } catch (Exception e) {
                      //  Toast.makeText(FeedBackActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    //Toast.makeText(FeedBackActivity.this, "Volley: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> feedMap = new HashMap<>();
                    feedMap.put("userid", id);
                    feedMap.put("feedback", feedback);
                    feedMap.put("againtid", againstID);
                    feedMap.put("rate",rate);
                    return feedMap;
                }
            };
            Volley.newRequestQueue(this).add(feedbackRequest);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
