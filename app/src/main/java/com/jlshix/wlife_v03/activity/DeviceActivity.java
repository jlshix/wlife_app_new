package com.jlshix.wlife_v03.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.fragment.Appliance;
import com.jlshix.wlife_v03.fragment.Envir;
import com.jlshix.wlife_v03.fragment.Light;
import com.jlshix.wlife_v03.fragment.Others;
import com.jlshix.wlife_v03.fragment.Plug;
import com.jlshix.wlife_v03.tool.BaseActivity;
import com.jlshix.wlife_v03.tool.L;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_device)
public class DeviceActivity extends BaseActivity {

    //toolbar
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    //mViewPager 作为容器
    @ViewInject(R.id.vp)
    private ViewPager vp;

    //tabs 选项卡
    @ViewInject(R.id.tabs)
    private TabLayout tabs;

    //fragments
    private Envir envir;
    private Plug plug;
    private Light light;
    private Others others;
    private Appliance appliance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

    }

// TODO: 2016/7/22 完善语音

    /**
     * 初始化界面
     */
    private void initView() {
        envir = new Envir();
        plug = new Plug();
        light = new Light();
        others = new Others();
        appliance = new Appliance();
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return envir;
                    case 1:
                        return plug;
                    case 2:
                        return light;
                    case 3:
                        return others;
                    case 4:
                        return appliance;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String[] title = getResources().getStringArray(R.array.frag_title);
                return title[position];
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 滚动至顶部
                switch (position) {
                    case 0:
                        envir.getHandler().sendEmptyMessage(Envir.FOCUS_UP);
                        break;
                    case 1:
                        plug.getHandler().sendEmptyMessage(Plug.FOCUS_UP);
                        break;
                    case 2:
                        light.getHandler().sendEmptyMessage(Light.FOCUS_UP);
                        break;
                    case 3:
                        others.getHandler().sendEmptyMessage(Others.FOCUS_UP);
                        break;
                    case 4:
                        appliance.getHandler().sendEmptyMessage(Appliance.FOCUS_UP);
                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setOffscreenPageLimit(4);
        tabs.setupWithViewPager(vp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            // 跳转添加设备或家电
            if (vp.getCurrentItem() == 4) {
                Intent intent = new Intent(getApplicationContext(), AddApplianceActivity.class);
                startActivityForResult(intent, L.ADD_REQUEST);
            } else {
                Intent intent = new Intent(getApplicationContext(), AddDeviceActivity.class);
                startActivityForResult(intent, L.ADD_REQUEST);
            }
            return true;
        }
        if (id == R.id.action_msg) {
            Intent intent = new Intent(getApplicationContext(), MsgActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == L.ADD_REQUEST && resultCode == L.ADD_RETURN) {
            // 发送广播 TODO 接收广播
            String type = data.getStringExtra("type");
            Intent intent = new Intent("android.intent.action.MY_BROADCAST");
            intent.putExtra("msg", "REFRESH");
            intent.putExtra("type", type);
            sendBroadcast(intent);
        }
    }
}

