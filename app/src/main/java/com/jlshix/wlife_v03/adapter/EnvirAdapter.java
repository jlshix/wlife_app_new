package com.jlshix.wlife_v03.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.data.EnvirData;
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

    // 构造函数
    public EnvirAdapter(Context context, List<EnvirData> datas) {
        this.context = context;
        this.datas = datas;
    }


    /**
     * 内部类 ViewHolder
     * //TODO xUtils 关于ViewHolder的Inject
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
    public void onBindViewHolder(EnvirAdapter.EnvirViewHolder holder, int position) {
        EnvirData data = datas.get(position);
        String name = L.nameText(data.getPlaceNo(), data.getPlace());
        holder.place.setText(name);
        holder.temp.setText(data.getTemp());
        holder.humi.setText(data.getHumi());
        holder.light.setText(data.getLight());
        // 上下文菜单
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                menu.getMenuInflater().inflate(R.menu.menu_item, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.i(TAG, "onMenuItemClick: " + item.getTitle());
                        return false;
                    }
                });
                menu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



}
