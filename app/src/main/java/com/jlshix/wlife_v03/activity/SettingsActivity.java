package com.jlshix.wlife_v03.activity;

import android.os.Bundle;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.fragment.SettingsFrag;
import com.jlshix.wlife_v03.tool.BaseActivity;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_settings)
public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 放置fragment
        getFragmentManager().beginTransaction().replace(R.id.container, new SettingsFrag()).commit();

    }

}
