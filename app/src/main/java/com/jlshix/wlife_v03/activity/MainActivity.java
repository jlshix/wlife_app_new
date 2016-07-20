package com.jlshix.wlife_v03.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.data.GateData;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "MAIN_ACTIVITY";

    @ViewInject(R.id.toolbar)
    Toolbar toolbar;

    //swipe
    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    // weather
    @ViewInject(R.id.weather)
    private CardView weather;

    // temperature
    @ViewInject(R.id.temp)
    private TextView temperature;

    // weatherState
    @ViewInject(R.id.state)
    private TextView weatherState;

    // place but now pm2.5
    @ViewInject(R.id.place)
    private TextView place;

    // ImageView
    @ViewInject(R.id.weather_pic)
    private ImageView weatherPic;

    // no_device
    @ViewInject(R.id.no_device_card)
    private CardView noDevice;

    // device
    @ViewInject(R.id.device)
    private CardView device;

    @ViewInject(R.id.title)
    private TextView name;

    @ViewInject(R.id.summary)
    private TextView gateState;

    @ViewInject(R.id.line)
    private ImageView line;

    // spinner for mode
    @ViewInject(R.id.mode_spinner)
    private Spinner spinner;
    // 是否主动选定 暂时未实现主动被动的选定
    private boolean positive = true;
    // 是否第一次启动 用于初始化spinner时不发命令
    private boolean isFirst = true;



    // handler
    public static final int REFRESH = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    swipe.setRefreshing(true);
                    showCard();
                    updateWeather();
                    if (L.isBIND()) {
                        updateGate();
                    }
                    swipe.setRefreshing(false);
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        showCard();

        line.setEnabled(false);
        swipe.setOnRefreshListener(this);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner,
                getResources().getStringArray(R.array.mode)));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (positive) {
                    changeMode(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        handler.sendEmptyMessage(REFRESH);
    }

    private void showCard() {
        if (!L.isBIND()) {
            noDevice.setVisibility(View.VISIBLE);
            device.setVisibility(View.GONE);
        } else {
            noDevice.setVisibility(View.GONE);
            device.setVisibility(View.VISIBLE);
        }
    }

    @Event(R.id.weather)
    private void refreshWeather(View view) {
        updateWeather();
        Snackbar.make(view, "实时天气已更新", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 天气菜单
     * @param view menu btn
     */
    @Event(R.id.menu_weather)
    private void weatherMenu(View view) {
        final PopupMenu menu = new PopupMenu(MainActivity.this, view);
        menu.getMenuInflater().inflate(R.menu.menu_weather, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_position:
                        changePosition();
                        break;
                    case R.id.action_state:
                        getState();
                        break;
                }
                return false;
            }


            /**
             * 更改当前位置
             */
            private void changePosition() {
                final LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(16, 16, 16, 16);
                final EditText city = new EditText(MainActivity.this);
                city.setHint("城市名");
                final EditText address = new EditText(MainActivity.this);
                address.setHint("地址 越详细越准确");
                layout.addView(city);
                layout.addView(address);
                new AlertDialog.Builder(MainActivity.this).setTitle("更改位置").setView(layout)
                        .setPositiveButton("更改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String cityText = city.getText().toString().trim();
                                String addrText = address.getText().toString().trim();
                                if (cityText.equals("") || addrText.equals("")) {
                                    L.snack(layout, "信息不完整");
                                    return;
                                }
                                RequestParams params = new RequestParams(L.URL_TO_GPS);
                                params.addParameter("output", "json");
                                params.addParameter("ak", "kBjrIu4NjUASe2BNU9DuxTHL");
                                params.addParameter("city", cityText);
                                params.addParameter("address", addrText);
                                x.http().get(params, new Callback.CommonCallback<JSONObject>() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        Log.i(TAG, "onSuccess: " + result.toString());
                                        if (result.optInt("status") != 0) {
                                            L.snack(layout, "WEATHER_CODE_ERR");
                                            return;
                                        }
                                        JSONObject location = result.optJSONObject("result")
                                                .optJSONObject("location");
                                        double lng = location.optDouble("lng");
                                        double lat = location.optDouble("lat");
                                        DecimalFormat df =new DecimalFormat("0.0000");
                                        df.format(lng);
                                        df.format(lat);
                                        Log.i(TAG, "onSuccess: "+ lng + "--" + lat);
                                        L.setLng(String.valueOf(lng));
                                        L.setLat(String.valueOf(lat));
                                        handler.sendEmptyMessage(REFRESH);

                                    }

                                    @Override
                                    public void onError(Throwable ex, boolean isOnCallback) {
                                        L.snack(toolbar, ex.getMessage());
                                    }

                                    @Override
                                    public void onCancelled(CancelledException cex) {

                                    }

                                    @Override
                                    public void onFinished() {

                                    }
                                });

                            }
                        }).setNegativeButton("取消", null).create().show();
            }

            /**
             * 获取当前的位置描述
             */
            private void getState() {
                RequestParams params = new RequestParams(L.URL_TO_GPS);
                params.addParameter("output", "json");
                params.addParameter("ak", "kBjrIu4NjUASe2BNU9DuxTHL");
                params.addParameter("location", L.getBDLocation());
                x.http().get(params, new Callback.CommonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.i(TAG, "onSuccess: " + result);
                        if (result.optInt("status") != 0) {
                            L.snack(toolbar, "STATE_CODE_ERR");
                            return;
                        }
                        JSONObject res = result.optJSONObject("result");
                        JSONObject location = res.optJSONObject("location");
                        String addr = res.optString("formatted_address");
                        String describ = res.optString("sematic_description");
                        String msg = "坐标:\n" + "\t经度: " + location.optString("lng") +
                                "\n" + "\t纬度: " + location.optString("lat") +
                                "\n\n" + "位置描述:\n\t" + addr +
                                "附近 " + describ;

                        new AlertDialog.Builder(MainActivity.this).setTitle("当前位置")
                                .setMessage(msg).setPositiveButton("确认", null)
                                .create().show();

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        L.snack(toolbar, ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });

            }
        });
        menu.show();
    }

    /**
     * 网关菜单
     * @param view menu btn
     */
    @Event(R.id.menu_gate)
    private void gateMenu(View view) {
        PopupMenu menu = new PopupMenu(MainActivity.this, view);
        menu.getMenuInflater().inflate(R.menu.menu_gate, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        unbindGate(toolbar);
                        break;
                    case R.id.action_rename:
                        L.snack(toolbar, "开发中");
                        break;
                }
                return false;
            }
        });
        menu.show();
    }



    /**
     * 更改模式
     * @param position 模式编号
     */
    private void changeMode(int position) {
        if (isFirst) {
            isFirst = false;
            return;
        }
        // 推送 模式标志为1 始于1 (1 日常) (2 观影) (3 睡眠) (4 外出) (5 夜间)
        L.send2Gate(L.getGateImei(), "1" + (position + 1) + "0000");
        // 数据库
        RequestParams params = new RequestParams(L.URL_SET_GATE);
        params.addParameter("imei", L.getGateImei());
        params.addParameter("mode", String.valueOf(position + 1));
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Snackbar.make(spinner, "模式已更改", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Snackbar.make(spinner, "模式更改失败，请稍候重试", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        // 改回被动
//        positive = false;
    }

    // device OnClickListener
    @Event(value = R.id.device,
            type = View.OnClickListener.class)
    private void toDeviceActivity(View v) {
        Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
        startActivity(intent);
    }

    /**
     * 解绑设备
     * @param v view
     * @return boolean
     */
    @Event(value = R.id.device, type = View.OnLongClickListener.class)
    private boolean unbindGate(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("确认解绑网关？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                L.unbindGate(MainActivity.this);
                handler.sendEmptyMessage(REFRESH);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            // 再刷新一次
                            handler.sendEmptyMessage(REFRESH);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
        return true;
    }

    /**
     * 查看监控
     * @param view view
     */
    @Event(R.id.camera)
    private void camera(View view) {
        // TODO: 2016/7/16 环信
        L.snack(view, "开发中...");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            if (L.isBIND()) {
                L.toast(MainActivity.this, "目前仅支持一台设备，可长按删除并绑定新设备");
                return true;
            }
            Intent intent = new Intent(getApplicationContext(), GateBindActivity.class);
            startActivityForResult(intent, L.ADD_REQUEST);
            return true;
        }

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(intent, L.SETTINGS_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == L.ADD_REQUEST && resultCode == L.ADD_RETURN) {
            showCard();
            handler.sendEmptyMessage(REFRESH);
        }
        if (requestCode == L.SETTINGS_REQUEST && resultCode == L.SETTINGS_RETURN) {
            finish();
        }
    }

    @Override
    public void onRefresh() {


        handler.sendEmptyMessage(REFRESH);
    }


    /**
     * 更新天气数据
     */
    private void updateWeather() {
        RequestParams params = new RequestParams(L.URL_WEATHER + L.getCYLocation() + "/realtime.json");
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (!result.optString("status").equals("ok")) {
                    L.toast(MainActivity.this, "CODE_ERR");
                    return;
                }
                setWeatherCard(result.optJSONObject("result"));


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
     * 更新网关数据
     */
    private void updateGate() {
        RequestParams params = new RequestParams(L.URL_GATE);
        params.addParameter("mail", L.getPhone());
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (!result.optString("code").equals("1")) {
                    L.toast(MainActivity.this, "CODE_ERR");
                    return;
                }
                // 设定天气卡片 json内容太复杂，不再使用新类
                setGateCard(new GateData(result.optJSONObject("info")));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.toast(MainActivity.this, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void setGateCard(GateData info) {
        name.setText(info.getName());
        if (info.getOnline().equals("1")){
            gateState.setText(R.string.online);
            line.setEnabled(true);
        } else {
            gateState.setText(R.string.offline);
            line.setEnabled(false);
        }
        int index = Integer.valueOf(info.getMode());
        // 不是用户主动发起
//        positive = false;
//        spinner.setSelection(index - 1);
    }

    private void setWeatherCard(JSONObject object) {
        String temp = String.valueOf((int) object.optDouble("temperature")) + "°";
        String skycon = object.optString("skycon");
        String humidity = String.valueOf(object.optDouble("humidity") * 100) + "%";

        temperature.setText(temp);
        place.setText(humidity);
        /*
        Skycon 的取值包括
            CLEAR_DAY：晴天
            CLEAR_NIGHT：晴夜
            PARTLY_CLOUDY_DAY：多云
            PARTLY_CLOUDY_NIGHT：多云
            CLOUDY：阴
            RAIN： 雨
            SLEET：冻雨
            SNOW：雪
            WIND：风
            FOG：雾
            HAZE：霾
         */
        switch (skycon) {
            case "CLEAR_DAY":
                weatherState.setText("晴天");
                weatherPic.setImageResource(R.drawable.clear_day);
                break;
            case "CLEAR_NIGHT":
                weatherState.setText("晴夜");
                weatherPic.setImageResource(R.drawable.clear_night);
                break;
            case "PARTLY_CLOUDY_DAY":
                weatherState.setText("多云");
                weatherPic.setImageResource(R.drawable.partly_cloudy_day);
                break;
            case "PARTLY_CLOUDY_NIGHT":
                weatherState.setText("多云");
                weatherPic.setImageResource(R.drawable.partly_cloudy_night);
                break;
            case "CLOUDY":
                weatherState.setText("阴");
                weatherPic.setImageResource(R.drawable.cloudy);
                break;
            case "RAIN":
                weatherState.setText("雨");
                weatherPic.setImageResource(R.drawable.rain);
                break;
            case "SLEET":
                weatherState.setText("冻雨");
                weatherPic.setImageResource(R.drawable.sleet);
                break;
            case "SNOW":
                weatherState.setText("雪");
                weatherPic.setImageResource(R.drawable.snow);
                break;
            case "WIND":
                weatherState.setText("风");
                weatherPic.setImageResource(R.drawable.wind);
                break;
            case "FOG":
                weatherState.setText("雾");
                weatherPic.setImageResource(R.drawable.fog);
                break;
            case "HAZE":
                weatherState.setText("霾");
                weatherPic.setImageResource(R.drawable.fog);
                break;

        }
    }

}
