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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class StoreEditProductActivity extends AppCompatActivity {
    String productID,image,name,price,updateProduct_URL,deleteProduct_URL,storeid,
            currentUserid,saveCurrentDate,saveCurrentTime,postRandomName,downloadUrl;
    private ImageView itemImage;
    private EditText itemName,itemPrice;

    Button btn_delete,btn_update;
    Toolbar mToolbar;


    private static int GALLERY_PICK = 1;
    private Uri imageUri=null;
    private ProgressDialog progressDialog;
    PrettyDialog prettyDialog;
    public IPADDRESS ipaddress;
    StorageReference filePath;
    private UserSessionManager userSessionManager;
    private SessionDetails sessionDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_edit_product);
        mToolbar = findViewById(R.id.sep_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.EditProduct);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        itemImage=findViewById(R.id.sep_image);
        itemName=findViewById(R.id.sep_name);
        itemPrice=findViewById(R.id.sep_price);
        btn_update=findViewById(R.id.sep_update);
        btn_delete=findViewById(R.id.sep_delete);
        //getProductFromPreviousActivity
        productID=getIntent().getExtras().get("productid").toString();
        image=getIntent().getExtras().get("productimage").toString();
        downloadUrl=image;
        price=getIntent().getExtras().get("productprice").toString();
        name=getIntent().getExtras().get("productname").toString();
        //SetProductDetailsHere
        Picasso.get().load(image).into(itemImage);
        itemName.setText(name);
        itemPrice.setText(price);
        //ForUpdationDetails
        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
       updateProduct_URL="http://"+ip+"/VeterinarySystem/updatestoreitem.php";
       deleteProduct_URL="http://"+ip+"/VeterinarySystem/deletestoreitem.php";
        //updateProduct_URL="http://www.veterinarysystem.ga/updatestoreitem.php";
        //deleteProduct_URL="http://www.veterinarysystem.ga/deletestoreitem.php";

        progressDialog=new ProgressDialog(this);
        prettyDialog=new PrettyDialog(this);

        userSessionManager=new UserSessionManager(this);
        sessionDetails =userSessionManager.getSessionDetails();
        storeid=String.valueOf(sessionDetails.getId());

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());
        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:aa");
        saveCurrentTime = currentTime.format(calFordTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        filePath = FirebaseStorage.getInstance().getReference().child("Add Item Images").child(postRandomName + ".jpg");

        //SetButtonOnClickEvents
       btn_update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               updateProduct();
           }
       });
       btn_delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               deleteProduct();
           }
       });
       //ForGettingImage
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);
            }
        });

    }



    private void updateProduct() {
        String nameupdate=itemName.getText().toString();
        String priceupdate=itemPrice.getText().toString();
        if (TextUtils.isEmpty(nameupdate) && TextUtils.isEmpty(priceupdate))
        {
            itemName.setError(getResources().getString(R.string.pleaseEnterName));
            itemPrice.setError(getResources().getString(R.string.PleaseEnterPrice));
        }
        else if (TextUtils.isEmpty(nameupdate))
        {
            itemName.setError(getResources().getString(R.string.pleaseEnterName));
        } else if (TextUtils.isEmpty(priceupdate))
        {
            itemPrice.setError(getResources().getString(R.string.PleaseEnterPrice));
        }
        else
        {
            progressDialog.setTitle(getResources().getString(R.string.Updatingyourproduct));
            progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            if (imageUri!=null)
            {
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
                            //Toast.makeText(StoreEditProductActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            StringRequest updateProductRequest=new StringRequest(Request.Method.POST, updateProduct_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject object=new JSONObject(response);
                        boolean status=object.getBoolean("status");
                        if (status)
                        {
                          prettyDialog.setTitle(getResources().getString(R.string.status))
                          .setMessage(getResources().getString(R.string.suyp))
                                  .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.oK), R.color.design_default_color_primary_dark, R.color.loginBackgroundcolor, new PrettyDialogCallback() {
                              @Override
                              public void onClick() {
                                  prettyDialog.dismiss();
                                  Intent intent=new Intent(getApplicationContext(),StoreMyProductsActivity.class);
                                  startActivity(intent);
                                  finish();
                              }
                          }).show();

                        }
                        else
                        {
                            Toast.makeText(StoreEditProductActivity.this, "Not Update", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch (Exception e)
                    {
                       // Toast.makeText(StoreEditProductActivity.this, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                   // Toast.makeText(StoreEditProductActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> updateMap=new HashMap<>();
                    updateMap.put("pImage",downloadUrl);
                    updateMap.put("pName",nameupdate);
                    updateMap.put("pPrice",priceupdate);
                    updateMap.put("pId",productID);
                    return updateMap;
                }
            };
            Volley.newRequestQueue(this).add(updateProductRequest);
        }

    }
    private void deleteProduct() {

        progressDialog.setTitle(getResources().getString(R.string.Addingyourproduct));
        progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest deleteProductRequest=new StringRequest(Request.Method.POST, deleteProduct_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object=new JSONObject(response);
                    boolean status=object.getBoolean("status");
                    if (status)
                    {
                        prettyDialog.setTitle(getResources().getString(R.string.status))
                                .setMessage(getResources().getString(R.string.sdyp))
                                .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.status), R.color.design_default_color_primary_dark, R.color.loginBackgroundcolor, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                                Intent intent=new Intent(getApplicationContext(),StoreMyProductsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).show();

                    }
                    else
                    {
                       // Toast.makeText(StoreEditProductActivity.this, "Not Update", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e)
                {
                   // Toast.makeText(StoreEditProductActivity.this, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
               // Toast.makeText(StoreEditProductActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> deleteMap=new HashMap<>();
                deleteMap.put("productID",productID);
                deleteMap.put("sID",storeid);
                return deleteMap;
            }
        };
        Volley.newRequestQueue(this).add(deleteProductRequest);

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
            Intent intent=new Intent(getApplicationContext(),StoreMyProductsActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent(getApplicationContext(), StoreMyProductsActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backIntent);
        finish();
    }

}
