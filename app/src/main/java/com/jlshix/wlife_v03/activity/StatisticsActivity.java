package com.jlshix.wlife_v03.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.fragment.DailyStatistics;
import com.jlshix.wlife_v03.fragment.HourlyStatistics;
import com.jlshix.wlife_v03.tool.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_msg)
public class StatisticsActivity extends BaseActivity {

    //toolbar
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    //mViewPager 作为容器
    @ViewInject(R.id.vp)
    private ViewPager vp;

    //tabs 选项卡
    @ViewInject(R.id.tabs)
    private TabLayout tabs;

    private HourlyStatistics hourlyStatistics;
    private DailyStatistics dailyStatistics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        initView(intent.getIntExtra("no", 0));
    }


    /**
     * 初始化界面
     * @param no
     */
    private void initView(int no) {
        hourlyStatistics = new HourlyStatistics();
        hourlyStatistics.setDevNo("0" + no);
        dailyStatistics = new DailyStatistics();
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return hourlyStatistics;
                    case 1:
                        return dailyStatistics;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String[] title = getResources().getStringArray(R.array.statistics_frag_title);
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

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabs.setupWithViewPager(vp);

    }


}
