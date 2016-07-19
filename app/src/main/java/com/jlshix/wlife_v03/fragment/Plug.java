package com.jlshix.wlife_v03.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.adapter.PlugAdapter;
import com.jlshix.wlife_v03.data.PlugData;
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
 * 插座 Fragment
 */

//设定布局文件, 然而只有一个RecyclerView
@ContentView(R.layout.fragment_device)
public class Plug extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "PLUG_FRAGMENT";

    @ViewInject(R.id.scroll)
    NestedScrollView scroll;

    @ViewInject(R.id.swipe)
    SwipeRefreshLayout swipe;

    @ViewInject(R.id.recycler)
    RecyclerView recycler;

    // 准备的数据
    private List<PlugData> list;

    // 准备的Adapter（重点）
    private PlugAdapter adapter;

    // 分布图部分
    @ViewInject(R.id.container)
    CardView container;

    CardView layout;
    LinearLayout[] layouts;

    public static final int REFRESH = 0x01;
    public static final int CODE_ERR = 0x02;
    public static final int HTTP_ERR = 0x03;
    public static final int FOCUS_UP = 0x04;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    swipe.setRefreshing(true);
                    getData();
                    scroll.fullScroll(View.FOCUS_UP);
                    swipe.setRefreshing(false);
                    break;
                case FOCUS_UP:
                    scroll.fullScroll(View.FOCUS_UP);
                    break;

            }
        }
    };


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipe.setOnRefreshListener(this);
        // 先准备数据再初始化界面？
        list = new ArrayList<>();
        list.add(new PlugData());
        initView();

    }

    private void initView() {
        scroll.setNestedScrollingEnabled(true);
        scroll.setSmoothScrollingEnabled(true);
        adapter = new PlugAdapter(getContext(), handler, list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyItemRangeChanged(0, list.size());
        scroll.fullScroll(View.FOCUS_UP);

        //分布图初始化
        switch (L.getLayout()) {
            case 1:
                layouts = L.initGraph(getActivity(), 1, container);
                break;
        }
        handler.sendEmptyMessage(REFRESH);
    }


    /**
     * 获取数据并显示
     */
    private void getData() {
        list.clear();
        RequestParams params = new RequestParams(L.URL_GET + "?gate=" + L.getGateImei() + "&type=0A");
        params.addParameter("gate", L.getGateImei());
        params.addParameter("type", "0A");
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
                            setGraph();
                            adapter.notifyDataSetChanged();
//                            adapter.notifyItemRangeChanged(0, list.size());
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
                            list.add(new PlugData(name, state, sign, placeNo));
                        }
                    }

                    // 刷新数据时用于更新分布图
                    private void setGraph() {
                        for (LinearLayout layout :
                                layouts) {
                            int count = layout.getChildCount();
                            if (count > 1) {
                                layout.removeViews(1, count - 1);
                            }
                        }
                        for (PlugData data :
                                list) {
                            int place = data.getPlaceNo();
                            int sign = data.getSign();
                            ImageView img = new ImageView(getContext());
                            img.setImageResource(R.drawable.blue_selector);
                            img.setPadding(5, 5, 5, 5);
                            img.setColorFilter(L.signs[sign]);
                            layouts[place].addView(img);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        L.toast(getContext(), ex.getMessage());
                        Log.i(TAG, "onError: " + ex.getMessage());
                        ex.printStackTrace();
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

    public Handler getHandler() {
        return handler;
    }
}
