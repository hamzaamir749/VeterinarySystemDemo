package com.hamza.veterinarysystemdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hamza.veterinarysystemdemo.DoctorSection.DoctorDrawerActivity;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.hamza.veterinarysystemdemo.StoreSection.StoreDrawerActivity;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.UserSection.PhoneVerificationsActivity.PhoneVerificationActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView forgetPassword,login;
    Button signUp;
    private EditText Email,Password;
    private RadioButton rbDoctor,rbCient,rbStore;
    private String loginType=null;
    IPADDRESS ipaddress;
    public String name,address,login_URL,saveToken_URL;
    String ip;
    private ProgressDialog progressDialog;
    private  JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ipaddress=new IPADDRESS();
        ip=ipaddress.getIpaddress();
        login_URL="http://" + ip + "/VeterinarySystem/login.php";
        saveToken_URL="http://" + ip + "/VeterinarySystem/getToken.php";
        //login_URL="http://www.veterinarysystem.ga/login.php";
        //saveToken_URL="http://www.veterinarysystem.ga/getToken.php";

        signUp=findViewById(R.id.btnlogsignup);
        forgetPassword=findViewById(R.id.btnForgetPassword);
        login=findViewById(R.id.btnlogin);
        rbCient=findViewById(R.id.rbClient);
        rbDoctor=findViewById(R.id.rbDoctor);
        rbStore=findViewById(R.id.rbStore);
        Email=findViewById(R.id.edtlogEmail);
        Password=findViewById(R.id.edtlogpassword);
        progressDialog=new ProgressDialog(this);
       // Email.setText("hamzaamir749@gmail.com");
        //Password.setText("03072039383");

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
        String emaill=Email.getText().toString().trim();
        String password=Password.getText().toString().trim();
        if (TextUtils.isEmpty(emaill) && TextUtils.isEmpty(password))
        {
            Password.setError(getResources().getString(R.string.pleaseEnterPassword));
            Email.setError(getResources().getString(R.string.pleaseEnterEmail));
        } else if (TextUtils.isEmpty(emaill))
        {
            Email.setError(getResources().getString(R.string.pleaseEnterEmail));
        } else if (TextUtils.isEmpty(password))
        {
            Password.setError(getResources().getString(R.string.pleaseEnterPassword));
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

                checkLoginDetails(emaill,password);

        }


      // onlyDirectActivity();
    }

    private void checkLoginDetails(final String emaill, final String password) {

        progressDialog.setTitle(R.string.PDLoginTital);
        progressDialog.setMessage(getResources().getString(R.string.PDLoginMessage));
        progressDialog.show();

        StringRequest loginCheckRequest=new StringRequest(Request.Method.POST, login_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
               try {
                   SessionDetails sessionDetails =new SessionDetails();
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
                           String cEmail=jsonObject.getString("email");

                           sessionDetails.setId(cId);
                           sessionDetails.setAddress(cAddress);
                           sessionDetails.setName(cName);
                           sessionDetails.setPhone(cPhoneno);
                           sessionDetails.setProfilepicture(cProfilepic);
                           sessionDetails.setEmailAddress(cEmail);
                           sessionDetails.setType("user");
                           UserSessionManager clientSessionManager=new UserSessionManager(LoginActivity.this);
                           clientSessionManager.setLoggedIn(true);
                           clientSessionManager.setSessionDetails(sessionDetails);

                           saveTokenToDatabase(cId);
                           SendUserToCLientActivity();

                       } else if (GCategory.equals("1"))
                       {

                           int sId=jsonObject.getInt("id");
                           String sName=jsonObject.getString("name");
                           String sProfilepic=jsonObject.getString("profileimage");
                           String  sAddress=jsonObject.getString("address");
                           String  sPhoneno=jsonObject.getString("phone");
                           String sEmail=jsonObject.getString("email");

                           sessionDetails.setId(sId);
                           sessionDetails.setAddress(sAddress);
                           sessionDetails.setName(sName);
                           sessionDetails.setPhone(sPhoneno);
                           sessionDetails.setProfilepicture(sProfilepic);
                           sessionDetails.setEmailAddress(sEmail);
                           sessionDetails.setType("store");
                           UserSessionManager storeSessionManager=new UserSessionManager(LoginActivity.this);
                           storeSessionManager.setLoggedIn(true);
                           storeSessionManager.setSessionDetails(sessionDetails);
                           saveTokenToDatabase(sId);
                           SendUserToStoreActivity();

                       }
                       else if (GCategory.equals("2"))
                       {
                           String dName=jsonObject.getString("name");
                           int dId=jsonObject.getInt("id");
                           String dProfilepic=jsonObject.getString("profileimage");
                           String  dAddress=jsonObject.getString("address");
                           String  dPhoneno=jsonObject.getString("phone");
                           String dEmail=jsonObject.getString("email");

                           sessionDetails.setId(dId);
                           sessionDetails.setAddress(dAddress);
                           sessionDetails.setName(dName);
                           sessionDetails.setPhone(dPhoneno);
                           sessionDetails.setProfilepicture(dProfilepic);
                           sessionDetails.setEmailAddress(dEmail);
                           sessionDetails.setType("doctor");
                           UserSessionManager doctorSessionManager=new UserSessionManager(LoginActivity.this);
                           doctorSessionManager.setLoggedIn(true);
                           doctorSessionManager.setSessionDetails(sessionDetails);
                           saveTokenToDatabase(dId);
                           SendUserToDoctorActivity();

                       }


                   }
                   else
                   {
                       Email.setError("Please Check");
                       Password.setError("Please Check");
                      // Toast.makeText(LoginActivity.this, getResources().getString(R.string.pleaseGiveCorrectinfo), Toast.LENGTH_SHORT).show();
                   }

               } catch (Exception e)
               {
                   Email.setError("Please Check");
                   Password.setError("Please Check");
                   //Toast.makeText(getApplicationContext(),"Exception: "+e.getMessage(),Toast.LENGTH_SHORT).show();
               }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Volley: "+error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> loginMap=new HashMap<>();
                loginMap.put("email",emaill);
                loginMap.put("password",password);
                loginMap.put("loginType",loginType);
                return loginMap;
            }
        };
        Volley.newRequestQueue(this).add(loginCheckRequest);

    }

    private void saveTokenToDatabase(final int Id) {
        final String token= FirebaseInstanceId.getInstance().getToken();

        StringRequest saveTokenRequest=new StringRequest(Request.Method.POST, saveToken_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> saveTokenMap=new HashMap<>();
                saveTokenMap.put("token",token);
                saveTokenMap.put("uid",String.valueOf(Id));
                return saveTokenMap;
            }
        };
        Volley.newRequestQueue(this).add(saveTokenRequest);
    }

    private void SendUserToDoctorActivity() {
        Intent dIntent=new Intent(getApplicationContext(), DoctorDrawerActivity.class);
        dIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(dIntent);
    }

    private void SendUserToStoreActivity() {
        Intent sIntent=new Intent(getApplicationContext(), StoreDrawerActivity.class);
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
        Intent signUpIntent=new Intent(getApplicationContext(), PhoneVerificationActivity.class);
        startActivity(signUpIntent);
    }
}
