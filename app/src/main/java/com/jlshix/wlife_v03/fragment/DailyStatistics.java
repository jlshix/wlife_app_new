package com.jlshix.wlife_v03.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Leo on 2016/8/13.
 * 按天统计
 */
@ContentView(R.layout.content_statistics)
public class DailyStatistics extends BaseFragment {

    private static final String TAG = "DailyFragment";

    @ViewInject(R.id.date_layout)
    private LinearLayout dateLayout;

    @ViewInject(R.id.count)
    private Spinner spinner;

    @ViewInject(R.id.count_tv)
    TextView countText;

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

        dateLayout.setVisibility(View.GONE);

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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        getRawData(devNo, 5);
                        break;
                    case 1:
                        getRawData(devNo, 10);
                        break;
                    case 2:
                        getRawData(devNo, 20);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        chart.setNoDataText("没有相应数据");
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

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMinValue(0f);

        Legend l = chart.getLegend();
        l.setXEntrySpace(10f);
    }


    private BarData getSpecData(int type, int count) {
        ArrayList<IBarDataSet> sets = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();

        ArrayList<StatisticsData> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < count; i++) {
            boolean sign = false;
            for (int j = 0; j < datas.size(); j++) {
                if (Integer.parseInt(datas.get(j).getDate().substring(8)) == today) {
//                    datas.get(j).getDate().substring(8).equals(String.valueOf(today))
                    list.add(i, datas.get(j));
                    sign = true;
                }
            }
            if (!sign) {
                list.add(new StatisticsData(devNo));
            }
            // TODO 跨月有问题
            today--;
        }
        Collections.reverse(list);

        int size = list.size();
        list.add(size, list.get(size-1));
        for (int k = list.size() - 2; k > 0 ; k--) {
            list.set(k, list.get(k-1));
        }
        list.set(0, new StatisticsData(devNo));



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

    private void getRawData(String no, final int count) {
        datas.clear();
        countText.setText("注: 第" + count + "天为当天的数据");
        RequestParams params = new RequestParams(L.URL_STATISTICS);
        params.addParameter("date", getToday());
        params.addParameter("gate", L.getGateImei());
        params.addParameter("no", no);
        params.addParameter("count", String.valueOf(count));
        params.setMethod(HttpMethod.POST);
        Log.i(TAG, "getRawData: " + params.toString());
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
                Log.i(TAG, "onSuccess: " + datas.toString());
                // 更新图表
                updateCharts(count);
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

    private void updateCharts(int count) {
        tmpChart.setData(getSpecData(TMP, getCount()));
        humiChart.setData(getSpecData(HUMI, getCount()));
        lightChart.setData(getSpecData(LIGHT, getCount()));
        tmpChart.notifyDataSetChanged();
        humiChart.notifyDataSetChanged();
        lightChart.notifyDataSetChanged();
        tmpChart.getXAxis().setAxisMaxValue(count+1);
        humiChart.getXAxis().setAxisMaxValue(count+1);
        lightChart.getXAxis().setAxisMaxValue(count+1);
        tmpChart.animateXY(2000, 2000);
        humiChart.animateXY(2000, 2000);
        lightChart.animateXY(2000, 2000);
    }

    private int getCount() {
        switch (spinner.getSelectedItemPosition()) {
            case 0:
                return 5;
            case 1:
                return 10;
            case 2:
                return 20;
            default:
                return 1;
        }
    }
}
