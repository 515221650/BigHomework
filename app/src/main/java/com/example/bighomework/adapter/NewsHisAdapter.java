package com.example.bighomework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bighomework.R;
import com.example.bighomework.database.NewsHistory;
import com.example.bighomework.database.NewsSearchItem;

import java.util.ArrayList;

public class NewsHisAdapter extends BaseAdapter {
    public ArrayList<NewsHistory> newsHistoryArrayList = new ArrayList<>();

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public NewsHisAdapter(Context context)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return newsHistoryArrayList.size();
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
            view =  mLayoutInflater.inflate(R.layout.layout_history_item, null);
            holder = new NewsHisAdapter.ViewHolder();
            holder.tvTitle = view.findViewById(R.id.tv_newshis_title);
            holder.tvTime  = view.findViewById(R.id.tv_newshis_time);
            holder.tvType = view.findViewById(R.id.tv_newshis_source);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvTitle.setText(newsHistoryArrayList.get(position).getTitle());
        holder.tvTime.setText(newsHistoryArrayList.get(position).getTime());
        holder.tvType.setText(newsHistoryArrayList.get(position).getSource());

        return view;
    }



}
