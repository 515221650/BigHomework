package com.java.yangzhuoyi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.util.Event;
import com.java.yangzhuoyi.common.DefineView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends BaseAdapter{
    public List<Event> eventList = new ArrayList<>();
    private Context mContext;
    protected LayoutInflater mLayoutInflater;

    public EventAdapter(Context context)
    {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        Log.d("Adapter count", "is "+eventList.size());
        return eventList.size();
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
        public TextView hot1,hot2,hot3;
        public TextView keyword1,keyword2,keyword3;
        public TextView title1;
        public TextView tv_rank;
//        public TextView title2,title3;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view==null)
        {
            view = mLayoutInflater.inflate(R.layout.card_item, null);
            holder = new ViewHolder();
            holder.hot1 = view.findViewById(R.id.hotnumber1);
            holder.hot2 = view.findViewById(R.id.hotnumber2);
            holder.hot3 = view.findViewById(R.id.hotnumber3);
            holder.keyword1 = view.findViewById(R.id.keyword1);
            holder.keyword2 = view.findViewById(R.id.keyword2);
            holder.keyword3 = view.findViewById(R.id.keyword3);
            holder.title1 = view.findViewById(R.id.title1);
            holder.tv_rank = view.findViewById(R.id.tv_rank);
//            holder.title2 = view.findViewById(R.id.title2);
//            holder.title3 = view.findViewById(R.id.title3);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }
        Event event = eventList.get(i);
        holder.keyword1.setText(event.keywords[0]);
        holder.keyword2.setText(event.keywords[1]);
        holder.keyword3.setText(event.keywords[2]);
        holder.hot1.setText(event.hotnumber[0]);
        holder.hot2.setText(event.hotnumber[1]);
        holder.hot3.setText(event.hotnumber[2]);
        holder.title1.setText("共计 "+event.events.size() + " 条");
        holder.tv_rank.setText(Integer.toString(i+1));
//        holder.title2.setText("");
//        holder.title3.setText("");
//        holder.title2.setText(event.events.get(1).getTitle());
//        holder.title3.setText(event.events.get(2).getTitle());
//        holder.title1.setText(event.events.get(0).getTitle());
//        holder.title2.setText(event.events.get(1).getTitle());
//        holder.title3.setText(event.events.get(2).getTitle());
        return view;
    }
}
