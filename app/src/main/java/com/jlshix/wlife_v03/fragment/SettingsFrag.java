package com.jlshix.wlife_v03.fragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.L;

/**
 * Created by Leo on 2016/7/11.
 *
 */
public class SettingsFrag extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设定相关xml
        addPreferencesFromResource(R.xml.prefer);

        // 自动登录设定
        CheckBoxPreference auto = (CheckBoxPreference) findPreference("auto_login");
        auto.setChecked(L.isAutoLogin());
        // 点击设定
        auto.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean isChecked = ((CheckBoxPreference)preference).isChecked();
                // why?
                L.setAutoLogin(!isChecked);
                // have to
                return true;
            }
        });

    }
}
