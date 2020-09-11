package com.java.yangzhuoyi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.util.NewsDigest;

import java.util.ArrayList;
import java.util.HashSet;

public class NewsListAdapter extends BaseAdapter {
    public ArrayList<NewsDigest> newsDigestArrayList = new ArrayList<>();
    public HashSet<String> IdHashSet = new HashSet<>();

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public NewsListAdapter(Context context)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        return 0;
        return newsDigestArrayList.size();
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
        public TextView tvTitle, tvDigest, tvTime, tvSource;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null)
        {
            view = mLayoutInflater.inflate(R.layout.layout_newslist_item, null);
            holder = new ViewHolder();
            holder.tvTitle = view.findViewById(R.id.tv_newslist_title);
            holder.tvDigest = view.findViewById(R.id.tv_newslist_digest);
            holder.tvTime = view.findViewById(R.id.tv_newslist_time);
            holder.tvSource = view.findViewById(R.id.tv_newslist_source);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvTitle.setText(newsDigestArrayList.get(position).getTitle());
        holder.tvDigest.setText(newsDigestArrayList.get(position).getDigest());
        holder.tvTime.setText(newsDigestArrayList.get(position).getTime());
        holder.tvSource.setText(newsDigestArrayList.get(position).getSource());

        if(newsDigestArrayList.get(position).getHasRead())
        {
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.garyDark));
            holder.tvSource.setTextColor(mContext.getResources().getColor(R.color.gray));
            holder.tvDigest.setTextColor(mContext.getResources().getColor(R.color.gray));
            holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.gray));
        }
        else
        {
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.tvSource.setTextColor(mContext.getResources().getColor(R.color.garyDark));
            holder.tvDigest.setTextColor(mContext.getResources().getColor(R.color.garyDark));
            holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.garyDark));
        }

        return view;
    }
}
