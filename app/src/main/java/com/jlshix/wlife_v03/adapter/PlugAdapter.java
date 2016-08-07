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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.data.PlugData;
import com.jlshix.wlife_v03.fragment.Plug;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Leo on 2016/6/16.
 * 插座Fragment的Adapter
 * 注意extend Adapter<>
 */
public class PlugAdapter extends RecyclerView.Adapter<PlugAdapter.PlugViewHolder> {

    private static final String TAG = "PLUG_ADAPTER";
    // 上下文 与 数据
    private Context context;
    // handler 用于发送消息刷新
    private Handler handler;
    private List<PlugData> datas;

    // 构造函数
    public PlugAdapter(Context context, Handler handler, List<PlugData> datas) {
        this.context = context;
        this.handler = handler;
        this.datas = datas;
    }


    /**
     * 内部类 ViewHolder
     * //TODO xUtils 关于ViewHolder的Inject
     */
    static class PlugViewHolder extends RecyclerView.ViewHolder {

        Switch[] mSwitch;
        TextView place;
        Button menu;
        ImageView img;

        public PlugViewHolder(View itemView) {
            super(itemView);

            place = (TextView) itemView.findViewById(R.id.place);
            mSwitch = new Switch[4];
            mSwitch[0] = (Switch) itemView.findViewById(R.id.s0);
            mSwitch[1] = (Switch) itemView.findViewById(R.id.s1);
            mSwitch[2] = (Switch) itemView.findViewById(R.id.s2);
            mSwitch[3] = (Switch) itemView.findViewById(R.id.s3);
            menu = (Button) itemView.findViewById(R.id.menu);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }



    /**
     * 设定布局文件，还要ViewHolder一起
     * @param parent viewGroup
     * @param viewType type
     * @return viewHolder
     */
    @Override
    public PlugViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.plug_list_item, parent, false);
        return new PlugViewHolder(v);
    }

    /**
     * 绑定， 包括布局和自己实现的监听器
     * @param holder viewHolder
     * @param position 第几个
     */
    @Override
    public void onBindViewHolder(final PlugAdapter.PlugViewHolder holder, int position) {

        final PlugData data = datas.get(position);
        holder.img.setColorFilter(L.signs[data.getSign()]);

        // 每个单元是一个开关组 0123
        for (int i = 0; i < holder.mSwitch.length; i++) {
            // 从获取的数据初始化状态
            holder.mSwitch[i].setChecked(data.getState(i));

            String name = L.nameText(data.getPlaceNo(), data.getName());
            holder.place.setText(name);
            // I为单元内计数
            final int finalI = i;
            // Position为第几单元计数
            final int finalPosition = position;

            //为每一个单元内的每一个开关设置监听器
            holder.mSwitch[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    //获取编号  TODO 现在只是十个以内
                    String no = "0"+(finalPosition+1);

                    // 获取每个单元的开关状态
                    String state;
                    boolean[] tmp = new boolean[4];
                    for (int i = 0; i < holder.mSwitch.length; i++) {
                        tmp[i] = holder.mSwitch[i].isChecked();
                    }
                    state = bool2String(tmp);

                    // 更新数据库
                    upload2Server(no, state);

                    // 推送
                    String b = isChecked ? "1":"0";
                    String order = "0A0" + (finalPosition + 1) + (finalI + 1) + b;
                    L.send2Gate(L.getGateImei(), order);

                }
            });

            // 上下文菜单
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu menu = new PopupMenu(context, view);
                    menu.getMenuInflater().inflate(R.menu.menu_item, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_rename:
                                    L.devRename(context, handler, Plug.REFRESH, "0A", "0" + data.getNo(), data.getName());
                                    break;
                                case R.id.action_delete:
                                    L.delDev(context, handler, Plug.REFRESH, "0A", "0" + data.getNo());
                                    break;
                                case R.id.action_position:
                                    L.placeDev(context, handler, Plug.REFRESH, "0A", "0" + data.getNo(), data.getPlaceNo());
                                    break;
                            }
                            return false;
                        }

                    });
                    menu.show();
                }
            });
        }

    }

    private void upload2Server(String no, String state) {

        RequestParams params = new RequestParams(L.URL_SET);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("type", "0A");
        params.addParameter("no", no);
        params.addParameter("state", state);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("code").equals("1")) {
                    handler.sendEmptyMessage(Plug.REFRESH);
                } else {
                    handler.sendEmptyMessage(Plug.CODE_ERR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(Plug.HTTP_ERR);
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
     * 将按钮的布尔值转化为字符串0101
     * @param tmp bool[]
     * @return String
     */
    private static String bool2String(boolean[] tmp) {
        StringBuilder sb = new StringBuilder();
        for (boolean aTmp : tmp) {
            if (aTmp) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
