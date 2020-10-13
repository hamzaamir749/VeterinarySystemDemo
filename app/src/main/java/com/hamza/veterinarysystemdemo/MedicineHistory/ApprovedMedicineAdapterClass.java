package com.hamza.veterinarysystemdemo.MedicineHistory;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.FeedBackActivity;
import com.hamza.veterinarysystemdemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ApprovedMedicineAdapterClass extends RecyclerView.Adapter<ApprovedMedicineAdapterClass.modelViewHolder> {
    Context context;
    List<ApprovedMedicineModelClass> list;

    public ApprovedMedicineAdapterClass(Context context, List<ApprovedMedicineModelClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.activity_approved_medicine_recycler_layout,viewGroup,false);
        modelViewHolder obj=new modelViewHolder(view);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {

        Picasso.get().load(list.get(i).getMedicineimage()).into(holder.image);
        holder.name.setText(list.get(i).getMedicinename());
        holder.price.setText(list.get(i).getMedicineprice());
        holder.quantity.setText(list.get(i).getMedicinequantity());
        holder.time.setText(list.get(i).getTime());
        holder.date.setText(list.get(i).getDate());
        final String storeid=list.get(i).getStoreid();

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToFeedbackActivity(storeid);
            }
        });
    }

    private void SendUserToFeedbackActivity(String storeid) {
        Intent bookIntent=new Intent(context.getApplicationContext(), FeedBackActivity.class);
        bookIntent.putExtra("Key",storeid);
        bookIntent.putExtra("section","medicinesapprovedappointments");
        context.startActivity(bookIntent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name,price,quantity,time,date;
        Button button;
        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.maa_medicineimage);
            name=itemView.findViewById(R.id.maa_medicine_name);
            price=itemView.findViewById(R.id.maa_medicine_price);
            quantity=itemView.findViewById(R.id.maa_medicine_quantity);
            time=itemView.findViewById(R.id.maa_medicine_time);
            date=itemView.findViewById(R.id.maa_medicine_date);
            button=itemView.findViewById(R.id.maa_medicine_feedback);

        }
    }
}
