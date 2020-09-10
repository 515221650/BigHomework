package com.java.yangzhuoyi.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.java.yangzhuoyi.NewsDetailActivity;
import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.adapter.NewsListAdapter;
import com.java.yangzhuoyi.common.DefineView;
import com.java.yangzhuoyi.database.NewsHistory;
import com.java.yangzhuoyi.database.NewsHistoryDao;
import com.java.yangzhuoyi.database.NewsHistoryDatabase;
import com.java.yangzhuoyi.util.NewsDigest;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class PageFragment extends BaseFragment implements DefineView {
    private View mView;
    private static final String KEY = "EXTRA";
    private String message;
    private TextView tvPage;
    private RefreshLayout refreshLayout;
    private ListView lvNews;
    public NewsListAdapter newsListAdapter;

    private int maxPage = 0;
    int maxPageBound = -1;
    private Boolean forward = false;

    private NewsHistoryDatabase newsHistoryDatabase;
    private NewsHistoryDao newsHistoryDao;

    public static PageFragment newInstance(String extra) {
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
        if (bundle != null) {
            message = bundle.getString(KEY);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        newsListAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.page_fragment_layout, container, false);

            initView();
            initValidData();
            initListener();
            binData();
            newsListAdapter.notifyDataSetChanged();
        }
        return mView;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void initView() {

        lvNews = (ListView) mView.findViewById(R.id.lv_newslist);
        newsListAdapter = new NewsListAdapter(getActivity());
        lvNews.setAdapter(newsListAdapter);


        refreshLayout = (RefreshLayout) mView.findViewById(R.id.refreshLayout);
        BezierRadarHeader bezierRadarHeader = new BezierRadarHeader(getActivity()).setEnableHorizontalDrag(true);
//        bezierRadarHeader.setBackgroundColor(R.color.white);
//        bezierRadarHeader.setPrimaryColor(R.color.white);

        refreshLayout.setRefreshHeader(bezierRadarHeader);

        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setEnableOverScrollBounce(true);//是否启用越界回弹

        FetchNewsList process = new FetchNewsList();
        process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    @Override
    public void initValidData() {
        newsHistoryDatabase = NewsHistoryDatabase.getDatabase(getActivity());
        newsHistoryDao = newsHistoryDatabase.getNewsHistoryDao();

    }

    @Override
    public void initListener() {
        //noinspection NullableProblems
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                forward = true;
                FetchNewsList process = new FetchNewsList();
                process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        //noinspection NullableProblems
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                forward = false;
                FetchNewsList process = new FetchNewsList();
                process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion,long arg3) {
                String clickId = newsListAdapter.newsDigestArrayList.get(postion).getId();
                newsListAdapter.newsDigestArrayList.get(postion).setHasRead(true);
                Intent intent = new Intent(arg0.getContext(), NewsDetailActivity.class);
                intent.putExtra("ID", clickId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void binData() {

    }


    @SuppressLint("StaticFieldLeak")
    private class FetchNewsList extends AsyncTask<Void, Void, Void> {
        String newsClass = "";
        final int pageSize = 20;

        @Override
        protected Void doInBackground(Void... voids) {
            newsClass = message.toLowerCase();
            if(forward)
            {
                getContent(1, true);
            }
            else
            {
                if(maxPageBound == -1 || maxPage < maxPageBound) {
                    maxPage++;
                }
                while(true)
                {
                    boolean res = getContent(maxPage, false);
                    if(res || (maxPageBound == maxPage))
                    {
                        break;
                    }
                    else maxPage++;
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(forward) refreshLayout.finishRefresh();
            else refreshLayout.finishLoadMore();
            newsListAdapter.notifyDataSetChanged();
        }

        private boolean getContent(int page, boolean ifforward)
        {
            boolean hasNewNews = false;
            try {
                URL url = new URL("https://covid-dashboard.aminer.cn/api/events/list?type="+newsClass+"&page="+page+"&size="+pageSize);
                StringBuilder data = new StringBuilder();
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(20000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while(line != null)
                    {
                        line = bufferedReader.readLine();
                        data.append(line);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    JSONObject JO = new JSONObject(data.toString());
                    JSONArray JA2 = (JSONArray) JO.get("data");

                    int NewsNum = JA2.length();
                    if(NewsNum < pageSize) maxPageBound = page;
                    int posCnt = 0;
                    for(int i=0; i<NewsNum; i++)
                    {
                        NewsDigest newsDigest = new NewsDigest();
                        JSONObject JO2 = (JSONObject) JA2.get(i);
                        newsDigest.setId((String) JO2.get("_id"));
                        String _title = (String) JO2.get("title");
                        boolean isEn = _title.substring(0, min(1, _title.length())).matches("^[a-zA-Z]*");
                        int base = 1;
                        if(isEn) base = 4;
                        newsDigest.setTitle((_title.substring(0, min(30*base, _title.length()))));
                        newsDigest.setTime((String) JO2.get("time"));
                        String _source = JO2.optString("source");
                        if(_source.equals("null"))
                            newsDigest.setSource("");
                        else newsDigest.setSource(_source);
                        String _content = (String) JO2.get("content");
                        newsDigest.setDigest((_content).substring(0, min(60*base, _content.length())));

                        List<NewsHistory> his = newsHistoryDao.getNewsWithId(newsDigest.getId());
                        if(his.size() != 0)
                        {
                            newsDigest.setHasRead(true);
                        }

                        if(!newsListAdapter.IdHashSet.contains(newsDigest.getId()))
                        {
                            hasNewNews = true;
                            newsListAdapter.IdHashSet.add(newsDigest.getId());
                            if(!ifforward)
                                newsListAdapter.newsDigestArrayList.add(newsDigest);
                            else
                            {
                                newsListAdapter.newsDigestArrayList.add(posCnt, newsDigest);
                                posCnt++;
                            }
                        }

                    }
                    httpURLConnection.disconnect();

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return hasNewNews;
        }

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