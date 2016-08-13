package com.jlshix.wlife_v03.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by Leo on 2016/8/13.
 * 按天统计
 */
@ContentView(R.layout.content_statistics)
public class DailyStatistics extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    @ViewInject(R.id.date_layout)
    private LinearLayout dateLayout;


    @ViewInject(R.id.tmp_chart)
    private BarChart tmpChart;

    @ViewInject(R.id.humi_chart)
    private BarChart humiChart;

    @ViewInject(R.id.light_chart)
    private BarChart lightChart;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateLayout.setVisibility(View.GONE);
        setupChart(tmpChart);
        setupChart(humiChart);
        setupChart(lightChart);
        tmpChart.setData(setData());
    }


    private void setupChart(BarChart chart) {
        chart.setDescription("");
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setXOffset(10f);
        xAxis.setAxisMinValue(-1f);
        xAxis.setAxisMaxValue(25f);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setEnabled(false);

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setAxisMinValue(0f);

        Legend l = chart.getLegend();
        l.setXEntrySpace(10f);
    }


    private BarData setData() {
        ArrayList<IBarDataSet> sets = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            entries.add(new BarEntry(i, i+1));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Tmp");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        sets.add(dataSet);
        return new BarData(sets);
    }

    @Override
    public void onRefresh() {

    }
}
