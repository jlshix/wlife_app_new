package com.jlshix.wlife_v03.tool;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jlshix.wlife_v03.App;
import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.data.SimpleDeviceData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 2016/7/11.
 * 工具类, 集中网址 & sp读写 & 简单工具
 */
public class L {
    private static final String TAG = "TOOL_L";
    public static final int ADD_REQUEST = 1000;
    public static final int ADD_RETURN = 2000;
    public static final int SETTINGS_REQUEST = 3000;
    public static final int SETTINGS_RETURN = 4000;
    public static final int DIALOG_REQUEST = 5000;
    public static final int DIALOG_RETURN = 6000;


    /**
     * 地点与标志常量
     */
    public static final String[] rooms = {
            "我家", "客厅", "卧室", "餐厅", "书房", "卫生间"
    };
    public static final int LIVING_ROOM = 1;
    public static final int BEDROOM = 2;
    public static final int DINING_ROOM = 3;
    public static final int BOOK_ROOM = 4;
    public static final int RESTROOM = 5;

    public static final int[] signs = {
            Color.TRANSPARENT, Color.RED, Color.GREEN, Color.BLUE
    };
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int BLUE = 3;
    public static final int DEV_IMEI_LENGTH = 12;



    /**
     * debug模式
     */
    public static boolean DEBUG = true;

    public static boolean BIND = false;

    /**
     * 请求网址集中区
     * TODO https
     */
    public static final String URL_LOGIN = "http://jlshix.com/wlife2/login.php/";
    public static final String URL_REG = "http://jlshix.com/wlife2/register.php/";
    public static final String URL_WEATHER = "https://api.caiyunapp.com/v2/X6f3oc9bahTuV6Bv/";
    public static final String URL_TO_GPS    = "http://api.map.baidu.com/geocoder/v2/";
    public static final String URL_UNBIND = "http://jlshix.com/wlife2/unbind_gate.php/";
    public static final String URL_PUSH = "http://jlshix.com/wlife2/togate.php/";
    public static final String URL_SET_GATE = "http://jlshix.com/wlife2/set_gate.php/";
    public static final String URL_GATE = "http://jlshix.com/wlife2/get_gate.php/";
    public static final String URL_GATE_BIND = "http://jlshix.com/wlife2/bind_gate.php/";
    public static final String URL_GET_MSG = "http://jlshix.com/wlife2/get_msg.php/";
    public static final String URL_GET = "http://jlshix.com/wlife2/get.php/";
    public static final String URL_SET = "http://jlshix.com/wlife2/set.php/";
    public static final String URL_ADD_DEV = "http://jlshix.com/wlife2/add_dev.php/";
    public static final String URL_GET_BOARD = "http://jlshix.com/wlife2/get_board.php";
    public static final String URL_ADD_BOARD = "http://jlshix.com/wlife2/add_board.php";
    public static final String URL_GET_NAME_LIST = "http://jlshix.com/wlife2/get_name_list.php";
    public static final String URL_DEL_MEMBER = "http://jlshix.com/wlife2/del_member.php";
    public static final String URL_GET_MEMBER = "http://jlshix.com/wlife2/get_member.php";
    public static final String URL_IS_MASTER = "http://jlshix.com/wlife2/is_su.php";
    public static final String URL_QR_CODE = "http://qr.liantu.com/api.php";
    public static final String URL_ADD_MEMBER = "http://jlshix.com/wlife2/add_member.php";
    public static final String URL_DEL_ORDER = "http://jlshix.com/wlife2/del_order.php";
    public static final String URL_GET_ORDER = "http://jlshix.com/wlife2/get_order.php";


    /**
     * 是否登录
     * @return boolean
     */
    public static boolean isLogin() {
        return App.sp.getBoolean("isLogin", false);
    }

    /**
     * 设置是否登录
     * @param b boolean
     */
    public static void setLogin(boolean b) {
        App.sp.edit().putBoolean("isLogin", b).apply();
    }

    /**
     * 是否自动登录
     * @return boolean
     */
    public static boolean isAutoLogin() {
        return App.sp.getBoolean("autoLogin", false);
    }


    /**
     * 设置是否自动登录
     * @param b boolean
     */
    public static void setAutoLogin(boolean b) {
        App.sp.edit().putBoolean("autoLogin", b).apply();
    }

    /**
     * 是否记住密码
     * @return boolean
     */
    public static boolean isRemember() {
        return App.sp.getBoolean("remember", false);
    }


