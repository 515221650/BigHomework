package com.java.yangzhuoyi.fragment;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.sequence.ISequenceFormat;
import com.bin.david.form.data.table.TableData;
import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.common.DefineView;
import com.java.yangzhuoyi.util.MyBarDataSet;
import com.java.yangzhuoyi.view.CustomScrollView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.tabs.TabLayout;

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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class DataFragment extends BaseFragment implements DefineView {
    private View mView;
    private BarChart barChart;
    private SmartTable<Pos> smartTableOut,smartTableIn;
    private String[] tabTxt = {"国外疫情直方图","国外疫情表格","国内疫情表格"};
    private TabLayout tabLayout;
    private CustomScrollView scrollView;
    private List<View> anchorList = new ArrayList<>();
    // 1由scrollview引起， 0由tablayout引起
    private boolean isScroll = true;
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    private int lastPos;
    List<Pos> chinaMap = new ArrayList<>();
    List<Pos> worldMap = new ArrayList<>();

    class Pos{
        String position;
        Integer confirmed,dead,cured;
        Pos(String position, int confirmed, int dead, int cured)
        {
            this.position = position;
            this.confirmed = confirmed;
            this.dead = dead;
            this.cured = cured;
        }
    }

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
            FetchData process = new FetchData();
            process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        return mView;
    }

    @Override
    public void initView() {

//        init_bar();

        smartTableIn = mView.findViewById(R.id.table_in);
        smartTableOut = mView.findViewById(R.id.table_out);
        barChart = mView.findViewById(R.id.bar_cart);
        init_table(smartTableIn, "国内疫情数据");
        init_table(smartTableOut, "国外疫情数据");
        anchorList.add(mView.findViewById(R.id.ll_bar_char));
        anchorList.add(mView.findViewById(R.id.ll_table_out));
        anchorList.add(mView.findViewById(R.id.ll_table_in));

        tabLayout =  mView.findViewById(R.id.data_tab_layout);
        scrollView = (CustomScrollView)mView.findViewById(R.id.scrollView);
    }


    @SuppressLint("StaticFieldLeak")
    public class FetchData extends AsyncTask<Void, Void, Void> {
        List<Pos> in_chinaMap = new ArrayList<>();
        List<Pos> in_worldMap = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
//                Log.d("url", "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=");
                StringBuilder data = new StringBuilder();
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(7000);
                    httpURLConnection.setReadTimeout(20000);
                    InputStream inputStream = httpURLConnection.getInputStream();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while (line != null) {
                        line = bufferedReader.readLine();
                        data.append(line);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    JSONObject JO = new JSONObject(data.toString());
                    Iterator iterator = JO.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        String[] poss = key.split("\\|");
                        JSONObject value = (JSONObject) JO.get(key);
                        JSONArray jsonArray = value.getJSONArray("data");
                        jsonArray = jsonArray.getJSONArray(jsonArray.length()-1);
                        if (poss.length == 2 && poss[0].equals("China"))
                        {
                            in_chinaMap.add(new Pos(poss[1], jsonArray.getInt(0),jsonArray.getInt(3),jsonArray.getInt(2)));
                        }
                        else if(poss.length == 1 && !poss[0].equals("World"))
                        {
                            in_worldMap.add(new Pos(poss[0], jsonArray.getInt(0),jsonArray.getInt(3),jsonArray.getInt(2)));
                        }
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            chinaMap.addAll(in_chinaMap);
            worldMap.addAll(in_worldMap);
            chinaMap.sort(new Comparator<Pos>() {
                @Override
                public int compare(Pos pos, Pos t1) {
                    return t1.confirmed.compareTo(pos.confirmed);
                }
            });
            worldMap.sort(new Comparator<Pos>() {
                @Override
                public int compare(Pos pos, Pos t1) {
                    return t1.confirmed.compareTo(pos.confirmed);
                }
            });
//            Log.d("LIstsiz", worldMap.size()+"");
            init_bar(barChart, worldMap);
            init_table(smartTableIn,chinaMap,"国内疫情数据");
            init_table(smartTableOut,worldMap,"国外疫情数据");
        }
    }

    public void init_table(SmartTable<Pos> smartTable, String tableName)
    {
        smartTable.getConfig().setMinTableWidth(1100);
        smartTable.getConfig().setFixedCountRow(true);
        Column<Integer> column0 = new Column<>("国家", "position");
        Column<Integer> column1 = new Column<>("确诊", "confirmed");
        Column<Integer> column2 = new Column<>("治愈", "cured");
        Column<Integer> column3 = new Column<>("死亡", "dead");
        List<Pos> posList = new ArrayList<>();
        final TableData<Pos> tableData = new TableData<Pos>(tableName,posList,column0,column1,column2,column3);
        smartTable.setTableData(tableData);
    }

    public void init_table(SmartTable<Pos> smartTable, List<Pos> map, String tableName)
    {

        final List<String> nameList = new ArrayList<>();
        List<Pos> posList = map;
        Log.d("name2LIstsiz", tableName+" "+map.size()+"");
        Log.d("nameLIstsiz", tableName+" "+nameList.size()+"");


        Column<Integer> column0;
        if(tableName.equals("国内疫情数据"))column0= new Column<>("省份", "position");
        else column0= new Column<>("国家", "position");
        Column<Integer> column1 = new Column<>("确诊", "confirmed");
        Column<Integer> column2 = new Column<>("治愈", "cured");
        Column<Integer> column3 = new Column<>("死亡", "dead");
        final TableData<Pos> tableData = new TableData<Pos>(tableName,posList,column0, column1,column2,column3);
        //设置数据
        tableData.setYSequenceFormat(new ISequenceFormat() {
            @Override
            public void draw(Canvas canvas, int sequence, Rect rect, TableConfig config) {

            }

            @Override
            public String format(Integer integer) {

                if(integer<=nameList.size())return nameList.get(integer-1);
                return "";
            }
        });
        smartTable.getConfig().setShowXSequence(false);
        smartTable.getConfig().setShowYSequence(false);
        //table.setZoom(true,3);是否缩放
        ICellBackgroundFormat<Integer> backgroundFormat = new BaseCellBackgroundFormat<Integer>() {
            @Override
            public int getBackGroundColor(Integer position) {
                if(position%2 == 1){
                    return ContextCompat.getColor(getActivity(),R.color.lightBlue);
                }
                return TableConfig.INVALID_COLOR;

            }
            @Override
            public int getTextColor(Integer position) {
                if(position%2 == 1) {
                    return ContextCompat.getColor(getActivity(), R.color.white);
                }
                return TableConfig.INVALID_COLOR;
            }
        };
        smartTable.getConfig().setYSequenceCellBgFormat(backgroundFormat);
        smartTable.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
            @Override
            public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                if(cellInfo.row%2==0){
                    paint.clearShadowLayer();
                    paint.setColor(getResources().getColor(R.color.lightBlue));
                    canvas.drawRect(rect,paint);
                }
            }

            @Override
            public int getTextColor(CellInfo cellInfo) {
                return 0;
            }
        });

        smartTable.setTableData(tableData);
    }
    public void init_bar(BarChart barChart, List<Pos> map2){

        final List<Pos> map = map2.subList(0,7);
        Description description = new Description();
        description.setText("");
        description.setTextSize(18f);
        description.setTextAlign(Paint.Align.CENTER); // 文本居中对齐
        // 计算描述位置
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(outMetrics);
        Paint paint = new Paint();
        paint.setTextSize(18f);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        float x = outMetrics.widthPixels / 2;
        float y =  Utils.calcTextHeight(paint, "233") + Utils.convertDpToPixel(24);
        description.setPosition(x, y);
        barChart.setDescription(description);

        List<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i=0;i<map.size();i++){
            entries.add(new BarEntry(i, map.get(i).confirmed));
        }

        // xAxis
        XAxis xAxis = barChart.getXAxis();
