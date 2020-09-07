package com.example.bighomework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bighomework.R;
import com.example.bighomework.database.NewsFavor;

import java.util.ArrayList;

public class NewsFavorAdapter extends BaseAdapter {
    public ArrayList<NewsFavor> newsFavorArrayList = new ArrayList<com.example.bighomework.database.NewsFavor>();

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public NewsFavorAdapter(Context context)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return newsFavorArrayList.size();
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
            holder = new NewsFavorAdapter.ViewHolder();
            holder.tvTitle = view.findViewById(R.id.tv_newshis_title);
            holder.tvTime  = view.findViewById(R.id.tv_newshis_time);
            holder.tvType = view.findViewById(R.id.tv_newshis_source);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvTitle.setText(newsFavorArrayList.get(position).getTitle());
        holder.tvTime.setText(newsFavorArrayList.get(position).getTime());
        holder.tvType.setText(newsFavorArrayList.get(position).getSource());

        return view;
    }



}
