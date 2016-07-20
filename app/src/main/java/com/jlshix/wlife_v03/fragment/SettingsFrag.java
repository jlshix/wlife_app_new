package com.jlshix.wlife_v03.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.jlshix.wlife_v03.App;
import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.L;

/**
 * Created by Leo on 2016/7/11.
 *
 */
public class SettingsFrag extends PreferenceFragment {

    
    private CheckBoxPreference auto;
    private Preference clear, wx, share, help, about, update, quit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设定相关xml
        addPreferencesFromResource(R.xml.prefer);

        // 清除密码
        clear = findPreference("clear");
        clear.setEnabled(L.getPw().length() > 0);
        clear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                L.setPw("");
                L.setLogin(false);
                L.setAutoLogin(false);
                L.setRemember(false);
                clear.setEnabled(false);
                auto.setChecked(false);
                auto.setEnabled(false);
                return false;
            }
        });

        // 自动登录设定
        auto = (CheckBoxPreference) findPreference("auto_login");
        auto.setChecked(L.isAutoLogin());
        if (!clear.isEnabled()) {
            auto.setEnabled(false);
        }
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

        // 退出
        quit = findPreference("quit");
        quit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确认退出?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        App.sp.edit().clear().apply();
                        getActivity().setResult(L.SETTINGS_RETURN);
                        getActivity().finish();
                    }
                }).setNegativeButton("取消", null).create().show();
                return false;
            }
        });

        // 微信
        wx = findPreference("wx");
        wx.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ImageView img = new ImageView(getActivity());
                img.setImageResource(R.drawable.clear_day);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.settings_wx).setView(img)
                        .setPositiveButton("确认", null).create().show();
                return false;
            }
        });

//        // 帮助
//        help = findPreference("help");
//        help.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle(R.string.help).setMessage(R.string.large_text)
//                        .setPositiveButton("确认", null).create().show();
//                return false;
//            }
//        });

        // 关于
        about = findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.about).setMessage(R.string.about_dialog)
                        .setPositiveButton("确认", null).create().show();
                return false;
            }
        });

        // 分享
        share = findPreference("share");
        share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                L.toast(getActivity(), "开发中...");
                return false;
            }
        });

//        // update
//        update = findPreference("update");
//        update.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                L.toast(getActivity(), "开发中...");
//                return false;
//            }
//        });


    }
}
