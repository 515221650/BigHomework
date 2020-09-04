package com.example.bighomework.fragment;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.bighomework.R;
import com.example.bighomework.TabGridViewActivity;
import com.example.bighomework.adapter.FixedPagerAdapter;
import com.example.bighomework.common.DefineView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainInfoFragment extends BaseFragment implements DefineView {
    private View mView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FixedPagerAdapter fixedPagerAdapter;
    private String[] titles = new String[]{"news", "paper", "event"};
    private String[] invisbleTitles = new String[]{};
    private List<BaseFragment> fragments;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null)
        {
            mView = inflater.inflate(R.layout.main_info_fragment_layout, container, false);
            initView();
            initValidData();
            initListener();
            binData();
        }
        return mView;
    }

    @Override
    public void initView() {
        tabLayout = (TabLayout)mView.findViewById(R.id.tab_layout);
        viewPager = (ViewPager)mView.findViewById(R.id.view_pager);

    }

    @Override
    public void initValidData() {
        fixedPagerAdapter = new FixedPagerAdapter(getChildFragmentManager());
        fixedPagerAdapter.setTitles(titles);
        fragments = new ArrayList<BaseFragment>();

        for(int i=0;i<titles.length;i++) {
            fragments.add(PageFragment.newInstance(titles[i]));
        }
        fixedPagerAdapter.setFragments(fragments);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setAdapter(fixedPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void initListener() {
        Button button = (Button)mView.findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TabGridViewActivity.class);
                intent.putExtra("types", titles);
                intent.putExtra("invisible_types", invisbleTitles);

                startActivityForResult(intent,1 );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        titles = data.getStringArrayExtra("titles");
        invisbleTitles = data.getStringArrayExtra("invisible_titles");
        update();
    }

    @Override
    public void binData() {

    }

    private void update()
    {
        fragments.clear();
        for (String tmp_title : titles) {
            fragments.add(PageFragment.newInstance(tmp_title));
        }
        Log.d("size", fragments.size()+"size");
        fixedPagerAdapter.updateList(fragments, titles);
    }
}