//        xAxis.setAxisLineWidth(10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 位于底部
        xAxis.setDrawGridLines(false); // 不绘制X轴网格线
        xAxis.setAxisMinimum(-0.5f); // 最小值-0.3f，为了使左侧留出点空间
//        xAxis.setGranularity(1f); // 间隔尺寸1
        xAxis.setTextSize(7f); // 文本大小14
//        xAxis.setTypeface(Typeface.DEFAULT_BOLD); // 加粗字体
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int id = (int)value;
//                Log.d("?????", value+"");
                return map.get(id).position;
            }
        });
//        BarDataSet barDataSet1 = new BarDataSet(entries, "data");
//        barDataSet1.setColor(R.color.tabBack);
//        barDataSet1.setValueTextColor(R.color.black);

        MyBarDataSet set = new MyBarDataSet(entries, "data");
        int[] colors = new int[8];
        colors[0] = ContextCompat.getColor(getContext(), R.color.deepred);
        colors[7] = ContextCompat.getColor(getContext(), R.color.orange);
        colors[3] = ContextCompat.getColor(getContext(), R.color.orangered);
        colors[1] = colors[2] = ContextCompat.getColor(getContext(), R.color.red);
        colors[4] = colors[5] = colors[6] = ContextCompat.getColor(getContext(), R.color.orange);

        set.setColors(colors);
        set.setValueTextSize(10);


        // yAxis
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setAxisMinimum(0); // 最小值为0
        axisLeft.setAxisMaximum(map.get(0).confirmed); // 最大值为1200

        // 右侧Y轴
        barChart.getAxisRight().setEnabled(false); // 不启用



        BarData lineData = new BarData(set);
        lineData.setBarWidth(0.4f);
        barChart.setData(lineData);
        barChart.invalidate(); // refresh
    }

    @Override
    public void initValidData() {
        for(int i=0;i<tabTxt.length;i++)
        {
            tabLayout.addTab(tabLayout.newTab().setText(tabTxt[i]));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initListener() {
        scrollView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_UP){
                    isScroll = true;
                }
                return false;
            }
        });
        scrollView.setCallbacks(new CustomScrollView.Callbacks() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
                if(isScroll){
                    for(int i=tabTxt.length-1;i>=0;i--)
                    {
                        if(y>anchorList.get(i).getTop()-1)
                        {
                            if(i!=tabTxt.length-1&&y<anchorList.get(i+1).getTop())
                            {
                                setScrollPos(i);
                                break;
                            }
                            if(i==tabTxt.length-1)
                            {
                                setScrollPos(i);
                                break;
                            }
                        }
                    }
                }
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isScroll = false;
                int pos = tab.getPosition();
                int top = anchorList.get(pos).getTop();
                scrollView.smoothScrollTo(0, top);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setScrollPos(int newPos)
    {
        if(lastPos!=newPos)
        {
            tabLayout.setScrollPosition(newPos, 0, true);
        }
        lastPos = newPos;
    }



    @Override
    public void binData() {

    }
}
