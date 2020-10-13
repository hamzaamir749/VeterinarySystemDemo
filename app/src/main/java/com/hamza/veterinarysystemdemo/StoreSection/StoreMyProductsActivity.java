package com.hamza.veterinarysystemdemo.StoreSection;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.hamza.veterinarysystemdemo.StoreSection.Adapter.StoreMyProductsAdapter;
import com.hamza.veterinarysystemdemo.StoreSection.Model.StoreMyProductsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreMyProductsActivity extends AppCompatActivity {
    RecyclerView myProductsRecycler;
    RecyclerView.LayoutManager gridLayoutManager;
    SearchView searchView;
    Toolbar mToolbar;
    Context context;
    List<StoreMyProductsModel> myPoductsList;
    StoreMyProductsAdapter storeMyProductsAdapter;
    StoreMyProductsModel storeMyProductsModel;

    String getProducts_URL;
    UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    IPADDRESS ipaddress;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_my_products);
        myProductsRecycler=findViewById(R.id.smp_recycler);
        searchView=findViewById(R.id.smp_etSearchBox);
        progressBar=findViewById(R.id.smp_loading);
        mToolbar = findViewById(R.id.toolbar_for_myProduct_activity);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.myProducts);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        context=this;
        myPoductsList=new ArrayList<>();
        myPoductsList.clear();
        gridLayoutManager=new GridLayoutManager(this,2);
        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        getProducts_URL="http://"+ip+"/VeterinarySystem/myproducts.php";
        //getProducts_URL="http://www.veterinarysystem.ga/myproducts.php";
        myProductsRecycler.setHasFixedSize(true);
        myProductsRecycler.setLayoutManager(gridLayoutManager);
        userSessionManager=new UserSessionManager(context);
        sessionDetails=userSessionManager.getSessionDetails();
        progressBar.setIndeterminateDrawable(new Circle());
        getProducts();


    }

    private void getProducts() {

        StringRequest myProductsRequest=new StringRequest(Request.Method.POST, getProducts_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object=new JSONObject(response);
                    boolean status=object.getBoolean("status");
                    JSONArray jsonArray=object.getJSONArray("myproducts");
                    if (status)
                    {
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String image=jsonObject.getString("itemimage");
                            String name=jsonObject.getString("itemname");
                            String price=jsonObject.getString("itemprice");
                            int id=jsonObject.getInt("id");

                            storeMyProductsModel=new StoreMyProductsModel(id,image,name,price);
                            myPoductsList.add(storeMyProductsModel);


                        }
                        progressBar.setVisibility(View.GONE);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String s) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String s) {
                                storeMyProductsAdapter.getFilter().filter(s);
                                return false;
                            }
                        });
                        storeMyProductsAdapter=new StoreMyProductsAdapter(myPoductsList,context);
                        myProductsRecycler.setAdapter(storeMyProductsAdapter);


                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                    }
                }
                catch (Exception e)
                {
                    progressBar.setVisibility(View.GONE);
                   // Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(context, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myProductMap=new HashMap<>();
                myProductMap.put("storeID",String.valueOf(sessionDetails.getId()));
                return myProductMap;
            }
        };
        Volley.newRequestQueue(context).add(myProductsRequest);
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
