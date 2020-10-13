package com.hamza.veterinarysystemdemo.CartPackage;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.MainActivity;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
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
    SessionDetails sessionDetails;
    Toolbar mToolBar;
    IPADDRESS ipaddress;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        ipaddress = new IPADDRESS();
        String ip = ipaddress.getIpaddress();
        //placeOrder_URL="http://www.veterinarysystem.ga/checkout.php";

        placeOrder_URL = "http://" + ip + "/VeterinarySystem/checkout.php";

        progressDialog = new ProgressDialog(this);


        address = findViewById(R.id.chkoutOrderAddress);
        placeorderbtn = findViewById(R.id.chkoutbtnPlaceYourOrder);
        finalprice = findViewById(R.id.chkoutFinalTotalPrice);

        mToolBar = findViewById(R.id.toolbar_for_checkout_activity);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.checkout);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }


        userSessionManager = new UserSessionManager(this);
        sessionDetails = userSessionManager.getSessionDetails();

        finalprice.setText(getResources().getString(R.string.rs) + String.valueOf(UserSessionManager.cart.getTotalPrice()));

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:aa");
        saveCurrentTime = currentTime.format(calFordTime.getTime());


        Gson gson = new Gson();

        if (UserSessionManager.cart != null) {

            placeorderbtn.setVisibility(View.VISIBLE);
        } else {
            placeorderbtn.setVisibility(View.INVISIBLE);
        }


        orderDataList = gson.toJson(UserSessionManager.cart.productList);
        //Log.d("cJson", orderDataList);
        placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(CheckOutActivity.this, "Pressed", Toast.LENGTH_SHORT).show();
                placeorderFunction();
            }
        });


    }

    private void placeorderFunction() {
        final String getaddress = address.getText().toString();
        if (getaddress.isEmpty()) {
            address.setError(getResources().getString(R.string.pleaseEnterAddress));
        } else {

            progressDialog.setTitle(getResources().getString(R.string.loading));
            progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            StringRequest placeorderRequest = new StringRequest(Request.Method.POST, placeOrder_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                       // Toast.makeText(CheckOutActivity.this, response, Toast.LENGTH_SHORT).show();
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {

                            UserSessionManager.cart.productList.clear();
                            new PrettyDialog(CheckOutActivity.this)
                                    .setTitle(getResources().getString(R.string.status))
                                    .setMessage(getResources().getString(R.string.successfullyPlaceYourOrder))
                                    .setIcon(R.drawable.appplus).addButton(getResources().getString(R.string.oK), R.color.design_default_color_primary_dark, R.color.loginBackgroundcolor, new PrettyDialogCallback() {
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

                 //   Toast.makeText(CheckOutActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> placeOrderMap = new HashMap<>();
                    placeOrderMap.put("cartdata", orderDataList);
                    placeOrderMap.put("userid", String.valueOf(sessionDetails.getId()));
                    placeOrderMap.put("username", sessionDetails.getName());
                    placeOrderMap.put("useraddress", getaddress);
                    placeOrderMap.put("userphone", sessionDetails.getPhone());
                    placeOrderMap.put("orderTime", saveCurrentTime);
                    placeOrderMap.put("orderDate", saveCurrentDate);
                    placeOrderMap.put("userimage", sessionDetails.getProfilepicture());
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
