package com.example.klc.my_first_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.klc.my_first_project.Functions.SendRequest;
import com.example.klc.my_first_project.Object.FeedPost;
import com.example.klc.my_first_project.Object.JSONObjectList;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity{

    AccessTokenTracker accessTokenTracker;
    public static AccessToken accessToken;
    Button choosePostBtn;
    public List<FeedPost> feedPost;
    int data_limit = 50;
    EditText edit;
    RequestQueue mQueue;
    Button buttonChoose;
    String pageid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        init();
    }

    private void init() {

        edit = (EditText)findViewById(R.id.editText);
        buttonChoose = (Button)findViewById(R.id.button2);
        accessTokenTracker = SendRequest.accessTokenTracker;
        feedPost = new ArrayList<>();
        choosePostBtn = (Button)findViewById(R.id.button2);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();


    }

    public void choosePost(View v){
        SendRequest.boolean_post=false;
        JSONObjectList.FeedPostDetialList.clear();
        SendRequest.limit_counts = 0;
        SendRequest.boolean_post =false;
        SendRequest.getAllPosted(pageid,accessToken,data_limit,"");
        JSONObjectList.PostLikeDetialList = new ArrayList<>();
        JSONObjectList.PostCommentsDetialList = new ArrayList<>();
        JSONObjectList.PostSharedDetialList = new ArrayList<>();
        Intent in = new Intent();
        in.setClass(this,PostListView.class);
        startActivity(in);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    public void getGroupID(View v){
        String url = edit.getText().toString().split("facebook.com/")[1];
        SendRequest.getPageID(accessToken,url);


    }

    public void showAnimation(){
        TranslateAnimation ta = new TranslateAnimation(0,0,-500,0);
        ta.setDuration(1000);
        ta.setInterpolator(new AccelerateInterpolator(1.0f));
        buttonChoose.startAnimation(ta);
    }
}
