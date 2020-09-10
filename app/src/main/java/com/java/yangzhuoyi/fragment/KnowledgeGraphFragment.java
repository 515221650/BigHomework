package com.java.yangzhuoyi.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.adapter.KnowledgePropertyListAdapter;
import com.java.yangzhuoyi.adapter.KnowledgeItemListAdapter;
import com.java.yangzhuoyi.util.KnowledgeItem;
import com.java.yangzhuoyi.util.KnowledgeProperty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class KnowledgeGraphFragment extends Fragment {
    private View mView;
    private TextView tvTitle, tvIntro;
    ImageView ivPic;
    ListView lvProperty, lvRelation;
    SearchView searchView;

    public KnowledgePropertyListAdapter knowledgePropertyListAdapter;
//    public KnowledgeItemListAdapter knowledgeItemListAdapter;

    public KnowledgeGraphFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(mView == null)
        {
            mView = inflater.inflate(R.layout.fragment_knowledge_graph, container, false);
            initView();
            initListener();
        }
        return mView;
    }

    public void initListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("tagg", query);
                FetchKnowledge process = new FetchKnowledge();
                process.queryStr = query;
                process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    public void initView() {
        tvTitle = mView.findViewById(R.id.tv_knowledge_title);
        tvIntro = mView.findViewById(R.id.tv_knowledge_intro);
        ivPic = mView.findViewById(R.id.iv_knowledge);
        lvProperty = mView.findViewById(R.id.lv_knowedge_property);
//        lvRelation = mView.findViewById(R.id.lv_knowedge_relation);

//        knowledgeItemListAdapter = new KnowledgeItemListAdapter(getActivity());
//        lvRelation.setAdapter(knowledgeItemListAdapter);
        knowledgePropertyListAdapter = new KnowledgePropertyListAdapter(getActivity());
        lvProperty.setAdapter(knowledgePropertyListAdapter);

        searchView = mView.findViewById(R.id.search_view_knowledge);
    }

    @SuppressLint("StaticFieldLeak")
    public class FetchKnowledge extends AsyncTask<Void, Void, Void> {
        String queryStr;
        String titleTXT, introTXT;
        boolean imgVis;
        String imgUrl = "";
        @Override
        protected Void doInBackground(Void... voids) {
            knowledgePropertyListAdapter.kPropertyList.clear();
            knowledgePropertyListAdapter.kItemList.clear();
//            knowledgeItemListAdapter.kItemList.clear();
            try {
                URL url = new URL("https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity="+queryStr);
                Log.d("url", "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity="+queryStr);
                StringBuilder data = new StringBuilder();
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(20000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while (line != null) {
                        line = bufferedReader.readLine();
                        data.append(line);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    Log.d("return val", data.toString());
                    JSONObject JO = new JSONObject(data.toString());
                    JSONArray JA2 = (JSONArray) JO.get("data");
                    Integer resNum = JA2.length();

                    if(resNum == 0)
                    {
                        imgVis = false;
                        titleTXT = "NOT FOUND";
                        introTXT = "未找到结果";

                    }
                    else
                    {
                        imgVis = true;
                        JSONObject JO2 = (JSONObject) JA2.get(0);
                        imgUrl = JO2.getString("img");
                        titleTXT = JO2.getString("label");
                        JSONObject JOinfo = (JSONObject) JO2.get("abstractInfo");
                        introTXT = JOinfo.getString("enwiki") +JOinfo.getString("baidu") + JOinfo.getString("zhwiki");
                        JSONObject JOlist = (JSONObject) JOinfo.get("COVID");

                        JSONObject JOpro = (JSONObject) JOlist.get("properties");
                        Iterator<String> it = JOpro.keys();
                        while(it.hasNext()){
                            String key = it.next();
                            String value = JOpro.getString(key);
                            KnowledgeProperty property = new KnowledgeProperty();
                            property.key = key;
                            Log.d("key", key);
                            property.value = value;
                            Log.d("value", value);
                            knowledgePropertyListAdapter.kPropertyList.add(property);
                        }

                        JSONArray JArela = (JSONArray) JOlist.get("relations");
                        int relaNum = JArela.length();
                        for(int i=0; i<relaNum; i++)
                        {
                            JSONObject JOrelaItem = (JSONObject) JArela.get(i);
                            KnowledgeItem item = new KnowledgeItem();
                            item.relation = JOrelaItem.getString("relation");
                            item.label = JOrelaItem.getString("label");
                            item.forward = JOrelaItem.getBoolean("forward");
                            knowledgePropertyListAdapter.kItemList.add(item);
//                            knowledgeItemListAdapter.kItemList.add(item);
                        }

                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(imgVis) ivPic.setVisibility(View.VISIBLE);
            else ivPic.setVisibility(View.INVISIBLE);
            tvTitle.setText(titleTXT);
            tvIntro.setText(introTXT);

//            setListViewHeightBasedOnChildren(lvRelation);
//            setListViewHeightBasedOnChildren(lvProperty);
//            knowledgeItemListAdapter.notifyDataSetChanged();
            knowledgePropertyListAdapter.notifyDataSetChanged();

            Log.d("log pic ", imgUrl);
            Glide.with(getContext()).load(imgUrl).into(ivPic);
        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            Log.d("getcount", "is "+listAdapter.getCount());
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}