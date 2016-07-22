package com.jlshix.wlife_v03.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.BaseFragment;
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

/**
 * Created by Leo on 2016/7/22.
 * 显示来自网关的消息
 */

@ContentView(R.layout.fragment_gate_msg)
public class GateMsg extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @ViewInject(R.id.msg_list)
    private ListView list;

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    @ViewInject(R.id.spinner)
    private Spinner spinner;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipe.setOnRefreshListener(this);
        String[] entries = getResources().getStringArray(R.array.gate_msg_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner, entries);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getMsg(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                            item.put("msg", object.optString("msg"));
                            item.put("dt", object.optString("dt"));
                            items.add(item);
                        }
                        SimpleAdapter adapter = new SimpleAdapter(getContext(), items,
                                R.layout.msg_gate_list_item, new String[] {"msg", "dt"},
                                new int[] {R.id.msg, R.id.dt});
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

    @Override
    public void onRefresh() {
        getMsg(0);
    }
}
