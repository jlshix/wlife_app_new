package com.jlshix.wlife_v03;

import android.app.Application;
import android.content.SharedPreferences;

import org.xutils.x;

/**
 * Created by Leo on 2016/7/6.
 * App
 */
public class App extends Application {

    // SharedPreferences definition
    public static SharedPreferences sp;
    @Override
    public void onCreate() {
        super.onCreate();

        // xUtils
        x.Ext.init(this);
        x.Ext.setDebug(true);

        //sp
        sp = getSharedPreferences("conf", MODE_PRIVATE);
    }
}
