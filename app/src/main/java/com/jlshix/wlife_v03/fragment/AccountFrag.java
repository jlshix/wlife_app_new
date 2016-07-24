package com.jlshix.wlife_v03.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.L;

import org.xutils.x;

/**
 * Created by Leo on 2016/7/11.
 *
 */
public class AccountFrag extends PreferenceFragment {

    private Handler handler;
    private Preference mail, name, pw, qr;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设定相关xml
        addPreferencesFromResource(R.xml.account_prefer);


        // mail
        mail = findPreference("mail");
        mail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("开发中...").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       
                    }
                }).setNegativeButton("取消", null).create().show();
                return false;
            }
        });
        
        // name
        name = findPreference("name");
        name.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("开发中...").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       
                    }
                }).setNegativeButton("取消", null).create().show();
                return false;
            }
        });
        
        // pw
        pw = findPreference("pw");
        pw.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("开发中...").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       
                    }
                }).setNegativeButton("取消", null).create().show();
                return false;
            }
        });
        
        // qr
        qr = findPreference("qr");
        qr.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                ImageView img = new ImageView(getActivity());

                x.image().bind(img, L.URL_QR_CODE + "?text=" + L.getName());
                img.setMaxWidth(300);
                img.setMaxHeight(300);
                img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("扫码添加成员").setView(img)
                        .setPositiveButton("确认", null).create().show();
                return false;
            }
        });
        
        


    }
}
