package com.example.klc.my_first_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    LoginResult loginResult;
    Button choosePostBtn;
    public List<FeedPost> feedPost;
    int data_limit = 50;
    TextView info_count;
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
        info_count = (TextView)findViewById(R.id.textView);
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
                    final String postid = "/"+data.getExtras().getString("contentID");
                    Log.d("MYLOG",postid);
                    choosePostBtn.setText(postid);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getAllInformation(accessToken,postid);
                        }
                    }).start();
                }else{
                    Toast.makeText(this, "請選擇貼文", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getAllInformation(final AccessToken Token, final String contentID) {
        Thread TD = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    SendRequest.getlikeUser(Token, "", contentID);
                    SendRequest.getcommentsUser(Token, "", contentID);
                    SendRequest.getShareUser(Token, "", contentID);
                    Log.d("MYLOG","讀取資料中..");
                }
                synchronized (this){
                    Log.d("MYLOG","取得數量中..");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        info_count.setText("like: " + JSONObjectList.PostLikeDetialList.size() +
                                "  comments: " + JSONObjectList.PostCommentsDetialList.size() +
                                "  sharedpost: " + JSONObjectList.PostSharedDetialList.size());
                    }
                });
            }
            }
        });
        TD.start();
    }




    public void choosePost(View v){
        JSONObjectList.PostLikeDetialList = new ArrayList<>();
        JSONObjectList.PostCommentsDetialList = new ArrayList<>();
        JSONObjectList.PostSharedDetialList = new ArrayList<>();
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
