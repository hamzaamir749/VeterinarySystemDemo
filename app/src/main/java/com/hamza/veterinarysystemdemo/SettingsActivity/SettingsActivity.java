package com.hamza.veterinarysystemdemo.SettingsActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
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

public class SettingsActivity extends AppCompatActivity {

    private EditText name, phone, email, address;
    private Button updateProfile;
    String getProfile_URL = "", updateprofile_url = "", Name, Email, Addess, PhoneNo, ProfileLink, saveCurrentDate, saveCurrentTime, ImageLink = "", profileRandomKey, newImageLink;
    private CircleImageView profileImage;
    int userID;
    private static int GALLERY_PICK = 1;
    private StorageReference userProfileImage;
    private ProgressDialog progressDialog;
    private IPADDRESS ipaddress;
    PrettyDialog prettyDialog;
    private Toolbar mToolbar;
    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    String emailTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = findViewById(R.id.settings_toolbar);
        name = findViewById(R.id.edt_update_name);
        phone = findViewById(R.id.edt_update_phone);
        email = findViewById(R.id.edt_update_email);
        address = findViewById(R.id.edt_update_address);
        updateProfile = findViewById(R.id.btn_update_profile);
        profileImage = findViewById(R.id.Settings_profile_image);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.profileSettings);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        //getProfile_URL = "http://www.veterinarysystem.ga/getprofile.php";
        //updateprofile_url = "http://www.veterinarysystem.ga/updateprofile.php";
        getProfile_URL="http://" + ip + "/VeterinarySystem/getprofile.php";
        updateprofile_url="http://" + ip + "/VeterinarySystem/updateprofile.php";

        progressDialog = new ProgressDialog(this);
        userSessionManager=new UserSessionManager(this);
        sessionDetails=userSessionManager.getSessionDetails();
        userProfileImage = FirebaseStorage.getInstance().getReference().child("Profile Images");



        emailTest=sessionDetails.getEmailAddress();

        name.setText(sessionDetails.getName());
        phone.setText(sessionDetails.getPhone());
        email.setText(emailTest);
        address.setText(sessionDetails.getAddress());
        Picasso.get().load(sessionDetails.getProfilepicture()).into(profileImage);
        userID=sessionDetails.getId();
        ImageLink=sessionDetails.getProfilepicture();


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });


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
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:aa");
                saveCurrentTime = currentTime.format(calFordTime.getTime());

                String key =  saveCurrentTime + saveCurrentDate;

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
                            Picasso.get().load(ImageLink).into(profileImage);
                        }
                    }
                });

            }


        }
    }
    private void updateProfile() {
        Name = name.getText().toString();
        Addess = address.getText().toString();
        PhoneNo = phone.getText().toString();
        Email = email.getText().toString();

        if (TextUtils.isEmpty(Name) && TextUtils.isEmpty(Addess) && TextUtils.isEmpty(PhoneNo) && TextUtils.isEmpty(Email)) {
            name.setError(getResources().getString(R.string.pleaseEnterName));
            address.setError(getResources().getString(R.string.pleaseEnterAddress));
            phone.setError(getResources().getString(R.string.pleaseEnterPhoneNo));
            email.setError(getResources().getString(R.string.pleaseEnterEmail));
        } else if (TextUtils.isEmpty(Name)) {
            name.setError(getResources().getString(R.string.pleaseEnterName));

        } else if (TextUtils.isEmpty(Addess)) {
            address.setError(getResources().getString(R.string.pleaseEnterAddress));

        } else if (TextUtils.isEmpty(PhoneNo)) {
            phone.setError(getResources().getString(R.string.pleaseEnterPhoneNo));

        } else if (TextUtils.isEmpty(Email)) {
            email.setError(getResources().getString(R.string.pleaseEnterEmail));
        } else {

            if (ImageLink.equals("")) {
                newImageLink = ProfileLink;
            } else {
                newImageLink = ImageLink;
            }

            progressDialog.setTitle(getResources().getString(R.string.updatingAccount));
            progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            StringRequest updateProfileRequest = new StringRequest(Request.Method.POST, updateprofile_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            sessionDetails.setName(Name);
                            sessionDetails.setEmailAddress(Email);
                            sessionDetails.setAddress(Addess);
                            sessionDetails.setPhone(PhoneNo);
                            sessionDetails.setProfilepicture(newImageLink);
                            sessionDetails.setId(userID);
                            userSessionManager.setSessionDetails(sessionDetails);
                            new PrettyDialog(SettingsActivity.this)
                                    .setTitle(getResources().getString(R.string.status))
                                    .setMessage(getResources().getString(R.string.successfullyUpdatedYourAccount))
                                    .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.Done), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).show();
                        }

                    } catch (Exception e) {
                      //  Toast.makeText(SettingsActivity.this, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                   // Toast.makeText(SettingsActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> updateprofileMap = new HashMap<>();
                    updateprofileMap.put("name", Name);
                    updateprofileMap.put("phone", PhoneNo);
                    updateprofileMap.put("address", Addess);
                    updateprofileMap.put("profilepic", newImageLink);
                    updateprofileMap.put("email", Email);
                    updateprofileMap.put("userid", String.valueOf(userID));
                    return updateprofileMap;
                }
            };
            Volley.newRequestQueue(this).add(updateProfileRequest);
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
