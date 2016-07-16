package com.jlshix.wlife_v03.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.data.LightData;
import com.jlshix.wlife_v03.fragment.Light;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Leo on 2016/6/16.
 * Light Adapter
 */
public class LightAdapter extends RecyclerView.Adapter<LightAdapter.LightViewHolder> {

    private static final String TAG = "LIGHT_ADAPTER";
    private Context context;
    private List<LightData> datas;
    private Handler handler;

    public LightAdapter(Context context, Handler handler, List<LightData> list) {
        this.context = context;
        this.handler = handler;
        this.datas = list;
    }

    @Override
    public LightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.light_list_item, parent, false);
        return new LightViewHolder(v);


    }

    @Override
    public void onBindViewHolder(final LightViewHolder holder, final int position) {
        holder.place.setText(datas.get(position).getName());
        holder.value.setText(String.valueOf(datas.get(position).getState()));
        holder.seek.setProgress(datas.get(position).getState());

        holder.seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                holder.value.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 更新数据库
                uploadToServer(position, seekBar.getProgress());
                // 推送
                String msg = "090" + (position + 1) + "0" + seekBar.getProgress();
                L.send2Gate(L.getGateImei(), msg);
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

    private void uploadToServer(int position, int progress) {
        String URL = L.URL_SET + "?gate=4718&type=09&no=0" + (position+1) + "&state=" + progress;
        Log.e(TAG, "uploadToServer: " + URL);
        // 数据库
        RequestParams params = new RequestParams(URL);
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("code").equals("1")) {
                    handler.sendEmptyMessage(Light.REFRESH);
                } else {
                    handler.sendEmptyMessage(Light.CODE_ERR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                    handler.sendEmptyMessage(Light.HTTP_ERR);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    static class LightViewHolder extends RecyclerView.ViewHolder{

        TextView place;
        TextView value;
        SeekBar seek;
        Button menu;

        public LightViewHolder(View itemView) {
            super(itemView);

            place = (TextView) itemView.findViewById(R.id.place);
            value = (TextView) itemView.findViewById(R.id.value);
            seek = (SeekBar) itemView.findViewById(R.id.seek);
            menu = (Button) itemView.findViewById(R.id.menu);
        }
    }
}
