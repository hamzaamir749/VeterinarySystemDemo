package com.hamza.veterinarysystemdemo.MedicineHistory;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PendingMedicineAdapterClass extends RecyclerView.Adapter<PendingMedicineAdapterClass.modelViewHolder> {
    Context context;
    List<PendingMedicineModelClass> list;

    public PendingMedicineAdapterClass(Context context, List<PendingMedicineModelClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.activity_pending_medicine_recycler_layout,viewGroup,false);
       modelViewHolder obj=new modelViewHolder(view);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {


        holder.name.setText(list.get(i).getMedicinename());
        holder.price.setText(list.get(i).getMedicineprice());
        holder.quantity.setText(list.get(i).getMedicinequantity());
        holder.time.setText(list.get(i).getTime());
        holder.date.setText(list.get(i).getDate());
        Picasso.get().load(list.get(i).getMedicineimage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
       ImageView imageView;
        TextView name,price,quantity,time,date;
        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.mpa_medicine_image);
            name=itemView.findViewById(R.id.mpa_medicine_name);
            price=itemView.findViewById(R.id.mpa_medicine_price);
            quantity=itemView.findViewById(R.id.mpa_medicine_quantity);
            time=itemView.findViewById(R.id.mpa_medicine_time);
            date=itemView.findViewById(R.id.mpa_medicine_date);


        }
    }
}
