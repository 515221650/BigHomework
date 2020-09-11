package com.java.yangzhuoyi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.java.yangzhuoyi.adapter.EventAdapter;
import com.java.yangzhuoyi.adapter.EventListAdapter;
import com.java.yangzhuoyi.common.DefineView;
import com.java.yangzhuoyi.util.Event;
import com.java.yangzhuoyi.util.NewsDigest;

import java.util.List;

public class EventListActivity extends AppCompatActivity implements DefineView {

    TextView key1,key2,key3;
    ListView listView;
    EventListAdapter eventAdapter;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.event_list);
        initView();
        initValidData();
        initListener();
        binData();
    }

    @Override
    public void initView() {
        key1 = findViewById(R.id.key1);
        key2 = findViewById(R.id.key2);
        key3 = findViewById(R.id.key3);
        listView = findViewById(R.id.event_list2);
        Button buttonBack = findViewById(R.id.btn_news_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initValidData() {
        Intent intent = getIntent();
        eventAdapter = new EventListAdapter(this, Event.getInstance().events);
        listView.setAdapter(eventAdapter);
        key1.setText("# " +  Event.getInstance().keywords[0]);
        key2.setText("# " +  Event.getInstance().keywords[1]);
        key3.setText("# " +  Event.getInstance().keywords[2]);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void binData() {

    }
}
