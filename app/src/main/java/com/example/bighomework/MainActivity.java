package com.example.bighomework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;

import com.example.bighomework.common.DefineView;
import com.example.bighomework.fragment.DataFragment;
import com.example.bighomework.fragment.MainInfoFragment;

import me.shihao.library.XRadioGroup;

public class MainActivity extends AppCompatActivity implements DefineView {

    private RadioButton rb1,rb2;
    private MainInfoFragment mainInfoFragment;
    private DataFragment dataFragment;
    private XRadioGroup radioGroup;
    private Fragment currentFragment;
    private SearchView searchView;

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
        radioGroup = (XRadioGroup)findViewById(R.id.main_rg);
        mainInfoFragment = new MainInfoFragment();
        dataFragment = new DataFragment();
        searchView = (SearchView)findViewById(R.id.search_view);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fragment_container, dataFragment);
        transaction.add(R.id.fragment_container, mainInfoFragment);

        transaction.hide(dataFragment);

        transaction.commit();

        currentFragment = mainInfoFragment;
    }

    @Override
    public void initValidData() {
        ImageView imageView = (ImageView)searchView.findViewById(R.id.search_button);
        imageView.setColorFilter(R.color.black);
    }


    @Override
    public void initListener() {
        radioGroup.setOnCheckedChangeListener(new XRadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(XRadioGroup radioGroup, int id) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (id)
                {
                    case R.id.rb_news:
                        transaction.hide(currentFragment).show(mainInfoFragment).commit();
                        currentFragment = mainInfoFragment;
                        break;
                    case R.id.rb_data:
                        transaction.hide(currentFragment).show(dataFragment).commit();
                        currentFragment = dataFragment;
                        break;
                    case R.id.rb_cate:
                        transaction.hide(currentFragment).show(dataFragment).commit();
                        break;
                    case R.id.rb_doctor:
                        transaction.hide(currentFragment).show(dataFragment).commit();
                        break;
                    case R.id.rb_relation:
                        transaction.hide(currentFragment).show(dataFragment).commit();
                        break;
                }
            }
        });
    }

    @Override
    public void binData() {

    }
}
