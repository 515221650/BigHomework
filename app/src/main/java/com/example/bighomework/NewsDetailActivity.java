package com.example.bighomework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class NewsDetailActivity extends AppCompatActivity {
    String newsId = "";
    String newsTitle = "";
    String newsTime = "";
    String newsSource = "";
    String newsContent = "";

    TextView tvTitle, tvContent, tvSource, tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        tvTitle = findViewById(R.id.tv_newsdetail_title);
        tvContent = findViewById(R.id.tv_newsdetail_content);
        tvSource = findViewById(R.id.tv_newsdetail_source);
        tvTime = findViewById(R.id.tv_newsdetail_time);

        Button buttonBack = findViewById(R.id.btn_news_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("yyds",this.toString());
                finish();
            }
        });

        newsId = getIntent().getStringExtra("ID");
        FetchContent process = new FetchContent();
        process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private class FetchContent extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            StringBuilder data = new StringBuilder();
            try {
                URL url = new URL("https://covid-dashboard-api.aminer.cn/event/"+newsId);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while(line != null)
                    {
                        line = bufferedReader.readLine();
                        data.append(line);
                    }

                    JSONObject JO = new JSONObject(data.toString());
                    JSONObject JO2 = (JSONObject) JO.get("data");
                    newsContent = JO2.getString("content");
                    newsTime = (String) JO2.get("time");
                    newsSource = (String) JO2.get("source");
                    newsTitle = (String) JO2.get("title");
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
            tvContent.setText(newsContent);
            tvTime.setText(newsTime);
            tvTitle.setText(newsTitle);
            tvSource.setText(newsSource);
        }
    }
}