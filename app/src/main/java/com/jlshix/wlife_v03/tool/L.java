package com.jlshix.wlife_v03.tool;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jlshix.wlife_v03.App;

/**
 * Created by Leo on 2016/7/11.
 * 工具类 常用sp读写封装与简单工具
 */
public class L {
    /**
     * debug模式
     */
    public static boolean DEBUG = true;

    /**
     * 请求网址集中区
     */
    public static String URL_LOGIN = "https://z.leoshi.me/wlife/login.php";
    public static String URL_REG = "https://z.leoshi.me/wlife/register.php";

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
}
