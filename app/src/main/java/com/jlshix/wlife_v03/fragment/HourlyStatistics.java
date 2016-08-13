package com.jlshix.wlife_v03.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.jlshix.wlife_v03.data.StatisticsData;
import com.jlshix.wlife_v03.tool.BarMarker;
import com.jlshix.wlife_v03.tool.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Leo on 2016/8/13.
 * 按小时统计
 */

@ContentView(R.layout.content_statistics)
public class HourlyStatistics extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    @ViewInject(R.id.date_tv)
    private TextView dateText;

    @ViewInject(R.id.date_btn)
    private Button dateBtn;

    @ViewInject(R.id.tmp_chart)
    private BarChart tmpChart;

    @ViewInject(R.id.humi_chart)
    private BarChart humiChart;

    @ViewInject(R.id.light_chart)
    private BarChart lightChart;

    private List<StatisticsData> datas;

    public static final int REFRESH = 0x01;
    public static final int TMP = 0x02;
    public static final int HUMI = 0x03;
    public static final int LIGHT = 0x04;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    onRefresh();
                    break;
            }
        }
    };


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datas = new ArrayList<>();
        datas = getRawData();
        setupChart(tmpChart);
        setupChart(humiChart);
        setupChart(lightChart);
        tmpChart.setData(getSpecData(TMP));
        humiChart.setData(getSpecData(HUMI));
        lightChart.setData(getSpecData(LIGHT));
        tmpChart.animateXY(2000, 2000);
        humiChart.animateXY(2000, 2000);
        lightChart.animateXY(2000, 2000);

    }


    private void setupChart(BarChart chart) {
        chart.setDescription("");
        chart.setNoDataText("没有数据,请检查数据库");
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

//        YAxis axisLeft = chart.getAxisLeft();
//        axisLeft.setAxisMinValue(0f);

        BarMarker marker = new BarMarker(getContext(), R.layout.custom_marker_view);
        chart.setMarkerView(marker);

        Legend l = chart.getLegend();
        l.setXEntrySpace(10f);
    }


    private BarData getSpecData(int type) {
        ArrayList<IBarDataSet> sets = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            switch (type) {
                case TMP:
                    entries.add(new BarEntry(i, datas.get(i).getTemp()));
                    break;
                case HUMI:
                    entries.add(new BarEntry(i, datas.get(i).getHumi()));
                    break;
                case LIGHT:
                    entries.add(new BarEntry(i, datas.get(i).getLight()));
                    break;
            }
        }
        BarDataSet dataSet = new BarDataSet(entries, "Values");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        sets.add(dataSet);
        BarData data = new BarData(sets);
        data.setBarWidth(5f);
        data.setDrawValues(false);
        return new BarData(sets);
    }

    private List<StatisticsData> getRawData() {

        List<StatisticsData> datas = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 24; i++) {
            int tmp = r.nextInt(16) + 16;
            int humi = r.nextInt(50) + 20;
            int light = r.nextInt() % 100 + 1000;
            datas.add(new StatisticsData("01", tmp, humi, light, "0", "0"));
        }
        return datas;
    }

    @Override
    public void onRefresh() {
        datas.clear();
        datas = getRawData();
        updateCharts();
        swipe.setRefreshing(false);
    }

    private void updateCharts() {
        tmpChart.notifyDataSetChanged();
        humiChart.notifyDataSetChanged();
        lightChart.notifyDataSetChanged();
        tmpChart.setData(getSpecData(TMP));
        humiChart.setData(getSpecData(HUMI));
        lightChart.setData(getSpecData(LIGHT));
        tmpChart.animateXY(2000, 2000);
        humiChart.animateXY(2000, 2000);
        lightChart.animateXY(2000, 2000);
    }
}

