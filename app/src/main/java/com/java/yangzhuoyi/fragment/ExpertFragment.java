package com.java.yangzhuoyi.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.java.yangzhuoyi.ExpertActivity;
import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.adapter.ExpertListAdapter;
import com.java.yangzhuoyi.common.DefineView;
import com.java.yangzhuoyi.util.Expert;

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
import java.util.ArrayList;
import java.util.List;

public class ExpertFragment extends BaseFragment implements DefineView {
    private View mView;
    private ListView expertList;
    private ExpertListAdapter expertListAdapter;
    List<Expert>live_expert = new ArrayList<>();
    List<Expert>not_live_expert = new ArrayList<>();
    private RadioGroup radioGroup;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null)
        {
            mView = inflater.inflate(R.layout.expert_fragment, container, false);
            initView();
            initValidData();
            initListener();
            binData();
            FetchExpert process = new FetchExpert();
            process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        return mView;
    }

    @Override
    public void initView() {
        expertList = mView.findViewById(R.id.expert_list);
        expertListAdapter = new ExpertListAdapter(getActivity());
        expertList.setAdapter(expertListAdapter);
        radioGroup = mView.findViewById(R.id.rg_expert);
    }

    @Override
    public void initValidData() {

    }

    @Override
    public void initListener() {
        Log.d("AAS", radioGroup.toString());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                expertListAdapter.expertList.clear();
                if(i==R.id.rb11)
                {
                    expertListAdapter.expertList.addAll(live_expert);
                    expertListAdapter.notifyDataSetChanged();
                }
                else
                {
//                    Log.d("ASD","DSA");
                    expertListAdapter.expertList.addAll(not_live_expert);
                    expertListAdapter.notifyDataSetChanged();
                }
            }
        });
        expertList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ExpertActivity.class);
                intent.putExtra("expert", expertListAdapter.expertList.get(i));
                Log.d("!!!","!!!!!!!!!!!!!");
                startActivity(intent);
            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    public class FetchExpert extends AsyncTask<Void, Void, Void> {
        List<Expert>live = new ArrayList<>();
        List<Expert>not_live = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2");
//                Log.d("url", "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=");
                StringBuilder data = new StringBuilder();
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while (line != null) {
                        line = bufferedReader.readLine();
                        data.append(line);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    Log.d("return val", data.toString());
                    JSONObject JO = new JSONObject(data.toString());
                    JSONArray dataArray = JO.getJSONArray("data");
                    Log.d("dataArray", dataArray.length()+"");
                    for (int i=0;i<dataArray.length();i++)
                    {
                        JSONObject expert = dataArray.getJSONObject(i);
                        Expert tmp_expert = new Expert();
                        tmp_expert.name = expert.getString("name");
                        tmp_expert.name_zh = expert.getString("name_zh");
                        if(tmp_expert.name_zh.length()<=1)tmp_expert.name_zh = tmp_expert.name;
                        tmp_expert.avatar = expert.getString("avatar");

                        JSONObject indices = expert.getJSONObject("indices");
                        tmp_expert.activity = indices.getDouble("activity");
                        tmp_expert.hindex = indices.getInt("hindex");
                        tmp_expert.citations = indices.getInt("citations");
                        tmp_expert.pubs = indices.getInt("pubs");
                        tmp_expert.sociability = indices.getDouble("sociability");

                        JSONObject profile = expert.getJSONObject("profile");
                        tmp_expert.bio = profile.getString("bio");
                        tmp_expert.affiliation = profile.getString("affiliation");
                        if(profile.has("edu"))
                            tmp_expert.edu = profile.getString("edu");
                        else tmp_expert.edu = "";
                        if(profile.has("work"))
                            tmp_expert.work = profile.getString("work");
                        else tmp_expert.work = "";

                        tmp_expert.bio = tmp_expert.bio.replaceAll("<br><br>","\n");
                        tmp_expert.work = tmp_expert.work.replaceAll("<br><br>","\n");
                        tmp_expert.edu = tmp_expert.edu.replaceAll("<br><br>","\n");

                        tmp_expert.bio = tmp_expert.bio.replaceAll("<br>"," ");
                        tmp_expert.work = tmp_expert.work.replaceAll("<br>"," ");
                        tmp_expert.edu = tmp_expert.edu.replaceAll("<br>"," ");



                        boolean is_pass = expert.getBoolean("is_passedaway");
                        Log.d("is_pass", is_pass+"");
                        if(is_pass)
                            not_live.add(tmp_expert);
                        else live.add(tmp_expert);
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
            expertListAdapter.expertList.addAll(live);
            live_expert = live;
            not_live_expert = not_live;
            expertListAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void binData() {

    }
}