    /**
     * 设置是否记住密码
     * @param b boolean
     */
    public static void setRemember(boolean b) {
        App.sp.edit().putBoolean("remember", b).apply();
    }
    
    

    /**
     * 获取保存的用户名
     * @return name number
     */
    public static String getName() {
        return App.sp.getString("name", "");
    }


    /**
     * 设置保存的用户名
     * @param s name number
     */
    public static void setName(String s) {
        App.sp.edit().putString("name", s).apply();
    }
    /**
     * 获取保存的手机号
     * @return phone number
     */
    public static String getPhone() {
        return App.sp.getString("phone", "");
    }


    /**
     * 设置保存的手机号
     * @param s phone number
     */
    public static void setPhone(String s) {
        App.sp.edit().putString("phone", s).apply();
    }

    /**
     * 获取保存的密码
     * @return pw
     */
    public static String getPw() {
        return App.sp.getString("pw", "");
    }


    /**
     * 设置保存的密码
     * @param s pw
     */
    public static void setPw(String s) {
        App.sp.edit().putString("pw", s).apply();
    }
    
    
    /**
     * 获取保存的IMEI
     * @return gateImei
     */
    public static String getGateImei() {
        return App.sp.getString("gateImei", "");
    }


    /**
     * 设置保存的IMEI
     * @param s gateImei
     */
    public static void setGateImei(String s) {
        App.sp.edit().putString("gateImei", s).apply();
    }

    /**
     * 获取保存的layout
     * @return layout
     */
    public static int getLayout() {
        return App.sp.getInt("layout", 1);
    }


    /**
     * 设置保存的layout
     * @param s layout
     */
    public static void setLayout(int s) {
        App.sp.edit().putInt("layout", s).apply();
    }


    
    /**
     * 是否绑定
     * @return boolean
     */
    public static boolean isBIND() {
        String imei = getGateImei();
        return imei.length() > 3;
    }

    /**
     * 设定绑定状态
     * @param BIND boolean
     */
    public static void setBIND(boolean BIND) {
        L.BIND = BIND;
        App.sp.edit().putBoolean("bind", BIND).apply();
    }


    /**
     * 获取存储的当前位置
     * @return place
     */
    public static String getCYLocation() {
        return App.sp.getString("lng", "120.1227") + "," + App.sp.getString("lat", "36.0001");
    }

    public static String getBDLocation() {
        return App.sp.getString("lat", "36.0001") + "," + App.sp.getString("lng", "120.1227");
    }

    /**
     * 记录当前位置
     * @param lat place
     */
    public static void setLat(String lat) {
        App.sp.edit().putString("lat", lat).apply();
    }

    public static void setLng(String lng) {
        App.sp.edit().putString("lng", lng).apply();
    }


    




    private static Toast toast;
    /**
     * 简洁版toast, 可直接覆盖
     * @param c context
     * @param s string
     */
    public static void toast (Context c, String s) {
        if (toast == null) {
            toast = Toast.makeText(c, s, Toast.LENGTH_LONG);
        } else {
            toast.setText(s);
        }
        toast.show();
    }

