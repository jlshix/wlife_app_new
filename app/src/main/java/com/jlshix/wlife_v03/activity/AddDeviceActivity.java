package com.jlshix.wlife_v03.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.CaptureActivityAnyOrientation;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_add_device)
public class AddDeviceActivity extends BaseActivity {

    private static final String TAG = "AddDeviceActivity";

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.dev_imei)
    private EditText devImei;

    @ViewInject(R.id.dev_name)
    private EditText devName;

    @ViewInject(R.id.sign)
    private RadioGroup signs;

    @ViewInject(R.id.spinner)
    private Spinner spinner;
    
    @ViewInject(R.id.btn_add_dev)
    private Button devAdd;

    @ViewInject(R.id.type)
    private TextView devType;
    

    private int sign = 1;
    private int place = 1;

    private String type;
    private String no;
    private String state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        // 初始化选定sign
        ((RadioButton) signs.getChildAt(0)).setChecked(true);
        // 选定后更改sign值
        signs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.sign0:
                        sign = 1;
                        break;
                    case R.id.sign1:
                        sign = 2;
                        break;
                    case R.id.sign2:
                        sign = 3;
                        break;

                }
            }
        });
        
        // 选定后更改place值
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                place = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == L.DEV_IMEI_LENGTH) {
                    devAdd.setEnabled(true);
                } else {
                    devAdd.setEnabled(false);
                    devType.setText("");
                }

                if (s.length() >= 8) {
                    type = devImei.getText().toString().substring(4, 6);
                    no = devImei.getText().toString().substring(6, 8);
                    switch (type) {
                        case "02":
                            devType.setText("温湿度光照检测器");
                            state = "--------";
                            break;
                        case "04":
                            devType.setText("烟雾监测器");
                            state = "-";
                            break;
                        case "05":
                            devType.setText("红外防盗器");
                            state = "-";
                            break;
                        case "06":
                            devType.setText("窗帘控制器");
                            state = "-";
                            break;
                        case "09":
                            devType.setText("多级调光灯");
                            state = "0";
                            break;
                        case "0A":
                            devType.setText("智能插座");
                            state = "0000";
                            break;
                        default:
                            devType.setText("暂未支持，敬请期待");
                            devAdd.setEnabled(false);
                    }
                }


            }
        };
        // 设定监听器 显示类型 控制按钮是否可用
        devImei.addTextChangedListener(textWatcher);
    }


    // 扫描二维码
    @Event(R.id.scan)
    private void scanCode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(AddDeviceActivity.this);
        integrator.setPrompt("请扫描设备生成的二维码").setCaptureActivity(CaptureActivityAnyOrientation.class)
                .setOrientationLocked(true).initiateScan();
    }

    /**
     * 扫描二维码返回值
     * @param requestCode request
     * @param resultCode result
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && result != null) {
            String code = result.getContents();
            Log.i(TAG, "onActivityResult: " + code);
            devImei.setText(code);
        }

    }

    @Event(R.id.btn_add_dev)
    private void addDev(View view) {
        /**
         * imei 示例
         * 201302010809
         * 012345678901
         * 0123 45 67 8901
         * 年份4类型2编号2编号4 总计12
         */
        final String devImei1 = devImei.getText().toString();
        final String name1 = devName.getText().toString();
        if (devImei1.length() != L.DEV_IMEI_LENGTH || name1.length() == 0) {
            L.toast(AddDeviceActivity.this, "信息不完整");
            return;
        }

        type = devImei1.substring(4, 6);
        no = devImei1.substring(6, 8);

        RequestParams params = new RequestParams(L.URL_ADD_DEV);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("dev", devImei1);
        params.addParameter("type", type);
        params.addParameter("no", no);
        params.addParameter("name", name1);
        params.addParameter("state", state);
        params.addParameter("sign", sign);
        params.addParameter("place", place);

        // TODO: 2016/7/23 post 无效
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.i(TAG, "onSuccess: " + result.toString());
                try {
                    if (!result.getString("code").equals("1")) {
                        L.toast(AddDeviceActivity.this, "CODE_ERR");
                    } else {

                        // 结束
                        Intent intent = new Intent();
                        intent.putExtra("type", type);
                        intent.putExtra("name", name1);
                        intent.putExtra("state", state);
                        AddDeviceActivity.this.setResult(L.ADD_RETURN, intent);
                        AddDeviceActivity.this.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.toast(AddDeviceActivity.this, "ADD_ERR " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
}
