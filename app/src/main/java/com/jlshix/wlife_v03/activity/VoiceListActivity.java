package com.jlshix.wlife_v03.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.adapter.VoiceListAdapter;
import com.jlshix.wlife_v03.data.OrderData;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_voice)
public class VoiceListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    @ViewInject(R.id.recycler)
    private RecyclerView recycler;

    private List<OrderData> list;
    private VoiceListAdapter adapter;
    
    private Activity activity = VoiceListActivity.this;

    public static final int REFRESH = 0x01;
    public static final int CODE_ERR = 0x02;
    public static final int HTTP_ERR = 0x03;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    onRefresh();
                    break;
                case CODE_ERR:
                    L.snack(toolbar, "CODE_ERR");
                    break;
                case HTTP_ERR:
                    L.snack(toolbar, "HTTP_ERR");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipe.setOnRefreshListener(this);
        list = new ArrayList<>();
        list.add(new OrderData());
        initView();
        onRefresh();
    }

    private void initView() {
        adapter = new VoiceListAdapter(activity, handler, list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyItemRangeChanged(0, list.size());
    }

    @Override
    public void onRefresh() {
        getData();
        swipe.setRefreshing(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(VoiceListActivity.this, AddOrderActivity.class);
                intent.putExtra("type", "voice");
                startActivityForResult(intent, L.ADD_REQUEST);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == L.ADD_REQUEST && resultCode == L.ADD_RETURN) {
            handler.sendEmptyMessage(REFRESH);
        }
    }


    /**
     * VoiceList 显示全部命令包括模式
     */
    private void getData() {
        RequestParams params = new RequestParams(L.URL_GET_ORDER);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("type", "all");
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (!result.optString("code").equals("1")) {
                    handler.sendEmptyMessage(CODE_ERR);
                    return;
                }
                json2List(result.optJSONArray("info"));
                adapter.notifyDataSetChanged();
            }

            private void json2List(JSONArray info) {
                list.clear();
                for (int i = 0; i < info.length(); i++) {
                    JSONObject object = info.optJSONObject(i);
                    int id = object.optInt("id");
                    String name = object.optString("name");
                    int equal = object.optInt("equal");
                    String type = object.optString("type");
                    String actions = object.optString("actions");
                    String des = object.optString("des");
                    list.add(new OrderData(id, name, equal, type, actions, des));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(HTTP_ERR);
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
