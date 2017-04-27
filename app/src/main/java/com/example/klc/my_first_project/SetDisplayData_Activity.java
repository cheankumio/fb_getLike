package com.example.klc.my_first_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.klc.my_first_project.Functions.SendRequest;
import com.example.klc.my_first_project.Object.JSONObjectList;
import com.facebook.AccessToken;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static com.example.klc.my_first_project.Functions.SendRequest.getShareUser;
import static com.example.klc.my_first_project.Functions.SendRequest.getcommentsUser;
import static com.example.klc.my_first_project.Functions.SendRequest.getlikeUser;
import static com.example.klc.my_first_project.MainActivity.accessToken;

/**
 * Created by c1103304 on 2017/4/24.
 */

public class SetDisplayData_Activity extends AppCompatActivity {
    InterstitialAd mInterstitialAd;
    Handler watch_data;
    ProgressDialog pgd;
    TextView info_count;
    TextView filter_count;
    public static CheckBox likes,commants,sharedposts,commentsDuplicate,removeLuckyMan;
    List<String> newList;
    public static List<String> likes_name;
    public static List<String> commants_name;
    List<String> commants_name2;
    public static List<String> sharedpost_name;
    List<String> sendList;
    EditText title;
    int a=0,b=0,c=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaydata_layout);

        getSupportActionBar().hide();

        Intent getContentData = getIntent();
        String postid = "/"+getContentData.getExtras().getString("contentID");


        init();
        getAllInformation(accessToken,postid);
        adsShow();
        checkBoxFunction();
    }

    private void adsShow() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7003556787929258/9376652927");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();


    }

    private void checkBoxFunction() {
        title = (EditText)findViewById(R.id.editText2);
        likes.setOnCheckedChangeListener(check);
        commants.setOnCheckedChangeListener(check);
        sharedposts.setOnCheckedChangeListener(check);
        commentsDuplicate.setOnCheckedChangeListener(check);
        removeLuckyMan.setOnCheckedChangeListener(check);
    }

    CompoundButton.OnCheckedChangeListener check = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(commentsDuplicate.isChecked()){
                commants_name2 = removeDuplicate(commants_name);
            }else{
                commants_name2 = commants_name;
            }
            //Log.d("MYLOG","comments count: "+commants_name2.size()+" / "+commants_name.size());
            sendList = new ArrayList<>();

            a = likes.isChecked()?1:0;
            b = commants.isChecked()?2:0;
            c = sharedposts.isChecked()?4:0;
            int num = a+b+c;
            switch (num){
                case 1:
                    sendList = likes_name;
                    break;
                case 2:
                    sendList = commants_name2;
                    break;
                case 3:
                    sendList = intersect(likes_name,commants_name2);
                    break;
                case 4:
                    sendList = sharedpost_name;
                    break;
                case 5:
                    sendList = intersect(likes_name,sharedpost_name);
                    break;
                case 6:
                    sendList = intersect(commants_name2,sharedpost_name);
                    break;
                case 7:
                    sendList = intersect(likes_name,intersect(commants_name2,sharedpost_name));
                    break;
            }
            if(sendList.size()<1){
                if(num>0){
                    filter_count.setText("共有 " + sendList.size() + " 名粉絲");
                }else{
                    filter_count.setText("請選擇抽獎依據\n choose any option");
                }

            }else {
                filter_count.setText("共有 " + sendList.size() + " 名粉絲");
            }



        }
    };

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void init() {
        newList = new ArrayList<>();
        likes_name = new ArrayList<>();
        commants_name = new ArrayList<>();
        commants_name2 = new ArrayList<>();
        sharedpost_name = new ArrayList<>();
        likes = (CheckBox)findViewById(R.id.checkBox);
        commants = (CheckBox)findViewById(R.id.checkBox2);
        sharedposts = (CheckBox)findViewById(R.id.checkBox3);
        commentsDuplicate = (CheckBox)findViewById(R.id.checkBox4);
        removeLuckyMan = (CheckBox)findViewById(R.id.checkBox5);
        info_count = (TextView)findViewById(R.id.textView2);
        filter_count = (TextView)findViewById(R.id.textView3);
        watch_data = new Handler();
        pgd = new ProgressDialog(this);

    }


    private void getAllInformation(final AccessToken Token, final String contentID) {
        watch_data.postDelayed(runnable,1000);
        SendRequest.boolean_likes=false;
        SendRequest.boolean_commends=false;
        SendRequest.boolean_shared=false;
        JSONObjectList.PostLikeDetialList = new ArrayList<>();
        JSONObjectList.PostCommentsDetialList = new ArrayList<>();
        JSONObjectList.PostSharedDetialList = new ArrayList<>();
        pgd.setTitle("請稍候");
        pgd.setCancelable(false);
        pgd.setMessage("資料讀取中");
        pgd.show();
        getlikeUser(Token, "", contentID);
        getcommentsUser(Token, "", contentID);
        getShareUser(Token, "", contentID);

        Log.d("MYLOG","讀取資料中..");
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(SendRequest.boolean_likes && SendRequest.boolean_commends && SendRequest.boolean_shared){
                pgd.dismiss();
                info_count.setText("Likes: " + JSONObjectList.PostLikeDetialList.size() +
                        "  Commants: " + JSONObjectList.PostCommentsDetialList.size() +
                        "  Sharedpost: " + JSONObjectList.PostSharedDetialList.size()+
                        "  \n讚: " + JSONObjectList.PostLikeDetialList.size() +
                        "  留言: " + JSONObjectList.PostCommentsDetialList.size() +
                        "  分享: " + JSONObjectList.PostSharedDetialList.size()
                );
                watch_data.removeCallbacks(runnable);
                transAllData();
            }else{
                watch_data.postDelayed(runnable,1000);
            }
        }
    };

    private void transAllData() {
        for(int i=0;i<JSONObjectList.PostLikeDetialList.size();i++){
            String str = JSONObjectList.PostLikeDetialList.get(i).getName()+"/"+JSONObjectList.PostLikeDetialList.get(i).getId();
            likes_name.add(str);
            //Log.d("MYLOG","likes: "+str);
        }
        for(int i=0;i<JSONObjectList.PostCommentsDetialList.size();i++){
            String str = JSONObjectList.PostCommentsDetialList.get(i).getFrom().getName()+"/"+JSONObjectList.PostCommentsDetialList.get(i).getFrom().getId();
            commants_name.add(str);
            //Log.d("MYLOG","commants: "+str);
        }

        for(int i=0;i<JSONObjectList.PostSharedDetialList.size();i++){
            String name = JSONObjectList.PostSharedDetialList.get(i).getFrom().getName();
            String id = JSONObjectList.PostSharedDetialList.get(i).getFrom().getId();
            sharedpost_name.add(name+"/"+id);
            //Log.d("MYLOG","shares: "+name+"/"+id);
        }
    }

    public List intersect(List ls, List ls2) {
        List list = new ArrayList(Arrays.asList(new Object[ls.size()]));
        Collections.copy(list, ls);
        list.retainAll(ls2);
        return list;
    }

    public void alreadyGO(View v){
        final Handler hnn = new Handler();
        hnn.post(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    hnn.removeCallbacks(this);
                }else{
                    hnn.postDelayed(this,1000);
                }
            }
        });
        Intent go = new Intent();
        go.setClass(this,Lottery_Activity.class);
        go.putExtra("lotteryObject",sendList.toArray(new String[0]));
        go.putExtra("title",title.getText().toString());
        startActivity(go);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public List<String> removeDuplicate(List<String> list) {
        List<String> list2;
        list2 = new ArrayList<>();
        list2.addAll(list);
        HashSet h = new HashSet(list2);
        list2.clear();
        list2.addAll(h);
        return list2;
    }

    public static void removeLucker(String str,List<String> list){
        Iterator<String> itr = list.iterator();
        while (itr.hasNext()){
            String e = itr.next();
            if(e.equals(str))itr.remove();
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("2C3CCB95BCF1C694D8D939B882F755CD")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
}
