package com.hamza.veterinarysystemdemo.UserSection.PhoneVerificationsActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hamza.veterinarysystemdemo.R;

import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {
    private Button sendverifcationcodeButton, verifycodeButton;
    private EditText inputPhoneNumber;
    Pinview inputVerificationCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog progressDialog;
    String verificationcode;
    Toolbar mToolBar;
    TextView p1, p2, p3, p4, p5, p6;
    String phonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        mToolBar = findViewById(R.id.phone_verification_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.Phoneverification);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        p1 = findViewById(R.id.pv_msg1);
        p2 = findViewById(R.id.pv_msg2);
        p3 = findViewById(R.id.pv_msg3);
        p4 = findViewById(R.id.pv_msg4);
        p5 = findViewById(R.id.pv_msg5);
        p6 = findViewById(R.id.pv_msg6);

        mAuth = FirebaseAuth.getInstance();
        sendverifcationcodeButton = findViewById(R.id.btn_send_verification_code);
        verifycodeButton = findViewById(R.id.btn_verify_verification_code);
        inputPhoneNumber = findViewById(R.id.edt_phone_number_input);
        inputVerificationCode = findViewById(R.id.edt_verification_code_input);
        progressDialog = new ProgressDialog(this);

        inputVerificationCode.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {

                verificationcode = pinview.getValue();

            }
        });
        sendverifcationcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                phonenumber = inputPhoneNumber.getText().toString();

                if (TextUtils.isEmpty(phonenumber)) {
                    inputPhoneNumber.setError(getResources().getString(R.string.pleaseEnterPhoneNo));
                } else {

                    progressDialog.setTitle(getResources().getString(R.string.Phoneverification));
                    progressDialog.setMessage(getResources().getString(R.string.pwwaaun));
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phonenumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneVerificationActivity.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });
        verifycodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendverifcationcodeButton.setVisibility(View.INVISIBLE);
                inputPhoneNumber.setVisibility(View.INVISIBLE);

                if (TextUtils.isEmpty(verificationcode)) {
                    Toast.makeText(PhoneVerificationActivity.this, getResources().getString(R.string.entercode), Toast.LENGTH_SHORT).show();
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

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(PhoneVerificationActivity.this, getResources().getString(R.string.invaildnumber), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;

                progressDialog.dismiss();
                Toast.makeText(PhoneVerificationActivity.this, "Code Send Successfully...", Toast.LENGTH_SHORT).show();

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
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    intent.putExtra("pn", phonenumber);
                    startActivity(intent);
                    finish();
                } else {

                  //  Toast.makeText(PhoneVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

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
