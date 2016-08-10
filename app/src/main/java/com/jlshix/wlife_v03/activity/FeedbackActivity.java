package com.jlshix.wlife_v03.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

@ContentView(R.layout.activity_feedback)
public class FeedbackActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;

    @ViewInject(R.id.suggest_con)
    EditText con;

    @ViewInject(R.id.suggest_num)
    EditText num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Event(R.id.suggest_btn)
    private void submit(View view) {
        String content = con.getText().toString().trim();
        String number = num.getText().toString().trim();

        if (content.equals("")) {
            L.snack(toolbar, "请填写您的意见");
            return;
        }
        if (number.equals("")) {
            L.snack(toolbar, "请填写您的联系方式");
            return;
        }

        RequestParams params = new RequestParams(L.URL_FEEDBACK);
        params.addParameter("mail", L.getPhone());
        params.addParameter("contact", number);
        params.addParameter("msg", content);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (!result.optString("code").equals("1")) {
                    L.toast(FeedbackActivity.this, "CODE_ERR");
                    return;
                }
                L.toast(FeedbackActivity.this, "感谢您的反馈");
                finish();
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
