package com.example.klc.my_first_project;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makeramen.roundedimageview.RoundedImageView;

import static com.example.klc.my_first_project.Functions.SendRequest.boolean_picture;

/**
 * Created by c1103304 on 2017/4/26.
 */

public class LuckActivity extends AppCompatActivity {
    TextView title;
    RoundedImageView pic;
    String pictureUrl ;
    String luckmansName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);
        getSupportActionBar().hide();

        init();

        final Handler hn = new Handler();
        hn.post(new Runnable() {
            @Override
            public void run() {
                if(boolean_picture==true){
                    LoadPic();
                    hn.removeCallbacks(this);
                }else{
                    hn.postDelayed(this,1000);
                }
            }
        });


    }

    private void LoadPic() {
        title.setText(luckmansName);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        ImageRequest getRequest = new ImageRequest(pictureUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap s) {
                        pic.setImageBitmap(s);
                    }
                }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pic.setImageResource(R.drawable.ic_error_outline_white_48dp);
            }
        });
        mQueue.add(getRequest);
    }

    private void init() {
        title = (TextView)findViewById(R.id.textView5) ;
        pic = (RoundedImageView)findViewById(R.id.imageView);
        pictureUrl = Lottery_Activity.pictureUrl;
        luckmansName = Lottery_Activity.luckmansName;
    }

    public void exits(View v){
        finish();
    }
}
