package com.hamza.veterinarysystemdemo.AdminPostsActivity;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hamza.veterinarysystemdemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminPostsAdapterUrdu extends RecyclerView.Adapter<AdminPostsAdapterUrdu.modelViewHolder> {
    Context mContext;
    List<AdminPostsModel> postsListAdmin;

    public AdminPostsAdapterUrdu(Context mContext, List<AdminPostsModel> postsListAdmin) {
        this.mContext = mContext;
        this.postsListAdmin = postsListAdmin;
    }

    @NonNull
    @Override
    public AdminPostsAdapterUrdu.modelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.admin_posts_layout_urdu, viewGroup, false);
        modelViewHolder modelViewHolderobj = new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPostsAdapterUrdu.modelViewHolder holder, int i) {

        holder.nameP.setText(postsListAdmin.get(i).getName()+" ");
        holder.dateP.setText(postsListAdmin.get(i).getDate()+" ");
        holder.timeP.setText(" "+postsListAdmin.get(i).getTime());
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
        Intent commentsActivityIntent = new Intent(mContext.getApplicationContext(), AdminPostCommentsActivity.class);
        commentsActivityIntent.putExtra("postid", id);
        mContext.startActivity(commentsActivityIntent);
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {

        TextView nameP, timeP, dateP, desciptionP, commentsP;
        CircleImageView profilepicCirculview;
        ImageView imageP;


        public modelViewHolder(@NonNull View itemView) {
            super(itemView);

            nameP = itemView.findViewById(R.id.post_admin_name_urdu);
            timeP = itemView.findViewById(R.id.post_admin_time_urdu);
            dateP = itemView.findViewById(R.id.post_admin_date_urdu);
            desciptionP = itemView.findViewById(R.id.post_admin_discription_urdu);
            imageP = itemView.findViewById(R.id.post_admin_image_description_urdu);
            profilepicCirculview = itemView.findViewById(R.id.post_admin_image_urdu);
            commentsP = itemView.findViewById(R.id.comment_admin_button_urdu);


        }
    }
}
