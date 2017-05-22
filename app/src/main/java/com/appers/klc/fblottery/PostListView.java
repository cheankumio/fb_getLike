package com.appers.klc.fblottery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.appers.klc.fblottery.Object.JSONObjectList;
import com.appers.klc.fblottery.Object.MyPostAdapter;

import es.dmoral.toasty.Toasty;

/**
 * Created by klc on 2017/4/23.
 */

public class PostListView extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.appers.klc.fblottery.R.layout.post_listview);

        getSupportActionBar().hide();

        Toasty.success(this,"載入成功, 請選擇貼文",Toast.LENGTH_LONG,true).show();

        ListView mListView = (ListView)findViewById(com.appers.klc.fblottery.R.id.listView);
        MyPostAdapter myPostAdapter = new MyPostAdapter(this, JSONObjectList.FeedPostDetialList);
        mListView.setAdapter(myPostAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent SetDisplayData = new Intent();
                SetDisplayData.setClass(PostListView.this,SetDisplayData_Activity.class);
                SetDisplayData.putExtra("contentID",JSONObjectList.FeedPostDetialList.get(position).getId());
                startActivity(SetDisplayData);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                //finish();
            }
        });

    }
}
