package com.hamza.veterinarysystemdemo.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.diseasesAnimalListActivity;
import com.hamza.veterinarysystemdemo.models.diseasesAnimalsNamesModel;

import java.util.List;

public class diseasesAnimalsNamesAdapter extends RecyclerView.Adapter<diseasesAnimalsNamesAdapter.modelDCAViewHolder> {

    Context mContext;
    List<diseasesAnimalsNamesModel> diseasesAnimalsNamesList;

    public diseasesAnimalsNamesAdapter(Context mContext, List<diseasesAnimalsNamesModel> diseasesAnimalsNamesList) {
        this.mContext = mContext;
        this.diseasesAnimalsNamesList = diseasesAnimalsNamesList;
    }

    @NonNull
    @Override
    public modelDCAViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_diseases_category_layout,viewGroup,false);
        modelDCAViewHolder modelDCAViewHolderobj=new modelDCAViewHolder(view);
        return modelDCAViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelDCAViewHolder holder, int i) {

        holder.AllNamesAnimals.setText(diseasesAnimalsNamesList.get(i).getName());
        final int id=diseasesAnimalsNamesList.get(i).getId();
        holder.AllNamesAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToAnimalsDisesesList(id);
            }
        });

    }

    private void SendUserToAnimalsDisesesList(int id) {
        //Toast.makeText(mContext.getApplicationContext(), ""+id, Toast.LENGTH_SHORT).show();
        Intent NextAnimalsDiseases=new Intent(mContext, diseasesAnimalListActivity.class);
        NextAnimalsDiseases.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NextAnimalsDiseases.putExtra("animalnameKey",id);
        mContext.startActivity(NextAnimalsDiseases);
    }

    @Override
    public int getItemCount() {
        return diseasesAnimalsNamesList.size();
    }

    public class modelDCAViewHolder extends RecyclerView.ViewHolder{

        TextView AllNamesAnimals;

    public modelDCAViewHolder(@NonNull View itemView) {
        super(itemView);
        AllNamesAnimals=itemView.findViewById(R.id.diseases_animal_name);
    }
}
}
