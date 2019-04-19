package com.hamza.veterinarysystemdemo.user_doctor_Activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.diseasesAnimalListActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_DA_Adapter extends RecyclerView.Adapter<User_DA_Adapter.modelViewHolder> {

    List<User_DA_Model> doctorsList;
    Context context;

    public User_DA_Adapter(List<User_DA_Model> doctorsList, Context context) {
        this.doctorsList = doctorsList;
        this.context = context;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.doctor_list_uda,viewGroup,false);
        modelViewHolder modelViewHolderobj=new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {

        holder.dName.setText(doctorsList.get(i).getName());
        holder.dPlace.setText(doctorsList.get(i).getTehseelplace());
        Picasso.get().load(doctorsList.get(i).getImage()).into(holder.imageDoctor);
        final int id=doctorsList.get(i).getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDoctorBookingActivity(id);
            }
        });



    }

    private void goToDoctorBookingActivity(int id) {
        Intent NextToDoctorBooking=new Intent(context, User_doctor_booked_Activity.class);
        NextToDoctorBooking.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NextToDoctorBooking.putExtra("doctorKey",id);
        context.startActivity(NextToDoctorBooking);

    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageDoctor;
        TextView dName,dPlace;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);

            imageDoctor=itemView.findViewById(R.id.image_uda_doctor);
            dName=itemView.findViewById(R.id.doctor_name_uda);
            dPlace=itemView.findViewById(R.id.doctor_place_uda);

        }
    }
}
