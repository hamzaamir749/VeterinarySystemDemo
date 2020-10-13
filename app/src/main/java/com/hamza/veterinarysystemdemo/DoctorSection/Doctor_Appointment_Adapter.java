package com.hamza.veterinarysystemdemo.DoctorSection;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.hamza.veterinarysystemdemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Doctor_Appointment_Adapter extends RecyclerView.Adapter<Doctor_Appointment_Adapter.modelViewHolder> {
    List<Doctor_Appointment_Model> clientsList;
    Context context;

    public Doctor_Appointment_Adapter(List<Doctor_Appointment_Model> clientsList, Context context) {
        this.clientsList = clientsList;
        this.context = context;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.activity_doctor_appointment_layout,viewGroup,false);
        modelViewHolder modelViewHolderobj=new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {
        holder.cName.setText(clientsList.get(i).getName());
        holder.cPlace.setText(clientsList.get(i).getTehseelplace());
        Picasso.get().load(clientsList.get(i).getImage()).into(holder.imageClient);
        final int id=clientsList.get(i).getId();
        final String name=clientsList.get(i).getName();
        final String phone=clientsList.get(i).getPhone();
        final String address=clientsList.get(i).getTehseelplace();
        final String lati=clientsList.get(i).getLat();
        final String longi=clientsList.get(i).getLang();
        final String image=clientsList.get(i).getImage();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDoctorBookedActivity(id,name,phone,address,lati,longi,image);
            }
        });

    }

    private void goToDoctorBookedActivity(int id, String name, String phone, String address, String lati, String longi, String image) {
        Intent NextToDoctorBooked=new Intent(context, DoctorBookedActivity.class);
        NextToDoctorBooked.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NextToDoctorBooked.putExtra("clientKey",id);
        NextToDoctorBooked.putExtra("clientname",name);
        NextToDoctorBooked.putExtra("clientimage",image);
        NextToDoctorBooked.putExtra("clientaddress",address);
        NextToDoctorBooked.putExtra("clientlati",lati);
        NextToDoctorBooked.putExtra("clientlongi",longi);
        NextToDoctorBooked.putExtra("clientphone",phone);
        context.startActivity(NextToDoctorBooked);
        ((DoctorAppointmentActivity)context).finish();
    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }
    public class modelViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageClient;
        TextView cName,cPlace;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);

            imageClient=itemView.findViewById(R.id.da_client_profile_image);
            cName=itemView.findViewById(R.id.da_client_name);
            cPlace=itemView.findViewById(R.id.da_client_tehseel);

        }
    }
}
