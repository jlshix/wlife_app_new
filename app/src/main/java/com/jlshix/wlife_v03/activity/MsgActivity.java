package com.jlshix.wlife_v03.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
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

@ContentView(R.layout.content_msg)
public class MsgActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

//    @ViewInject(R.id.toolbar)
//    Toolbar toolbar;

    @ViewInject(R.id.msg_list)
    private ListView list;

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipe.setOnRefreshListener(this);
        getMsg(0);
    }

    /**
     * 获取消息列表
     * 0 all, 1 sys, 3 envir, 2 security
     */
    private void getMsg(int type) {
        // 开始刷新
        swipe.setRefreshing(true);

        RequestParams params = new RequestParams(L.URL_GET_MSG);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("type", String.valueOf(type));
        x.http().post(params,
                new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            String code = object.optString("code");
                            if (code.equals("1")) {
                                JSONArray info = object.optJSONArray("info");
                                setList(info, list);
                            } else {
                                L.toast(MsgActivity.this, "CODE_ERROR");
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
                            item.put("msg", object.optString("msg"));
                            item.put("dt", object.optString("dt"));
                            items.add(item);
                        }
                        SimpleAdapter adapter = new SimpleAdapter(MsgActivity.this, items,
                                R.layout.msg_list_item, new String[] {"msg", "dt"},
                                new int[] {R.id.msg, R.id.dt});
                        list.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        L.toast(MsgActivity.this, ex.getMessage());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_msg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all:
                L.log("action_all");
                getMsg(0);
                break;
            case R.id.action_sys:
                L.log("action_sys");
                getMsg(1);
                break;
            case R.id.action_envir:
                L.log("action_envir");
                getMsg(3);
                break;
            case R.id.action_security:
                L.log("action_security");
                getMsg(2);
                break;
        }
        return true;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        getMsg(0);
    }

}
