package com.example.bighomework.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bighomework.R;
import com.example.bighomework.common.DefineView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class DataFragment extends BaseFragment implements DefineView {
    private View mView;
    private BarChart barChart;

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
        init_bar();
    }

    public void init_bar(){

        barChart = (BarChart)mView.findViewById(R.id.bar_cart);

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
        final String[] countries = {"美国", "中国"};
        int[] values = {200,100};
        for (int i=0;i<values.length;i++){
            entries.add(new BarEntry(i, values[i]));
        }

        // xAxis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 位于底部
        xAxis.setDrawGridLines(false); // 不绘制X轴网格线
        xAxis.setAxisMinimum(-0.3f); // 最小值-0.3f，为了使左侧留出点空间
        xAxis.setGranularity(1f); // 间隔尺寸1
        xAxis.setTextSize(14f); // 文本大小14
        xAxis.setTypeface(Typeface.DEFAULT_BOLD); // 加粗字体
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int id = (int)value;
                Log.d("?????", value+"");
                return countries[id];
            }
        });
        BarDataSet barDataSet1 = new BarDataSet(entries, "data");
        barDataSet1.setColor(R.color.tabBack);
        barDataSet1.setValueTextColor(R.color.black);

        // yAxis
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setAxisMinimum(0); // 最小值为0
        axisLeft.setAxisMaximum(300); // 最大值为1200
        // 右侧Y轴
        barChart.getAxisRight().setEnabled(false); // 不启用


        BarData lineData = new BarData(barDataSet1);
        barChart.setData(lineData);
        barChart.invalidate(); // refresh

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
