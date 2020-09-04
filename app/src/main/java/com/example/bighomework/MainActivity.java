package com.example.bighomework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;

import com.example.bighomework.common.DefineView;

public class MainActivity extends AppCompatActivity implements DefineView {

    private RadioButton rb1,rb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initValidData();
        initListener();
        binData();
    }

    @Override
    public void initView() {
        rb1 = (RadioButton)findViewById(R.id.rb1);
        rb2 = (RadioButton)findViewById(R.id.rb2);

    }

    @Override
    public void initValidData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void binData() {

    }
}
