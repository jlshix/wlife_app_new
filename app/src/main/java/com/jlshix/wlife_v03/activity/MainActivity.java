package com.jlshix.wlife_v03.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.data.GateData;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.JsonParser;
import com.jlshix.wlife_v03.tool.L;
import com.jlshix.wlife_v03.tool.VideoCallActivity;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "MAIN_ACTIVITY";
    private Activity activity = MainActivity.this;

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

    @ViewInject(R.id.name)
    private TextView weatherTitile;

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

    // 语音听写对象
    private com.iflytek.cloud.SpeechRecognizer recognizer;
    // 语音听写UI
    private RecognizerDialog recognizerDialog;



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

        // 初始化
        SpeechUtility.createUtility(activity, SpeechConstant.APPID + "=5794c319");

        recognizer = com.iflytek.cloud.SpeechRecognizer.createRecognizer(activity, mInitListener);
        recognizerDialog = new RecognizerDialog(activity, mInitListener);
        handler.sendEmptyMessage(REFRESH);

        L.setOnline(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.setOnline(false);
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
     * 获取当前的位置的城市名和地区
     */
    private void setWeaherCardTitle() {
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
                JSONObject addressComponent = res.optJSONObject("addressComponent");
                String city = addressComponent.optString("city");
                String district = addressComponent.optString("district");
                weatherTitile.setText(city+district);
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



    /**
     * 天气菜单
     * @param view menu btn
     */
    @Event(R.id.menu_weather)
    private void weatherMenu(View view) {
        final PopupMenu menu = new PopupMenu(activity, view);
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
                final LinearLayout layout = new LinearLayout(activity);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(16, 16, 16, 16);
                final EditText city = new EditText(activity);
                city.setHint("城市名");
                final EditText address = new EditText(activity);
                address.setHint("地址 越详细越准确");
                layout.addView(city);
                layout.addView(address);
                new AlertDialog.Builder(activity).setTitle("更改位置").setView(layout)
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

                        new AlertDialog.Builder(activity).setTitle("当前位置")
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
        PopupMenu menu = new PopupMenu(activity, view);
        menu.getMenuInflater().inflate(R.menu.menu_gate, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        unbindGate(toolbar);
                        break;
                    case R.id.action_rename:
                        rename();
                        break;
                }
                return false;
            }
        });
        menu.show();
    }


    /**
     * 重命名网关
     */
    private void rename() {
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_rename, null);
        TextView currentName = (TextView) layout.findViewById(R.id.current_name);
        currentName.setText(name.getText());
        final EditText newName = (EditText) layout.findViewById(R.id.new_name);
        new AlertDialog.Builder(MainActivity.this).setTitle("重命名").setView(layout)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RequestParams params = new RequestParams(L.URL_RENAME_GATE);
                params.addParameter("imei", L.getGateImei());
                params.addParameter("name", newName.getText().toString().trim());
                x.http().post(params, new Callback.CommonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        if (!result.optString("code").equals("1")) {
                            L.snack(toolbar, "RENAME_CODE_ERR");
                            return;
                        }
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

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        L.snack(toolbar, "GATE_RENAME_ON_ERR: " + ex.getMessage());
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
     * 视频通话
     */
    @Event(R.id.camera)
    private void camera(View view) {
        if (!EMChatManager.getInstance().isConnected()) {
            L.snack(toolbar, "未连接到服务器");
        }
        else {
            String toUser = L.getGateImei();
            if (TextUtils.isEmpty(toUser)){
                L.snack(toolbar, "您还未绑定设备");
                return ;
            }
            Intent intent = new Intent(MainActivity.this, VideoCallActivity.class);
            intent.putExtra("username", toUser);
            intent.putExtra("isComingCall", false);
            startActivity(intent);
        }
    }

    @Event(R.id.mode)
    private void mode(View view) {
        startActivity(new Intent(MainActivity.this, ModeActivity.class));
    }

    @Event(R.id.fab)
    private void startRec(View view) {
        setParam();
        recognizerDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if (!isLast) {
                    processVoice(recognizerResult);
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                Log.e(TAG, "recognizerDialogListener-->onError");
                Log.i(TAG, "onError: " + speechError.getPlainDescription(true));
            }
        });
        recognizerDialog.show();
    }

    /**
     * 处理语音识别结果
     * @param recognizerResult result
     */
    private void processVoice(RecognizerResult recognizerResult) {
        String text = JsonParser.parseIatResult(recognizerResult.getResultString());
        RequestParams params = new RequestParams(L.URL_VOICE);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("orderstr", text);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.i(TAG, "voiceResult: " + result.toString());
                if (!result.optString("code").equals("1")) {
                    L.snack(toolbar, "VOICE_CODE_ERR");
                    return;
                }
                L.snack(toolbar, result.optString("info"));

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

    public void setParam() {
        Log.e(TAG, "setParam");
        // 清空参数
        recognizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        recognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

        // 设置语言
        recognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        recognizer.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置语音前端点
        recognizer.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点
        recognizer.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号 1为有标点 0为没标点
        recognizer.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径
        recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                Environment.getExternalStorageDirectory()
                        + "/xiaobailong24/xunfeiyun");
    }


    // device OnClickListener
    @Event(R.id.device)
    private void toDeviceActivity(View v) {
        Intent intent = new Intent(activity, DeviceActivity.class);
        startActivity(intent);
    }

    /**
     * 解绑设备
     * @param v view
     * @return boolean
     */
    private boolean unbindGate(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("确认解绑网关？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                L.unbindGate(activity);
                HashSet<String> tag = new HashSet<>();
                tag.add("unbind");
                JPushInterface.setTags(MainActivity.this, tag, new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                        Log.i(TAG, "Unbind_gotResult: " + set.toString());
                    }
                });
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
                L.toast(activity, "目前仅支持一台设备，可在菜单中删除并绑定新设备");
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
                    L.toast(activity, "CODE_ERR");
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
                    L.toast(activity, "CODE_ERR");
                    return;
                }
                // 设定网关卡片 json内容太复杂，不再使用新类
                setGateCard(new GateData(result.optJSONObject("info")));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.toast(activity, ex.getMessage());
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
        L.setLayout(info.getLayout());
        L.setGateMaster(info.getMaster());
    }

    private void setWeatherCard(JSONObject object) {
        String temp = String.valueOf((int) object.optDouble("temperature")) + "°";
        String skycon = object.optString("skycon");
        String humidity = String.valueOf((int) (object.optDouble("humidity") * 100)) + "%";

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
        // 更改标题
        setWeaherCardTitle();
    }


    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                L.toast(activity, "初始化失败，错误码：" + code);
            }
        }
    };

}
