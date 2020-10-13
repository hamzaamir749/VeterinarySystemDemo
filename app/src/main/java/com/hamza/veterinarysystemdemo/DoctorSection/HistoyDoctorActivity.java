package com.hamza.veterinarysystemdemo.DoctorSection;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.hamza.veterinarysystemdemo.DoctorSection.DoctorAppointmentHistory.DoctorAppointmentsHistoryActivity;
import com.hamza.veterinarysystemdemo.MedicineHistory.ApprovedMedicineActivity;
import com.hamza.veterinarysystemdemo.MedicineHistory.PendingMedicineActivity;
import com.hamza.veterinarysystemdemo.R;

public class HistoyDoctorActivity extends AppCompatActivity {
    LinearLayout approvedusers,pendingStore,approvedStore;
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histoy_doctor);
        mToolBar=findViewById(R.id.doctor_history_toolbar);
        approvedusers=findViewById(R.id.dh_clientsHistory);
        pendingStore=findViewById(R.id.dh_pendingmedicine);
        approvedStore=findViewById(R.id.dh_approvedmedicine);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.history);
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
        approvedusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDoctorToAppointmentClients();
            }
        });
    }

    private void SendDoctorToAppointmentClients() {
        Intent approvedUsersIntent=new Intent(getApplicationContext(), DoctorAppointmentsHistoryActivity.class);
        startActivity(approvedUsersIntent);
    }

    private void SendUserToPendingMedicine() {
        Intent pendingIntent=new Intent(getApplicationContext(), PendingMedicineActivity.class);
        startActivity(pendingIntent);
    }

    private void SendUserToApprovedMedicine() {
        Intent appIntent=new Intent(getApplicationContext(), ApprovedMedicineActivity.class);
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
