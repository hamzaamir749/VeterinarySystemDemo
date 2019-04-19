package com.hamza.veterinarysystemdemo;

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
    private FirebaseAuth mAuth;
    StorageReference filePath;
    private UserSessionManager userSessionManager;
    private StoreSessionDetails storeSessionDetails;
    private String downloadUrl,postRandomName,saveCurrentTime,saveCurrentDate,currentUserid,additems_URL,storeid,storename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add_items);


        mToolbar = findViewById(R.id.store_add_item_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Items");
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


        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        currentUserid=mAuth.getCurrentUser().getUid();
        userSessionManager=new UserSessionManager(this);
        storeSessionDetails=userSessionManager.getStoreDataDetails();
        storeid=String.valueOf(storeSessionDetails.getId());
        storename=storeSessionDetails.getName();

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());
        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime +currentUserid;

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
                    itemName.setError("Please Enter Name");
                    itemPrice.setError("Please Enter Price");
                }
                else if (TextUtils.isEmpty(name))
                {
                    itemName.setError("Please Enter Name");
                } else if (TextUtils.isEmpty(price))
                {
                    itemPrice.setError("Please Enter Price");
                }
                else
                {
                    addItemTodataBase(name,price);
                }

            }
        });

    }

    private void addItemTodataBase(final String name, final String price) {
        StringRequest additemRequest=new StringRequest(Request.Method.POST, additems_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean ResponseMessage=jsonObject.getBoolean("status");

                    if (ResponseMessage)
                    {
                        new PrettyDialog(StoreAddItemsActivity.this)
                                .setTitle("Status")
                                .setMessage("Successfully Added Item")
                                .setIcon(R.drawable.appplus).addButton("OK", R.color.design_default_color_primary_dark, R.color.loginBackgroundcolor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                Intent intent=new Intent(getApplicationContext(),StoreAddItemsActivity.class);
                                startActivity(intent);
                                finish();
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
        Volley.newRequestQueue(this).add(additemRequest);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            itemImage.setImageURI(imageUri);


        }
        if (resultCode == RESULT_OK) {
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
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(StoreAddItemsActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
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