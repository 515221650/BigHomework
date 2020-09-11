package com.java.yangzhuoyi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.java.yangzhuoyi.util.Expert;

public class ExpertActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expert_detail);
        Button buttonBack = findViewById(R.id.btn_news_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        Expert expert = (Expert) intent.getSerializableExtra("expert");

        TextView enName = findViewById(R.id.expert_en_name);
        enName.setText(expert.name);
        TextView name = findViewById(R.id.expert_name);
        name.setText(expert.name_zh);
        TextView bio = findViewById(R.id.bio);
        bio.setText(expert.bio);
        TextView edu = findViewById(R.id.edu);
        edu.setText(expert.edu);
        TextView work = findViewById(R.id.work);
        work.setText(expert.work);
        TextView ali = findViewById(R.id.affiliation);
        ali.setText(expert.affiliation);
        ImageView avatar = findViewById(R.id.avatar);
        Glide.with(this).load(expert.avatar).into(avatar);


    }
}
