package com.hamza.veterinarysystemdemo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.DoubleBounce;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        progressBar.setIndeterminateDrawable(new Circle());

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
