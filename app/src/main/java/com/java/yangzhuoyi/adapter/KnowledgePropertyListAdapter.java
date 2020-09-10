package com.java.yangzhuoyi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.room.PrimaryKey;

import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.util.KnowledgeItem;
import com.java.yangzhuoyi.util.KnowledgeProperty;

import java.util.ArrayList;

public class KnowledgePropertyListAdapter extends BaseAdapter {
    public ArrayList<KnowledgeProperty> kPropertyList = new ArrayList<>();
    public ArrayList<KnowledgeItem> kItemList = new ArrayList<>();

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public KnowledgePropertyListAdapter(Context context)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return kPropertyList.size()+kItemList.size();
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
        public TextView tvRelation, tvName;
        public ImageView ivForward;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.d("listPos", "is "+position);
        KnowledgePropertyListAdapter.ViewHolder holder = null;
        if(position < kPropertyList.size())
        {
//            if(view == null)
//            {
                view = mLayoutInflater.inflate(R.layout.layout_knowledgeproperty_item, null);
                holder = new KnowledgePropertyListAdapter.ViewHolder();
                holder.tvKey = view.findViewById(R.id.tv_knowledgeproperty_key);
                holder.tvValue = view.findViewById(R.id.tv_knowledgeproperty_value);
                view.setTag(holder);
//            }
//            else
//            {
//                holder = (KnowledgePropertyListAdapter.ViewHolder) view.getTag();
//            }
            holder.tvKey.setText(kPropertyList.get(position).key);
            holder.tvValue.setText(kPropertyList.get(position).value);
        }
        else
        {
//            if(view == null)
//            {
                view = mLayoutInflater.inflate(R.layout.layout_knowledgeitem_item, null);
                holder = new KnowledgePropertyListAdapter.ViewHolder();
                holder.tvName = view.findViewById(R.id.tv_knowledgeitem_name);
                holder.tvRelation = view.findViewById(R.id.tv_knowledgeitem_relation);
                holder.ivForward = view.findViewById(R.id.iv_knowledgeitem);
                view.setTag(holder);
//            }
//            else
//            {
//                holder = (KnowledgePropertyListAdapter.ViewHolder) view.getTag();
////            }
//            if(holder==null)
//            {
//                Log.d("1","1");
//            }
            holder.tvName.setText(kItemList.get(position-kPropertyList.size()).label);
            holder.tvRelation.setText(kItemList.get(position-kPropertyList.size()).relation);
            if(kItemList.get(position-kPropertyList.size()).forward)
                holder.ivForward.setImageResource(R.drawable.right_arrow);
            else holder.ivForward.setImageResource(R.drawable.left_arrow);
        }


        return view;
    }
}
