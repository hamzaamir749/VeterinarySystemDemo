package com.hamza.veterinarysystemdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.ExtractedText;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class AddProblemsActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private ImageView imgPost;
    private EditText desPost;
    private Button addPost;
    private static int GALLERY_PICK=1;
    private Uri imageUri;
    private String postRandomName,saveCurrentTime,saveCurrentDate,downloadUrl="",postDesctiption="",currentUserid,usernamePost,userProfilePost;
    private String addpost_URL;
    private ProgressDialog progressDialog;
    IPADDRESS ipaddress;
    private FirebaseAuth mAuth;

    private UserSessionManager userSessionManager;
    ClientSessionDetails clientSessionDetails;
    StorageReference PostsImagesReference,filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problems);

        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        addpost_URL="http://www.veterinarysystem.ga/addproblems.php";

        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        currentUserid=mAuth.getCurrentUser().getUid();
        userSessionManager=new UserSessionManager(this);
        clientSessionDetails=userSessionManager.getClientDataDetails();

        usernamePost=clientSessionDetails.getName();
        userProfilePost=clientSessionDetails.getProfilepicture();

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime +currentUserid;

        filePath = FirebaseStorage.getInstance().getReference().child("Post Images").child(postRandomName + ".jpg");

        mToolBar=findViewById(R.id.addproblemActivityToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Add Problem");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        else
        {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        imgPost=findViewById(R.id.addproblemImageView);
        desPost=findViewById(R.id.addproblemDescription);
        addPost=findViewById(R.id.addproblembtn);
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);

            }
        });
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePostDetailsToDataBase();

            }
        });



    }

    private void savePostDetailsToDataBase() {
        postDesctiption=desPost.getText().toString();
        StringRequest addpostRequest=new StringRequest(Request.Method.POST, addpost_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String ResponseMessage=jsonObject.getString("success");

                    if (ResponseMessage.equals("1"))
                    {
                        new PrettyDialog(AddProblemsActivity.this)
                                .setTitle("Status")
                                .setMessage("Successfully Added Problem")
                                .setIcon(R.drawable.appplus).addButton("OK", R.color.design_default_color_primary_dark, R.color.loginBackgroundcolor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                SendUserToUserProblemsActivity();
                            }
                        }).show();

                    }



                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> addpostMap=new HashMap<>();
                addpostMap.put("pname",usernamePost);
                addpostMap.put("ptime",saveCurrentTime);
                addpostMap.put("pdate",saveCurrentDate);
                addpostMap.put("pimage",downloadUrl);
                addpostMap.put("pprofileimage",userProfilePost);
                addpostMap.put("pdes",postDesctiption);
                return addpostMap;
            }
        };
        Volley.newRequestQueue(this).add(addpostRequest);

    }

    private void SendUserToUserProblemsActivity() {
        Intent userProblemsIntent=new Intent(getApplicationContext(),UserProblemsActivity.class);
        //userProblemsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(userProblemsIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_PICK && resultCode==RESULT_OK && data!=null)
        {
            imageUri=data.getData();
            imgPost.setImageURI(imageUri);


        }
        if (resultCode==RESULT_OK)
        {
            filePath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downUri = task.getResult();

                        downloadUrl = downUri.toString();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(AddProblemsActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        //StoringImageToFirebaseStorage();

    }
    private void StoringImageToFirebaseStorage() {


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
