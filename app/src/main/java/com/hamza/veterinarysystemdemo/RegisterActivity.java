package com.hamza.veterinarysystemdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class RegisterActivity extends AppCompatActivity {
    private TextView haveAccount, signUp;
    private EditText Email, Password, PhoneNo, Address, UserName;
    private String saveCurrentDate, saveCurrentTime, ImageLink, profileRandomKey;
    private CircleImageView profilePic;
    private static int GALLERY_PICK = 1;
    private StorageReference userProfileImage;
    IPADDRESS ipaddress;
    private String registerURL;
    private KProgressHUD kProgressHUD;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        registerURL = "http://www.veterinarysystem.ga/reg.php";

        haveAccount = (TextView) findViewById(R.id.btnHaveAccount);
        signUp = findViewById(R.id.btnSignUpCreate);
        Email = findViewById(R.id.edtsignupemail);
        Password = findViewById(R.id.edtsignuppassword);
        PhoneNo = findViewById(R.id.edtsignupphone);
        UserName = findViewById(R.id.edtsignupusername);
        Address = findViewById(R.id.edtsignAddress);
        profilePic = findViewById(R.id.img_profile_register);
        userProfileImage = FirebaseStorage.getInstance().getReference().child("Profile Images");
        mAuth = FirebaseAuth.getInstance();
        // kProgressHUD=new KProgressHUD(this);
        progressDialog = new ProgressDialog(this);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserAccount();
            }


        });


        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLogInActivity();
            }

            private void sendUserToLogInActivity() {

                Intent logInIntent = new Intent(getApplicationContext(), LoginActivity.class);
                logInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logInIntent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {

            mAuth.signInWithEmailAndPassword("hamzaamir749@gmail.com", "123456").addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        profileRandomKey = mAuth.getCurrentUser().getUid();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Calendar calFordDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                saveCurrentDate = currentDate.format(calFordDate.getTime());

                Calendar calFordTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                saveCurrentTime = currentTime.format(calFordTime.getTime());

                String key = profileRandomKey + saveCurrentTime + saveCurrentDate;

                final StorageReference filePath = userProfileImage.child(key + ".jpg");
                filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();

                            ImageLink = downUri.toString();
                            Picasso.get().load(ImageLink).into(profilePic);
                        }
                    }
                });

            }


        }
    }

    private void registerUserAccount() {
        final String email, password, username, address, phoneno;
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        username = UserName.getText().toString();
        address = Address.getText().toString();
        phoneno = PhoneNo.getText().toString().trim();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(username) && TextUtils.isEmpty(address) && TextUtils.isEmpty(phoneno)) {
            Email.setError("Please Enter Email");
            Password.setError("Please Enter Password");
            UserName.setError("Please Enter Name");
            Address.setError("Please Enter Address");
            PhoneNo.setError("Please Enter Mobile Number");

        } else if (TextUtils.isEmpty(email)) {
            Email.setError("Please Enter Email");
        } else if (TextUtils.isEmpty(password)) {
            Password.setError("Please Enter Password");
        } else if (TextUtils.isEmpty(username)) {
            UserName.setError("Please Enter Name");
        } else if (TextUtils.isEmpty(address)) {
            Address.setError("Please Enter Address");
        } else if (TextUtils.isEmpty(phoneno)) {
            PhoneNo.setError("Please Enter Mobile Number");
        } else {

            progressDialog.setTitle(R.string.PDRegisterTital);
            progressDialog.setMessage(getResources().getString(R.string.PDRegisterMessage));
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();


           /* KProgressHUD.create(RegisterActivity.this)
                    .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Registering Account")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f).setBackgroundColor(getResources().getColor(R.color.loginBackgroundcolor))
                    .show(); */


            StringRequest registerRequest = new StringRequest(Request.Method.POST, registerURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // kProgressHUD.dismiss();
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        String ResponseMessage = jsonObject.getString("success");

                        if (ResponseMessage.equals("2")) {

                            Email.setError("Please Enter Valid Email");
                        } else if (ResponseMessage.equals("0")) {

                            Toast.makeText(getApplicationContext(), "Registration Fail", Toast.LENGTH_SHORT).show();
                        } else if (ResponseMessage.equals("1")) {
                            new PrettyDialog(RegisterActivity.this)
                                    .setTitle("Status")
                                    .setMessage("Successfully Register Your Account")
                                    .setIcon(R.drawable.appplus).addButton("Done", R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    SendUserToLoginActivity();
                                }
                            }).show();

                        } else if (ResponseMessage.equals("3")) {
                            new PrettyDialog(RegisterActivity.this)
                                    .setTitle("Status")
                                    .setMessage("Error While Registering Your Account Please Try Again")
                                    .setIcon(R.drawable.appplus).addButton("Done", R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    SendUserToLoginActivity();
                                }
                            }).show();

                        }


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // kProgressHUD.dismiss();
                    progressDialog.show();
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("name", username);
                    userMap.put("phone", phoneno);
                    userMap.put("address", address.toString());
                    userMap.put("profilepic", ImageLink);
                    userMap.put("usercategory", "0");
                    userMap.put("email", email);
                    userMap.put("password", password);
                    userMap.put("time", saveCurrentTime);
                    userMap.put("date", saveCurrentDate);

                    return userMap;
                }
            };
            Volley.newRequestQueue(this).add(registerRequest);

        }
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}