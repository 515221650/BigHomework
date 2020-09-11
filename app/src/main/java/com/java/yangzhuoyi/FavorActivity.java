package com.java.yangzhuoyi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.java.yangzhuoyi.adapter.NewsFavorAdapter;
import com.java.yangzhuoyi.database.NewsFavor;
import com.java.yangzhuoyi.database.NewsFavorDao;
import com.java.yangzhuoyi.database.NewsFavorDatabase;

import java.util.List;

public class FavorActivity extends AppCompatActivity {
    private ListView lvFavor;
    private NewsFavorDatabase newsFavorDatabase;
    private NewsFavorDao newsFavorDao;
    private NewsFavorAdapter newsFavorAdapter;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor);
        lvFavor = findViewById(R.id.lv_favor);
        newsFavorAdapter = new NewsFavorAdapter(this);
        lvFavor.setAdapter(newsFavorAdapter);
        newsFavorDatabase = NewsFavorDatabase.getDatabase(this);
        newsFavorDao = newsFavorDatabase.getNewsFavorDao();

        buttonBack = findViewById(R.id.btn_favor_back);
        init_listener();

//        FetchFavor process = new FetchFavor();
//        process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        List<NewsFavor> lsFavor = newsFavorDao.getAllFavor();
        newsFavorAdapter.newsFavorArrayList.clear();
        newsFavorAdapter.newsFavorArrayList.addAll(lsFavor);
        newsFavorAdapter.notifyDataSetChanged();

    }

    void init_listener()
    {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lvFavor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion,long arg3) {
                String clickId = newsFavorAdapter.newsFavorArrayList.get(postion).getNewsId();
                String clickTitle = newsFavorAdapter.newsFavorArrayList.get(postion).getTitle();
                String clickTime = newsFavorAdapter.newsFavorArrayList.get(postion).getTime();
                String clickSource = newsFavorAdapter.newsFavorArrayList.get(postion).getSource();
                String clickContent = newsFavorAdapter.newsFavorArrayList.get(postion).getContent();
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

    private class FetchFavor extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            List<NewsFavor> lsFavor = newsFavorDao.getAllFavor();
            newsFavorAdapter.newsFavorArrayList.clear();
            newsFavorAdapter.newsFavorArrayList.addAll(lsFavor);

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            newsFavorAdapter.notifyDataSetChanged();
        }
    }
}