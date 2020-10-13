package com.hamza.veterinarysystemdemo.AdminPostsActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.hamza.veterinarysystemdemo.CommentsActivity.commentsAdapter;
import com.hamza.veterinarysystemdemo.CommentsActivity.commentsModel;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminPostCommentsActivity extends AppCompatActivity {
    RecyclerView commentsRecycler;
    Toolbar mToolbar;
    LinearLayoutManager linearLayoutManager;
    List<commentsModel> allCommentsList;
    commentsAdapter cAddapter;
    Context context;
    String getComments_URL,postComments_url;
    String postId;
    private UserSessionManager userSessionManager;
    SessionDetails sessionDetails;
    private String saveCurrentTime,saveCurrentDate,currentUserid,usernamecomment,userProfilecomment;

    private EditText commentText;
    private ImageView putComment;
    IPADDRESS ipaddress;
    ProgressDialog progressDialog;
    String lang;
    ProgressBar progressBar;
    boolean isUrdu=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_post_comments);
        progressBar=findViewById(R.id.comadmin_loading);
        commentText=findViewById(R.id.admincommentInput);
        putComment=findViewById(R.id.btn_admin_comment_post);
        commentsRecycler=findViewById(R.id.admincommentRecycler);
        mToolbar= findViewById(R.id.admincommentsToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.commentstool);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        else
        {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        //getComments_URL="http://www.veterinarysystem.ga/getcomments.php";
        //postComments_url="http://www.veterinarysystem.ga/postcomments.php";
        getComments_URL="http://" + ip + "/VeterinarySystem/getAdminComments.php";
        postComments_url="http://" + ip + "/VeterinarySystem/postAdminComments.php";

        //SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        //lang = sharedPreferences.getString("my_lang", "");

        postId=getIntent().getExtras().get("postid").toString();



        context=this;
        allCommentsList=new ArrayList<>();
        allCommentsList.clear();
        linearLayoutManager =new LinearLayoutManager(this);
        progressDialog=new ProgressDialog(this);
        linearLayoutManager.setStackFromEnd(true);
        commentsRecycler.setLayoutManager(linearLayoutManager);
        userSessionManager=new UserSessionManager(this);
        sessionDetails =userSessionManager.getSessionDetails();

        usernamecomment= sessionDetails.getName();
        userProfilecomment= sessionDetails.getProfilepicture();
        currentUserid=String.valueOf(sessionDetails.getId());

        progressBar.setIndeterminateDrawable(new Circle());
        getComments();

        putComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentTextDes=commentText.getText().toString();
                if (TextUtils.isEmpty(commentTextDes))
                {
                    commentText.setError(getResources().getString(R.string.pleaseenterhere));
                }
                else
                {
                    //   allCommentsList.clear();
                    postComment(commentTextDes);

                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
        lang = sharedPreferences.getString("my_lang", "en");
        if (lang.equals("ur"))
        {
            putComment.setImageDrawable(getResources().getDrawable(R.drawable.sendiconur));
        }
        else
        {
            putComment.setImageDrawable(getResources().getDrawable(R.drawable.sendiconen));
        }

    }
    private void getComments() {
        allCommentsList.clear();
        StringRequest getCommentsRequest=new StringRequest(Request.Method.POST, getComments_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("commentsdata");
                    boolean status = jsonObject.getBoolean("status");
                    if (status)
                    {
                        for (int i=0; i<jsonArray.length();i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            String cname=object.getString("comment_author");
                            String cdes=object.getString("comment");
                            String cpimage=object.getString("image");
                            String ctime=object.getString("createtime");
                            String cdate=object.getString("createdate");
                            commentsModel commentsModelobj=new commentsModel(cname,cdate,cdes,ctime,cpimage);
                            allCommentsList.add(commentsModelobj);


                        }
                        progressBar.setVisibility(View.GONE);
                        cAddapter=new commentsAdapter(context,allCommentsList);
                        commentsRecycler.setAdapter(cAddapter);

                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                       // Toast.makeText(getApplicationContext(), getResources().getString(R.string.nocomments), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e)
                {
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(),"parsing exception: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> getMap=new HashMap<>();
                getMap.put("postidd",postId);
                return getMap;
            }
        };
        Volley.newRequestQueue(this).add(getCommentsRequest);

    }

    private void postComment(final String commentTextDes)
    {

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordTime.getTime());
        progressDialog.setTitle(getResources().getString(R.string.commenting));
        progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest postCommentsRequest=new StringRequest(Request.Method.POST, postComments_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean status=jsonObject.getBoolean("status");
                    if (status){
                        allCommentsList.clear();
                        commentText.setText("");
                        getComments();

                      //  Toast.makeText(getApplicationContext(), "Comment Posted....", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.badWordsUsed), Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e)
                {
                     //oast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                 //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> postCommentMap=new HashMap<>();
                postCommentMap.put("postidd",postId);
                postCommentMap.put("cnamee",usernamecomment);
                postCommentMap.put("cdescriptionn",commentTextDes);
                postCommentMap.put("ctimee",saveCurrentTime);
                postCommentMap.put("cdatee",saveCurrentDate);
                postCommentMap.put("cprofileimagee",userProfilecomment);
                postCommentMap.put("cuserid",currentUserid);
                return postCommentMap;
            }
        };
        Volley.newRequestQueue(this).add(postCommentsRequest);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
