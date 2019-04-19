package com.hamza.veterinarysystemdemo.CartPackage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.CheckOutActivity;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.UserSessionManager;
import com.hamza.veterinarysystemdemo.models.userMedicineListModel;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView lvCartItems;
    LinearLayoutManager linearLayoutManager;
    CartAdapter cartAdapter;
    Context context;
    UserSessionManager userSessionManager;

    static TextView tvNoOrder;
    static TextView tvTotalPrice;
    static LinearLayout checkoutLayout;
    static List<userMedicineListModel> productList;
    Button btnCheckout;
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        checkoutLayout = findViewById(R.id.checkoutLayout);
        tvNoOrder = findViewById(R.id.tvNoCartItem);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        mToolBar=findViewById(R.id.toolbar_for_cart_activity);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Cart");
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



        context = this;
        lvCartItems = findViewById(R.id.lvCartItems);
        linearLayoutManager = new LinearLayoutManager(this);
        lvCartItems.setHasFixedSize(true);
        lvCartItems.setLayoutManager(linearLayoutManager);
        userSessionManager = new UserSessionManager(this);
        setCartItemList();
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToCheckOutActivity();
            }
        });

    }

    private void SendUserToCheckOutActivity() {
        Intent checkoutIntent=new Intent(getApplicationContext(), CheckOutActivity.class);
        startActivity(checkoutIntent);
    }

    public void setCartItemList() {
        if (UserSessionManager.cart != null) {
            lvCartItems.setVisibility(View.VISIBLE);
            tvNoOrder.setVisibility(View.GONE);
            checkoutLayout.setVisibility(View.VISIBLE);
            productList = UserSessionManager.cart.productList;
            cartAdapter = new CartAdapter(productList, context);
            lvCartItems.setAdapter(cartAdapter);
            setTotalPrice();
        } else {
            lvCartItems.setVisibility(View.GONE);
            checkoutLayout.setVisibility(View.GONE);
            tvNoOrder.setVisibility(View.VISIBLE);
        }
    }

    public void setTotalPrice() {

        int price = (int) UserSessionManager.cart.getTotalPrice();
        tvTotalPrice.setText("Rs. " + String.valueOf(price));
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
