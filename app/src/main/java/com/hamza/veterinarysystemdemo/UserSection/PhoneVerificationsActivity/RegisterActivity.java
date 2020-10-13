package com.hamza.veterinarysystemdemo.UserSection.PhoneVerificationsActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.LoginActivity;
import com.hamza.veterinarysystemdemo.R;
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
    private Button haveAccount, signUp;
    TextView PhoneNo;
    private EditText Email, Password, Address, UserName;
    private String saveCurrentDate, saveCurrentTime, ImageLink, profileRandomKey;
    private CircleImageView profilePic;
    private static int GALLERY_PICK = 1;
    private StorageReference userProfileImage;
    IPADDRESS ipaddress;
    private String registerURL;
    private ProgressDialog progressDialog;
    Toolbar mToolBar;
    String PN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        haveAccount = findViewById(R.id.btnHaveAccount);
        signUp = findViewById(R.id.btnSignUpCreate);
        Email = findViewById(R.id.edtsignupemail);
        Password = findViewById(R.id.edtsignuppassword);
        PhoneNo = findViewById(R.id.edtsignupphone);
        UserName = findViewById(R.id.edtsignupusername);
        Address = findViewById(R.id.edtsignAddress);
        profilePic = findViewById(R.id.img_profile_register);
        mToolBar = findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.RegisterAccount);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        PN = getIntent().getExtras().get("pn").toString();
        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        registerURL = "http://" + ip + "/VeterinarySystem/reg.php";

        userProfileImage = FirebaseStorage.getInstance().getReference().child("Profile Images");

        progressDialog = new ProgressDialog(this);
        PhoneNo.setText(PN);

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

    String getname(Uri fileUri) {
        Cursor returnCursor =
                getContentResolver().query(fileUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        return returnCursor.getString(nameIndex);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            profileRandomKey = getname(imageUri);
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
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:aa");
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
            Email.setError(getResources().getString(R.string.pleaseEnterEmail));
            Password.setError(getResources().getString(R.string.pleaseEnterPassword));
            UserName.setError(getResources().getString(R.string.pleaseEnterName));
            Address.setError(getResources().getString(R.string.pleaseEnterAddress));
            PhoneNo.setError(getResources().getString(R.string.pleaseEnterPhoneNo));

        } else if (TextUtils.isEmpty(email)) {
            Email.setError(getResources().getString(R.string.pleaseEnterEmail));
        } else if (TextUtils.isEmpty(password)) {
            Password.setError(getResources().getString(R.string.pleaseEnterPassword));
        } else if (TextUtils.isEmpty(username)) {
            UserName.setError(getResources().getString(R.string.pleaseEnterName));
        } else if (TextUtils.isEmpty(address)) {
            Address.setError(getResources().getString(R.string.pleaseEnterAddress));
        } else {

            progressDialog.setTitle(R.string.RegisterAccount);
            progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();


            StringRequest registerRequest = new StringRequest(Request.Method.POST, registerURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        String ResponseMessage = jsonObject.getString("success");

                        if (ResponseMessage.equals("2")) {

                            Email.setError(getResources().getString(R.string.emailcheck));
                        } else if (ResponseMessage.equals("0")) {

                            Toast.makeText(getApplicationContext(), "Registration Fail", Toast.LENGTH_SHORT).show();
                        } else if (ResponseMessage.equals("1")) {
                            new PrettyDialog(RegisterActivity.this)
                                    .setTitle(getResources().getString(R.string.status))
                                    .setMessage(getResources().getString(R.string.Srya))
                                    .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.Done), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    SendUserToLoginActivity();
                                }
                            }).show();

                        } else if (ResponseMessage.equals("3")) {
                            new PrettyDialog(RegisterActivity.this)
                                    .setTitle(getResources().getString(R.string.status))
                                    .setMessage(getResources().getString(R.string.erroraccount))
                                    .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.Done), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    SendUserToLoginActivity();
                                }
                            }).show();

                        }


                    } catch (Exception e) {
                        //Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    //Toast.makeText(getApplicationContext(), "Volley: " + error.getMessage(), Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("name", username);
                    userMap.put("phone", PN);
                    userMap.put("address", address);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}