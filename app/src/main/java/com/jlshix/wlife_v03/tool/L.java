package com.jlshix.wlife_v03.tool;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jlshix.wlife_v03.App;

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

    /**
     * debug模式
     */
    public static boolean DEBUG = true;

    public static boolean BIND = false;

    /**
     * 请求网址集中区
     */
    public static String URL_LOGIN = "http://jlshix.com/wlife2/login.php/";
    public static String URL_REG = "http://jlshix.com/wlife2/register.php/";
    public static final String URL_WEATHER = "https://api.caiyunapp.com/v2/X6f3oc9bahTuV6Bv/";
    // TODO: 2016/7/12 check php
    private static final String URL_UNBIND = "http://jlshix.com/wlife2/unbind_gate.php/";
    public static String URL_PUSH = "http://jlshix.com/wlife2/togate.php/";
    public static String URL_SET_GATE = "http://jlshix.com/wlife2/set_gate.php/";
    public static final String URL_GATE = "http://jlshix.com/wlife2/get_gate.php/";
    public static final String URL_GATE_BIND = "http://jlshix.com/wlife2/bind_gate.php/";
    public static String URL_GET_MSG = "http://jlshix.com/wlife2/get_msg.php/";
    public static String URL_GET = "http://jlshix.com/wlife2/get.php/";
    public static String URL_SET = "http://jlshix.com/wlife2/set.php/";
    public static final String URL_ADD_DEV = "http://jlshix.com/wlife2/add_dev.php/";



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
     * 获取GPS信息
     * @param c context
     * @return string
     */
    public static String getGPS(Context c) {
        String mDefault = "120.1227,36.0001";
        //
//        LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
//
//        LocationListener locationListener = new LocationListener() {
//
//            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//                Log.e("Map", "onStatusChanged");
//            }
//
//            // Provider被enable时触发此函数，比如GPS被打开
//            @Override
//            public void onProviderEnabled(String provider) {
//                Log.e("Map", "onProviderEnabled");
//            }
//
//            // Provider被disable时触发此函数，比如GPS被关闭
//
//            @Override
//            public void onProviderDisabled(String provider) {
//                Log.e("Map", "onProviderDisabled");
////                loc_flag = "" + 1;
//            }
//
//            //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//            @Override
//            public void onLocationChanged(Location location) {
//                if (location != null) {
//                    Log.e("Map", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
//                }
//            }
//        };
//        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            L.toast(c, "LOCATION_PERMISSION_DENIED");
//            return null;
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        if(location != null){
////            loc_flag = ""+2;
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();
//            DecimalFormat df = new DecimalFormat("#.0000");
//            df.format(longitude);
//            df.format(latitude);
//            Log.e(TAG, "getGPS: " + longitude + "---" + latitude);
//            // TODO 使用 SharedPreferences 保存
//            return longitude + "," + latitude;
//        }
        return mDefault;
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
}
