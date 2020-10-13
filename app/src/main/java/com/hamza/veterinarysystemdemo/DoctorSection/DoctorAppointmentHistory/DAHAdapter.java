package com.hamza.veterinarysystemdemo.DoctorSection.DoctorAppointmentHistory;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.FeedBackActivity;
import com.hamza.veterinarysystemdemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DAHAdapter extends RecyclerView.Adapter<DAHAdapter.modelViewHolder> {
    Context context;
    List<DAHModel> historyList;

    public DAHAdapter(Context context, List<DAHModel> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.activity_doctor_appointment_history_layout,viewGroup,false);
        modelViewHolder modelViewHolderobj=new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {

        holder.CName.setText(historyList.get(i).getName());
        holder.CAddress.setText(historyList.get(i).getAddress());
        holder.CPhone.setText(historyList.get(i).getPhone());
        Picasso.get().load(historyList.get(i).getImage()).into(holder.CImage);
        holder.time.setText(historyList.get(i).getTime());
        holder.date.setText(historyList.get(i).getDate());
        final int id=historyList.get(i).getId();
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToFeedbackActivity(id);
            }
        });


    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
    private void SendUserToFeedbackActivity(int id) {
        Intent bookIntent=new Intent(context.getApplicationContext(), FeedBackActivity.class);
        bookIntent.putExtra("Key",String.valueOf(id));
        bookIntent.putExtra("section","doctorsapprovedappointments");
        context.startActivity(bookIntent);
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {

        CircleImageView CImage;
        TextView CName,CAddress,CPhone,time,date;
        Button button;
        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            CImage=(itemView).findViewById(R.id.dah_image);
            CName=(itemView).findViewById(R.id.dah__name);
            CAddress=(itemView).findViewById(R.id.dah_address);
            CPhone=(itemView).findViewById(R.id.dah_phoneNo);
            time=(itemView).findViewById(R.id.dah_time);
            date=(itemView).findViewById(R.id.dah_date);
            button=(itemView).findViewById(R.id.dah_feedback);
        }
    }
}
