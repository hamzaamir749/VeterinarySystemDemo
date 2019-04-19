package com.hamza.veterinarysystemdemo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hamza.veterinarysystemdemo.CartPackage.Cart;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.UserSessionManager;
import com.hamza.veterinarysystemdemo.models.userMedicineListModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class userMedicineListAdapter extends RecyclerView.Adapter<userMedicineListAdapter.modelViewHolder> {

    List<userMedicineListModel> medicineList;
    Context mContext;
    userMedicineListModel product;


    public userMedicineListAdapter(List<userMedicineListModel> medicineList, Context mContext) {
        this.medicineList = medicineList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.user_medicine_list_layout, viewGroup, false);
        modelViewHolder mvh = new modelViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final modelViewHolder holder, int i) {
        final userMedicineListModel product = medicineList.get(i);
        final String price=product.getmPrice();
        if (UserSessionManager.cart !=null)
        {
           int quantity=UserSessionManager.cart.getItemQuantity(product.getmID());
            if (quantity != 0)
            {
                holder.add.setText(String.valueOf(UserSessionManager.cart.getItemQuantity(product.getmID())));
                holder.btnPlus.setVisibility(View.VISIBLE);
                holder.btnMinus.setVisibility(View.VISIBLE);
                holder.add.setFocusable(false);
                holder.add.setClickable(false);
                holder.btnPlus.setClickable(true);
                holder.btnPlus.setFocusable(true);
                holder.btnMinus.setClickable(true);
                holder.btnMinus.setFocusable(true);
            }
            else
            {
                holder.add.setText("ADD");
            }

        }

           holder.mName.setText(product.getmName());
        holder.mPrice.setText(product.getmPrice());
        holder.sName.setText( product.getmStoreName());
        Picasso.get().load(product.getmImage()).into(holder.mImage);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                product.setQuantity(1);
                product.setPrice(Double.valueOf(price));
                addToCart(product);

                holder.add.setText(String.valueOf(UserSessionManager.cart.getItemQuantity(product.getmID())));
                holder.btnPlus.setVisibility(View.VISIBLE);
                holder.btnMinus.setVisibility(View.VISIBLE);
                holder.add.setFocusable(false);
                holder.add.setClickable(false);
                holder.btnPlus.setClickable(true);
                holder.btnPlus.setFocusable(true);
                holder.btnMinus.setClickable(true);
                holder.btnMinus.setFocusable(true);
                Toast.makeText(mContext.getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(product);
                product.setPrice(Double.valueOf(price));
                product.setQuantity(UserSessionManager.cart.getItemQuantity(product.getmID()));
                int qty = (int)product.getQuantity();
                holder.add.setText(String.valueOf(qty));
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSessionManager.cart.removeFromCart(product);
                int qty = (int)UserSessionManager.cart.getItemQuantity(product.getmID());
                if(UserSessionManager.cart.getTotalItems()==0){
                    UserSessionManager.cart.removeBadge();
                    UserSessionManager.cart.productList.clear();
                    UserSessionManager.cart = null;
                }else{
                    UserSessionManager.cart.setBadge(UserSessionManager.cart.getTotalItems()+"");
                }
                if(qty==0){
                    holder.add.setText("ADD");
                    holder.btnMinus.setVisibility(View.GONE);
                    holder.btnPlus.setVisibility(View.GONE);
                    holder.add.setFocusable(true);
                    holder.add.setClickable(true);
                    holder.btnPlus.setClickable(false);
                    holder.btnPlus.setFocusable(false);
                    holder.btnMinus.setClickable(false);
                    holder.btnMinus.setFocusable(false);
                    return;
                }
                product.setPrice(Double.valueOf(price));
                holder.add.setText(""+qty);

            }
        });


    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    private void addToCart(userMedicineListModel product) {
        if (UserSessionManager.cart == null) {
            UserSessionManager.cart = new Cart(mContext);
        }
        UserSessionManager.cart.addToCart(product);

        int totalItems = UserSessionManager.cart.getTotalItems();
        UserSessionManager.cart.setBadge(""+totalItems);
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mName, sName, mPrice;
        Button btnPlus,btnMinus;
        TextView add;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.uml_medicine_image);
            mName = itemView.findViewById(R.id.uml_medicine_name);
            sName = itemView.findViewById(R.id.uml_storename);
            mPrice = itemView.findViewById(R.id.uml_price);
            add = itemView.findViewById(R.id.tvQuantity);
            btnMinus=itemView.findViewById(R.id.btnMinusUML);
            btnPlus=itemView.findViewById(R.id.btnPlusUML);

        }
    }
}
