package com.example.bighomework.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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
import androidx.core.content.ContextCompat;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.table.TableData;
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
    private SmartTable<User> smartTable;

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
        init_table();
    }

    class User{
        String name,portrait;
        Integer age;
        Long time=12L;
    }

    public void init_table()
    {
        Column<String> column1 = new Column<>("姓名", "name");
        Column<Integer> column2 = new Column<>("年龄", "age");
        Column<Long> column3 = new Column<>("更新时间", "time");
        Column<String> column4 = new Column<>("头像", "portrait");
        //如果是多层，可以通过.来实现多级查询
        Column<String> column5 = new Column<>("班级", "class.className");
        //组合列
        Column totalColumn1 = new Column("组合列名",column1,column2);
        //表格数据 datas是需要填充的数据
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        final TableData<User> tableData = new TableData<>("表格名",userList,totalColumn1,column3);
        //设置数据
        smartTable = mView.findViewById(R.id.table);
        //table.setZoom(true,3);是否缩放
        ICellBackgroundFormat<Integer> backgroundFormat = new BaseCellBackgroundFormat<Integer>() {
            @Override
            public int getBackGroundColor(Integer position) {
                if(position%2 == 1){
                    return ContextCompat.getColor(getActivity(),R.color.tabBack);
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
                    paint.setColor(getResources().getColor(R.color.tabBack));
                    Log.d("??",paint.getColor()+"");
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
