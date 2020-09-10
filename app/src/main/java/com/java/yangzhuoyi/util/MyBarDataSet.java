package com.java.yangzhuoyi.util;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class MyBarDataSet extends BarDataSet {


    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        if(getEntryForXValue(index, Float.NaN).getY() < 647000) // less than 95 green
            return mColors.get(7);
        if(getEntryForXValue(index, Float.NaN).getY() < 680000) // less than 95 green
            return mColors.get(6);
        else if(getEntryForXValue(index, Float.NaN).getY() < 696100) // less than 100 orange
            return mColors.get(5);
        else if(getEntryForXValue(index, Float.NaN).getY() < 900000) // less than 100 orange
            return mColors.get(4);
        else if(getEntryForXValue(index, Float.NaN).getY() < 2000000) // less than 100 orange
            return mColors.get(3);
        else if(getEntryForXValue(index, Float.NaN).getY() < 4300000) // less than 100 orange
            return mColors.get(2);
        else if(getEntryForXValue(index, Float.NaN).getY() < 6000000) // less than 100 orange
            return mColors.get(1);
        else // greater or equal than 100 red
            return mColors.get(0);
    }

}