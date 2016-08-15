package com.jlshix.wlife_v03.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
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
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Leo on 2016/8/13.
 * 按小时统计
 */

@ContentView(R.layout.content_statistics)
public class HourlyStatistics extends BaseFragment {

    private static final String TAG = "HourlyFragment";

    @ViewInject(R.id.count)
    private Spinner count;

    @ViewInject(R.id.date_tv)
    private TextView dateText;

    @ViewInject(R.id.tmp_chart)
    private BarChart tmpChart;

    @ViewInject(R.id.humi_chart)
    private BarChart humiChart;

    @ViewInject(R.id.light_chart)
    private BarChart lightChart;

    private List<StatisticsData> datas;
    private String devNo;

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public static final int REFRESH = 0x01;
    public static final int TMP = 0x02;
    public static final int HUMI = 0x03;
    public static final int LIGHT = 0x04;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        count.setVisibility(View.GONE);

        dateText.setText(getToday());
        // 图表基本设置
        setupChart(tmpChart);
        setupChart(humiChart);
        setupChart(lightChart);
        // Marker
        BarMarker tmpMarker = new BarMarker(getContext(), R.layout.custom_marker_view, "℃");
        tmpChart.setMarkerView(tmpMarker);
        BarMarker humiMarker = new BarMarker(getContext(), R.layout.custom_marker_view, "％");
        humiChart.setMarkerView(humiMarker);
        BarMarker lightMarker = new BarMarker(getContext(), R.layout.custom_marker_view, "Lx");
        lightChart.setMarkerView(lightMarker);

        // data
        datas = new ArrayList<>();
        getRawData(devNo, getToday());

    }

    private String getToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "-"
                + format(calendar.get(Calendar.MONTH) + 1) + "-"
                + format(calendar.get(Calendar.DAY_OF_MONTH));
    }


    /**
     * 格式化时间，标准两位
     * @param x 时间int
     * @return 时间String
     */
    private String format(int x) {
        if (x >= 0 && x < 10) {
            return "0" + x;
        } else {
            return String.valueOf(x);
        }
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

        Legend l = chart.getLegend();
        l.setXEntrySpace(10f);
    }

    @Event(R.id.date_btn)
    private void changeDate(View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String date = year + "-" + format(month + 1) + "-" + format(dayOfMonth);
                dateText.setText(date);
                getRawData(devNo, dateText.getText().toString());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private BarData getSpecData(int type) {
        ArrayList<IBarDataSet> sets = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();

        ArrayList<StatisticsData> list = new ArrayList<>();
//        for (int i = 0; i < datas.size(); i++) {
//            int no = Integer.parseInt(datas.get(i).getTime().substring(0, 2));
//            list.add(no, datas.get(i));
//        }
//        for (int i = 0; i < 23; i++) {
//            if (list.get(i) == null) {
//                list.add(i, new StatisticsData(devNo));
//            }
//        }
//
//        Log.i(TAG, "getSpecData: " + list.toString());

        for (int i = 0; i < 24; i++) {
//            boolean sign = false;
            for (int j = 0; j < datas.size(); j++) {
                if (Integer.parseInt(datas.get(j).getTime().substring(0,2)) == i) {
                    list.add(i, datas.get(j));
//                    sign = true;
                }
            }
//            if (!sign) {
//                list.add(i, new StatisticsData(devNo));
//            }
        }



        for (int i = 0; i < list.size(); i++) {
            switch (type) {
                case TMP:
                    entries.add(new BarEntry(i, list.get(i).getTemp()));
                    break;
                case HUMI:
                    entries.add(new BarEntry(i, list.get(i).getHumi()));
                    break;
                case LIGHT:
                    entries.add(new BarEntry(i, list.get(i).getLight()));
                    break;
            }
        }

        BarDataSet dataSet;
        switch (type) {
            case TMP:
                dataSet = new BarDataSet(entries, "温度");
                break;
            case HUMI:
                dataSet = new BarDataSet(entries, "湿度");
                break;
            case LIGHT:
                dataSet = new BarDataSet(entries, "光照");
                break;
            default:
                dataSet = new BarDataSet(entries, "Values");
                break;
        }

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        sets.add(dataSet);
        BarData data = new BarData(sets);
        data.setBarWidth(5f);
        data.setDrawValues(false);
        return new BarData(sets);
    }

    private void getRawData(String no, String date) {
        datas.clear();

        RequestParams params = new RequestParams(L.URL_STATISTICS);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("no", no);
        params.addParameter("date", date);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.i(TAG, "onSuccess: " + result.toString());
                if (!result.optString("code").equals("1")) {
                    L.toast(getContext(), "STATISTICS_RAW_DATA_CODE_ERR");
                    return;
                }
                JSONArray info = result.optJSONArray("info");
                for (int i = 0; i < info.length(); i++) {
                    JSONObject object = info.optJSONObject(i);
                    String state = object.optString("state");
                    String date = object.optString("date");
                    String time = object.optString("time");
                    datas.add(new StatisticsData(devNo, state, date, time));

                }
                // 更新图表
                updateCharts();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.toast(getContext(), "STATISTICS_RAW_DATA_ERR");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void updateCharts() {
        tmpChart.setData(getSpecData(TMP));
        humiChart.setData(getSpecData(HUMI));
        lightChart.setData(getSpecData(LIGHT));
        tmpChart.notifyDataSetChanged();
        humiChart.notifyDataSetChanged();
        lightChart.notifyDataSetChanged();
        tmpChart.animateXY(2000, 2000);
        humiChart.animateXY(2000, 2000);
        lightChart.animateXY(2000, 2000);
    }
}

