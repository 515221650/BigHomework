package com.java.yangzhuoyi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.java.yangzhuoyi.FavorActivity;
import com.java.yangzhuoyi.HistoryActivity;
import com.java.yangzhuoyi.MainActivity;
import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.TabGridViewActivity;
import com.java.yangzhuoyi.adapter.FixedPagerAdapter;
import com.java.yangzhuoyi.common.DefineView;
import com.java.yangzhuoyi.NewsSearchActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainInfoFragment extends BaseFragment implements DefineView {
    private View mView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FixedPagerAdapter fixedPagerAdapter;
    private String[] titles = new String[]{"news", "paper"};
    private String[] invisbleTitles = new String[]{};
    private List<BaseFragment> fragments;
    private SearchView searchView;

    FloatingActionButton fab, fabHis, fabFavor;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;

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
        searchView = mView.findViewById(R.id.search_view_news_main);
        fab = mView.findViewById(R.id.fab_more);
        fabFavor = mView.findViewById(R.id.fab_fav);
        fabHis = mView.findViewById(R.id.fab_his);
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("tagg", query);
                Intent intent = new Intent(getActivity(), NewsSearchActivity.class);
                intent.putExtra("keyword", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                animateFab();
            }
        });
        fabHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });
        fabFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FavorActivity.class);
                startActivity(intent);
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

    private void animateFab()
    {
        if(isOpen)
        {
            fab.startAnimation(rotateBackward);
            fabFavor.startAnimation(fabClose);
            fabHis.startAnimation(fabClose);
            fabHis.setClickable(false);
            fabFavor.setClickable(false);
            isOpen = false;
        }
        else
        {
            fab.startAnimation(rotateForward);
            fabFavor.startAnimation(fabOpen);
            fabHis.startAnimation(fabOpen);
            fabHis.setClickable(true);
            fabFavor.setClickable(true);
            isOpen = true;
        }
    }
}
