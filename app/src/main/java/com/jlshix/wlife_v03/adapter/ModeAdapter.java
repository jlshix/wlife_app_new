package com.jlshix.wlife_v03.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.activity.ModeActivity;
import com.jlshix.wlife_v03.data.OrderData;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Leo on 2016/6/16.
 * 环境Fragment的Adapter
 * 注意extend Adapter<>
 */
public class ModeAdapter extends RecyclerView.Adapter<ModeAdapter.ModeViewHolder> {

    private static final String TAG = "MODE_ADAPTER";
    // 上下文 与 数据
    private Context context;
    private List<OrderData> datas;
    // handler 用于发送消息刷新
    private Handler handler;

    // 构造函数
    public ModeAdapter(Context context, Handler handler, List<OrderData> datas) {
        this.context = context;
        this.handler = handler;
        this.datas = datas;
    }


    /**
     * 内部类 ViewHolder
     */
    static class ModeViewHolder extends RecyclerView.ViewHolder {
        CardView mode;
        TextView name;
        Button menu;

        public ModeViewHolder(View itemView) {
            super(itemView);
            mode = (CardView) itemView.findViewById(R.id.mode_card);
            name = (TextView) itemView.findViewById(R.id.name);
            menu = (Button) itemView.findViewById(R.id.del);
        }
    }



    /**
     * 设定布局文件，还要ViewHolder一起
     * @param parent viewGroup
     * @param viewType type
     * @return viewHolder
     */
    @Override
    public ModeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mode_list_item, parent, false);
        return new ModeViewHolder(v);
    }

    /**
     * 绑定， 包括布局和自己实现的监听器
     * @param holder viewHolder
     * @param position 第几个
     */
    @Override
    public void onBindViewHolder(ModeAdapter.ModeViewHolder holder, int position) {
        
        final OrderData data = datas.get(position);
        holder.name.setText(data.getName());
        holder.mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                L.toast(context, "已设定为" + data.getName());
                L.send2Gate(L.getGateImei(), data.getActions());
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                menu.getMenuInflater().inflate(R.menu.menu_mode_item, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                       switch (item.getItemId()) {
                           case R.id.action_detail:
                               showDetail();
                               break;
                           case R.id.action_set:
                               setMode();
                               break;
                           case R.id.action_delete:
                               new AlertDialog.Builder(context).setMessage("确认删除此模式?")
                                       .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface, int i) {
                                               delOrder(data.getId());
                                           }
                                       }).setNegativeButton("取消", null).create().show();
                       }
                        return false;
                    }

                    private void setMode() {
                        L.toast(context, "已设定为" + data.getName());
                        L.send2Gate(L.getGateImei(), data.getActions());
                    }

                    private void showDetail() {
                        new AlertDialog.Builder(context).setTitle("模式详情")
                                .setMessage(data.getDes().replace(',', '\n'))
                                .setPositiveButton("确认", null).create().show();
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
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("code").equals("1")) {
                    handler.sendEmptyMessage(ModeActivity.REFRESH);
                } else {
                    handler.sendEmptyMessage(ModeActivity.CODE_ERR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(ModeActivity.HTTP_ERR);
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
