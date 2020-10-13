package com.hamza.veterinarysystemdemo.StoreSection;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProfileActivity extends AppCompatActivity {
    String image,name,id,address,phone;
    CircleImageView p_image;
    TextView p_name,p_phone,p_address;
    Button viewOrders;
    Toolbar mToolbar;
    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        p_image=findViewById(R.id.cp_profile_image);
        p_name=findViewById(R.id.cp_name);
        p_phone=findViewById(R.id.cp_phone);
        p_address=findViewById(R.id.cp_address);
        viewOrders=findViewById(R.id.cp_view_order);
        mToolbar= findViewById(R.id.cp_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.customerProfile));
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
        image=getIntent().getExtras().getString("clientimage").toString();
        id=getIntent().getExtras().getString("clientKey").toString();
        name=getIntent().getExtras().getString("clientname").toString();
        phone=getIntent().getExtras().getString("clientphone").toString();
        address=getIntent().getExtras().getString("clientaddress").toString();
        userSessionManager=new UserSessionManager(this);
        sessionDetails=userSessionManager.getSessionDetails();

        Picasso.get().load(image).into(p_image);
        p_name.setText(name);
        p_phone.setText(phone);
        p_address.setText(address);

        viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetailsIntent=new Intent(getApplicationContext(),StoreOrderDetailsActivity.class);
                orderDetailsIntent.putExtra("clientID",id);
                orderDetailsIntent.putExtra("storeID",String.valueOf(sessionDetails.getId()));
                startActivity(orderDetailsIntent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
