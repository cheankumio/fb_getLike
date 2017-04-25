package com.example.klc.my_first_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Random;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Created by c1103304 on 2017/4/24.
 */

public class Lottery_Activity extends AppCompatActivity implements NumberPickerView.OnScrollListener, NumberPickerView.OnValueChangeListener,
        NumberPickerView.OnValueChangeListenerInScrolling{

    Handler roll_view;
    NumberPickerView picker;
    String[] display;
    int timer;
    int counts = 1;
    TextView title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_layout);

        getSupportActionBar().hide();

        Intent getArray = getIntent();
        display = getArray.getStringArrayExtra("lotteryObject");
        picker = (NumberPickerView) findViewById(R.id.picker);
        title = (TextView)findViewById(R.id.textView4);
        title.setText(getArray.getStringExtra("title"));
        picker.setOnScrollListener(this);
        picker.setOnValueChangedListener(this);
        picker.setOnValueChangeListenerInScrolling(this);
        picker.refreshByNewDisplayedValues(display);
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
            }
        }
    };

    public void rollbtn(View v){
        counts = 1;
        Random random2 = new Random();
        timer = display.length < 3000 ? random2.nextInt(2000)+3000:random2.nextInt(2000)+timer;
        roll_view.post(roll);
    }
}
