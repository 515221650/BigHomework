package com.example.bighomework.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bighomework.R;
import com.example.bighomework.fragment.ExpertFragment;
import com.example.bighomework.util.Expert;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExpertListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public ArrayList<Expert> expertList = new ArrayList<>();

    public ExpertListAdapter(Context context)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return expertList.size();
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
        public TextView indexP,indexH,indexC,indexS,indexA;
        public TextView expertName,expertEnName;
        public TextView affiliation;
        public ImageView avatar;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view==null)
        {
            view = mLayoutInflater.inflate(R.layout.expert_item, null);
            holder = new ViewHolder();
            holder.indexP = view.findViewById(R.id.index_p);
            holder.indexH = view.findViewById(R.id.index_h);
            holder.indexS = view.findViewById(R.id.index_s);
            holder.indexC = view.findViewById(R.id.index_c);
            holder.indexA = view.findViewById(R.id.index_a);
            holder.expertName = view.findViewById(R.id.expert_name);
            holder.expertEnName = view.findViewById(R.id.expert_en_name);
            holder.affiliation = view.findViewById(R.id.affiliation);
            holder.avatar = view.findViewById(R.id.avatar);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        Expert expert = expertList.get(i);
        holder.indexP.setText(String.valueOf(expert.pubs));
        holder.indexH.setText(String.valueOf(expert.hindex));
        holder.indexC.setText(String.valueOf(expert.citations));
        String tmp_a = String.valueOf(expert.activity);
        if(tmp_a.length()>5)tmp_a = tmp_a.substring(0,5);

        String tmp_s = String.valueOf(expert.sociability);
        if(tmp_s.length()>5)tmp_s = tmp_s.substring(0,5);

        holder.indexA.setText(tmp_a);
        holder.indexS.setText(tmp_s);

        holder.expertName.setText(expert.name_zh);
        holder.expertEnName.setText(expert.name);
        holder.affiliation.setText(expert.affiliation);
        holder.avatar.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(expert.avatar).into(holder.avatar);
        Log.d("print",i+"");
        return view;
    }
}
