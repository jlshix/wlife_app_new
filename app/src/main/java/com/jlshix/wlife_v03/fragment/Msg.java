package com.jlshix.wlife_v03.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
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
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 2016/6/14.
 * 消息 Fragment
 */

//设定布局文件
@ContentView(R.layout.fragment_msg)
public class Msg extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @ViewInject(R.id.msg_list)
    private ListView list;

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    private static final int REFRESH = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    getMsg();
                    swipe.setRefreshing(false);
                    break;
            }
        }
    };


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipe.setOnRefreshListener(this);
        getMsg();

    }

    /**
     * 获取消息列表
     */
    private void getMsg() {
        RequestParams params = new RequestParams(L.URL_GET_MSG + "?gate="+ L.getGateImei() + "&type=0");
        params.addParameter("gate", L.getGateImei());
        params.addParameter("type", "0");
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
                                R.layout.msg_list_item, new String[] {"msg", "dt"},
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
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        swipe.setRefreshing(true);
        handler.sendEmptyMessage(REFRESH);
    }
}
