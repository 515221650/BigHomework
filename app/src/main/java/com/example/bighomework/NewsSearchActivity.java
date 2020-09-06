package com.example.bighomework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.app.slice.SliceItem;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

import com.example.bighomework.R;
import com.example.bighomework.adapter.NewsSearchAdapter;
import com.example.bighomework.database.NewsSearchItem;
import com.example.bighomework.database.NewsSearchItemDao;
import com.example.bighomework.database.NewsSearchItemDatabase;
import com.example.bighomework.fragment.KnowledgeGraphFragment;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.Collections;
import java.util.List;

public class NewsSearchActivity extends AppCompatActivity {
    SearchView searchView;
    ListView newsList;
    Button backButton;
    String keyword = "null";

    public NewsSearchAdapter newsSearchAdapter;
    private NewsSearchItemDao newsSearchItemDao;
    private NewsSearchItemDatabase newsSearchItemDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyword = getIntent().getStringExtra("keyword");
        setContentView(R.layout.activity_news_search);
        searchView = findViewById(R.id.search_view_news);
        newsList = findViewById(R.id.lv_search);
        backButton = findViewById(R.id.btn_news_back);

        newsSearchAdapter = new NewsSearchAdapter(this);
        newsList.setAdapter(newsSearchAdapter);
        newsSearchItemDatabase = NewsSearchItemDatabase.getDatabase(this);
        newsSearchItemDao = newsSearchItemDatabase.getNewsSearchItemDao();
        init_listener();
        searchAsyncTask process = new searchAsyncTask(newsSearchItemDao);
        process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, keyword);
    }

    void init_listener()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAsyncTask process = new searchAsyncTask(newsSearchItemDao);
                process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //TO DO
                return false;
            }
        });
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion,long arg3) {
                String clickId = newsSearchAdapter.newsSearchItemArrayList.get(postion).getNewsId();
                Intent intent = new Intent(arg0.getContext(), NewsDetailActivity.class);
                intent.putExtra("ID", clickId);
                startActivity(intent);
            }
        });
    }

    class searchAsyncTask extends AsyncTask<String, Void, Void> {
        private NewsSearchItemDao newsSearchItemDao;

        public searchAsyncTask(NewsSearchItemDao newsSearchItemDao) {
            this.newsSearchItemDao = newsSearchItemDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            List<NewsSearchItem> reslist = newsSearchItemDao.getNewsContains(strings[0]);
            Collections.reverse(reslist);
            newsSearchAdapter.newsSearchItemArrayList.clear();
            newsSearchAdapter.newsSearchItemArrayList.addAll(reslist);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            newsSearchAdapter.notifyDataSetChanged();
        }
    }


}