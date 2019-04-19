package com.hamza.veterinarysystemdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView forgetPassword,signUp,login;
    private EditText Email,Password;
    private RadioButton rbDoctor,rbCient,rbStore;
    private String loginType=null;
    IPADDRESS ipaddress;
    public String name,category,address,phoneno,profilepic,login_URL;
    private int id;
    String ip;
    private ProgressDialog progressDialog;
    private  JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ipaddress=new IPADDRESS();
        ip=ipaddress.getIpaddress();
        login_URL="http://www.veterinarysystem.ga/login.php";

        signUp=findViewById(R.id.btnlogsignup);
        forgetPassword=findViewById(R.id.btnForgetPassword);
        login=findViewById(R.id.btnlogin);
        rbCient=findViewById(R.id.rbClient);
        rbDoctor=findViewById(R.id.rbDoctor);
        rbStore=findViewById(R.id.rbStore);
        Email=findViewById(R.id.edtlogEmail);
        Password=findViewById(R.id.edtlogpassword);
        progressDialog=new ProgressDialog(this);
        Email.setText("hamzaamir749@gmail.com");
        Password.setText("03072039383");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFunction();
            }


        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToSignUpActivity();
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToForgetPasswordActivity();
            }
        });
    }
    private void LoginFunction() {

        ipaddress.getIpaddress();
        String email=Email.getText().toString().trim();
        String password=Password.getText().toString().trim();
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
        {
            Password.setError("Please Enter Password");
            Email.setError("Please Enter Email");
        } else if (TextUtils.isEmpty(email))
        {
            Email.setError("Please Enter Email");
        } else if (TextUtils.isEmpty(password))
        {
            Password.setError("Please Enter Password");
        } else
        {
            if (rbCient.isChecked())
            {
                loginType="0";
            } else if(rbStore.isChecked()){
                loginType="1";
            } else if (rbDoctor.isChecked())
            {
                loginType="2";
            }

                checkLoginDetails(email,password);

        }


      // onlyDirectActivity();
    }

    private void checkLoginDetails(final String email, final String password) {

        progressDialog.setTitle(R.string.PDLoginTital);
        progressDialog.setMessage(getResources().getString(R.string.PDLoginMessage));
        progressDialog.show();

        StringRequest loginCheckRequest=new StringRequest(Request.Method.POST, login_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
               try {
                   jsonObject=new JSONObject(response);
                   String GCategory=jsonObject.getString("usercategory");
                   String success=jsonObject.getString("success");
                   if (success.equals("1"))
                   {

                       if (GCategory.equals("0"))
                       {
                           String cName=jsonObject.getString("name");
                           int cId=jsonObject.getInt("id");
                           String cProfilepic=jsonObject.getString("profileimage");
                           String  cAddress=jsonObject.getString("address");
                           String  cPhoneno=jsonObject.getString("phone");

                           ClientSessionDetails clientSessionDetails=new ClientSessionDetails();
                           clientSessionDetails.setId(cId);
                           clientSessionDetails.setAddress(cAddress);
                           clientSessionDetails.setName(cName);
                           clientSessionDetails.setPhone(cPhoneno);
                           clientSessionDetails.setProfilepicture(cProfilepic);
                           UserSessionManager userSessionManager=new UserSessionManager(LoginActivity.this);
                           userSessionManager.setClientLoggedIn(true);
                           userSessionManager.setClientDataDetails(clientSessionDetails);

                           SendUserToCLientActivity();

                       } else if (GCategory.equals("1"))
                       {

                           int sId=jsonObject.getInt("id");
                           String sName=jsonObject.getString("name");
                           String sProfilepic=jsonObject.getString("profileimage");
                           String  sAddress=jsonObject.getString("address");
                           String  sPhoneno=jsonObject.getString("phone");

                           StoreSessionDetails storeSessionDetails=new StoreSessionDetails();
                           storeSessionDetails.setId(sId);
                           storeSessionDetails.setAddress(sAddress);
                           storeSessionDetails.setName(sName);
                           storeSessionDetails.setPhone(sPhoneno);
                           storeSessionDetails.setProfilepicture(sProfilepic);
                           UserSessionManager storeSessionManager=new UserSessionManager(LoginActivity.this);
                           storeSessionManager.setStoreLoggedIn(true);
                           storeSessionManager.setStoreDataDetails(storeSessionDetails);
                           SendUserToStoreActivity();

                       }
                       else if (GCategory.equals("2"))
                       {
                           String dName=jsonObject.getString("name");
                           int dId=jsonObject.getInt("id");
                           String dProfilepic=jsonObject.getString("profileimage");
                           String  dAddress=jsonObject.getString("address");
                           String  dPhoneno=jsonObject.getString("phone");
                           DoctorSessionDetails doctorSessionDetails=new DoctorSessionDetails();
                           doctorSessionDetails.setId(dId);
                           doctorSessionDetails.setAddress(dAddress);
                           doctorSessionDetails.setName(dName);
                           doctorSessionDetails.setPhone(dPhoneno);
                           doctorSessionDetails.setProfilepicture(dProfilepic);
                           UserSessionManager doctorSessionManager=new UserSessionManager(LoginActivity.this);
                           doctorSessionManager.setDoctorLoggedIn(true);
                           doctorSessionManager.setDoctorDataDetails(doctorSessionDetails);
                           SendUserToDoctorActivity();

                       }


                   }

               } catch (Exception e)
               {
                   Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
               }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> loginMap=new HashMap<>();
                loginMap.put("email",email);
                loginMap.put("password",password);
                loginMap.put("loginType",loginType);
                return loginMap;
            }
        };
        Volley.newRequestQueue(this).add(loginCheckRequest);

    }

    private void SendUserToDoctorActivity() {
        Intent dIntent=new Intent(getApplicationContext(),DoctorDrawerActivity.class);
        dIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(dIntent);
    }

    private void SendUserToStoreActivity() {
        Intent sIntent=new Intent(getApplicationContext(),StoreDrawerActivity.class);
        sIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(sIntent);
    }

    private void SendUserToCLientActivity() {
        Intent cIntent=new Intent(getApplicationContext(),MainActivity.class);
        cIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(cIntent);
    }

    private void onlyDirectActivity() {
        Intent loginIntent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToForgetPasswordActivity() {
        Intent forgetPasswordIntent=new Intent(getApplicationContext(),ForgetPasswordActivity.class);
        startActivity(forgetPasswordIntent);
    }

    private void sendUserToSignUpActivity() {
        Intent signUpIntent=new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(signUpIntent);
    }
}
