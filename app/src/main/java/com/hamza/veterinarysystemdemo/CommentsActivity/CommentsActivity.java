package com.hamza.veterinarysystemdemo.CommentsActivity;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamza.veterinarysystemdemo.ClientSessionDetails;
import com.hamza.veterinarysystemdemo.IPADDRESS;
import com.hamza.veterinarysystemdemo.R;
import com.hamza.veterinarysystemdemo.UserSessionManager;
import com.hamza.veterinarysystemdemo.adapters.commentsAdapter;
import com.hamza.veterinarysystemdemo.adapters.userProblemsAdapter;
import com.hamza.veterinarysystemdemo.models.commentsModel;
import com.hamza.veterinarysystemdemo.models.userProblemsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {
    RecyclerView commentsRecycler;
    Toolbar mToolbar;
    LinearLayoutManager linearLayoutManager;
    List<commentsModel> allCommentsList;
    commentsAdapter cAddapter;
    Context context;
    String getComments_URL,postComments_url;
    String postId;
    private UserSessionManager userSessionManager;
    ClientSessionDetails clientSessionDetails;
    private String saveCurrentTime,saveCurrentDate,currentUserid,usernamecomment,userProfilecomment;

   private EditText commentText;
   private ImageView putComment;
   IPADDRESS ipaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        ipaddress=new IPADDRESS();
        String ip=ipaddress.getIpaddress();
        getComments_URL="http://www.veterinarysystem.ga/getcomments.php";
        postComments_url="http://www.veterinarysystem.ga/postcomments.php";

        postId=getIntent().getExtras().get("postid").toString();


        commentText=findViewById(R.id.commentInput);
        putComment=findViewById(R.id.btn_comment_post);
        commentsRecycler=findViewById(R.id.commentRecycler);
        context=this;
        allCommentsList=new ArrayList<>();
        linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(linearLayoutManager);
        userSessionManager=new UserSessionManager(this);
        clientSessionDetails=userSessionManager.getClientDataDetails();

        usernamecomment=clientSessionDetails.getName();
        userProfilecomment=clientSessionDetails.getProfilepicture();
        mToolbar= findViewById(R.id.commentsToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Comments");
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
        getComments();

        putComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentTextDes=commentText.getText().toString();
                if (TextUtils.isEmpty(commentTextDes))
                {
                    commentText.setError("Please Enter Here...");
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
                            cAddapter=new commentsAdapter(context,allCommentsList);

                        }

                        commentsRecycler.setAdapter(cAddapter);

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Could not get post data", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"parsing exception: "+e.getMessage(),Toast.LENGTH_LONG).show();
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
        StringRequest postCommentsRequest=new StringRequest(Request.Method.POST, postComments_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean status=jsonObject.getBoolean("status");
                    if (status){
                        allCommentsList.clear();
                        commentText.setText("");
                        getComments();

                        Toast.makeText(getApplicationContext(), "Comment Posted....", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Comment not Posted....", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e)
                {
                 //   Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
