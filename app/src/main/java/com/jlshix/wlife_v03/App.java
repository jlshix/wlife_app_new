package com.jlshix.wlife_v03;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.jlshix.wlife_v03.receiver.CallReceiver;

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

    //easemob
    public static Context applicationContext;
    private CallReceiver callReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        // xUtils
        x.Ext.init(this);
        x.Ext.setDebug(false);

        //sp
        sp = getSharedPreferences("conf", MODE_PRIVATE);


        // jpush
        HashSet<String> tag = new HashSet<>();
        tag.add("unbind");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.setTags(this, tag, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.i(TAG, "gotResult: " + set.toString());
            }
        });

        //easemob
        applicationContext = this;
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);
        IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingCallBroadcastAction());
        if(callReceiver == null){
            callReceiver = new CallReceiver();
        }
        //注册通话广播接收者
        this.registerReceiver(callReceiver, callFilter);
    }
}
