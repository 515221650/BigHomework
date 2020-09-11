package com.java.yangzhuoyi;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.java.yangzhuoyi.adapter.TabGridViewAdapter;
import com.java.yangzhuoyi.common.DefineView;

public class TabGridViewActivity extends AppCompatActivity implements DefineView {
    private GridView mGv1, mGv2;
    private TabGridViewAdapter tabGridViewAdapter1, tabGridViewAdapter2;
    private Button button;
    private LayoutTransition mTransitioner1, mTransitioner2;
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
        mTransitioner1 = new LayoutTransition();
        mGv1.setLayoutTransition(mTransitioner1);
        mTransitioner2 = new LayoutTransition();
        mGv2.setLayoutTransition(mTransitioner2);
        setTransition();
    }



    private void setTransition() {
        /**
         * 添加View时过渡动画效果
         */
//        ObjectAnimator addAnimator = ObjectAnimator.ofFloat(null, "rotationY", 0, 90,0).
//                setDuration(mTransitioner.getDuration(LayoutTransition.APPEARING));
//        mTransitioner.setAnimator(LayoutTransition.APPEARING, addAnimator);

        ObjectAnimator addAnimator1 = ObjectAnimator.ofFloat(null, "TranslationY", 400, 50).
                setDuration(mTransitioner1.getDuration(LayoutTransition.APPEARING));
        mTransitioner1.setAnimator(LayoutTransition.APPEARING, addAnimator1);


        ObjectAnimator addAnimator2 = ObjectAnimator.ofFloat(null, "TranslationY", -350, 50).
                setDuration(mTransitioner2.getDuration(LayoutTransition.APPEARING));
        mTransitioner2.setAnimator(LayoutTransition.APPEARING, addAnimator2);
        /**
         * 移除View时过渡动画效果
         */
//        ObjectAnimator removeAnimator = ObjectAnimator.ofFloat(null, "TranslationY", 0, 200).
//                setDuration(mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
//        mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, removeAnimator);

        /**
         * view 动画改变时，布局中的每个子view动画的时间间隔
         */
//        mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
//        mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 300);

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

                tabGridViewAdapter2.add((String)tabGridViewAdapter1.getItem(i));
                tabGridViewAdapter1.delete(i);
            }
        });

        mGv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
