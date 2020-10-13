package com.hamza.veterinarysystemdemo.UserSection.Growth;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;

import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class GrowthAdapterClass extends RecyclerView.Adapter<GrowthAdapterClass.modelViewHolder> {
    Context context;
    List<GrowthModelClass> list;
    PrettyDialog prettyDialog;

    public GrowthAdapterClass(Context context, List<GrowthModelClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.activity_growth_animal_list_recycler_layout,viewGroup,false);
        modelViewHolder obj=new modelViewHolder(view);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {

        holder.name.setText(list.get(i).getName());

        final int type=list.get(i).getType();
        final int id=list.get(i).getId();
        prettyDialog=new PrettyDialog(context);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type==1)
                {
                    new PrettyDialog(context)
                            .setTitle(context.getResources().getString(R.string.PleaseSelect))
                            .setIcon(R.drawable.appplus).addButton(context.getResources().getString(R.string.IncreaseMilk), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            prettyDialog.dismiss();
                            SendUsertodescriptionActity("milk",id);
                        }
                    }).addButton(context.getResources().getString(R.string.IncreaseMeet), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            prettyDialog.dismiss();
                            SendUsertodescriptionActity("meet",id);
                        }
                    }).show();
                }
                else if (type==0)
                {
                    new PrettyDialog(context)
                            .setTitle(context.getResources().getString(R.string.PleaseSelect))
                            .setIcon(R.drawable.appplus).addButton(context.getResources().getString(R.string.IncreaseMeet), R.color.loginBackgroundcolor, R.color.design_default_color_primary_dark, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            prettyDialog.dismiss();
                            SendUsertodescriptionActity("meet",id);
                        }
                    }).show();
                }
            }
        });
    }

    private void SendUsertodescriptionActity(String type, int id) {
        Intent intent=new Intent(context.getApplicationContext(),GrowthDescriptionActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.growth_animal_list_name);
        }
    }
}
