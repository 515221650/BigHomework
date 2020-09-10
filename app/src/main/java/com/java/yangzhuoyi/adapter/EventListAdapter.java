package com.java.yangzhuoyi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.common.DefineView;
import com.java.yangzhuoyi.util.Event;
import com.java.yangzhuoyi.util.NewsDigest;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends BaseAdapter implements DefineView {
    public List<NewsDigest> eventArrayList = new ArrayList<>();
    private Context mcontex;
    private LayoutInflater layoutInflater;

    public EventListAdapter(Context context, List<NewsDigest> initList)
    {
        this.eventArrayList.addAll(initList);
        this.mcontex = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return eventArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder{
        public TextView title, date, company;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null)
        {
            view = layoutInflater.inflate(R.layout.event_detail, null);
            holder = new ViewHolder();
            holder.date = view.findViewById(R.id.tv_newslist_time);
            holder.company = view.findViewById(R.id.tv_newslist_source);
            holder.title = view.findViewById(R.id.tv_newslist_title);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.title.setText(eventArrayList.get(i).getTitle());
        holder.date.setText(eventArrayList.get(i).getTime());
        holder.company.setText(eventArrayList.get(i).getSource());
        return view;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initValidData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void binData() {

    }
}