    /**
     * 简洁版 snackbar
     * @param v view
     * @param s string
     */
    public static void snack (View v, String s) {
        Snackbar.make(v, s, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 简洁版log
     * @param s string
     */
    public static void log (String s) {
        Log.w("L_LOG", "log: " + s);
    }


    /**
     * 向网关推送
     * @param tag tag
     * @param msg msg
     */
    public static void send2Gate(final String tag, final String msg) {

        RequestParams params = new RequestParams(URL_PUSH + "?tag=" + tag + "&msg=" + msg);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.optString("code").equals("1")) {
                        Log.e(TAG, msg + " to " + tag);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "PUSH_ERR" + ex.getMessage());
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
     * 解绑网关
     * @param c context
     */
    public static void unbindGate(final Context c) {
        RequestParams params = new RequestParams(URL_UNBIND);
        params.addParameter("mail", getPhone());
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("code").equals("1")) {
                    L.toast(c, "已解除网关绑定， 请点击 + 按钮添加新设备");
                    // 清除本地数据
                    setGateImei("x");
                    setBIND(false);
                } else {
                    L.toast(c, "UNBIND_CODE_ERR");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.toast(c, "UNBIND_ERR: " + ex.getMessage() );
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
     * 生成地点+名称
     * @param placeNo placeNo
     * @param name place in db, name in fact
     * @return String
     */
    public static String nameText(int placeNo, String name) {
        return rooms[placeNo] + "-" + name;
    }

    /**
     * type 代码转 名称
     * @param type typeNo
     * @return typeText
     */
    public static String typeText(String type) {
        switch (type) {
            case "04":
                return "烟雾监测器";
            case "06":
                return "窗帘控制器";
            default:
                return "暂不支持";
        }
    }


    /**
     * 设定布局图
     * @param activity activity
     * @param i layout xml No
     * @param root container
     * @return layouts
     */
    public static LinearLayout[] initGraph(Activity activity, int i, ViewGroup root) {
        LinearLayout[] layouts = new LinearLayout[6];
        if (i == 1) {
            layouts[0] = (LinearLayout) activity.getLayoutInflater()
                    .inflate(R.layout.graph_layout_1, null);
        }
        root.addView(layouts[0]);
        layouts[1] = (LinearLayout) layouts[0].findViewById(R.id.living_room_layout);
        layouts[2] = (LinearLayout) layouts[0].findViewById(R.id.bedroom_layout);
        layouts[3] = (LinearLayout) layouts[0].findViewById(R.id.dining_room_layout);
        layouts[4] = (LinearLayout) layouts[0].findViewById(R.id.book_room_layout);
        layouts[5] = (LinearLayout) layouts[0].findViewById(R.id.restroom_layout);

        return layouts;
    }

    /**
     * 右滑返回Listener
     * @param activity activity
     * @return listener
     */
    public static GestureDetector.OnGestureListener getSwipeBackListener(final Activity activity) {
        return new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                // 右滑返回
                if (motionEvent1.getX() - motionEvent.getX() > 200) {
                    activity.onBackPressed();
                }
                return false;
            }
        };
    }

    // list 不得已放在外面...
    private static List<SimpleDeviceData> list = new ArrayList<>();
    private static SimpleDeviceData data = null;
    /**
     * 使用 Dialog 生成命令
     * @return map 包括描述和命令
     */
    public static SimpleDeviceData getActionByDialog(final Activity activity) {
        // 初始加个默认的
        list.add(new SimpleDeviceData());

        // 辅助map
        Map<String, Integer> placeNo = new HashMap<>();
        placeNo.put("客厅", 1);
        placeNo.put("卧室", 2);
        placeNo.put("餐厅", 3);
        placeNo.put("厨房", 4);
        placeNo.put("卫生间", 5);

        Map<String, String> typeNo = new HashMap<>();
//        typeNo.put("温湿度光照检测器", "02");
        typeNo.put("烟雾监测器", "04");
        typeNo.put("红外防盗器", "05");
        typeNo.put("窗帘控制器", "06");
        typeNo.put("多级调光灯", "09");
        typeNo.put("智能插座", "0A");

        // 获取布局
        LinearLayout layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.dialog_add_action, null);
        //spinner们
        AppCompatSpinner placeSpinner = (AppCompatSpinner) layout.findViewById(R.id.spinner_place);
        AppCompatSpinner typeSpinner = (AppCompatSpinner) layout.findViewById(R.id.spinner_type);
        AppCompatSpinner nameSpinner = (AppCompatSpinner) layout.findViewById(R.id.spinner_name);
        // 三类只显示一个
        final SwitchCompat switchCompat = (SwitchCompat) layout.findViewById(R.id.on_off);
        final LinearLayout plugLinear = (LinearLayout) layout.findViewById(R.id.plug_linear);
        final LinearLayout lightLinear = (LinearLayout) layout.findViewById(R.id.light_linear);
        final AppCompatSeekBar seekBar = (AppCompatSeekBar) layout.findViewById(R.id.light_seek_bar);
        final TextView lightText = (TextView) layout.findViewById(R.id.light_text);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (i) {
                    case 0:
                        lightText.setText("关");
                        break;
                    case 1:
                        lightText.setText("暗");
                        break;
                    case 2:
                        lightText.setText("亮");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // adapter们
        ArrayAdapter<String> placeAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, activity.getResources().getStringArray(R.array.rooms_spinner));
        placeSpinner.setAdapter(placeAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, activity.getResources().getStringArray(R.array.device_list));
        typeSpinner.setAdapter(typeAdapter);

        // 获取当前文字?
        final String placeK = placeSpinner.getSelectedItem().toString();
        final int place = placeNo.get(placeK);
        final String typeK = typeSpinner.getSelectedItem().toString();
        String type = typeNo.get(typeK);
        // type 显示三类之一
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        switchCompat.setVisibility(View.VISIBLE);
                        plugLinear.setVisibility(View.GONE);
                        lightLinear.setVisibility(View.GONE);
                        break;
                    case 3:
                        switchCompat.setVisibility(View.GONE);
                        plugLinear.setVisibility(View.GONE);
                        lightLinear.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        switchCompat.setVisibility(View.GONE);
                        plugLinear.setVisibility(View.VISIBLE);
                        lightLinear.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        RequestParams params = new RequestParams(L.URL_GET_NAME_LIST);
        params.addParameter("gate", getGateImei());
        params.addParameter("place", place);
        params.addParameter("type", type);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (!result.optString("code").equals("1")) {
                    L.toast(activity, "DIALOG_CODE_ERR");
                    return;
                }

                JSONArray info = result.optJSONArray("info");
                list.clear();
                for (int i = 0; i < info.length(); i++) {
                    JSONObject obj = info.optJSONObject(i);
                    list.add(new SimpleDeviceData(obj.optString("no"), obj.optString("name")));
                }

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


        String [] names = list2Names(list);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, names);
        nameSpinner.setAdapter(nameAdapter);

