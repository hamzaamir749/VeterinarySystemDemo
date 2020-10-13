package com.hamza.veterinarysystemdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hamza.veterinarysystemdemo.UserSection.PhoneVerificationsActivity.PhoneVerificationActivity;
import com.hamza.veterinarysystemdemo.UserSection.PhoneVerificationsActivity.RegisterActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class ForgetPasswordActivity extends AppCompatActivity {
    private Button sendverifcationcodeButton, verifycodeButton,reset;
    private EditText inputPhoneNumber,inputNewPassword;
    Pinview inputVerificationCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog progressDialog;
    String verificationcode,check_URL,update_URL;
    IPADDRESS ipaddress;
    Toolbar mToolBar;
    String phonenumber,newpassword;
    TextView p1, p2, p3, p4, p5, p6, p7, p8, p9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mToolBar = findViewById(R.id.ForgetPassword_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.ForgetPassword);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        p1 = findViewById(R.id.fv_msg1);
        p2 = findViewById(R.id.fv_msg2);
        p3 = findViewById(R.id.fv_msg3);
        p4 = findViewById(R.id.fv_msg4);
        p5 = findViewById(R.id.fv_msg5);
        p6 = findViewById(R.id.fv_msg6);
        p7 = findViewById(R.id.fv_msg7);
        p8 = findViewById(R.id.fv_msg8);
        p9 = findViewById(R.id.fv_msg9);

        mAuth = FirebaseAuth.getInstance();
        sendverifcationcodeButton = findViewById(R.id.ForgetPassword_send_verification_code);
        verifycodeButton = findViewById(R.id.ForgetPassword_verify_verification_code);
        inputPhoneNumber = findViewById(R.id.ForgetPassword_phone_number_input);
        inputVerificationCode = findViewById(R.id.ForgetPassword_verification_code_input);
        reset = findViewById(R.id.ForgetPassword_reset_code_btn);
        inputNewPassword = findViewById(R.id.ForgetPassword_new_password_input);

        progressDialog = new ProgressDialog(this);
        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        check_URL = "http://" + ip + "/VeterinarySystem/checknumberforpassword.php";
        update_URL = "http://" + ip + "/VeterinarySystem/updatepassword.php";

        sendverifcationcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                phonenumber = inputPhoneNumber.getText().toString();

                if (TextUtils.isEmpty(phonenumber)) {
                    inputPhoneNumber.setError(getResources().getString(R.string.pleaseEnterPhoneNo));
                } else {


                   CheckNumberFromDBAndSendCode();
                }
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
               inputPhoneNumber.setError(getResources().getString(R.string.invaildnumber));
                progressDialog.dismiss();

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;

                progressDialog.dismiss();
                Toast.makeText(ForgetPasswordActivity.this, "Code Send Successfully...", Toast.LENGTH_SHORT).show();

                inputVerificationCode.setVisibility(View.VISIBLE);
                verifycodeButton.setVisibility(View.VISIBLE);
                p4.setVisibility(View.VISIBLE);
                p5.setVisibility(View.VISIBLE);
                p6.setVisibility(View.VISIBLE);

                sendverifcationcodeButton.setVisibility(View.GONE);
                inputPhoneNumber.setVisibility(View.GONE);
                p1.setVisibility(View.GONE);
                p2.setVisibility(View.GONE);
                p3.setVisibility(View.GONE);
            }
        };

        verifycodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendverifcationcodeButton.setVisibility(View.INVISIBLE);
                inputPhoneNumber.setVisibility(View.INVISIBLE);

                if (TextUtils.isEmpty(verificationcode)) {
                    Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.entercode), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setTitle(getResources().getString(R.string.Codeverification));
                    progressDialog.setMessage(getResources().getString(R.string.pwwwavyc));
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationcode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                newpassword = inputNewPassword.getText().toString();

                if (TextUtils.isEmpty(phonenumber)) {
                    inputNewPassword.setError(getResources().getString(R.string.pleaseEnterPassword));
                } else {


                  SavePasswordToDataBase();
                }
            }
        });
    }

    private void SavePasswordToDataBase() {
        progressDialog.setTitle(getResources().getString(R.string.updatingPassword));
        progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest updatepassword=new StringRequest(Request.Method.POST, update_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object=new JSONObject(response);
                    Boolean status=object.getBoolean("status");
                    if (status)
                    {
                        new PrettyDialog(ForgetPasswordActivity.this)
                                .setTitle(getResources().getString(R.string.status))
                                .setMessage(getResources().getString(R.string.spu))
                                .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.Done), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                SendUserToLoginActivity();
                            }
                        }).show();
                    }
                    else
                    {
                       // Toast.makeText(ForgetPasswordActivity.this, "Not Updated", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    //Toast.makeText(ForgetPasswordActivity.this, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
               // Toast.makeText(ForgetPasswordActivity.this, "Volley: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("number",phonenumber);
                map.put("password",newpassword);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(updatepassword);
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void CheckNumberFromDBAndSendCode() {

        progressDialog.setTitle(getResources().getString(R.string.Phoneverification));
        progressDialog.setMessage(getResources().getString(R.string.pwwaaun));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest checknumber=new StringRequest(Request.Method.POST, check_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object=new JSONObject(response);
                    Boolean status=object.getBoolean("status");
                    if (status)
                    {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phonenumber,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                ForgetPasswordActivity.this,               // Activity (for callback binding)
                                mCallbacks);

                    }
                    else
                    {
                        inputPhoneNumber.setError(getResources().getString(R.string.invaildnumber));
                           // OnVerificationStateChangedCallbacks
                    }
                }
                catch (Exception e)
                {
                  //  Toast.makeText(ForgetPasswordActivity.this, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
              //  Toast.makeText(ForgetPasswordActivity.this, "Volley: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("number",phonenumber);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(checknumber);

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialog.setTitle(getResources().getString(R.string.Codeverification));
        progressDialog.setMessage(getResources().getString(R.string.pwwwavyc));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressDialog.dismiss();

                    inputNewPassword.setVisibility(View.VISIBLE);
                    reset.setVisibility(View.VISIBLE);
                    p7.setVisibility(View.VISIBLE);
                    p8.setVisibility(View.VISIBLE);
                    p9.setVisibility(View.VISIBLE);

                    inputVerificationCode.setVisibility(View.GONE);
                    verifycodeButton.setVisibility(View.GONE);
                    p4.setVisibility(View.GONE);
                    p5.setVisibility(View.GONE);
                    p6.setVisibility(View.GONE);

                } else {

                    Toast.makeText(ForgetPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
