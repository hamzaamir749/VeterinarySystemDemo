package com.hamza.veterinarysystemdemo.CommentsActivity;

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
import com.hamza.veterinarysystemdemo.Session.SessionDetails;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.Session.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
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
    private String lang,saveCurrentTime,saveCurrentDate,currentUserid,usernamecomment,userProfilecomment;

   private EditText commentText;
   private ImageView putComment;
   IPADDRESS ipaddress;
   ProgressBar progressBar;
   boolean isUrdu=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        mToolbar= findViewById(R.id.commentsToolbar);
        commentText=findViewById(R.id.commentInput);
        putComment=findViewById(R.id.btn_comment_post);
        commentsRecycler=findViewById(R.id.commentRecycler);
        progressBar=findViewById(R.id.com_loading);
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
        progressDialog=new ProgressDialog(this);
        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        //getComments_URL="http://www.veterinarysystem.ga/getcomments.php";
        //postComments_url="http://www.veterinarysystem.ga/postcomments.php";
        getComments_URL="http://" + ip + "/VeterinarySystem/getcomments.php";
        postComments_url="http://" + ip + "/VeterinarySystem/postcomments.php";

        postId=getIntent().getExtras().get("postid").toString();


        // SharedPreferences sharedPreferences = getSharedPreferences("langSetting", Activity.MODE_PRIVATE);
       // lang = sharedPreferences.getString("my_lang", "");

        postId=getIntent().getExtras().get("postid").toString();

        context=this;
        allCommentsList=new ArrayList<>();
        linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(linearLayoutManager);
        userSessionManager=new UserSessionManager(this);
        sessionDetails =userSessionManager.getSessionDetails();

        usernamecomment= sessionDetails.getName();
        userProfilecomment= sessionDetails.getProfilepicture();
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

    private void getComments() {

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
                            String cname=object.getString("cname");
                            String cdes=object.getString("cdescription");
                            String cpimage=object.getString("cprofileimage");
                            String ctime=object.getString("ctime");
                            String cdate=object.getString("cdate");
                            commentsModel commentsModelobj=new commentsModel(cname,cdate,cdes,ctime,cpimage);
                            allCommentsList.add(commentsModelobj);


                        }
                        progressBar.setVisibility(View.GONE);
                        cAddapter=new commentsAdapter(context,allCommentsList);
                        commentsRecycler.setAdapter(cAddapter);

                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.badWordsUsed), Toast.LENGTH_SHORT).show();
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
               // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void postComment(final String commentTextDes)
    {
        progressDialog.setTitle(getResources().getString(R.string.comments));
        progressDialog.setMessage(getResources().getString(R.string.pleaseWait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.dismiss();

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:aa");
        saveCurrentTime = currentTime.format(calFordTime.getTime());
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

                        //Toast.makeText(getApplicationContext(), "Comment Posted....", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.badWordsUsed), Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e)
                {
                 //   Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
               // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
