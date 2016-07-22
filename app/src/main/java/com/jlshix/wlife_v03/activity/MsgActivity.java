package com.jlshix.wlife_v03.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.fragment.GateMsg;
import com.jlshix.wlife_v03.fragment.MemberMsg;
import com.jlshix.wlife_v03.tool.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_msg)
public class MsgActivity extends BaseActivity {

    //toolbar
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    //mViewPager 作为容器
    @ViewInject(R.id.vp)
    private ViewPager vp;

    //tabs 选项卡
    @ViewInject(R.id.tabs)
    private TabLayout tabs;

    private GateMsg gateMsg;
    private MemberMsg memberMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }


    /**
     * 初始化界面
     */
    private void initView() {
        gateMsg = new GateMsg();
        memberMsg = new MemberMsg();
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return gateMsg;
                    case 1:
                        return memberMsg;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String[] title = getResources().getStringArray(R.array.msg_frag_title);
                return title[position];
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    // TODO: 2016/7/22 滚动到底部?

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabs.setupWithViewPager(vp);
        
    }


}
