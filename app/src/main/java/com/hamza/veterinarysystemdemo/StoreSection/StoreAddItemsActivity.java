package com.hamza.veterinarysystemdemo.StoreSection;

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
import android.widget.ImageView;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class StoreAddItemsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageView itemImage;
    private EditText itemName,itemPrice;
    private Button itemAdd;
    private static int GALLERY_PICK = 1;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    public IPADDRESS ipaddress;
    StorageReference filePath;
    private UserSessionManager userSessionManager;
    private SessionDetails sessionDetails;
    private String downloadUrl,postRandomName,saveCurrentTime,saveCurrentDate,currentUserid,additems_URL,storeid,storename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add_items);


        mToolbar = findViewById(R.id.store_add_item_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.AddItems));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }

        itemAdd = findViewById(R.id.store_add_item_add);
        itemName = findViewById(R.id.store_add_item_name);
        itemImage = findViewById(R.id.store_add_item_image);
        itemPrice=findViewById(R.id.store_add_item_price);

        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        additems_URL="http://"+ip+"/VeterinarySystem/additems.php";
        //additems_URL="http://www.veterinarysystem.ga/additems.php";


        progressDialog=new ProgressDialog(this);
        userSessionManager=new UserSessionManager(this);
        sessionDetails =userSessionManager.getSessionDetails();
        storeid=String.valueOf(sessionDetails.getId());
        storename= sessionDetails.getName();

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());
        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:aa");
        saveCurrentTime = currentTime.format(calFordTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        filePath = FirebaseStorage.getInstance().getReference().child("Add Item Images").child(postRandomName + ".jpg");


        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);
            }
        });

        itemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=itemName.getText().toString();
                String price=itemPrice.getText().toString();
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(price))
                {
                    itemName.setError(getResources().getString(R.string.pleaseEnterName));
                    itemPrice.setError(getResources().getString(R.string.PleaseEnterPrice));
                }
                else if (TextUtils.isEmpty(name))
                {
                    itemName.setError(getResources().getString(R.string.pleaseEnterName));
                } else if (TextUtils.isEmpty(price))
                {
                    itemPrice.setError(getResources().getString(R.string.PleaseEnterPrice));
                }
                else
                {
                    addItemTodataBase(name,price);
                }

            }
        });

    }

    private void addItemTodataBase(final String name, final String price) {

        progressDialog.setTitle(getResources().getString(R.string.Addingyourproduct));
        progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        filePath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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

                    downloadUrl = downUri.toString();
                    //UploadingSQLDB

                    StringRequest additemRequest=new StringRequest(Request.Method.POST, additems_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                Boolean ResponseMessage=jsonObject.getBoolean("status");

                                if (ResponseMessage)
                                {
                                    new PrettyDialog(StoreAddItemsActivity.this)
                                            .setTitle(getResources().getString(R.string.status))
                                            .setMessage(getResources().getString(R.string.SuccessfullyAddedItem))
                                            .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.oK), R.color.design_default_color_primary_dark, R.color.loginBackgroundcolor, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            Intent intent=new Intent(getApplicationContext(),StoreAddItemsActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();

                                }
                                else
                                {
                                  //  Toast.makeText(StoreAddItemsActivity.this, "Not Added", Toast.LENGTH_SHORT).show();
                                }


                            }catch (Exception e)
                            {
                              //  Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                           // Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> additemMap=new HashMap<>();
                            additemMap.put("iname",storename);
                            additemMap.put("itime",saveCurrentTime);
                            additemMap.put("idate",saveCurrentDate);
                            additemMap.put("iimage",downloadUrl);
                            additemMap.put("storeid",storeid);
                            additemMap.put("itemname",name);
                            additemMap.put("iprice",price);
                            return additemMap;
                        }
                    };
                    Volley.newRequestQueue(StoreAddItemsActivity.this).add(additemRequest);

//EndUpload

                } else {
                    String message = task.getException().getMessage();
                   // Toast.makeText(StoreAddItemsActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            itemImage.setImageURI(imageUri);


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