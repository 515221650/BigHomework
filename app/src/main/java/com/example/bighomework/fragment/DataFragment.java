package com.example.bighomework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bighomework.R;
import com.example.bighomework.common.DefineView;

public class DataFragment extends BaseFragment implements DefineView {
    private View mView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null)
        {
            mView = inflater.inflate(R.layout.data_fragment, container, false);
            initView();
            initValidData();
            initListener();
            binData();
        }
        return mView;
    }

    @Override
    public void initView() {

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
