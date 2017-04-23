package com.example.klc.my_first_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.klc.my_first_project.Functions.SendRequest;
import com.example.klc.my_first_project.Object.FeedPost;
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

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    LoginResult loginResult;
    Button choosePostBtn;
    public List<FeedPost> feedPost;
    int data_limit = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();



        if(accessToken!=null){
            Log.d("MYLOG",accessToken.toString());
            sendpost();
        }else{
            Log.d("MYLOG","accessToken is null");
        }
    }

    private void sendpost() {
        SendRequest.getAllPosted("",accessToken,data_limit,"");
    }




    private void init() {
        feedPost = new ArrayList<>();
        choosePostBtn = (Button)findViewById(R.id.button2);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "登入失敗", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 700:
                if(resultCode==RESULT_OK) {
                    String postid = "/"+data.getExtras().getString("contentID");
                    choosePostBtn.setText(postid);
                    getAllInformation(accessToken,postid);
                }else{
                    Toast.makeText(this, "請選擇貼文", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getAllInformation(AccessToken Token, String contentID) {
        SendRequest.getlikeUser(Token,"",contentID);
        SendRequest.getcommentsUser(Token,"",contentID);
        SendRequest.getShareUser(Token,"",contentID);
    }




    public void choosePost(View v){
        Intent in = new Intent();
        in.setClass(this,PostListView.class);
        startActivityForResult(in,700);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

}