package com.example.klc.my_first_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.klc.my_first_project.Object.JSONObjectList;
import com.example.klc.my_first_project.Object.MyPostAdapter;

import java.util.List;

/**
 * Created by klc on 2017/4/23.
 */

public class PostListView extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_listview);

        ListView mListView = (ListView)findViewById(R.id.listView);
        MyPostAdapter myPostAdapter = new MyPostAdapter(this, JSONObjectList.FeedPostDetialList);
        mListView.setAdapter(myPostAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(PostListView.this, "你點到了 "+JSONObjectList.FeedPostDetialList.get(position).getId(), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK,new Intent().putExtra("contentID",JSONObjectList.FeedPostDetialList.get(position).getId()));
                finish();
            }
        });

    }
}
