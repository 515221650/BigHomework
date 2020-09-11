package com.java.yangzhuoyi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.TextView;

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
import com.java.yangzhuoyi.adapter.SuggestArrayAdapter;
import com.java.yangzhuoyi.common.DefineView;
import com.java.yangzhuoyi.NewsSearchActivity;
import com.google.android.material.tabs.TabLayout;
import com.java.yangzhuoyi.util.SearchHistory;

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

    ListPopupWindow listPopupWindow;

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
        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setAdapter(new SuggestArrayAdapter(getContext()));
        listPopupWindow.setAnchorView(searchView);

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
                listPopupWindow.dismiss();
                Intent intent = new Intent(getActivity(), NewsSearchActivity.class);
                SearchHistory.saveSearchHistory(query, getContext());
                intent.putExtra("keyword", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });
        //监听输入内容焦点的变化
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("TextFocusChange",  " " +hasFocus);
                Log.d("get his num", ""+SearchHistory.getSearchHistory(getContext()).size());
                if(hasFocus) listPopupWindow.show();
                else listPopupWindow.dismiss();
            }
        });
//
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), NewsSearchActivity.class);
//                startActivity(intent);
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

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                Intent intent = new Intent(getActivity(), NewsSearchActivity.class);
                String keyword = SearchHistory.getSearchHistory(getContext()).get(position);
                intent.putExtra("keyword", keyword);
                SearchHistory.saveSearchHistory(keyword, getContext());
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
