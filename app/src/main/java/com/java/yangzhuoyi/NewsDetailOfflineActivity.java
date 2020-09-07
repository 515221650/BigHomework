package com.java.yangzhuoyi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.java.yangzhuoyi.database.NewsFavor;
import com.java.yangzhuoyi.database.NewsFavorDao;
import com.java.yangzhuoyi.database.NewsFavorDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NewsDetailOfflineActivity extends AppCompatActivity {
    String newsId = "";
    String newsTitle = "";
    String newsTime = "";
    String newsSource = "";
    String newsContent = "";

    TextView tvTitle, tvContent, tvSource, tvTime;

    private NewsFavorDatabase newsFavorDatabase;
    private NewsFavorDao newsFavorDao;

    FloatingActionButton fab, fabShare, fabFavor;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_offline);
        tvTitle = findViewById(R.id.tv_newsdetail_offline_title);
        tvContent = findViewById(R.id.tv_newsdetail_offline_content);
        tvSource = findViewById(R.id.tv_newsdetail_offline_source);
        tvTime = findViewById(R.id.tv_newsdetail_offline_time);

        newsFavorDatabase = NewsFavorDatabase.getDatabase(this);
        newsFavorDao = newsFavorDatabase.getNewsFavorDao();

        Button buttonBack = findViewById(R.id.btn_news_offline_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        newsId = getIntent().getStringExtra("ID");
        newsTitle = getIntent().getStringExtra("Title");
        newsContent = getIntent().getStringExtra("Content");
        newsSource = getIntent().getStringExtra("Source");
        newsTime = getIntent().getStringExtra("Time");

        tvTitle.setText(newsTitle);
        tvContent.setText(newsContent);
        tvSource.setText(newsSource);
        tvTime.setText(newsTime);

        fab = findViewById(R.id.fab_more);
        fabFavor = findViewById(R.id.fab_fav);
        fabShare = findViewById(R.id.fab_share);
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

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