package com.yjtse.lamp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.yjtse.lamp.R;
import com.yjtse.lamp.adapter.FragmentAdapter;
import com.yjtse.lamp.fragment.MyDeviceFragment;
import com.yjtse.lamp.fragment.SelfCenterFragment;
import com.yjtse.lamp.fragment.SettingFragment;
import com.yjtse.lamp.widgets.CenterTitleActionBar;

import java.util.ArrayList;
import java.util.List;

public class TabFragmentActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private ViewPager myViewPager;// 要使用的ViewPager

    private MyDeviceFragment mMyDeviceFragment;//我的设备
    private SelfCenterFragment mSelfCenterFragment;//列表，设置
    private SettingFragment mSettingFragment;//个人中心
    private Button fm_below_mydevice, fm_below_setting, fm_below_self_center;

    private CenterTitleActionBar actionBar;//中央标题actionBar组件
    private ActionBarSettingOnClickListener actionBarSettingOnClickListener;  //ActionBar事件监听事件对象
    private ActionBarMyDeviceOnClickListener actionBarMyDeviceOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_below);
        myViewPager = (ViewPager) findViewById(R.id.viewPager);
        mMyDeviceFragment = new MyDeviceFragment();//fragment
        mSelfCenterFragment = new SelfCenterFragment();//fragment
        mSettingFragment = new SettingFragment();//fragment
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(mMyDeviceFragment);
        fragmentList.add(mSettingFragment);
        fragmentList.add(mSelfCenterFragment);
        FragmentAdapter myFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);

        fm_below_mydevice = (Button) findViewById(R.id.fm_below_mydevice);
        fm_below_setting = (Button) findViewById(R.id.fm_below_setting);
        fm_below_self_center = (Button) findViewById(R.id.fm_below_self_center);
        myViewPager.setCurrentItem(0);  //初始在第一个界面
        fm_below_mydevice.setSelected(true);

        myViewPager.setOnPageChangeListener(this);
        actionBar = new CenterTitleActionBar(this, getActionBar());
        actionBar.setTitle("设备列表");//标题
        actionBar.setFirstBtnID(R.drawable.add);
        actionBar.setBackImageID(R.drawable.transparent);
        actionBar.setCustomActionBar();

        //一系列点击事件，包括标题，第一，第二，第三以及回退按钮
        actionBar.setOnClickActionBarListener(new CenterTitleActionBar.OnClickActionBarListener() {
            @Override
            public void onTitleClick() {
            }

            @Override
            public void onSecondBtnClick() {
            }

            @Override
            public void onFirstBtnClick() {
                if (actionBarMyDeviceOnClickListener != null) {
                    if (actionBar.getFirstBtnID() == R.drawable.add) {
                        actionBarMyDeviceOnClickListener.onMyDeviceFirstBtnClick();
                    }
                }
            }

            @Override
            public void onBackBtnClick() {
            }
        });
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                fm_below_mydevice.setSelected(true);
                fm_below_setting.setSelected(false);
                fm_below_self_center.setSelected(false);
                actionBar.setTitle("设备列表");
                actionBar.setFirstBtnID(R.drawable.add);
                actionBar.setCustomActionBar();//显示视图以及actionbar的属性
                break;
            case 1:
                fm_below_mydevice.setSelected(false);
                fm_below_setting.setSelected(true);
                fm_below_self_center.setSelected(false);
                actionBar.setTitle("动态");
                actionBar.setFirstBtnID(0);//默认无显示
                actionBar.setCustomActionBar();//显示视图以及actionbar的属性
                break;
            case 2:
                fm_below_mydevice.setSelected(false);
                fm_below_setting.setSelected(false);
                fm_below_self_center.setSelected(true);
                actionBar.setTitle("个人中心");
                actionBar.setFirstBtnID(0);//默认无显示
                actionBar.setCustomActionBar();//显示视图以及actionbar的属性
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.fm_below_mydevice:
                myViewPager.setCurrentItem(0);
                break;
            case R.id.fm_below_setting:
                myViewPager.setCurrentItem(1);
                break;
            case R.id.fm_below_self_center:
                myViewPager.setCurrentItem(2);
                break;
        }
    }

    public ActionBarMyDeviceOnClickListener getActionBarMyDeviceOnClickListener() {
        return actionBarMyDeviceOnClickListener;
    }

    public void setActionBarMyDeviceOnClickListener(ActionBarMyDeviceOnClickListener actionBarMyDeviceOnClickListener) {
        this.actionBarMyDeviceOnClickListener = actionBarMyDeviceOnClickListener;
    }

    public ActionBarSettingOnClickListener getActionBarSettingOnClickListener(ActionBarSettingOnClickListener actionBarOnClickListener) {
        return this.actionBarSettingOnClickListener;
    }

    public void setActionBarSettingOnClickListener(ActionBarSettingOnClickListener actionBarOnClickListener) {
        this.actionBarSettingOnClickListener = actionBarOnClickListener;
    }

    //静态接口
    public interface ActionBarSettingOnClickListener {
        void onMySettingFirstBtnClick();
    }

    public interface ActionBarMyDeviceOnClickListener {
        void onMyDeviceFirstBtnClick();
    }
}
