package com.jlshix.wlife_v03;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import org.xutils.x;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Leo on 2016/7/6.
 * App
 */
public class App extends Application {

    private static final String TAG = "APP";
    // SharedPreferences definition
    public static SharedPreferences sp;
    @Override
    public void onCreate() {
        super.onCreate();

        // xUtils
        x.Ext.init(this);
        x.Ext.setDebug(false);

        // jpush
        HashSet<String> tag = new HashSet<>();
        tag.add("unbind");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.setTags(this, tag, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.i(TAG, "gotResult: " + i);
                Log.i(TAG, "gotResult: " + s);
                Log.i(TAG, "gotResult: " + set.toString());
            }
        });

        //sp
        sp = getSharedPreferences("conf", MODE_PRIVATE);
    }
}
