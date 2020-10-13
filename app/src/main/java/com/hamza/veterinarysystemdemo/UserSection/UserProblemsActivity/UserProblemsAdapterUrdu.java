package com.hamza.veterinarysystemdemo.UserSection.UserProblemsActivity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamza.veterinarysystemdemo.CommentsActivity.CommentsActivity;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.models.userProblemsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProblemsAdapterUrdu extends RecyclerView.Adapter<UserProblemsAdapterUrdu.modelViewHolder> implements Filterable {
    public List<userProblemsModel> userproblemsList;
    public List<userProblemsModel> filterlist;
    UserProblemsFilterUrdu filter;
    Context mContext;

    public UserProblemsAdapterUrdu(List<userProblemsModel> userproblemsList, Context mContext) {
        this.userproblemsList = userproblemsList;
        this.filterlist = userproblemsList;
        this.mContext = mContext;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter=new UserProblemsFilterUrdu(filterlist,this);
        }
        return filter;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.activity_user_problems_layout_urdu, parent, false);
        modelViewHolder modelViewHolderobj = new modelViewHolder(view);
        return modelViewHolderobj;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int i) {
        holder.nameP.setText(userproblemsList.get(i).getName());
        holder.dateP.setText(userproblemsList.get(i).getDate());
        holder.timeP.setText(userproblemsList.get(i).getTime());
        holder.desciptionP.setText(userproblemsList.get(i).getDescription());
        String image = userproblemsList.get(i).getImage();
        if (TextUtils.isEmpty(image)) {
            holder.imageP.setVisibility(View.GONE);
        } else {
            Picasso.get().load(userproblemsList.get(i).getImage()).into(holder.imageP);
        }
        Picasso.get().load(userproblemsList.get(i).getProfileimagepost()).into(holder.profilepicCirculview);
        final int id = userproblemsList.get(i).getId();

        holder.commentsP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToCommentsActivity(id);
            }
        });
    }
    private void SendUserToCommentsActivity(int id) {
        Intent commentsActivityIntent = new Intent(mContext.getApplicationContext(), CommentsActivity.class);
        commentsActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        commentsActivityIntent.putExtra("postid", id);
        mContext.startActivity(commentsActivityIntent);
    }

    @Override
    public int getItemCount() {
        return userproblemsList.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {

        TextView nameP, timeP, dateP, desciptionP, commentsP;
        CircleImageView profilepicCirculview;
        ImageView imageP;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            nameP = itemView.findViewById(R.id.post_user_name_ur);
            timeP = itemView.findViewById(R.id.post_time_ur);
            dateP = itemView.findViewById(R.id.post_date_ur);
            desciptionP = itemView.findViewById(R.id.post_discription_ur);
            imageP = itemView.findViewById(R.id.post_image_ur);
            profilepicCirculview = itemView.findViewById(R.id.post_User_image_ur);
            commentsP = itemView.findViewById(R.id.comment_button_ur);
        }
    }

}
