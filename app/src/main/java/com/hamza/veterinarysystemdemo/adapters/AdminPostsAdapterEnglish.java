package com.hamza.veterinarysystemdemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.CommentsActivity.CommentsActivity;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.models.AdminPostsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminPostsAdapterEnglish extends RecyclerView.Adapter<AdminPostsAdapterEnglish.modelViewHolder> {
    Context mContext;
    List<AdminPostsModel> postsListAdmin;

    public AdminPostsAdapterEnglish(Context mContext, List<AdminPostsModel> postsListAdmin) {
        this.mContext = mContext;
        this.postsListAdmin = postsListAdmin;
    }

    @NonNull
    @Override
    public AdminPostsAdapterEnglish.modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.admin_posts_layout_english, viewGroup, false);
        modelViewHolder modelViewHolderobj = new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {

        holder.nameP.setText(postsListAdmin.get(i).getName());
        holder.dateP.setText(" "+postsListAdmin.get(i).getDate()+" ");
        holder.timeP.setText(postsListAdmin.get(i).getTime());
        holder.desciptionP.setText(postsListAdmin.get(i).getDescription());
        String image = postsListAdmin.get(i).getImage();
        if (TextUtils.isEmpty(image)) {
            holder.imageP.setVisibility(View.GONE);
        } else {
            Picasso.get().load(postsListAdmin.get(i).getImage()).into(holder.imageP);
        }
        Picasso.get().load(postsListAdmin.get(i).getProfileimagepost()).placeholder(R.drawable.navheaderprofilepicicon).into(holder.profilepicCirculview);
        final int id = postsListAdmin.get(i).getId();

        holder.commentsP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToCommentsActivity(id);
            }
        });


    }

    @Override
    public int getItemCount() {
        return postsListAdmin.size();
    }

    private void SendUserToCommentsActivity(int id) {
        Intent commentsActivityIntent = new Intent(mContext.getApplicationContext(), CommentsActivity.class);
        commentsActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        commentsActivityIntent.putExtra("postid", id);
        mContext.startActivity(commentsActivityIntent);
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        TextView nameP, timeP, dateP, desciptionP, commentsP;
        CircleImageView profilepicCirculview;
        ImageView imageP;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);

            nameP = itemView.findViewById(R.id.post_admin_name);
            timeP = itemView.findViewById(R.id.post_admin_time);
            dateP = itemView.findViewById(R.id.post_admin_date);
            desciptionP = itemView.findViewById(R.id.post_admin_discription);
            imageP = itemView.findViewById(R.id.post_admin_image_description);
            profilepicCirculview = itemView.findViewById(R.id.post_admin_image);
            commentsP = itemView.findViewById(R.id.comment_admin_button);

        }
    }

}
