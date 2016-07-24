package com.jlshix.wlife_v03.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.L;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_test)
public class TestActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    
    @ViewInject(R.id.fab)
    FloatingActionButton fab;
    
    @ViewInject(R.id.actions)
    TextView actionsT;
    
    @ViewInject(R.id.des)
    TextView desT;
    
    private String actions = "";
    private String des = "";

    public void setActions(String actions) {
        if (this.actions.equals("")) {
            this.actions = actions + ",";
        } else {
            this.actions += actions + ",";
        }
    }

    public void setDes(String des) {
        if (this.des.equals("")) {
            this.des = des + ",";
        } else {
            this.des += des + ",";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        
    }
    
    @Event(R.id.fab)
    private void get(View view) {
//        SimpleDeviceData data = L.getActionByDialog(TestActivity.this);
        startActivityForResult(new Intent(TestActivity.this, DialogActivity.class), L.DIALOG_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == L.DIALOG_REQUEST && resultCode == L.DIALOG_RETURN) {
            int code = data.getIntExtra("code", -1);
            switch (code) {
                case -1:
                    L.snack(toolbar, "return code err");
                    return;
                case 0:
                    L.snack(toolbar, "已取消");
                    return;
                case 1:
                    setActions(data.getStringExtra("action"));
                    setDes(data.getStringExtra("des"));
                    actionsT.setText(actions);
                    desT.setText(des);
            }
        }
    }
}
