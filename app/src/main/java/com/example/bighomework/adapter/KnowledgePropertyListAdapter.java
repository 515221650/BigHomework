package com.example.bighomework.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bighomework.R;
import com.example.bighomework.util.KnowledgeProperty;

import java.util.ArrayList;

public class KnowledgePropertyListAdapter extends BaseAdapter {
    public ArrayList<KnowledgeProperty> kPropertyList = new ArrayList<>();

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public KnowledgePropertyListAdapter(Context context)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return kPropertyList.size();
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
        public TextView tvValue, tvKey;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.d("listPos", "is "+position);
        KnowledgePropertyListAdapter.ViewHolder holder = null;
        if(view == null)
        {
            view = mLayoutInflater.inflate(R.layout.layout_knowledgeproperty_item, null);
            holder = new KnowledgePropertyListAdapter.ViewHolder();
            holder.tvKey = view.findViewById(R.id.tv_knowledgeproperty_key);
            holder.tvValue = view.findViewById(R.id.tv_knowledgeproperty_value);
            view.setTag(holder);
        }
        else
        {
            holder = (KnowledgePropertyListAdapter.ViewHolder) view.getTag();
        }

        holder.tvKey.setText(kPropertyList.get(position).key);
        holder.tvValue.setText(kPropertyList.get(position).value);
        return view;
    }
}
