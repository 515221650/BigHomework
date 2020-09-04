package com.example.bighomework;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bighomework.common.DefineView;
import com.google.android.material.tabs.TabLayout;

public class TabGridViewActivity extends AppCompatActivity implements DefineView {
    private GridView mGv1, mGv2;
    private TabGridViewAdapter tabGridViewAdapter1, tabGridViewAdapter2;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_gridview_activity);
        initView();
        initValidData();
        initListener();
        binData();
    }

    @Override
    public void initView() {
        mGv1 = (GridView)findViewById(R.id.gd);
        mGv2 = (GridView)findViewById(R.id.gd2);
        button = (Button)findViewById(R.id.return_bth);
    }

    @Override
    public void initValidData() {
        mGv1 = (GridView)findViewById(R.id.gd);
        mGv2 = (GridView)findViewById(R.id.gd2);
        Intent intent = getIntent();
        tabGridViewAdapter1 = new TabGridViewAdapter(TabGridViewActivity.this, intent.getStringArrayExtra("types"));
        tabGridViewAdapter2 = new TabGridViewAdapter(TabGridViewActivity.this, intent.getStringArrayExtra("invisible_types"));
        mGv1.setAdapter(tabGridViewAdapter1);
        mGv2.setAdapter(tabGridViewAdapter2);
    }

    @Override
    public void initListener() {
        mGv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("clik","clid");

                tabGridViewAdapter2.add((String)tabGridViewAdapter1.getItem(i));
                tabGridViewAdapter1.delete(i);
            }
        });

        mGv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("clik","clid");

                tabGridViewAdapter1.add((String)tabGridViewAdapter2.getItem(i));
                tabGridViewAdapter2.delete(i);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("titles", tabGridViewAdapter1.getTypes());
                intent.putExtra("invisible_titles", tabGridViewAdapter2.getTypes());
                setResult(1, intent);
                finish();
            }
        });

    }

    @Override
    public void binData() {

    }
}
