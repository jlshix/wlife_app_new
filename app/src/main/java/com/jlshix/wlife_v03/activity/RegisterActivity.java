package com.jlshix.wlife_v03.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.L;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    private static final String TAG = "REGISTER_ACTIVITY";

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;

    @ViewInject(R.id.mail)
    private EditText mail;

    @ViewInject(R.id.pw)
    private EditText pw;

    @ViewInject(R.id.pw2)
    private EditText pw2;

    @ViewInject(R.id.name)
    private EditText name;

    /**
     * 注册中...
     */
    private ProgressDialog dialog;

    private Context context = RegisterActivity.this;

    private static final int DIALOG = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DIALOG:
                    dialog = ProgressDialog.show(context,null, "注册中...", true, true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        ActionBar bar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 注册
     * @param v view
     */
    @Event(R.id.reg)
    private void doReg(View v) {

        // 获取数据
        final String et_mail = mail.getText().toString().trim();
        final String et_pw = pw.getText().toString().trim();
        final String et_pw2 = pw2.getText().toString().trim();
        final String et_name = name.getText().toString().trim();

        // 简单排错
        if (!et_pw.equals(et_pw2)) {
            L.snack(toolbar, "密码不一致");
            return;
        }
        if (et_mail.equals("") || et_name.equals("") || et_pw.equals("") || et_pw2.equals("")) {
            L.snack(toolbar, "信息不完整");
            return;
        }

        // 显示dialog
        handler.sendEmptyMessage(DIALOG);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    dialog.dismiss();
                    success("jlshix@163.com", "wlife");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

/*      调试期间暂时跳过
//TODO post

        // 发起请求
        RequestParams params = new RequestParams(L.URL_REG);
        params.addParameter("mail", et_mail);
        params.addParameter("name", et_name);
        params.addParameter("pw", et_pw);
//        params.addHeader("mail", et_mail);
//        params.addHeader("name", et_name);
//        params.addHeader("pw", et_pw);
//        params.addBodyParameter("mail", et_mail);
//        params.addBodyParameter("name", et_name);
//        params.addBodyParameter("pw", et_pw);
        Log.i(TAG, "doReg: " + params.toString());
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                dialog.dismiss();
                String code = result.optString("code", "x");
                switch (code) {
                    case "1":
                        success(et_mail, et_pw);
                        break;
                    case "0":
                        L.snack(toolbar, "错误0");
                        break;
                    case "-1":
                        L.snack(toolbar, "错误-1");
                        break;
                    default:
                        L.snack(toolbar, "错误x");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog.dismiss();
//                L.toast(RegisterActivity.this, ex.getMessage());
                L.log(ex.getMessage());
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
*/


    }

    private void success(String mail, String pw) {
        Intent back = new Intent();
        back.putExtra("status", "1");
        back.putExtra("mail", mail);
        back.putExtra("pw", pw);
        setResult(2000, back);
        finish();
    }

}
