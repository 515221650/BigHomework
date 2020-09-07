package com.example.bighomework.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.bighomework.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> types;
    public TabGridViewAdapter(Context context, String[] types)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.types = new ArrayList<String>();
        if(types!=null)
            this.types.addAll(Arrays.asList(types));

    }

    public String[] getTypes(){
        return types.toArray(new String[0]);
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public Object getItem(int i) {
        return types.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ViewHolder holder = null;
        if(view==null)
        {
            view = mLayoutInflater.inflate(R.layout.layout_tab_item, null);
            holder = new ViewHolder();
            view.setTag(holder);
            holder.button = (Button)view.findViewById(R.id.tab_bth);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.button.setText(types.get(i));
        return view;
    }

    public void delete(int position)
    {
        types.remove(position);
        notifyDataSetChanged();
    }

    public void add(String type)
    {
        types.add(type);
        notifyDataSetChanged();
    }

    static class ViewHolder{
        public Button button;
    }

}
