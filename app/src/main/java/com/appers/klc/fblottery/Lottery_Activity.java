package com.appers.klc.fblottery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appers.klc.fblottery.Functions.SendRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;
import es.dmoral.toasty.Toasty;

import static com.appers.klc.fblottery.Functions.SendRequest.boolean_picture;
import static java.lang.Thread.sleep;

/**
 * Created by c1103304 on 2017/4/24.
 */

public class Lottery_Activity extends AppCompatActivity implements NumberPickerView.OnScrollListener, NumberPickerView.OnValueChangeListener,
        NumberPickerView.OnValueChangeListenerInScrolling{
    public static String pictureUrl;
    public static String luckmansName;
    Handler roll_view;
    NumberPickerView picker;
    String[] display;
    int timer;
    int counts = 1;
    boolean isRoll = false;
    TextView title;
    TextView info;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.appers.klc.fblottery.R.layout.lottery_layout);

        getSupportActionBar().hide();

        Intent getArray = getIntent();
        display = getArray.getStringArrayExtra("lotteryObject");
        picker = (NumberPickerView) findViewById(com.appers.klc.fblottery.R.id.picker);
        title = (TextView)findViewById(com.appers.klc.fblottery.R.id.textView4);
        info = (TextView)findViewById(com.appers.klc.fblottery.R.id.textView7);
        title.setText(getArray.getStringExtra("title"));
        picker.setOnScrollListener(this);
        picker.setOnValueChangedListener(this);
        picker.setOnValueChangeListenerInScrolling(this);
        //picker.refreshByNewDisplayedValues(display);
        roll_view = new Handler();

    }

    @Override
    public void onScrollStateChange(NumberPickerView view, int scrollState) {

    }

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {

    }

    @Override
    public void onValueChangeInScrolling(NumberPickerView picker, int oldVal, int newVal) {

    }

    Runnable roll = new Runnable() {
        @Override
        public void run() {
            if(timer>0) {
                isRoll = true;
                int pass = display.length>15? display.length/10:1;
                Random random = new Random();
                if(timer>500) {
                    picker.smoothScrollToValue(picker.getValue(), picker.getValue() + pass);
                }else{
                    picker.smoothScrollToValue(picker.getValue(), picker.getValue() + 1);
                }
                timer-= random.nextInt(50)+75;
                roll_view.postDelayed(roll, timer>1000?100:1000-timer);
            }else{
                roll_view.removeCallbacks(roll);
                // 轉盤結束
                String[] content = picker.getDisplayedValues();
                String luckman = content[picker.getValue() - picker.getMinValue()];
                luckmansName = luckman.split("/")[0];
                Log.d("MYLOG",luckman);
                boolean_picture = false;
                SendRequest.getLuckerPicture(MainActivity.accessToken,luckman.split("/")[1]);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent forResult = new Intent();
                forResult.setClass(Lottery_Activity.this,LuckActivity.class);
                startActivity(forResult);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                if(SetDisplayData_Activity.removeLuckyMan.isChecked()) {
                    if (SetDisplayData_Activity.commants.isChecked())
                        SetDisplayData_Activity.removeLucker(luckman, SetDisplayData_Activity.commants_name);
                    if (SetDisplayData_Activity.likes.isChecked())
                        SetDisplayData_Activity.removeLucker(luckman, SetDisplayData_Activity.likes_name);
                    if (SetDisplayData_Activity.sharedposts.isChecked())
                        SetDisplayData_Activity.removeLucker(luckman, SetDisplayData_Activity.sharedpost_name);
                    display = removeString(display, luckman);
                }
                isRoll = false;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        picker.refreshByNewDisplayedValues(display);
        info.setText("參與抽獎人數: "+display.length);
    }

    public void rollbtn(View v){
        if(isRoll == true){
            Toasty.info(this,"請等待轉盤結束", Toast.LENGTH_LONG,true).show();
        }else {
            counts = 1;
            Random random2 = new Random();
            timer = display.length < 3000 ? random2.nextInt(2000) + 3000 : random2.nextInt(2000) + timer;
            roll_view.post(roll);
        }
    }

    public String[] removeString(String[] StrArray,String str){
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(StrArray));
        SetDisplayData_Activity.removeLucker(str,list);
        return list.toArray(new String[0]);
    }
}
