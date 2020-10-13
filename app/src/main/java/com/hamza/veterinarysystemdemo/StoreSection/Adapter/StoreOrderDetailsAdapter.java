package com.hamza.veterinarysystemdemo.StoreSection.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.StoreSection.Model.StoreOrderDetailsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoreOrderDetailsAdapter extends RecyclerView.Adapter<StoreOrderDetailsAdapter.modelViewHolder> {

    List<StoreOrderDetailsModel> clientmedicineList;
    Context context;

    public StoreOrderDetailsAdapter(List<StoreOrderDetailsModel> clientmedicineList, Context context) {
        this.clientmedicineList = clientmedicineList;
        this.context = context;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_store_order_details_layout,viewGroup,false);
        modelViewHolder modelViewHolderobj=new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {

        holder.mName.setText(clientmedicineList.get(i).getMedicineName());
        holder.mQuantity.setText(context.getResources().getString(R.string.Quantity)+" "+clientmedicineList.get(i).getMedicineQuantity());
        holder.mPrice.setText(context.getResources().getString(R.string.Price)+" "+clientmedicineList.get(i).getMedicinePrice()+ " RS");
        Picasso.get().load(clientmedicineList.get(i).getMedicineImage()).into(holder.mImage);

    }

    @Override
    public int getItemCount() {
        return clientmedicineList.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {

       ImageView mImage;
        TextView mName, mPrice , mQuantity ;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);

            mImage=itemView.findViewById(R.id.sod_medicine_image);
            mName=itemView.findViewById(R.id.sod_medicine_name);
            mPrice =itemView.findViewById(R.id.sod_medicine_price);
            mQuantity =itemView.findViewById(R.id.sod_medicine_quantity);

        }
    }
}
