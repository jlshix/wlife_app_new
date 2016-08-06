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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.data.ApplianceData;
import com.jlshix.wlife_v03.fragment.Appliance;
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
public class ApplianceAdapter extends RecyclerView.Adapter<ApplianceAdapter.ApplianceViewHolder> {

    private static final String TAG = "APPLIANCE_ADAPTER";
    private Context context;
    private List<ApplianceData> datas;
    private Handler handler;

    public ApplianceAdapter(Context context, Handler handler, List<ApplianceData> list) {
        this.context = context;
        this.handler = handler;
        this.datas = list;
    }

    @Override
    public ApplianceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.appliance_list_item, parent, false);
        return new ApplianceViewHolder(v);


    }

    @Override
    public void onBindViewHolder(final ApplianceViewHolder holder, final int position) {
        final ApplianceData data = datas.get(position);

        final String[] mode = {"自动模式", "制冷模式", "加热模式", "抽湿模式", "送风模式"};

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

        // 初始状态
        holder.modeText.setText("");
        holder.windText.setText("");
        holder.tmpText.setText("--℃");
        holder.modeBtn.setEnabled(false);
        holder.windBtn.setEnabled(false);
        holder.tmpSeek.setEnabled(false);

        String name = L.nameText(data.getPlaceNo(), data.getPlace());
        holder.place.setText(name);

        holder.powerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.powerText.getText().equals("关")) {

                    holder.modeBtn.setEnabled(true);
                    holder.windBtn.setEnabled(true);
                    holder.tmpSeek.setEnabled(true);

                    holder.powerText.setText("开");
                    holder.powerBtn.setText("关闭");
                    holder.modeText.setText(mode[0]);
                    holder.windText.setText("1");
                    holder.tmpSeek.setProgress(0);
                    holder.tmpText.setText(16 + "℃");

                    L.send2Gate(L.getGateImei(), "0f010003");

                } else {

                    holder.modeBtn.setEnabled(false);
                    holder.windBtn.setEnabled(false);
                    holder.tmpSeek.setEnabled(false);

                    holder.powerBtn.setEnabled(true);
                    holder.powerText.setText("关");
                    holder.powerBtn.setText("打开");

                    holder.modeText.setText("");
                    holder.windText.setText("");
                    holder.tmpText.setText("--℃");

                    L.send2Gate(L.getGateImei(), "0f010004");

                }
            }
        });

        holder.modeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (holder.modeText.getText().toString()) {
                    case "自动模式":
                        holder.modeText.setText(mode[1]);
                        L.send2Gate(L.getGateImei(), "0f010005");
                        break;
                    case "制冷模式":
                        holder.modeText.setText(mode[2]);
                        L.send2Gate(L.getGateImei(), "0f010006");
                        break;
                    case "加热模式":
                        holder.modeText.setText(mode[3]);
                        L.send2Gate(L.getGateImei(), "0f010008");
                        break;
                    case "抽湿模式":
                        holder.modeText.setText(mode[4]);
                        L.send2Gate(L.getGateImei(), "0f010009");
                        break;
                    case "送风模式":
                        holder.modeText.setText(mode[0]);
                        L.send2Gate(L.getGateImei(), "0f010000");
                        break;
                    default:
                        Log.e(TAG, "modeBtn " + "MODE_TEXT_ERR");
                        break;
                }
            }
        });

        holder.windBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (holder.windText.getText().toString()) {
                    case "1":
                        holder.windText.setText("2");
                        break;
                    case "2":
                        holder.windText.setText("3");
                        break;
                    case "3":
                        holder.windText.setText("1");
                        break;
                }
                L.send2Gate(L.getGateImei(), "0f010007");
            }
        });

        holder.tmpSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                holder.tmpText.setText(progress + 16 + "℃");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                L.send2Gate(L.getGateImei(), "0f0100" + (seekBar.getProgress() + 16));
            }
        });

    }

    private void uploadToServer(String type, String no, String last) {

        RequestParams params = new RequestParams(L.URL_SET);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("type", type);
        params.addParameter("no", no);
        params.addParameter("state", last.charAt(1));
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("code").equals("1")) {
                    handler.sendEmptyMessage(Appliance.REFRESH);
                } else {
                    handler.sendEmptyMessage(Appliance.CODE_ERR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(Appliance.HTTP_ERR);
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


    static class ApplianceViewHolder extends RecyclerView.ViewHolder{

        LinearLayout parent;
        TextView place, powerText, modeText, windText, tmpText;
        Button menu, powerBtn, modeBtn, windBtn;
        SeekBar tmpSeek;


        public ApplianceViewHolder(View itemView) {
            super(itemView);

            parent = (LinearLayout) itemView.findViewById(R.id.parent);

            place = (TextView) itemView.findViewById(R.id.place);
            menu = (Button) itemView.findViewById(R.id.menu);

            powerText = (TextView) itemView.findViewById(R.id.power_text);
            modeText = (TextView) itemView.findViewById(R.id.mode_text);
            windText = (TextView) itemView.findViewById(R.id.wind_text);

            powerBtn = (Button) itemView.findViewById(R.id.power_btn);
            modeBtn = (Button) itemView.findViewById(R.id.mode_btn);
            windBtn = (Button) itemView.findViewById(R.id.wind_btn);

            tmpSeek = (SeekBar) itemView.findViewById(R.id.seek);
            tmpText = (TextView) itemView.findViewById(R.id.value);

        }

    }

}
