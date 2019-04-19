package com.hamza.veterinarysystemdemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.diseasesDescriptionActivity;
import com.hamza.veterinarysystemdemo.models.diseasesAnimalsListModel;

import java.util.List;

public class diseasesAnimalsListAdapter extends RecyclerView.Adapter<diseasesAnimalsListAdapter.modelViewHolder> {
    Context mContext;
    List<diseasesAnimalsListModel> DAL;

    public diseasesAnimalsListAdapter(Context mContext, List<diseasesAnimalsListModel> DAL) {
        this.mContext = mContext;
        this.DAL = DAL;
    }

    @NonNull
    @Override
    public diseasesAnimalsListAdapter.modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.activity_diseases_animal_list_layout,viewGroup,false);
        modelViewHolder modelViewHolderobj=new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull diseasesAnimalsListAdapter.modelViewHolder holder, int i) {
        holder.diseasesnamelist.setText(DAL.get(i).getName());
        final int id=DAL.get(i).getId();
        holder.diseasesnamelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToDiscriptionActivity(id);
            }
        });

    }

    private void SendUserToDiscriptionActivity(int id) {
        Intent descriptionActivityIntent=new Intent(mContext.getApplicationContext(), diseasesDescriptionActivity.class);
        descriptionActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        descriptionActivityIntent.putExtra("did",id);
        mContext.startActivity(descriptionActivityIntent);
    }

    @Override
    public int getItemCount() {
        return DAL.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder
    {
        TextView diseasesnamelist;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            diseasesnamelist=itemView.findViewById(R.id.diseases_animal_list_layout);

        }
    }
}
