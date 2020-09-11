package com.java.yangzhuoyi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.util.Event;
import com.java.yangzhuoyi.util.SearchHistory;

import java.util.ArrayList;
import java.util.List;

public class SuggestArrayAdapter extends BaseAdapter {
    private Context mContext;
    protected LayoutInflater mLayoutInflater;

    public SuggestArrayAdapter(Context context)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return SearchHistory.getSearchHistory(mContext).size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder
    {
        public TextView text;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null)
        {
            view = mLayoutInflater.inflate(R.layout.layout_search_history, null);
            holder = new ViewHolder();
            holder.text = view.findViewById(R.id.tv_search_his);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.text.setText(SearchHistory.getSearchHistory(mContext).get(i));
        return view;
    }
}