package com.hamza.veterinarysystemdemo.CartPackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
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
    CartAdapterUrdu adapterUrdu;
    String lang;
    boolean isEnglish;

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
        getSupportActionBar().setTitle(R.string.cart);
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
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "en");
       // Toast.makeText(context, lang, Toast.LENGTH_SHORT).show();
        setCartItemList();
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToCheckOutActivity();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

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


            if (lang.equals("ur"))
            {
                adapterUrdu=new CartAdapterUrdu(productList,context);
                lvCartItems.setAdapter(adapterUrdu);
                adapterUrdu.notifyDataSetChanged();

            }
            else if(lang.equals("en"))
            {
                cartAdapter = new CartAdapter(productList, context);
                lvCartItems.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();
            }

            setTotalPrice();
        } else {
            lvCartItems.setVisibility(View.GONE);
            checkoutLayout.setVisibility(View.GONE);
            tvNoOrder.setVisibility(View.VISIBLE);
        }
    }

    public void setTotalPrice() {

        int price = (int) UserSessionManager.cart.getTotalPrice();
        tvTotalPrice.setText(String.valueOf(price));
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
