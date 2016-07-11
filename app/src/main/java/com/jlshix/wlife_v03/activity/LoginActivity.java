package com.jlshix.wlife_v03.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private String TAG = "LOGIN_ACTIVITY";
    /**
     * 将要写入sp的内容
     */
    private String userMail,userName, gateImei;

    /**
     * 无阴影标题栏
     */
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;


    /**
     * 邮箱
     */
    @ViewInject(R.id.mail)
    private EditText phone;

    /**
     * 密码
     */
    @ViewInject(R.id.pw)
    private EditText secret;

    /**
     * 记住密码
     */
    @ViewInject(R.id.remember)
    private AppCompatCheckBox remember;

    /**
     * 自动登录
     */
    @ViewInject(R.id.auto_login)
    private AppCompatCheckBox autoLogin;

    /**
     * 正在登录...
     */
    private ProgressDialog dialog;

    private Context context = LoginActivity.this;

    private static final int DIALOG = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DIALOG:
                    dialog = ProgressDialog.show(context,null, "正在登录", true, true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        initView();

        // 若设为自动登录则直接登录
        if (L.isAutoLogin()) {
            login(L.getPhone(), L.getPw());
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {

        // 保存设置到配置文件
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                L.setRemember(b);
            }
        });

        // 记住密码与自动登录的显示逻辑
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                L.setAutoLogin(b);
                if (b) {
                    remember.setChecked(true);
                    remember.setEnabled(false);
                } else {
                    remember.setChecked(false);
                    remember.setEnabled(true);
                }
            }
        });

        // 设定当前状态
        phone.setText(L.getPhone());
        remember.setChecked(L.isRemember());
        autoLogin.setChecked(L.isAutoLogin());
        if (L.isRemember()) {
            secret.setText(L.getPw());
        }
    }

    /**
     * 登录动作
     * @param v view
     */
    @Event(R.id.btn_login)
    private void preLogin(View v) {
        final String name = phone.getText().toString().trim();
        final String pw = secret.getText().toString().trim();
        if (name.equals("") || pw.equals("")) {
            L.snack(toolbar, "信息不完整");
            return;
        }

        // 记住账号
        L.setPhone(name);
        // 记住密码
        if (L.isRemember()) {
            L.setPw(pw);
        }
        /*
        //登录
        login(name, pw);
*/
        handler.sendEmptyMessage(DIALOG);
        gateImei = "4718";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    dialog.dismiss();
                    jump();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 登录 调用验证与跳转
     * @param name 用户名
     * @param pw 密码
     */
    private void login(final String name, final String pw) {
        // 显示正在登录
        handler.sendEmptyMessage(DIALOG);

        // 生成参数
        RequestParams params = new RequestParams(L.URL_LOGIN);
        params.addParameter("mail", name);
        params.addParameter("pw", pw);

        // 发送请求
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                String code = result.optString("code", "x");
                if (code.equals("1")) {
                    JSONObject content = result.optJSONObject("info");
                    userMail = content.optString("mail", null);
                    userName = content.optString("name", null);
                    gateImei = content.optString("gate_imei", null);
                    Log.e(TAG, "onSuccess: " + userMail + "--" + userName + "--" + gateImei);
                }
                // dismiss dialog
                dialog.dismiss();
                switch (code) {
                    case "1":
                        jump();
                        break;
                    case "-1":
                        L.snack(toolbar, "内部错误");
                        break;
                    case "3":
                        L.snack(toolbar, "密码错误");
                        break;
                    case "2":
                        L.snack(toolbar, "未注册");
                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.toast(LoginActivity.this, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 跳转
     */
    private void jump() {
        // TODO 写入数据 √ 极光推送 环信
        L.setLogin(true);
        L.setName(userName);
        L.setPhone(userMail);
        L.setPw(secret.getText().toString().trim());
        L.setGateImei(gateImei);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 注册
     * @param view view
     */
    @Event(R.id.new_usr)
    private void toReg(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 1000);
    }

    /**
     * 微信登录开发中
     * @param view view
     */
    @Event(R.id.wx_usr)
    private void wxLogin(View view) {
        L.snack(toolbar, "开发中");
    }

    /**
     * 注册返回
     * @param requestCode 请求码
     * @param resultCode 返回码
     * @param data 返回数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 2000) {
            phone.setText(data.getStringExtra("mail"));
            secret.setText(data.getStringExtra("pw"));
            L.snack(toolbar, "注册成功 点击登录");
        }
    }

}
