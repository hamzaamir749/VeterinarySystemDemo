package com.hamza.veterinarysystemdemo.CommentsActivity;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentsAdapter extends RecyclerView.Adapter<commentsAdapter.modelViewHolder> {

    Context mContext;

    List<commentsModel> commentsListadapter;

    public commentsAdapter(Context mContext, List<commentsModel> commentsListadapter) {
        this.mContext = mContext;
        this.commentsListadapter = commentsListadapter;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.acitivity_comments_layout,viewGroup,false);
        modelViewHolder modelViewHolderobj=new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {

        holder.cName.setText(commentsListadapter.get(i).getName());
        holder.cDate.setText(commentsListadapter.get(i).getDate());
        holder.cDes.setText(commentsListadapter.get(i).getDes());
        holder.cTime.setText(commentsListadapter.get(i).getTime());

        Picasso.get().load(commentsListadapter.get(i).getProfilepiccomment()).into(holder.cProfilePic);
    }

    @Override
    public int getItemCount() {
        return commentsListadapter.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        CircleImageView cProfilePic;
        TextView cTime,cDate,cName,cDes;
        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            cProfilePic=itemView.findViewById(R.id.comment_profile_pic);
            cTime=itemView.findViewById(R.id.comment_time);
            cDate=itemView.findViewById(R.id.comment_date);
            cName=itemView.findViewById(R.id.comment_user_name);
            cDes=itemView.findViewById(R.id.comment_text);
        }
    }
}
