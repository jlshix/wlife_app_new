package com.jlshix.wlife_v03.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_lock_log)
public class LockLogActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.msg_list)
    private ListView list;

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipe.setOnRefreshListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getMsg();
    }

    @Override
    public void onRefresh() {
        getMsg();
    }

    private void getMsg() {
        // 开始刷新
        swipe.setRefreshing(true);

        RequestParams params = new RequestParams(L.URL_LOCK_LOG);
        params.addParameter("gate", L.getGateImei());
        x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            String code = object.optString("code");
                            if (code.equals("1")) {
                                JSONArray info = object.optJSONArray("info");
                                setList(info, list);
                            } else {
                                L.toast(LockLogActivity.this, "LOCK_LOG_CODE_ERROR");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    /**
                     * 将消息显示到ListView
                     * @param info jsonArray
                     * @param list ListView
                     */
                    private void setList(JSONArray info, ListView list) {
                        List<Map<String, String>> items = new ArrayList<>();
                        for (int i = 0; i < info.length(); i++) {
                            JSONObject object = info.optJSONObject(i);
                            Map<String, String> item = new HashMap<>();
                            String name = object.optString("name");
                            String action = object.optString("action");
                            String msg;
                            if (action.equals("1")) {
                                msg = name + "打开了门锁";
                            } else {
                                msg = name + "关闭了门锁";
                            }
                            item.put("msg", msg);
                            item.put("dt", object.optString("dt"));
                            items.add(item);
                        }
                        SimpleAdapter adapter = new SimpleAdapter(LockLogActivity.this, items,
                                R.layout.msg_gate_list_item, new String[] {"msg", "dt"},
                                new int[] {R.id.msg, R.id.dt});
                        list.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        L.toast(LockLogActivity.this, ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
        // 结束
        swipe.setRefreshing(false);
    }
}
