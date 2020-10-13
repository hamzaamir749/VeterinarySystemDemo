package com.hamza.veterinarysystemdemo.StoreSection.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.StoreSection.CustomerProfileActivity;
import com.hamza.veterinarysystemdemo.StoreSection.Model.StoreOrderModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreOrderAdapter extends RecyclerView.Adapter<StoreOrderAdapter.modelViewHolder> {
    List<StoreOrderModel> clientsList;
    Context context;

    public StoreOrderAdapter(List<StoreOrderModel> clientsList, Context context) {
        this.clientsList = clientsList;
        this.context = context;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.activity_store_medicine_order_layout,viewGroup,false);
        modelViewHolder modelViewHolderobj=new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {
        holder.cName.setText(clientsList.get(i).getName());
        holder.time.setText(clientsList.get(i).getTime());
        Picasso.get().load(clientsList.get(i).getImage()).into(holder.imageClient);
        final int id=clientsList.get(i).getId();
        final String phone=clientsList.get(i).getPhone();
        final String address=clientsList.get(i).getAddress();
        final String image=clientsList.get(i).getImage();
        final String name=clientsList.get(i).getName();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStoreOrderDetailsActivity(id,phone,address,name,image);
            }
        });

    }

    private void goToStoreOrderDetailsActivity(int id, String Phone, String Address, String Name, String Image) {
        Intent NextToCustomerProfile=new Intent(context, CustomerProfileActivity.class);
        NextToCustomerProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NextToCustomerProfile.putExtra("clientKey",String.valueOf(id));
        NextToCustomerProfile.putExtra("clientphone",Phone);
        NextToCustomerProfile.putExtra("clientaddress",Address);
        NextToCustomerProfile.putExtra("clientname",Name);
        NextToCustomerProfile.putExtra("clientimage",Image);
        context.startActivity(NextToCustomerProfile);
    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageClient;
        TextView cName, time;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);

            imageClient=itemView.findViewById(R.id.smo_profile_image);
            cName=itemView.findViewById(R.id.smo_name);
            time =itemView.findViewById(R.id.smo_time);

        }
    }
}
