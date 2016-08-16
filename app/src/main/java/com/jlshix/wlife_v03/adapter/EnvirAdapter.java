package com.jlshix.wlife_v03.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.jlshix.wlife_v03.activity.StatisticsActivity;
import com.jlshix.wlife_v03.data.EnvirData;
import com.jlshix.wlife_v03.fragment.Envir;
import com.jlshix.wlife_v03.tool.L;

import java.util.List;

/**
 * Created by Leo on 2016/6/16.
 * 环境Fragment的Adapter
 * 注意extend Adapter<>
 */
public class EnvirAdapter extends RecyclerView.Adapter<EnvirAdapter.EnvirViewHolder> {

    private static final String TAG = "ENVIR_ADAPTER";
    // 上下文 与 数据
    private Context context;
    private List<EnvirData> datas;
    private Handler handler;

    // 构造函数
    public EnvirAdapter(Context context, List<EnvirData> datas, Handler handler) {
        this.context = context;
        this.datas = datas;
        this.handler = handler;
    }


    /**
     * 内部类 ViewHolder
     */
    static class EnvirViewHolder extends RecyclerView.ViewHolder {

        TextView place;
        TextView temp;
        TextView humi;
        TextView light;
        Button menu;

        public EnvirViewHolder(View itemView) {
            super(itemView);
            place = (TextView) itemView.findViewById(R.id.place);
            temp = (TextView) itemView.findViewById(R.id.temp);
            humi = (TextView) itemView.findViewById(R.id.humi);
            light = (TextView) itemView.findViewById(R.id.light);
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
    public EnvirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.envir_list_item, parent, false);
        return new EnvirViewHolder(v);
    }

    /**
     * 绑定， 包括布局和自己实现的监听器
     * @param holder viewHolder
     * @param position 第几个
     */
    @Override
    public void onBindViewHolder(final EnvirAdapter.EnvirViewHolder holder, int position) {
        final EnvirData data = datas.get(position);
        String name = L.nameText(data.getPlaceNo(), data.getPlace());
        holder.place.setText(name);
        holder.temp.setText(data.getTemp());
        holder.humi.setText(data.getHumi());
        holder.light.setText(lightFormat(data.getLight()));
        // 上下文菜单
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                menu.getMenuInflater().inflate(R.menu.menu_item_envir, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_rename:
                                L.devRename(context, handler, Envir.REFRESH, "02", "0" + data.getNo(), data.getPlace());
                                break;
                            case R.id.action_delete:
                                L.delDev(context, handler, Envir.REFRESH, "02", "0" + data.getNo());
                                break;
                            case R.id.action_position:
                                L.placeDev(context, handler, Envir.REFRESH, "02", "0" + data.getNo(), data.getPlaceNo());
                                break;
                            case R.id.action_statistics:
                                Intent intent = new Intent(context, StatisticsActivity.class);
                                intent.putExtra("no", data.getNo());
                                context.startActivity(intent);
                        }
                        return false;
                    }

                });
                menu.show();
            }
        });
    }

    private String lightFormat(String light) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (light.charAt(i) != '0') {
                sb.append(light.charAt(i));
            }
        }
        for (int i = 3; i < light.length(); i++) {
            sb.append(light.charAt(i));
        }
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



}
