package com.hamza.veterinarysystemdemo.UserSection;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.hamza.veterinarysystemdemo.MedicineHistory.ApprovedMedicineActivity;
import com.hamza.veterinarysystemdemo.MedicineHistory.PendingMedicineActivity;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.UserSection.AppointmentsDoctorHistory.ApprovedAppointmentsActivity;
import com.hamza.veterinarysystemdemo.UserSection.AppointmentsDoctorHistory.PendingAppointmentsActivity;

public class HistoryActivity extends AppCompatActivity {
    LinearLayout pendingDoctor,approvedDoctor,pendingStore,approvedStore;
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        pendingDoctor=findViewById(R.id.c3);
        approvedDoctor=findViewById(R.id.c4);
        pendingStore=findViewById(R.id.c5);
        approvedStore=findViewById(R.id.c6);
        mToolBar=findViewById(R.id.history_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.History);
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

        approvedDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToApprovedAppointmentActivity();
            }
        });
        pendingDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPendingAppointments();
            }
        });
        approvedStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToApprovedMedicine();
            }
        });
        pendingStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPendingMedicine();
            }
        });
    }

    private void SendUserToPendingMedicine() {
        Intent pendingIntent=new Intent(getApplicationContext(), PendingMedicineActivity.class);
        startActivity(pendingIntent);
    }

    private void SendUserToApprovedMedicine() {
        Intent appIntent=new Intent(getApplicationContext(), ApprovedMedicineActivity.class);
        startActivity(appIntent);
    }

    private void SendUserToPendingAppointments() {
        Intent pendingIntent=new Intent(getApplicationContext(), PendingAppointmentsActivity.class);
        startActivity(pendingIntent);
    }

    private void SendUserToApprovedAppointmentActivity() {
        Intent appIntent=new Intent(getApplicationContext(),ApprovedAppointmentsActivity.class);
        startActivity(appIntent);
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
