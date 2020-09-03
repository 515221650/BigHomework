package com.example.bighomework.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bighomework.R;
import com.example.bighomework.common.DefineView;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;


public class PageFragment extends BaseFragment implements DefineView {
    private View mView;
    private static final String KEY = "EXTRA";
    private String message;
    private TextView tvPage;
    private RefreshLayout refreshLayout;

    public static PageFragment newInstance(String extra){
        Bundle bundle = new Bundle();
        bundle.putString(KEY, extra);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            message = bundle.getString(KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null)
        {
            mView = inflater.inflate(R.layout.page_fragment_layout, container, false);

            initView();
            initValidData();
            initListener();
            binData();
        }
        return mView;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void initView() {
        tvPage = (TextView)mView.findViewById(R.id.tv_page);
        if(message!=null)
        {
            tvPage.setText(message);
        }

        refreshLayout = (RefreshLayout)mView.findViewById(R.id.refreshLayout);
        BezierRadarHeader bezierRadarHeader = new BezierRadarHeader(getActivity()).setEnableHorizontalDrag(true);
//        bezierRadarHeader.setBackgroundColor(R.color.white);
//        bezierRadarHeader.setPrimaryColor(R.color.white);

        refreshLayout.setRefreshHeader(bezierRadarHeader);

        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setEnableOverScrollBounce(true);//是否启用越界回弹


    }

    @Override
    public void initValidData() {

    }

    @Override
    public void initListener() {
        //noinspection NullableProblems
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        //noinspection NullableProblems
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    @Override
    public void binData() {

    }
}


//public abstract class FixedPagerAdapter<T> extends FragmentStatePagerAdapter {
//
//    private List<ItemObject> mCurrentItems = new ArrayList<>();
//
//    public FixedPagerAdapter(FragmentManager fragmentManager) {
//        super(fragmentManager);
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        while (mCurrentItems.size() <= position) {
//            mCurrentItems.add(null);
//        }
//        Fragment fragment = (Fragment) super.instantiateItem(container, position);
//        ItemObject object = new ItemObject(fragment, getItemData(position));
//        mCurrentItems.set(position, object);
//        return object;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        mCurrentItems.set(position, null);
//        super.destroyItem(container, position, ((ItemObject) object).fragment);
//    }
//
//    @Override
//    public int getItemPosition(Object object) {
//        ItemObject itemObject = (ItemObject) object;
//        if (mCurrentItems.contains(itemObject)) {
//            T oldData = itemObject.t;
//            int oldPosition = mCurrentItems.indexOf(itemObject);
//            T newData = getItemData(oldPosition);
//            if (equals(oldData, newData)) {
//                return POSITION_UNCHANGED;
//            } else {
//                int newPosition = getDataPosition(oldData);
//                return newPosition >= 0 ? newPosition : POSITION_NONE;
//            }
//        }
//        return POSITION_UNCHANGED;
//    }
//
//    @Override
//    public void setPrimaryItem(ViewGroup container, int position, Object object) {
//        super.setPrimaryItem(container, position, ((ItemObject) object).fragment);
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return super.isViewFromObject(view, ((ItemObject) object).fragment);
//    }
//
//    public abstract T getItemData(int position);
//
//    public abstract int getDataPosition(T t);
//
//    public abstract boolean equals(T oldD, T newD);
//
//    public class ItemObject {
//
//        public Fragment fragment;
//        public T t;
//
//        public ItemObject(Fragment fragment, T t) {
//            this.fragment = fragment;
//            this.t = t;
//        }
//    }
//
//}