package com.hamza.veterinarysystemdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hamza.veterinarysystemdemo.models.userMedicineListModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class CheckOutActivity extends AppCompatActivity {

    EditText address;
    LinearLayout placeorderbtn;
    static List<userMedicineListModel> productlist;
    UserSessionManager userSessionManager;
    TextView finalprice;
    String orderDataList, placeOrder_URL, saveCurrentDate, saveCurrentTime;
    ClientSessionDetails clientSessionDetails;
    Toolbar mToolBar;
    IPADDRESS ipaddress;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
      placeOrder_URL="http://www.veterinarysystem.ga/checkout.php";

       // placeOrder_URL = "http://"+ip+"/VeterinarySystem/checkout.php";

        progressDialog=new ProgressDialog(this);


        address = findViewById(R.id.chkoutOrderAddress);
        placeorderbtn = findViewById(R.id.chkoutbtnPlaceYourOrder);
        finalprice = findViewById(R.id.chkoutFinalTotalPrice);

        mToolBar=findViewById(R.id.toolbar_for_checkout_activity);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Check Out");
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


        userSessionManager = new UserSessionManager(this);
        clientSessionDetails = userSessionManager.getClientDataDetails();

        finalprice.setText("RS. " + String.valueOf(UserSessionManager.cart.getTotalPrice()));

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordTime.getTime());


        Gson gson = new Gson();

        if (UserSessionManager.cart != null) {

            placeorderbtn.setVisibility(View.VISIBLE);
        } else {
            placeorderbtn.setVisibility(View.INVISIBLE);
        }


        orderDataList = gson.toJson( UserSessionManager.cart.productList);
        Log.d("cJson",orderDataList);
        placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckOutActivity.this, "Pressed", Toast.LENGTH_SHORT).show();
                placeorderFunction();
            }
        });


    }

    private void placeorderFunction() {
        final String getaddress = address.getText().toString();
        if (getaddress.isEmpty()) {
            address.setError("Please Enter Address");
        } else {

            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            StringRequest placeorderRequest = new StringRequest(Request.Method.POST, placeOrder_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                      //  Toast.makeText(CheckOutActivity.this, response, Toast.LENGTH_SHORT).show();
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {

                          UserSessionManager.cart.productList.clear();
                            new PrettyDialog(CheckOutActivity.this)
                                    .setTitle("Status")
                                    .setMessage("Successfully Place Your Order")
                                    .setIcon(R.drawable.appplus).addButton("OK", R.color.design_default_color_primary_dark, R.color.loginBackgroundcolor, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    SendUserToUserMainActivity();
                                }
                            }).show();

                        }
                    } catch (Exception e) {

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(CheckOutActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> placeOrderMap = new HashMap<>();
                    placeOrderMap.put("cartdata", orderDataList);
                    placeOrderMap.put("userid", String.valueOf(clientSessionDetails.getId()));
                    placeOrderMap.put("username", clientSessionDetails.getName());
                    placeOrderMap.put("useraddress", getaddress);
                    placeOrderMap.put("userphone", clientSessionDetails.getPhone());
                    placeOrderMap.put("oderTimeDate", saveCurrentTime+" "+saveCurrentDate);
                    return placeOrderMap;
                }
            };
            Volley.newRequestQueue(this).add(placeorderRequest);

        }

    }

    private void SendUserToUserMainActivity() {

        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
