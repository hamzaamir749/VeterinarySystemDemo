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
import com.hamza.veterinarysystemdemo.StoreSection.Model.StoreHistoryDetailsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoreHistoryDetailsAdapter extends RecyclerView.Adapter<StoreHistoryDetailsAdapter.modelViewHolder> {

    Context context;
    List<StoreHistoryDetailsModel> list;

    public StoreHistoryDetailsAdapter(Context context, List<StoreHistoryDetailsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.activity_store_history_details_recycler_layout, viewGroup, false);
        modelViewHolder obj = new modelViewHolder(view);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {

        holder.name.setText(list.get(i).getName());
        holder.quantity.setText(list.get(i).getQuantity());

        Picasso.get().load(list.get(i).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, quantity;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.shd_medicine_image);
            name = itemView.findViewById(R.id.shd_medicine_name);
            quantity = itemView.findViewById(R.id.shd_quantity);
        }
    }
}
