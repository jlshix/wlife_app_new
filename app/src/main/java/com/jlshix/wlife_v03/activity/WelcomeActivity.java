package com.jlshix.wlife_v03.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.jlshix.wlife_v03.R;

import cn.jpush.android.api.JPushInterface;

/**
 * 欢迎界面 只有一张图
 * 后台判断逻辑
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在setContentView之前
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 设定布局
        setContentView(R.layout.activity_welcome);

        // 跳转
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(WelcomeActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(WelcomeActivity.this);
    }
}
