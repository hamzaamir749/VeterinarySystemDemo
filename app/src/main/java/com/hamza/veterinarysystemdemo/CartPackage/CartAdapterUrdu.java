package com.hamza.veterinarysystemdemo.CartPackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;
import com.hamza.veterinarysystemdemo.models.userMedicineListModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class CartAdapterUrdu extends RecyclerView.Adapter<CartAdapterUrdu.modelViewHolder> {
    private List<userMedicineListModel> productList;
    Context mContext;

    public CartAdapterUrdu(List<userMedicineListModel> productList, Context mContext) {
        this.productList = productList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.cart_item_list_urdu, parent, false);
        modelViewHolder modelViewHolderobj = new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {
        final userMedicineListModel product = productList.get(i);
        holder.medicineName.setText(product.getmName());
        holder.storename.setText(product.getmStoreName());
        holder.price.setText(String.valueOf(product.getmPrice()));
        holder.quantity.setText(String.valueOf(product.getQuantity()));
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PrettyDialog(mContext)
                        .setTitle(mContext.getResources().getString(R.string.Remove))
                        .setMessage(mContext.getResources().getString(R.string.areyousure))
                        .setIcon(R.drawable.appplus).addButton(mContext.getResources().getString(R.string.yes), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        UserSessionManager.cart.removeFromCart(product);
                        Intent intent = new Intent(mContext.getApplicationContext(), CartActivity.class);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();


                    }
                }).addButton(mContext.getResources().getString(R.string.no), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(mContext.getApplicationContext(), CartActivity.class);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();


                    }
                }).show();


            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = (int) product.getQuantity();
                qty++;
                UserSessionManager.cart.addToCart(product);
                holder.quantity.setText(String.valueOf(qty));
                new CartActivity().setTotalPrice();
                if (!holder.sub.isEnabled()) {
                    holder.sub.setEnabled(true);
                }


            }
        });
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = (int) product.getQuantity();
                qty--;
                if (qty > 0) {
                    UserSessionManager.cart.removeFromCart(product);
                    holder.quantity.setText(String.valueOf(qty));
                    new CartActivity().setTotalPrice();
                    if (qty < 2) {
                        holder.sub.setEnabled(false);
                    }
                }

            }
        });
        Picasso.get().load(product.getmImage()).placeholder(R.drawable.navheaderprofilepicicon).into(holder.medicinepic);


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {

        TextView medicineName, storename, price, quantity, cross;
        Button add, sub;
        ImageView medicinepic;
        public modelViewHolder(@NonNull View itemView) {
            super(itemView);

            medicineName = itemView.findViewById(R.id.tvCartItemName_ur);
            storename = itemView.findViewById(R.id.tvCartItemStoreName_ur);
            price = itemView.findViewById(R.id.tvCartItemDiscountedPrice_ur);
            quantity = itemView.findViewById(R.id.tvCartItemQuantity_ur);
            cross = itemView.findViewById(R.id.tvCross_ur);
            add = itemView.findViewById(R.id.btnPlus_ur);
            sub = itemView.findViewById(R.id.btnMinus_ur);
            medicinepic = itemView.findViewById(R.id.ivCartItemImage_ur);
        }
    }
}
