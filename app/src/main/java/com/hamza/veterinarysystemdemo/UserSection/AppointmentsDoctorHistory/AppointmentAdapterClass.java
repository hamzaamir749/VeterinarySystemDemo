package com.hamza.veterinarysystemdemo.UserSection.AppointmentsDoctorHistory;
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

public class AppointmentAdapterClass extends RecyclerView.Adapter<AppointmentAdapterClass.modelViewHolder> {

    Context context;
    List<AppointmentModelClass> list;

    public AppointmentAdapterClass(Context context, List<AppointmentModelClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.activity_approved_appointment_recycler_layout,viewGroup,false);
        modelViewHolder obj=new modelViewHolder(view);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {

        Picasso.get().load(list.get(i).getImage()).into(holder.image);
        holder.name.setText(list.get(i).getName());
        holder.address.setText(list.get(i).getAddress());
        holder.phone.setText(list.get(i).getPhone());
        holder.time.setText(list.get(i).getTime());
        holder.date.setText(list.get(i).getDate());
        final int id=list.get(i).getId();

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToFeedbackActivity(id);
            }
        });
    }

    private void SendUserToFeedbackActivity(int id) {
        Intent bookIntent=new Intent(context.getApplicationContext(), FeedBackActivity.class);
        bookIntent.putExtra("Key",String.valueOf(id));
        bookIntent.putExtra("section","userspprovedappointments");
        context.startActivity(bookIntent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name,address,phone,time,date;
        Button button;
        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.uaa_doctorimage);
            name=itemView.findViewById(R.id.uaa_doctor_name);
            address=itemView.findViewById(R.id.uaa_doctor_tehseel);
            phone=itemView.findViewById(R.id.uaa_doctor_phoneNo);
            time=itemView.findViewById(R.id.uaa_doctor_time);
            date=itemView.findViewById(R.id.uaa_doctor_date);
            button=itemView.findViewById(R.id.uaa_doctor_feedback);

        }
    }
}
