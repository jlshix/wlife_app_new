package com.jlshix.wlife_v03.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.JsonParser;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_add_order)
public class AddOrderActivity extends BaseActivity {

    private static final String TAG = "AddOrderActivity";

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    // 选择类型 根据添加模式或者语音命令显示或隐藏
    @ViewInject(R.id.type_linear)
    LinearLayout typeLinear;

    @ViewInject(R.id.equal)
    RadioButton equal;

    @ViewInject(R.id.include)
    RadioButton include;

    @ViewInject(R.id.add_notice)
    TextView notice;

    @ViewInject(R.id.voice_et)
    EditText name;

    @ViewInject(R.id.des)
    TextView desT;

    private Activity activity = AddOrderActivity.this;

    // 语音听写对象
    private SpeechRecognizer recognizer;
    // 语音听写UI
    private RecognizerDialog recognizerDialog;

    private String type;
    private int isEqual = 1;
    private String actions = "";
    private String des = "";

    private void setActions(String actions) {
        if (this.actions.equals("")) {
            this.actions = actions + ",";
        } else {
            this.actions += actions + ",";
        }
    }

    private void setDes(String des) {
        if (this.des.equals("")) {
            this.des = des + ",";
        } else {
            this.des += des + ",";
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        initView();

    }

    private void initView() {
        // 根据用途初始化
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        switch (type) {
            case "voice":
                setTitle("添加语音命令");
                typeLinear.setVisibility(View.VISIBLE);
                notice.setText("请输入或听写语音命令");
                break;
            case "mode":
                setTitle("添加模式");
                typeLinear.setVisibility(View.GONE);
                notice.setText("请输入模式");
                break;
            default:
                L.toast(activity, "INTENT_ERR");
                finish();
                break;
        }

        // RadioButton
        equal.setChecked(true);
        equal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isEqual = b ? 1 : 0;
            }
        });

        include.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isEqual = b ? 0 : 1;
            }
        });

        //voice
        // 初始化
        SpeechUtility.createUtility(activity, SpeechConstant.APPID + "=5794c319");

        recognizer = com.iflytek.cloud.SpeechRecognizer.createRecognizer(activity, mInitListener);
        recognizerDialog = new RecognizerDialog(activity, mInitListener);

    }

    @Event(R.id.help)
    private void help(View view) {
        new AlertDialog.Builder(activity).setMessage(R.string.equal_help)
                .setPositiveButton(R.string.confirm, null).create().show();
    }

    @Event(R.id.voice_btn)
    private void voice(View view) {
        setParam();
        recognizerDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if (!isLast) {
                    processVoice(recognizerResult);
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                Log.e(TAG, "recognizerDialogListener-->onError");
                Log.i(TAG, "onError: " + speechError.getPlainDescription(true));
            }
        });
        recognizerDialog.show();
    }

    /**
     * 处理语音识别结果
     * @param recognizerResult result
     */
    private void processVoice(RecognizerResult recognizerResult) {
        String text = JsonParser.parseIatResult(recognizerResult.getResultString());
        name.setText(text);
    }

    public void setParam() {
        Log.e(TAG, "setParam");
        // 清空参数
        recognizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        recognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

        // 设置语言
        recognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        recognizer.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置语音前端点
        recognizer.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点
        recognizer.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号 1为有标点 0为没标点
        recognizer.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径
        recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                Environment.getExternalStorageDirectory()
                        + "/xiaobailong24/xunfeiyun");
    }

    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                L.toast(activity, "初始化失败，错误码：" + code);
            }
        }
    };

    @Event(R.id.add_device)
    private void addDevice(View view) {
        startActivityForResult(new Intent(activity, DialogActivity.class), L.DIALOG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == L.DIALOG_REQUEST && resultCode == L.DIALOG_RETURN) {
            int code = data.getIntExtra("code", -1);
            switch (code) {
                case -1:
                    L.snack(toolbar, "return code err");
                    return;
                case 0:
                    L.snack(toolbar, "已取消");
                    return;
                case 1:
                    setActions(data.getStringExtra("action"));
                    setDes(data.getStringExtra("des"));
                    desT.setText(des.replace(',', '\n'));
            }
        }
    }

    @Event(R.id.add)
    private void add(View view) {
        if (name.getText().toString().trim().equals("") || desT.getText().toString().trim().equals("")) {
            L.toast(activity, "信息不完整");
            return;
        }
        RequestParams params = new RequestParams(L.URL_ADD_ORDER);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("name", name.getText().toString().trim());
        params.addParameter("type", type);
        params.addParameter("equal", isEqual);
        params.addParameter("actions", actions);
        params.addParameter("des", des);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (!result.optString("code").equals("1")) {
                    L.toast(activity, "CODE_ERR");
                    return;
                }
                AddOrderActivity.this.setResult(L.ADD_RETURN);
                AddOrderActivity.this.finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.toast(activity, ex.getMessage());
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
