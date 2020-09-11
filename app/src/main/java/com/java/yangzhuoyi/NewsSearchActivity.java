package com.java.yangzhuoyi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.adapter.NewsSearchAdapter;
import com.java.yangzhuoyi.adapter.SuggestArrayAdapter;
import com.java.yangzhuoyi.database.NewsSearchItem;
import com.java.yangzhuoyi.database.NewsSearchItemDao;
import com.java.yangzhuoyi.database.NewsSearchItemDatabase;
import com.java.yangzhuoyi.util.SearchHistory;

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
    ListPopupWindow listPopupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyword = getIntent().getStringExtra("keyword");
        setContentView(R.layout.activity_news_search);
        searchView = findViewById(R.id.search_view_news);
        newsList = findViewById(R.id.lv_search);
        backButton = findViewById(R.id.btn_news_back);
        listPopupWindow = new ListPopupWindow(NewsSearchActivity.this);
        listPopupWindow.setAdapter(new SuggestArrayAdapter(NewsSearchActivity.this));
        listPopupWindow.setAnchorView(searchView);

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
                listPopupWindow.dismiss();
                SearchHistory.saveSearchHistory(query, NewsSearchActivity.this);
                searchAsyncTask process = new searchAsyncTask(newsSearchItemDao);
                process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, query);
                searchView.clearFocus();
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
        //监听输入内容焦点的变化
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("TextFocusChange",  " " +hasFocus);
                Log.d("get his num", ""+SearchHistory.getSearchHistory(NewsSearchActivity.this).size());
                if(hasFocus) listPopupWindow.show();
                else listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                String keyword = SearchHistory.getSearchHistory(NewsSearchActivity.this).get(position);
                SearchHistory.saveSearchHistory(keyword, NewsSearchActivity.this);
                searchAsyncTask process = new searchAsyncTask(newsSearchItemDao);
                process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, keyword);
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