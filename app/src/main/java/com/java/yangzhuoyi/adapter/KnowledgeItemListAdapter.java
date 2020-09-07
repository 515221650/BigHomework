package com.java.yangzhuoyi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.util.KnowledgeItem;

import java.util.ArrayList;

public class KnowledgeItemListAdapter extends BaseAdapter {
    public ArrayList<KnowledgeItem> kItemList = new ArrayList<>();

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public KnowledgeItemListAdapter(Context context)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return kItemList.size();
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
        public TextView tvRelation, tvName;
        public ImageView ivForward;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null)
        {
            view = mLayoutInflater.inflate(R.layout.layout_knowledgeitem_item, null);
            holder = new KnowledgeItemListAdapter.ViewHolder();
            holder.tvName = view.findViewById(R.id.tv_knowledgeitem_name);
            holder.tvRelation = view.findViewById(R.id.tv_knowledgeitem_relation);
            holder.ivForward = view.findViewById(R.id.iv_knowledgeitem);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvName.setText(kItemList.get(position).label);
        holder.tvRelation.setText(kItemList.get(position).relation);
        if(kItemList.get(position).forward)
            holder.ivForward.setImageResource(R.drawable.right_arrow);
        else holder.ivForward.setImageResource(R.drawable.left_arrow);

        return view;
    }
}
