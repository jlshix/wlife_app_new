package com.jlshix.wlife_v03.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.jlshix.wlife_v03.App;
import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.activity.FeedbackActivity;
import com.jlshix.wlife_v03.activity.GraphActivity;
import com.jlshix.wlife_v03.activity.MemberActivity;
import com.jlshix.wlife_v03.activity.SettingsActivity;
import com.jlshix.wlife_v03.activity.VoiceListActivity;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Leo on 2016/7/11.
 *
 */
public class SettingsFrag extends PreferenceFragment {

    private Handler handler;
    private CheckBoxPreference auto;
    private Preference clear, wx, share, help, about,  feedback, update, quit;
    private Preference member, account, voice, graph;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

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
                        // TODO: 2016/8/2 环信退出?
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
                img.setImageResource(R.drawable.wlife_qr);
                img.setMaxHeight(300);
                img.setMaxWidth(300);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.settings_wx).setView(img)
                        .setPositiveButton("确认", null).create().show();
                return false;
            }
        });

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

//        // 分享
//        share = findPreference("share");
//        share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                L.toast(getActivity(), "开发中...");
//                return false;
//            }
//        });

        // 家庭组
        member = findPreference("member");
        member.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                memberManage();
                return false;
            }
        });

        // account
        account = findPreference("account");
        account.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                handler.sendEmptyMessage(SettingsActivity.ACCOUNT);
                return false;
            }
        });

        // voice
        voice = findPreference("voice");
        voice.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), VoiceListActivity.class));
                return false;
            }
        });
        
        // feedback
        feedback = findPreference("feedback");
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                return false;
            }
        });
        
        // graph
        graph = findPreference("graph");
        graph.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), GraphActivity.class));
                return false;
            }
        });
        
        
        
        

        


    }

    private void memberManage() {
        RequestParams params = new RequestParams(L.URL_IS_MASTER);
        params.addParameter("gate", L.getGateImei());
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            boolean flag = false;
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("code").equals("1")) {
                    String name = L.getName();
                    String info = result.optString("info");
                    flag = name.equals(info);
                    if (flag) {
                        startActivity(new Intent(getActivity(), MemberActivity.class));
                    } else {
                        L.snack(getView(), "此功能仅限家庭管理员使用");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
