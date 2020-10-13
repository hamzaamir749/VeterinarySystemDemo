package com.hamza.veterinarysystemdemo.StoreSection.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.StoreSection.Model.StoreHistoryModel;
import com.hamza.veterinarysystemdemo.StoreSection.StoreHistoryDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreHistoryAdapter extends RecyclerView.Adapter<StoreHistoryAdapter.modelViewHolder> {

    Context context;
    List<StoreHistoryModel> list;

    public StoreHistoryAdapter(Context context, List<StoreHistoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.activity_store_order_history_recycler_layout,viewGroup,false);
        modelViewHolder obj=new modelViewHolder(view);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {

        holder.name.setText(list.get(i).getName());
        holder.address.setText(list.get(i).getAddress());
        holder.phone.setText(list.get(i).getPhone());
        Picasso.get().load(list.get(i).getImage()).into(holder.profile);
        final int id=list.get(i).getId();
        final String address=list.get(i).getAddress();
        holder.orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStoreToDetailsActivity(id,address);
            }
        });

    }

    private void sendStoreToDetailsActivity(int id, String address) {

        Intent detailsIntent=new Intent(context.getApplicationContext(), StoreHistoryDetailsActivity.class);
        detailsIntent.putExtra("userid",id);
        detailsIntent.putExtra("useraddress",address);
        context.startActivity(detailsIntent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView name,address,phone;
        Button orders;
        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            profile=itemView.findViewById(R.id.soh_userImage);
            name=itemView.findViewById(R.id.soh_userName);
            address=itemView.findViewById(R.id.soh_userPlace);
            phone=itemView.findViewById(R.id.soh_userPhone);
            orders=itemView.findViewById(R.id.soh_check_orders);
        }
    }
}
