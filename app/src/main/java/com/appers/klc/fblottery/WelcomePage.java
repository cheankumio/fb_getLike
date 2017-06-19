package com.appers.klc.fblottery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.appers.klc.fblottery.Functions.SendRequest;
import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import es.dmoral.toasty.Toasty;
import io.fabric.sdk.android.Fabric;

import static com.appers.klc.fblottery.MainActivity.accessToken;

/**
 * Created by c1103304 on 2017/4/24.
 */

public class WelcomePage extends AppCompatActivity {
    CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(com.appers.klc.fblottery.R.layout.welcome_page);

        getSupportActionBar().hide();

        init();

        if(accessToken!=null){
            Log.d("MYLOG",accessToken.toString());
            toNextActivity();
        }else{
            Log.d("MYLOG","accessToken is null");
        }
    }

    private void toNextActivity() {
        Intent in = new Intent();
        in.setClass(this,MainActivity.class);
        startActivity(in);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

    private void init() {
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(com.appers.klc.fblottery.R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toasty.success(WelcomePage.this,"登入成功",Toast.LENGTH_SHORT,true).show();
                toNextActivity();
            }

            @Override
            public void onCancel() {
                Toasty.warning(WelcomePage.this,"登入失敗",Toast.LENGTH_SHORT,true).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toasty.error(WelcomePage.this,"ERROR",Toast.LENGTH_SHORT,true).show();
            }
        });
        SendRequest.accessTokenTracker = new AccessTokenTracker() {
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
    }
}
