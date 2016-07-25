package com.jlshix.wlife_v03.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.activity.VoiceListActivity;
import com.jlshix.wlife_v03.data.OrderData;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Leo on 2016/6/16.
 * VoiceList的Adapter
 * 注意extend Adapter<>
 */
public class VoiceListAdapter extends RecyclerView.Adapter<VoiceListAdapter.VoiceListViewHolder> {

    private static final String TAG = "VOICE_LIST_ADAPTER";
    // 上下文 与 数据
    private Context context;
    private List<OrderData> datas;
    // handler 用于发送消息刷新
    private Handler handler;

    // 构造函数
    public VoiceListAdapter(Context context, Handler handler, List<OrderData> datas) {
        this.context = context;
        this.handler = handler;
        this.datas = datas;
    }


    /**
     * 内部类 ViewHolder
     * //TODO xUtils 关于ViewHolder的Inject
     */
    static class VoiceListViewHolder extends RecyclerView.ViewHolder {

        TextView equal;
        TextView name;
        TextView des;
        Button menu;

        public VoiceListViewHolder(View itemView) {
            super(itemView);
            equal = (TextView) itemView.findViewById(R.id.place);
            name = (TextView) itemView.findViewById(R.id.name);
            des = (TextView) itemView.findViewById(R.id.des);
            menu = (Button) itemView.findViewById(R.id.menu);
        }
    }



    /**
     * 设定布局文件，还要ViewHolder一起
     * @param parent viewGroup
     * @param viewType type
     * @return viewHolder
     */
    @Override
    public VoiceListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.voice_list_item, parent, false);
        return new VoiceListViewHolder(v);
    }

    /**
     * 绑定， 包括布局和自己实现的监听器
     * @param holder viewHolder
     * @param position 第几个
     */
    @Override
    public void onBindViewHolder(VoiceListAdapter.VoiceListViewHolder holder, int position) {
        final OrderData data = datas.get(position);

        if (data.getEqual() == 1) {
            holder.equal.setText("匹配");
        } else {
            holder.equal.setText("包含");
        }

        holder.name.setText(data.getName());
        String des = data.getDes().replace(',', '\n');
        holder.des.setText(des);

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                menu.getMenuInflater().inflate(R.menu.menu_order, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_change:
                                // TODO: 2016/7/25 更改
                                break;
                            case R.id.action_delete:
                                delOrder(data.getId());
                                break;
                        }
                        return false;
                    }
                });
                menu.show();

            }
        });
    }

    private void delOrder(int id) {
        RequestParams params = new RequestParams(L.URL_DEL_ORDER);
        params.addParameter("id", id);
        params.addParameter("gate", L.getGateImei());
        // TODO: 2016/7/25 handler
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("code").equals("1")) {
                    handler.sendEmptyMessage(VoiceListActivity.REFRESH);
                } else {
                    handler.sendEmptyMessage(VoiceListActivity.CODE_ERR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(VoiceListActivity.HTTP_ERR);
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
    public int getItemCount() {
        return datas.size();
    }



}
