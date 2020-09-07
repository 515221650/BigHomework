package com.example.bighomework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.bighomework.adapter.NewsHisAdapter;
import com.example.bighomework.database.NewsHistory;
import com.example.bighomework.database.NewsHistoryDao;
import com.example.bighomework.database.NewsHistoryDatabase;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView lvHis;
    private NewsHistoryDatabase newsHistoryDatabase;
    private NewsHistoryDao newsHistoryDao;
    private NewsHisAdapter newsHisAdapter;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        lvHis = findViewById(R.id.lv_his);
        newsHisAdapter = new NewsHisAdapter(this);
        lvHis.setAdapter(newsHisAdapter);
        newsHistoryDatabase = NewsHistoryDatabase.getDatabase(this);
        newsHistoryDao = newsHistoryDatabase.getNewsHistoryDao();

        buttonBack = findViewById(R.id.btn_his_back);
        init_listener();

        FetchHis process = new FetchHis();
        process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    void init_listener()
    {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lvHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion,long arg3) {
                String clickId = newsHisAdapter.newsHistoryArrayList.get(postion).getNewsId();
                String clickTitle = newsHisAdapter.newsHistoryArrayList.get(postion).getTitle();
                String clickTime = newsHisAdapter.newsHistoryArrayList.get(postion).getTime();
                String clickSource = newsHisAdapter.newsHistoryArrayList.get(postion).getSource();
                String clickContent = newsHisAdapter.newsHistoryArrayList.get(postion).getContent();
                Intent intent = new Intent(arg0.getContext(), NewsDetailOfflineActivity.class);
                intent.putExtra("ID", clickId);
                intent.putExtra("Title", clickTitle);
                intent.putExtra("Time", clickTime);
                intent.putExtra("Source", clickSource);
                intent.putExtra("Content", clickContent);
                startActivity(intent);
            }
        });
    }

    private class FetchHis extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            List<NewsHistory> lsHis = newsHistoryDao.getAllHis();
            newsHisAdapter.newsHistoryArrayList.clear();
            newsHisAdapter.newsHistoryArrayList.addAll(lsHis);

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            newsHisAdapter.notifyDataSetChanged();
        }
    }
}