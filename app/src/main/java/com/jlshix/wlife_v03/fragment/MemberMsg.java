package com.jlshix.wlife_v03.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.BaseFragment;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 2016/7/22.
 * 显示来自家庭组的消息
 */
@ContentView(R.layout.fragment_member_msg)
public class MemberMsg extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @ViewInject(R.id.msg_list)
    private ListView list;

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    @ViewInject(R.id.msg_et)
    private EditText msg;

    public static final int LIST_END = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LIST_END:
                    list.setSelection(list.getBottom());
                    break;
            }
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        RequestParams params = new RequestParams(L.URL_GET_BOARD);
        params.addParameter("gate", L.getGateImei());
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
                                L.toast(getContext(), "CODE_ERROR");
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
                            item.put("name", object.optString("name"));
                            item.put("content", object.optString("content"));
                            item.put("dt", object.optString("dt"));
                            items.add(item);
                        }
                        SimpleAdapter adapter = new SimpleAdapter(getContext(), items,
                                R.layout.msg_member_list_item, new String[] {"name", "content", "dt"},
                                new int[] {R.id.name, R.id.content, R.id.dt});
                        list.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        L.toast(getContext(), ex.getMessage());
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

    @Event(R.id.msg_et)
    private void listEnd(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    handler.sendEmptyMessage(LIST_END);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Event(R.id.send)
    private void send(View view) {
        String content = msg.getText().toString().trim();
        if (content.equals("")) {
            L.toast(getContext(), "内容不能为空");
            return;
        }

        RequestParams params = new RequestParams(L.URL_ADD_BOARD);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("name", L.getName());
        params.addParameter("content", content);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                msg.setText("");
                onRefresh();
                // 滚动至底部
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                            handler.sendEmptyMessage(LIST_END);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.toast(getContext(), ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onRefresh() {
        getMsg(0);
    }
}
