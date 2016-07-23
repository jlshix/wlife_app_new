package com.jlshix.wlife_v03.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

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

@ContentView(R.layout.activity_gate_bind)
public class GateBindActivity extends BaseActivity {

    private static final String TAG = "GATE_BIND_ACTIVITY";

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;

    @ViewInject(R.id.gate_imei)
    private EditText gateImei;

    @ViewInject(R.id.gate_name)
    private EditText gateName;

    @ViewInject(R.id.scan)
    private Button scan;

    @ViewInject(R.id.name)
    private RadioGroup name;

    @ViewInject(R.id.btn_add_gate)
    private Button gateAdd;

    private String[] names = {
            "我的设备", "我家主控", "客厅网关"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        name.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.name0:
                        gateName.setText(names[0]);
                        break;
                    case R.id.name1:
                        gateName.setText(names[1]);
                        break;
                    case R.id.name2:
                        gateName.setText(names[2]);
                        break;
                }
            }
        });

    }

    // 扫描二维码
    @Event(R.id.scan)
    private void scanCode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(GateBindActivity.this);
        integrator.setPrompt("请扫描设备生成的二维码").setCaptureActivity(CaptureActivityAnyOrientation.class)
                .setOrientationLocked(true).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && result != null) {
            String code = result.getContents();
            Log.i(TAG, "onActivityResult: " + code);
            gateImei.setText(code);
        }

    }

    // 添加网关
    @Event(R.id.btn_add_gate)
    private void bindGate(View view) {
        final String gateImei1 = gateImei.getText().toString();
        String name1 = gateName.getText().toString();
        if (gateImei1.length() == 0 || name1.length() == 0) {
            L.toast(GateBindActivity.this, "信息不完整");
            return;
        }
        // TODO: 2016/7/12 post 接口更改
        RequestParams params = new RequestParams(L.URL_GATE_BIND);
        params.addParameter("mail", L.getPhone());
        params.addParameter("gate", gateImei1);
        params.addParameter("name", name1);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (!result.getString("code").equals("1")) {
                        L.toast(GateBindActivity.this, "CODE_ERR");
                    } else {
                        // 绑定成功后设定配置文件内容
                        L.setGateImei(gateImei1);
                        // 结束
                        GateBindActivity.this.setResult(L.ADD_RETURN);
                        GateBindActivity.this.finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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

