package com.example.klc.my_first_project.Object;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.klc.my_first_project.R;

import java.util.List;

/**
 * Created by klc on 2017/4/23.
 */

public class MyPostAdapter extends BaseAdapter implements Filterable {
    List<Detial> item;
    List<Detial> originalitem;
    private LayoutInflater mLayout;

    public  MyPostAdapter(Context context, List<Detial> mList){
        mLayout = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.item = mList;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = mLayout.inflate(R.layout.post_listview_content,parent,false);
        TextView message = (TextView)v.findViewById(R.id.message);
        TextView date = (TextView)v.findViewById(R.id.date);

        message.setText(item.get(position).getMessage());
        date.setText(item.get(position).getCreated_time());

        return v;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
