package com.hamza.veterinarysystemdemo;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.Wave;
import com.hamza.veterinarysystemdemo.DoctorSection.DoctorDrawerActivity;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.hamza.veterinarysystemdemo.StoreSection.StoreDrawerActivity;

public class SplashScreenActivity extends AppCompatActivity {
UserSessionManager sessionManager;
SessionDetails details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        sessionManager=new UserSessionManager(this);

        Boolean islogged=sessionManager.isLoggedIn();
        if (islogged)
        {
            details=sessionManager.getSessionDetails();
            String type=details.getType();
            if (type.equals("user"))
            {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if (type.equals("doctor"))
            {
                Intent intent=new Intent(getApplicationContext(), DoctorDrawerActivity.class);
                startActivity(intent);
                finish();
            }
            else if (type.equals("store"))
            {
                Intent intent=new Intent(getApplicationContext(), StoreDrawerActivity.class);
                startActivity(intent);
                finish();
            }

        }
        else
        {

            ProgressBar progressBar = findViewById(R.id.spin_kit);
            progressBar.setIndeterminateDrawable(new Wave());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            },4000);
        }

    }


}
