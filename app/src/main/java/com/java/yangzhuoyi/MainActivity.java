package com.java.yangzhuoyi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;

import com.java.yangzhuoyi.common.DefineView;
import com.java.yangzhuoyi.database.NewsSearchItem;
import com.java.yangzhuoyi.database.NewsSearchItemDao;
import com.java.yangzhuoyi.database.NewsSearchItemDatabase;
import com.java.yangzhuoyi.fragment.DataFragment;
import com.java.yangzhuoyi.fragment.EventFragment;
import com.java.yangzhuoyi.fragment.ExpertFragment;
import com.java.yangzhuoyi.fragment.KnowledgeGraphFragment;
import com.java.yangzhuoyi.fragment.MainInfoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

import me.shihao.library.XRadioGroup;

public class MainActivity extends AppCompatActivity implements DefineView {

    private RadioButton rb1,rb2;
    private MainInfoFragment mainInfoFragment;
    private DataFragment dataFragment;
    private ExpertFragment expertFragment;
    private EventFragment eventFragment;
    private KnowledgeGraphFragment knowledgeGraphFragment;
    private XRadioGroup radioGroup;
    private Fragment currentFragment;
    private NewsSearchItemDao newsSearchItemDao;
    private NewsSearchItemDatabase newsSearchItemDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initValidData();
        initListener();
        binData();

//        FetchAllNews process = new FetchAllNews();
//        process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void initView() {
        radioGroup = (XRadioGroup)findViewById(R.id.main_rg);


        mainInfoFragment = new MainInfoFragment();
//        dataFragment = new DataFragment();
        expertFragment = new ExpertFragment();
        knowledgeGraphFragment = new KnowledgeGraphFragment();
//        eventFragment = new EventFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

//        transaction.add(R.id.fragment_container, eventFragment);
        transaction.add(R.id.fragment_container, knowledgeGraphFragment);
//        transaction.add(R.id.fragment_container, dataFragment);
        transaction.add(R.id.fragment_container, expertFragment);
        transaction.add(R.id.fragment_container, mainInfoFragment);

//        transaction.hide(eventFragment);
        transaction.hide(expertFragment);
//        transaction.hide(dataFragment);
        transaction.hide(knowledgeGraphFragment);


        transaction.commit();

        currentFragment = mainInfoFragment;
    }

    @Override
    public void initValidData() {
        newsSearchItemDatabase = NewsSearchItemDatabase.getDatabase(this);
        newsSearchItemDao = newsSearchItemDatabase.getNewsSearchItemDao();
//        ImageView imageView = (ImageView)searchView.findViewById(R.id.search_button);
//        imageView.setColorFilter(R.color.black);
    }


    @Override
    public void initListener() {
        radioGroup.setOnCheckedChangeListener(new XRadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(XRadioGroup radioGroup, int id) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (id)
                {
                    case R.id.rb_news:
                        transaction.hide(currentFragment).show(mainInfoFragment).commit();
                        currentFragment = mainInfoFragment;
                        break;
                    case R.id.rb_data:
                        transaction.hide(currentFragment).show(dataFragment).commit();
                        currentFragment = dataFragment;
                        break;
                    case R.id.rb_cate:
                        transaction.hide(currentFragment).show(eventFragment).commit();
                        currentFragment = eventFragment;
                        break;
                    case R.id.rb_doctor:
                        transaction.hide(currentFragment).show(expertFragment).commit();
                        currentFragment = expertFragment;
                        break;
                    case R.id.rb_relation:
                        transaction.hide(currentFragment).show(knowledgeGraphFragment).commit();
                        currentFragment = knowledgeGraphFragment;
                        break;
                }
            }
        });



    }

    @Override
    public void binData() {

    }

    public class FetchAllNews extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            newsSearchItemDao.deleteAllNews();
            try {
                URL url = new URL("https://covid-dashboard.aminer.cn/api/dist/events.json");
                StringBuilder data = new StringBuilder();
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
                httpURLConnection.disconnect();


                JSONObject JO = new JSONObject(data.toString());
                JSONArray JA = (JSONArray) JO.get("datas");
                int NewsNum = JA.length();
                for(int i=0; i<NewsNum; i++)
                {
                    JSONObject JO2 = (JSONObject) JA.get(i);
                    NewsSearchItem tmpItem = new NewsSearchItem();
                    tmpItem.setNewsId(JO2.optString("_id"));
                    tmpItem.setType(JO2.optString("type"));
                    tmpItem.setTime(JO2.optString("time"));
                    tmpItem.setTitle(JO2.optString("title"));
                    newsSearchItemDao.insertNewsSearch(tmpItem);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


}
