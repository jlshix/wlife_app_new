package com.jlshix.wlife_v03.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.activity.MemberActivity;
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
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private static final String TAG = "MEMBER_ADAPTER";
    // 上下文 与 数据
    private Context context;
    private List<String> datas;
    // handler 用于发送消息刷新
    private Handler handler;

    // 构造函数
    public MemberAdapter(Context context, Handler handler, List<String> datas) {
        this.context = context;
        this.handler = handler;
        this.datas = datas;
    }


    /**
     * 内部类 ViewHolder
     */
    static class MemberViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        Button del;

        public MemberViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            del = (Button) itemView.findViewById(R.id.del);
        }
    }



    /**
     * 设定布局文件，还要ViewHolder一起
     * @param parent viewGroup
     * @param viewType type
     * @return viewHolder
     */
    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.member_list_item, parent, false);
        return new MemberViewHolder(v);
    }

    /**
     * 绑定， 包括布局和自己实现的监听器
     * @param holder viewHolder
     * @param position 第几个
     */
    @Override
    public void onBindViewHolder(MemberAdapter.MemberViewHolder holder, int position) {
        final String nameText = datas.get(position);
        holder.name.setText(nameText);
        // 管理员本身不能删除自己
        if (nameText.equals(L.getGateMaster())) {
            holder.del.setVisibility(View.INVISIBLE);
        }
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setMessage("确认删除?")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delMember(nameText);

                            }
                        }).setNegativeButton("取消", null).create().show();
            }
        });
    }

    private void delMember(String name) {
        RequestParams params = new RequestParams(L.URL_DEL_MEMBER);
        params.addParameter("name", name);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("code").equals("1")) {
                    handler.sendEmptyMessage(MemberActivity.REFRESH);
                } else {
                    handler.sendEmptyMessage(MemberActivity.CODE_ERR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(MemberActivity.HTTP_ERR);
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
