package com.jlshix.wlife_v03.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.data.SimpleDeviceData;
import com.jlshix.wlife_v03.tool.BaseActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_dialog)
public class DialogActivity extends BaseActivity {

    @ViewInject(R.id.spinner_place)
    private AppCompatSpinner placeSpinner;
    
    @ViewInject(R.id.spinner_type)
    private AppCompatSpinner typeSpinner;
    
    @ViewInject(R.id.spinner_name)
    private AppCompatSpinner nameSpinner;
    
    @ViewInject(R.id.on_off)
    private SwitchCompat switchCompat;
    
    @ViewInject(R.id.plug_linear)
    private LinearLayout plugLinear;
    
    @ViewInject(R.id.light_linear)
    private LinearLayout lightLinear;
    
    @ViewInject(R.id.light_seek_bar)
    private AppCompatSeekBar seekBar;
    
    @ViewInject(R.id.light_text)
    private TextView lightText;

    Map<String, Integer> placeNo;
    Map<String, String> typeNo;
    List<SimpleDeviceData> list;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initView();
    }

    private void initView() {
        placeNo = new HashMap<>();
        placeNo.put("客厅", 1);
        placeNo.put("卧室", 2);
        placeNo.put("餐厅", 3);
        placeNo.put("厨房", 4);
        placeNo.put("卫生间", 5);

        typeNo = new HashMap<>();
        typeNo.put("烟雾监测器", "04");
        typeNo.put("红外防盗器", "05");
        typeNo.put("窗帘控制器", "06");
        typeNo.put("多级调光灯", "09");
        typeNo.put("智能插座", "0A");

        // 初始化
        list = new ArrayList<>();
        list.add(new SimpleDeviceData());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (i) {
                    case 0:
                        lightText.setText("关");
                        break;
                    case 1:
                        lightText.setText("暗");
                        break;
                    case 2:
                        lightText.setText("亮");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // adapter们
        ArrayAdapter<String> placeAdapter = new ArrayAdapter<>(DialogActivity.this,
                android.R.layout.simple_spinner_dropdown_item, DialogActivity.this.getResources().getStringArray(R.array.rooms_spinner));
        placeSpinner.setAdapter(placeAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(DialogActivity.this,
                android.R.layout.simple_spinner_dropdown_item, DialogActivity.this.getResources().getStringArray(R.array.device_list));
        typeSpinner.setAdapter(typeAdapter);

        // 初始
        String [] names = list2Names();
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(DialogActivity.this,
                android.R.layout.simple_spinner_dropdown_item, names);
        nameSpinner.setAdapter(nameAdapter);

        // type 显示三类之一
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        switchCompat.setVisibility(View.GONE);
                        plugLinear.setVisibility(View.GONE);
                        lightLinear.setVisibility(View.GONE);
                        return;
                    case 1:
                    case 2:
                    case 3:
                        switchCompat.setVisibility(View.VISIBLE);
                        plugLinear.setVisibility(View.GONE);
                        lightLinear.setVisibility(View.GONE);
                        break;
                    case 4:
                        switchCompat.setVisibility(View.GONE);
                        plugLinear.setVisibility(View.GONE);
                        lightLinear.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        switchCompat.setVisibility(View.GONE);
                        plugLinear.setVisibility(View.VISIBLE);
                        lightLinear.setVisibility(View.GONE);
                        break;
                }

                getNameList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    /**
     * 连接数据库获取传感器名与编号放入list
     */
    private void getNameList() {
        list.clear();
        list.add(new SimpleDeviceData());

        // 获取当前文字?
        final String placeK = placeSpinner.getSelectedItem().toString();
        final int place = placeNo.get(placeK);
        final String typeK = typeSpinner.getSelectedItem().toString();
        String type = typeNo.get(typeK);

        // 生成请求参数
        RequestParams params = new RequestParams(L.URL_GET_NAME_LIST);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("place", place);
        params.addParameter("type", type);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                // 输出结果 onSuccess有延时, 应放入内部
                Log.i("DialogActivity", "onSuccess: " + result.toString());
                if (!result.optString("code").equals("1")) {
                    L.toast(DialogActivity.this, "DIALOG_CODE_ERR");
                    return;
                }

                JSONArray info = result.optJSONArray("info");
                for (int i = 0; i < info.length(); i++) {
                    JSONObject obj = info.optJSONObject(i);
                    list.add(new SimpleDeviceData(obj.optString("no"), obj.optString("name")));
                }
                String [] names = list2Names();
                ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(DialogActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, names);
                nameSpinner.setAdapter(nameAdapter);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.toast(DialogActivity.this, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private String[] list2Names() {
        String[] res = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i).getName();
        }
        return res;
    }

    @Event(R.id.positive)
    private void positive(View view) {
        String stateK = null;
        String state = null;
        if (switchCompat.getVisibility() == View.VISIBLE) {
            state = switchCompat.isChecked() ? "0001":"0000";
            stateK = switchCompat.isChecked() ? "开":"关";
        } else if (plugLinear.getVisibility() == View.VISIBLE) {
            state = getPlugState(plugLinear);
            stateK = getPlugStateK(plugLinear);
        } else if (lightLinear.getVisibility() == View.VISIBLE) {
            state = getLightState(lightLinear);
            stateK = getLightStateK(lightLinear);
        }
        String placeK = placeSpinner.getSelectedItem().toString();
        String typeK = typeSpinner.getSelectedItem().toString();
        String nameK = nameSpinner.getSelectedItem().toString();

        String typeString = typeNo.get(typeK);
        String noString = getTypeNoFromList(list, nameK);
        if (noString.equals("00")) {
            L.toast(DialogActivity.this, "请选择设备名");
            return;
        }

        String action = typeString + noString + state;
        String des = placeK + "-" + typeK + "-" + nameK + "-" + stateK ;

        Intent intent = new Intent();
        intent.putExtra("code", 1);
        intent.putExtra("action", action);
        intent.putExtra("des", des);
        setResult(L.DIALOG_RETURN, intent);
        finish();
    }

    private String getLightStateK(LinearLayout lightLinear) {
        AppCompatSeekBar seekBar = (AppCompatSeekBar) lightLinear.getChildAt(0);
        int progress = seekBar.getProgress();
        switch (progress) {
            case 0:
                return "关";
            case 1:
                return "暗";
            case 2:
                return "亮";

        }
        return "an";
    }
    private String getLightState(LinearLayout lightLinear) {
        AppCompatSeekBar seekBar = (AppCompatSeekBar) lightLinear.getChildAt(0);
        return "000" + seekBar.getProgress();
    }


    private String getPlugStateK(LinearLayout plugLinear) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plugLinear.getChildCount(); i++) {
            SwitchCompat compat = (SwitchCompat)plugLinear.getChildAt(i);
            sb.append(compat.isChecked() ? "开":"关");
        }
        return sb.toString();
    }
    private String getPlugState(LinearLayout plugLinear) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plugLinear.getChildCount(); i++) {
            SwitchCompat compat = (SwitchCompat)plugLinear.getChildAt(i);
            sb.append(compat.isChecked() ? "1":"0");
        }
        return sb.toString();
    }

    private static String getTypeNoFromList(List<SimpleDeviceData> list, String name) {
        for (SimpleDeviceData data : list) {
            if (data.getName().equals(name)) {
                return data.getNo();
            }
        }
        return "00";
    }

    @Event(R.id.negative)
    private void negative(View view) {
        Intent intent = new Intent();
        intent.putExtra("code", 0);
        setResult(L.DIALOG_RETURN, intent);
        finish();
    }
}
