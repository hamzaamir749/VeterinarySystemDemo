package com.hamza.veterinarysystemdemo.SettingsActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    String getProfile_URL = "", updateprofile_url = "", Name, Email, Addess, PhoneNo, ProfileLink, saveCurrentDate, saveCurrentTime, ImageLink = "", userprofileID, profileRandomKey, newImageLink;
    private CircleImageView profileImage;
    private static int GALLERY_PICK = 1;
    private StorageReference userProfileImage;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private IPADDRESS ipaddress;
    PrettyDialog prettyDialog;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile Settings");
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
        getProfile_URL = "http://www.veterinarysystem.ga/getprofile.php";
        updateprofile_url = "http://www.veterinarysystem.ga/updateprofile.php";

        progressDialog = new ProgressDialog(this);
        userProfileImage = FirebaseStorage.getInstance().getReference().child("Profile Images");
        mAuth = FirebaseAuth.getInstance();
        profileRandomKey = mAuth.getCurrentUser().getUid();


        name = findViewById(R.id.edt_update_name);
        phone = findViewById(R.id.edt_update_phone);
        email = findViewById(R.id.edt_update_email);
        address = findViewById(R.id.edt_update_address);
        updateProfile = findViewById(R.id.btn_update_profile);
        profileImage = findViewById(R.id.Settings_profile_image);
        userprofileID = getIntent().getExtras().get("profileid").toString();
        getProfile();


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
                            Picasso.get().load(ImageLink).into(profileImage);
                        }
                    }
                });

            }


        }
    }

    private void getProfile() {

        StringRequest getProfileRequest = new StringRequest(Request.Method.POST, getProfile_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String gname = jsonObject.getString("name");
                    String gemail = jsonObject.getString("email");
                    String gaddress = jsonObject.getString("address");
                    String gprofileimage = jsonObject.getString("profileimage");
                    String gphone = jsonObject.getString("phone");

                    name.setText(gname);
                    phone.setText(gphone);
                    address.setText(gaddress);
                    email.setText(gemail);
                    Picasso.get().load(gprofileimage).into(profileImage);

                    Email = gemail;
                    Name = gname;
                    Addess = gaddress;
                    ProfileLink = gprofileimage;
                    PhoneNo = gphone;


                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> getProfileMap = new HashMap<>();
                getProfileMap.put("uid", userprofileID);
                return getProfileMap;
            }
        };
        Volley.newRequestQueue(this).add(getProfileRequest);

    }

    private void updateProfile() {
        Name = name.getText().toString();
        Addess = address.getText().toString();
        PhoneNo = phone.getText().toString();
        Email = email.getText().toString();

        if (TextUtils.isEmpty(Name) && TextUtils.isEmpty(Addess) && TextUtils.isEmpty(PhoneNo) && TextUtils.isEmpty(Email)) {
            name.setError("Please Enter Name");
            address.setError("Please Enter Address");
            phone.setError("Please Enter Phone No");
            email.setError("Please Enter Email");
        } else if (TextUtils.isEmpty(Name)) {
            name.setError("Please Enter Name");

        } else if (TextUtils.isEmpty(Addess)) {
            address.setError("Please Enter Address");

        } else if (TextUtils.isEmpty(PhoneNo)) {
            phone.setError("Please Enter Phone No");

        } else if (TextUtils.isEmpty(Email)) {
            email.setError("Please Enter Email");
        } else {

            if (ImageLink.equals("")) {
                newImageLink = ProfileLink;
            } else {
                newImageLink = ImageLink;
            }

            progressDialog.setTitle("Updating Account");
            progressDialog.setMessage("Please Wait....");
            progressDialog.show();

            StringRequest updateProfileRequest = new StringRequest(Request.Method.POST, updateprofile_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Boolean status = jsonObject.getBoolean("status");
                        if (status) {

                            new PrettyDialog(SettingsActivity.this)
                                    .setTitle("Status")
                                    .setMessage("Successfully Updated Your Account")
                                    .setIcon(R.drawable.appplus).addButton("Done", R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                                    intent.putExtra("profileid", userprofileID);
                                    startActivity(intent);
                                    finish();
                                }
                            }).show();
                        }

                    } catch (Exception e) {

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
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
                    updateprofileMap.put("userid", userprofileID);
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
