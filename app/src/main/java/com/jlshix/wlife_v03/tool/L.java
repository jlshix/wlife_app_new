package com.jlshix.wlife_v03.tool;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jlshix.wlife_v03.App;
import com.jlshix.wlife_v03.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
            Color.TRANSPARENT, Color.parseColor("#FF4081"), Color.parseColor("#4CAF50"), Color.parseColor("#2196F3")
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
    public static final String URL_ADD_ORDER = "http://jlshix.com/wlife2/add_order.php";
    public static final String URL_FEEDBACK = "http://jlshix.com/wlife2/feedback.php";
    public static final String URL_RENAME_GATE = "http://jlshix.com/wlife2/rename_gate.php";
    public static final String URL_RENAME_DEV = "http://jlshix.com/wlife2/rename_device.php";
    public static final String URL_PLACE_DEV = "http://jlshix.com/wlife2/place_dev.php";
    public static final String URL_DEL_DEV = "http://jlshix.com/wlife2/del_dev.php";
    public static final String URL_ONLINE = "http://jlshix.com/wlife2/account_online.php";
    public static final String URL_STATISTICS = "http://jlshix.com/wlife2/statistics.php";


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
        Log.w("L_LOG", s);
    }


    /**
     * 向网关推送
     * @param tag tag
     * @param msg msg
     */
    public static void send2Gate(final String tag, final String msg) {

        RequestParams params = new RequestParams(URL_PUSH);
        params.addParameter("tag", L.getGateImei());
        params.addParameter("msg", msg);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                log(result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.optString("code").equals("1")) {
                        Log.e(TAG, msg + " to " + tag);
                    } else {
                        Log.e(TAG, "PUSH_CODE_ERR");
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
            case "05":
                return "红外防盗器";
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


    public static void setGateMaster(String gateMaster) {
        App.sp.edit().putString("master", gateMaster).apply();
    }

    public static String getGateMaster() {
        return App.sp.getString("master", "");
    }


    /**
     * 删除设备
     */
    public static void delDev(final Context context, final Handler handler, final int msg, final String type, final String no) {
        new AlertDialog.Builder(context)
                .setMessage("确认删除设备?")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RequestParams params = new RequestParams(L.URL_DEL_DEV);
                params.addParameter("gate", L.getGateImei());
                params.addParameter("type", type);
                params.addParameter("no", no);
                x.http().post(params, new Callback.CommonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.i(TAG, "onSuccess: " + result.toString());
                        if (!result.optString("code").equals("1")) {
                            L.toast(context, "DEL_CODE_ERR");
                            return;
                        }
                        handler.sendEmptyMessage(msg);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        L.toast(context, "DEL_ON_ERR " + ex.getMessage());
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
     * 更改设备位置
     */
    public static void placeDev(final Context context, final Handler handler, final int msg, final String type, final String no, int place) {
        LinearLayout layout = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_replace, null);
        TextView currentPlace = (TextView) layout.findViewById(R.id.current_place);
        currentPlace.setText(rooms[place]);
        final Spinner spinner = (Spinner) layout.findViewById(R.id.new_place);

        new AlertDialog.Builder(context).setTitle("更改位置").setView(layout)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int newPlace = spinner.getSelectedItemPosition() + 1;
                        RequestParams params = new RequestParams(L.URL_PLACE_DEV);
                        params.addParameter("gate", L.getGateImei());
                        params.addParameter("type", type);
                        params.addParameter("no", no);
                        params.addParameter("place", String.valueOf(newPlace));
                        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                if (!result.optString("code").equals("1")) {
                                    L.toast(context, "PLACE_CODE_ERR");
                                    return;
                                }
                                handler.sendEmptyMessage(msg);

                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                L.toast(context, "PLACE_ON_ERR " + ex.getMessage());
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
     * 重命名设备
     */
    public static void devRename(final Context context, final Handler handler, final int msg, final String type, final String no, String name) {
        LinearLayout layout = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_rename, null);
        TextView currentName = (TextView) layout.findViewById(R.id.current_name);
        currentName.setText(name);
        final EditText newName = (EditText) layout.findViewById(R.id.new_name);
        new AlertDialog.Builder(context).setTitle("重命名").setView(layout)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RequestParams params = new RequestParams(L.URL_RENAME_DEV);
                        params.addParameter("gate", L.getGateImei());
                        params.addParameter("type", type);
                        params.addParameter("no", no);
                        params.addParameter("name", newName.getText().toString().trim());
                        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                if (!result.optString("code").equals("1")) {
                                    L.toast(context, "RENAME_CODE_ERR");
                                    return;
                                }
                                handler.sendEmptyMessage(msg);

                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                L.toast(context, "RENAME_ON_ERR: " + ex.getMessage());
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
     * 设定网关是否在线
     * @param isOnline boolean
     */
    public static void setOnline(final boolean isOnline) {
        RequestParams params = new RequestParams(URL_ONLINE);
        params.addParameter("mail", getPhone());
        params.addParameter("online", isOnline ? "1":"0");
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (!result.optString("code").equals("1")) {
                    Log.e(TAG, "setOnline: " + "ONLINE_CODE_ERR");
                    return;
                }
                Log.e(TAG, "setOnline" + isOnline);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "setOnlineErr" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


}