        // TODO: 2016/7/23 通过网络进行初始化 选定后setVisibility 然后生成

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("请选择").setView(layout).setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String stateK = null;
                String state = null;
                if (switchCompat.getVisibility() == View.VISIBLE) {
                    state = switchCompat.isChecked() ? "0001":"0000";
                    stateK = switchCompat.isChecked() ? "开":"关";
                } else if (plugLinear.getVisibility() == View.VISIBLE) {
                    state = getPlugState(plugLinear);
                    stateK = getPlugStateK(plugLinear);
                } else if (lightLinear.getVisibility() == View.VISIBLE) {
                    state = getLightState(lightLinear);
                    stateK = getLightStateK(lightLinear);
                }
                String name = placeK + "-" + typeK + "-" + stateK ;
                data = new SimpleDeviceData(state, name);
            }

            private String getLightStateK(LinearLayout lightLinear) {
                AppCompatSeekBar seekBar = (AppCompatSeekBar) lightLinear.getChildAt(0);
                int progress = seekBar.getProgress();
                switch (progress) {
                    case 0:
                        return "关";
                    case 1:
                        return "暗";
                    case 2:
                        return "亮";

                }
                return "an";
            }
            private String getLightState(LinearLayout lightLinear) {
                AppCompatSeekBar seekBar = (AppCompatSeekBar) lightLinear.getChildAt(0);
                return "0000" + seekBar.getProgress();
            }


            private String getPlugStateK(LinearLayout plugLinear) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < plugLinear.getChildCount(); i++) {
                    SwitchCompat compat = (SwitchCompat)plugLinear.getChildAt(i);
                    sb.append(compat.isChecked() ? "开":"关");
                }
                return sb.toString();
            }
            private String getPlugState(LinearLayout plugLinear) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < plugLinear.getChildCount(); i++) {
                    SwitchCompat compat = (SwitchCompat)plugLinear.getChildAt(i);
                    sb.append(compat.isChecked() ? "1":"0");
                }
                return sb.toString();
            }

        }).setNegativeButton("取消", null).create().show();
        return data;
    }

    private static String[] list2Names(List<SimpleDeviceData> list) {
        String[] res = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i).getName();
        }
        return res;
    }

    private static String getTypeNoFromList(List<SimpleDeviceData> list, String name) {
        for (SimpleDeviceData data : list) {
            if (data.getName().equals(name)) {
                return data.getNo();
            }
        }
        return "00";
    }

    /**
     * 命令列表转字符串 用逗号分隔
     * @param list list
     * @return string
     */
    public static String array2Actions(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append(item);
            sb.append(",");
        }
        return sb.toString();
    }

    private static boolean masterFlag;
    public static boolean isMaster() {
        masterFlag = false;
        RequestParams params = new RequestParams(URL_IS_MASTER);
        params.addParameter("gate", getGateImei());
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            boolean flag = false;
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("code").equals("1")) {
                    String name = getName();
                    String info = result.optString("info");
                    flag = name.equals(info);

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                masterFlag = flag;

            }
        });
        return masterFlag;
    }


    public static void setGateMaster(String gateMaster) {
        App.sp.edit().putString("master", gateMaster).apply();
    }

    public static String getGateMaster() {
        return App.sp.getString("master", "");
    }
}
