package com.hamza.veterinarysystemdemo.StoreSection.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.StoreSection.Model.StoreMyProductsModel;
import com.hamza.veterinarysystemdemo.StoreSection.StoreEditProductActivity;
import com.hamza.veterinarysystemdemo.StoreSection.StoreMyProductsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;



public class StoreMyProductsAdapter extends RecyclerView.Adapter<StoreMyProductsAdapter.modelViewHolder> implements Filterable {

    public List<StoreMyProductsModel> myProducts;
    public List<StoreMyProductsModel> filterList;
    Context context;
    CustomFilter filter;

    public StoreMyProductsAdapter(List<StoreMyProductsModel> myProducts, Context context) {
        this.myProducts = myProducts;
        this.filterList=myProducts;
        this.context = context;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_store_my_products_layout,viewGroup,false);
        modelViewHolder modelViewHolderobj=new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {

        holder.medicineName.setText(myProducts.get(i).getmName());
        holder.medicinePrice.setText(myProducts.get(i).getmPrice());
        Picasso.get().load(myProducts.get(i).getmImage()).into(holder.medicineImage);
        final int id=myProducts.get(i).getId();
        final String name=myProducts.get(i).getmName();
        final String image=myProducts.get(i).getmImage();
        final String price=myProducts.get(i).getmPrice();
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToEditProductActivty(id,name,image,price);
            }
        });

    }

    private void sendUserToEditProductActivty(int id, String name, String image, String price) {
        Intent EditProductIntent=new Intent(context, StoreEditProductActivity.class);
        EditProductIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        EditProductIntent.putExtra("productid",id);
        EditProductIntent.putExtra("productimage",image);
        EditProductIntent.putExtra("productprice",price);
        EditProductIntent.putExtra("productname",name);
        context.startActivity(EditProductIntent);
        ((StoreMyProductsActivity)context).finish();

    }

    @Override
    public int getItemCount() {
        return myProducts.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null)
        {
            filter=new CustomFilter(this,filterList);
        }
        return filter;
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {

        ImageView medicineImage;
        TextView medicineName, medicinePrice;
        LinearLayout edit;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);

            medicineImage=itemView.findViewById(R.id.smp_medicine_image);
            medicineName=itemView.findViewById(R.id.smp_medicine_name);
            medicinePrice =itemView.findViewById(R.id.smp__medicine_price);
            edit=itemView.findViewById(R.id.smp_medicine_edit);

        }
    }
}
