package com.jlshix.wlife_v03.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.jlshix.wlife_v03.App;
import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_add_appliance)
public class AddApplianceActivity extends BaseActivity {

    private static final String TAG = "AddApplianceActivity";

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

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
        int currentNo = App.sp.getInt("currentNo", 0);
        final String devImei1 = "20160F0" + (currentNo + 1) + "0001";
        App.sp.edit().putInt("currentNo", currentNo + 1).apply();
        state = "0";
        final String name1 = devName.getText().toString();
        if (devImei1.length() != L.DEV_IMEI_LENGTH || name1.length() == 0) {
            L.toast(AddApplianceActivity.this, "信息不完整");
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

        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.i(TAG, "onSuccess: " + result.toString());
                try {
                    if (!result.getString("code").equals("1")) {
                        L.toast(AddApplianceActivity.this, "CODE_ERR");
                    } else {

                        // 结束
                        Intent intent = new Intent();
                        intent.putExtra("type", type);
                        intent.putExtra("name", name1);
                        intent.putExtra("state", state);
                        AddApplianceActivity.this.setResult(L.ADD_RETURN, intent);
                        AddApplianceActivity.this.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.toast(AddApplianceActivity.this, "ADD_ERR " + ex.getMessage());
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
