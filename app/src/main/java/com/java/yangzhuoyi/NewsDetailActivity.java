package com.java.yangzhuoyi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.java.yangzhuoyi.database.NewsFavor;
import com.java.yangzhuoyi.database.NewsFavorDao;
import com.java.yangzhuoyi.database.NewsFavorDatabase;
import com.java.yangzhuoyi.database.NewsHistory;
import com.java.yangzhuoyi.database.NewsHistoryDao;
import com.java.yangzhuoyi.database.NewsHistoryDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.java.yangzhuoyi.dialog.ShareDialog;
import com.java.yangzhuoyi.util.WebConstants;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.share.WbShareHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NewsDetailActivity extends AppCompatActivity {
    String newsId = "";
    String newsTitle = "";
    String newsTime = "";
    String newsSource = "";
    String newsContent = "";

    TextView tvTitle, tvContent, tvSource, tvTime;
    ShareDialog shareDialog;

    Oauth2AccessToken mAccessToken;

    private NewsHistoryDatabase newsHistoryDatabase;
    private NewsHistoryDao newsHistoryDao;
    private NewsFavorDatabase newsFavorDatabase;
    private NewsFavorDao newsFavorDao;

    private int weiboShareType =1;
    WbShareHandler wbShareHandler;

    FloatingActionButton fab, fabShare, fabFavor;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        tvTitle = findViewById(R.id.tv_newsdetail_title);
        tvContent = findViewById(R.id.tv_newsdetail_content);
        tvSource = findViewById(R.id.tv_newsdetail_source);
        tvTime = findViewById(R.id.tv_newsdetail_time);
        newsHistoryDatabase = NewsHistoryDatabase.getDatabase(this);
        newsHistoryDao = newsHistoryDatabase.getNewsHistoryDao();
        newsFavorDatabase = NewsFavorDatabase.getDatabase(this);
        newsFavorDao = newsFavorDatabase.getNewsFavorDao();

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

        fab = findViewById(R.id.fab_more);
        fabFavor = findViewById(R.id.fab_fav);
        fabShare = findViewById(R.id.fab_share);
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        AuthInfo mAuthInfo = new AuthInfo(this, WebConstants.APP_KEY, WebConstants.REDIRECT_URL, WebConstants.SCOPE);
        WbSdk.install(this, mAuthInfo);

        wbShareHandler = new WbShareHandler(this);
        wbShareHandler.registerApp();
        wbShareHandler.setProgressColor(0xff33b5e5);

        shareDialog = new ShareDialog(this, null, new ShareDialog.PriorityListener() {
            @Override
            public void getSource(String string) {
                if(string.equals("weibo"))
                {
                    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                    weiboMessage.textObject = getTextObj();
                    wbShareHandler.shareMessage(weiboMessage, false);
                }
            }
        });
    }


    private String getSharedText() {
        String text = "【来自"+newsSource+"的消息："+newsTitle+"】"+"\n"+newsContent;
        if(text.length()>=140)
            text.substring(0, 139);
        return text;
    }

    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = getSharedText();
        textObject.title = newsTitle;
        textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }

    class selfAuthorListener implements WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            NewsDetailActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                    weiboMessage.textObject = getTextObj();
                    wbShareHandler.shareMessage(weiboMessage, weiboShareType==1);
                }
            });
        }

        @Override
        public void cancel() {
            Toast.makeText(NewsDetailActivity.this,
                    "canceled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Toast.makeText(NewsDetailActivity.this, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
        }

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

            StoreHistory storeHistory = new StoreHistory();
            storeHistory.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    animateFab();
                }
            });
            fabFavor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StoreFavor process = new StoreFavor();
                    process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
            fabShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(shareDialog.isShowing()){
                        shareDialog.dismiss();
                    }else {
                        shareDialog.show();
                    }
                }
            });
        }
    }

    public class StoreHistory extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            List<NewsHistory> his = newsHistoryDao.getNewsWithId(newsId);
            if(his.size() == 0)
            {
                newsHistoryDao.insertNewsHistory(new NewsHistory(newsId, newsTitle, newsTime, newsSource, newsContent));
            }

            return null;
        }
    }

    public class StoreFavor extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            List<NewsFavor> fav = newsFavorDao.getNewsWithId(newsId);
            if(fav.size() == 0)
            {
                newsFavorDao.insertNewsFavor(new NewsFavor(newsId, newsTitle, newsTime, newsSource, newsContent));
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
            fabShare.startAnimation(fabClose);
            fabShare.setClickable(false);
            fabFavor.setClickable(false);
            isOpen = false;
        }
        else
        {
            fab.startAnimation(rotateForward);
            fabFavor.startAnimation(fabOpen);
            fabShare.startAnimation(fabOpen);
            fabShare.setClickable(true);
            fabFavor.setClickable(true);
            isOpen = true;
        }
    }
}