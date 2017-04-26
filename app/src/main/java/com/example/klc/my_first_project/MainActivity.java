package com.example.klc.my_first_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.klc.my_first_project.Functions.SendRequest;
import com.example.klc.my_first_project.Object.FeedPost;
import com.example.klc.my_first_project.Object.JSONObjectList;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

import static com.example.klc.my_first_project.Functions.SendRequest.boolean_post;
import static com.example.klc.my_first_project.Functions.SendRequest.pageid;

public class MainActivity extends AppCompatActivity{

    AccessTokenTracker accessTokenTracker;
    public static AccessToken accessToken;
    Button choosePostBtn;
    public static ProgressDialog pd;
    public List<FeedPost> feedPost;
    int data_limit = 100;
    EditText edit;
    RequestQueue mQueue;
    Button buttonChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mQueue = Volley.newRequestQueue(this);
        init();
    }

    private void init() {
        edit = (EditText)findViewById(R.id.editText);
        accessTokenTracker = SendRequest.accessTokenTracker;
        feedPost = new ArrayList<>();
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

    }

    public void getPostInfo(){
        if(pageid.equals("xxxxxxxxxxxxxxxxxxxx")==false){
            boolean_post = false;
            JSONObjectList.FeedPostDetialList.clear();
            SendRequest.limit_counts = 1;

            SendRequest.getAllPosted(pageid, accessToken, data_limit, "");

            final Handler hn2 = new Handler();
            hn2.post(new Runnable() {
                @Override
                public void run() {
                    if (boolean_post) {
                        pd.dismiss();
                        Intent in = new Intent();
                        in.setClass(MainActivity.this, PostListView.class);
                        startActivity(in);
                        hn2.removeCallbacks(this);
                    } else {
                        hn2.postDelayed(this, 1000);
                    }
                }
            });
        }else{
            pd.dismiss();
            pd.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    public void getGroupID(View v){
        pageid="";
        String userinput = edit.getText().toString();
        String url="";
        if(isInteger(userinput)){
            Log.d("MYLOG", "isInteger");
            url = userinput;
        }else{
            Log.d("MYLOG", "not Integer");
            if(userinput.contains("facebook.com/")) {
                Log.d("MYLOG", "is Facebook");
                url = userinput.split("facebook.com/")[1].split("/")[0];
            }else{
                Log.d("MYLOG", "not Facebook");
                url = userinput;
            }
        }

        Log.d("MYLOG", "取得URL: "+url);
        if(url.length()>1) {
            SendRequest.getPageID(this,accessToken, url);
            Log.d("MYLOG", "跳出dialog");
            pd = new ProgressDialog(this);
            pd.setTitle("請稍候");
            pd.setMessage("資料讀取中");
            pd.show();
            final Handler hn = new Handler();
            hn.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("MYLOG", "pageid != null");
                    if (pageid.length() > 5) {
                        getPostInfo();
                        hn.removeCallbacks(this);
                        Log.d("MYLOG", "取得 pageid");
                    } else {
                        Log.d("MYLOG", "handle 持續執行中");
                        hn.postDelayed(this, 1000);
                    }
                }
            });
        }else{
            Toasty.error(this, "資料格式錯誤.", Toast.LENGTH_SHORT, true).show();
        }
    }

    public void showAnimation(){
        TranslateAnimation ta = new TranslateAnimation(0,0,-500,0);
        ta.setDuration(1000);
        ta.setInterpolator(new AccelerateInterpolator(1.0f));
        buttonChoose.startAnimation(ta);
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public void EdittextClear(View v){
        edit.setText("");
    }
}
