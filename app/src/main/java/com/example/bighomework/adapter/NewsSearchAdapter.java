package com.example.bighomework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bighomework.R;
import com.example.bighomework.database.NewsSearchItem;

import java.util.ArrayList;

public class NewsSearchAdapter extends BaseAdapter {
    public ArrayList<NewsSearchItem> newsSearchItemArrayList = new ArrayList<>();

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public NewsSearchAdapter(Context context)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return newsSearchItemArrayList.size();
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
        public TextView tvTitle, tvTime, tvType;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null)
        {
            view =  mLayoutInflater.inflate(R.layout.layout_newssearch_item, null);
            holder = new NewsSearchAdapter.ViewHolder();
            holder.tvTitle = view.findViewById(R.id.tv_newssearch_title);
            holder.tvTime  = view.findViewById(R.id.tv_newssearch_time);
            holder.tvType = view.findViewById(R.id.tv_newssearch_type);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvTitle.setText(newsSearchItemArrayList.get(position).getTitle());
        holder.tvTime.setText(newsSearchItemArrayList.get(position).getTime());
        holder.tvType.setText(newsSearchItemArrayList.get(position).getType());

        return view;
    }



}
