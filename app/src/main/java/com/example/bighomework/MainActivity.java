package com.example.bighomework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;

import com.example.bighomework.adapter.KnowledgePropertyListAdapter;
import com.example.bighomework.common.DefineView;
import com.example.bighomework.database.NewsSearchItem;
import com.example.bighomework.database.NewsSearchItemDao;
import com.example.bighomework.database.NewsSearchItemDatabase;
import com.example.bighomework.fragment.DataFragment;
import com.example.bighomework.fragment.ExpertFragment;
import com.example.bighomework.fragment.KnowledgeGraphFragment;
import com.example.bighomework.fragment.MainInfoFragment;
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
    private KnowledgeGraphFragment knowledgeGraphFragment;
    private XRadioGroup radioGroup;
    private Fragment currentFragment;
    private NewsSearchItemDao newsSearchItemDao;
    private NewsSearchItemDatabase newsSearchItemDatabase;

    FloatingActionButton fab, fabHis, fabFavor;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initValidData();
        initListener();
        binData();

        FetchAllNews process = new FetchAllNews();
        process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void initView() {
        radioGroup = (XRadioGroup)findViewById(R.id.main_rg);

        fab = findViewById(R.id.fab_more);
        fabFavor = findViewById(R.id.fab_fav);
        fabHis = findViewById(R.id.fab_his);
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        mainInfoFragment = new MainInfoFragment();
        dataFragment = new DataFragment();
        expertFragment = new ExpertFragment();
        knowledgeGraphFragment = new KnowledgeGraphFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fragment_container, knowledgeGraphFragment);
        transaction.add(R.id.fragment_container, dataFragment);
        transaction.add(R.id.fragment_container, expertFragment);
        transaction.add(R.id.fragment_container, mainInfoFragment);


        transaction.hide(expertFragment);
        transaction.hide(dataFragment);
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
                        transaction.hide(currentFragment).show(dataFragment).commit();
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

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                animateFab();
            }
        });
        fabHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
        fabFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FavorActivity.class);
                startActivity(intent);
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

    private void animateFab()
    {
        if(isOpen)
        {
            fab.startAnimation(rotateBackward);
            fabFavor.startAnimation(fabClose);
            fabHis.startAnimation(fabClose);
            fabHis.setClickable(false);
            fabFavor.setClickable(false);
            isOpen = false;
        }
        else
        {
            fab.startAnimation(rotateForward);
            fabFavor.startAnimation(fabOpen);
            fabHis.startAnimation(fabOpen);
            fabHis.setClickable(true);
            fabFavor.setClickable(true);
            isOpen = true;
        }
    }
}
