package com.java.yangzhuoyi.adapter;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.java.yangzhuoyi.fragment.BaseFragment;

import java.util.List;

public class FixedPagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> fragments;
    private String[] titles;

    public FixedPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<BaseFragment> fragments)
    {
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("aa", position+"");
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = null;
        try{
            fragment = (Fragment)super.instantiateItem(container,position);
        } catch (Exception e) {
        }
        return fragment;
    }


    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public void updateList(List<BaseFragment> fragments, String[] titles) {
        this.fragments = fragments;
        this.titles = titles;
        Log.d("bb", this.fragments.size()+"");
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
