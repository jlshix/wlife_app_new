package com.jlshix.wlife_v03.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.adapter.EnvirAdapter;
import com.jlshix.wlife_v03.data.EnvirData;
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
import java.util.List;

/**
 * Created by Leo on 2016/6/14.
 * 环境 Fragment
 */

@ContentView(R.layout.fragment_envir)
public class Envir extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "ENVIR_FRAG";
    @ViewInject(R.id.recycler)
    private RecyclerView recycler;

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    // 准备的数据
    private List<EnvirData> list;

    // Adapter
    private EnvirAdapter adapter;

    private static final int REFRESH = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    swipe.setRefreshing(true);
                    getData();
                    swipe.setRefreshing(false);
                    break;
            }
        }
    };


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipe.setOnRefreshListener(this);
        list = new ArrayList<>();
        list.add(new EnvirData());
        initView();
    }

    private void initView() {
        adapter = new EnvirAdapter(getContext(), list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyItemRangeChanged(0, list.size());
        handler.sendEmptyMessage(REFRESH);
    }

    /**
     * 获取数据并显示
     */
    private void getData() {
        list.clear();
        RequestParams params = new RequestParams(L.URL_GET);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("type", "02");
        x.http().post(params,
                new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            if (!object.optString("code").equals("1")) {
                                L.toast(getContext(), "PLUG_CODE_ERR");
                                return;
                            }
                            json2List(object.optJSONArray("info"));
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    private void json2List(JSONArray info) {
                        list.clear();
                        for (int i = 0; i < info.length(); i++) {
                            JSONObject object = info.optJSONObject(i);
                            String name = object.optString("name");
                            String state = object.optString("state");
                            int sign = object.optInt("sign");
                            int placeNo = object.optInt("place");
                            list.add(new EnvirData(name, state, sign, placeNo));
                        }
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
        handler.sendEmptyMessage(REFRESH);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}