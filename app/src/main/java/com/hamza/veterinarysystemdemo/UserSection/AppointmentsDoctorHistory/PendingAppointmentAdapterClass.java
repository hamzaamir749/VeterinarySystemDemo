package com.hamza.veterinarysystemdemo.UserSection.AppointmentsDoctorHistory;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.user_doctor_Activity.User_doctor_booked_Activity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingAppointmentAdapterClass extends RecyclerView.Adapter<PendingAppointmentAdapterClass.modelViewHolder> {

    Context context;
    List<PendingAppointmentModelClass> list;

    public PendingAppointmentAdapterClass(Context context, List<PendingAppointmentModelClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.activity_pending_appointment_recycler_layout, viewGroup, false);
        modelViewHolder obj = new modelViewHolder(view);
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
        final int id = list.get(i).getId();
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToBookedActivity(id);
            }
        });
    }

    private void SendUserToBookedActivity(int id) {
        Intent NextToDoctorBooking = new Intent(context, User_doctor_booked_Activity.class);
        NextToDoctorBooking.putExtra("doctorKey", id);
        NextToDoctorBooking.putExtra("type", "pending");
        context.startActivity(NextToDoctorBooking);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name, address, phone, time, date;
        LinearLayout linearLayout;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.upa_doctorimage);
            name = itemView.findViewById(R.id.upa_doctor_name);
            address = itemView.findViewById(R.id.upa_doctor_tehseel);
            phone = itemView.findViewById(R.id.upa_doctor_phoneNo);
            time = itemView.findViewById(R.id.upa_doctor_time);
            date = itemView.findViewById(R.id.upa_doctor_date);
            linearLayout = itemView.findViewById(R.id.upa_linear);
        }
    }
}
