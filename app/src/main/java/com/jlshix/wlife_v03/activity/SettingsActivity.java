package com.jlshix.wlife_v03.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.fragment.AccountFrag;
import com.jlshix.wlife_v03.fragment.SettingsFrag;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.L;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_settings)
public class SettingsActivity extends BaseActivity {

    private GestureDetector gd;

    public static final int ACCOUNT = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ACCOUNT:
                    AccountFrag accountFrag = new AccountFrag();
                    accountFrag.setHandler(handler);
                    getFragmentManager().beginTransaction().
                            replace(R.id.container, accountFrag).addToBackStack(null).commit();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 放置fragment
        SettingsFrag frag = new SettingsFrag();
        frag.setHandler(handler);
        getFragmentManager().beginTransaction().replace(R.id.container, frag).commit();
        // 手势处理 只看 onFling
        gd = new GestureDetector(this, L.getSwipeBackListener(SettingsActivity.this));
    }

    /**
     * 点击事件处理移交给 gs
     * @param event 事件
     * @return boolean
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gd.onTouchEvent(event);
    }

}