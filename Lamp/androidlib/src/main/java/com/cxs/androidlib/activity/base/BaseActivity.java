package com.cxs.androidlib.activity.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;

import com.cxs.androidlib.constant.Constants;
import com.cxs.androidlib.utils.StatusBarUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by _CXS
 * Date:2016/3/30
 * Time:9:40
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    private Unbinder bind;
    public String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=getComponentName().getShortClassName();
        reverseStatusColor();
        init(savedInstanceState);
        if (setLayoutResourceID()!=0) {
            setContentView(setLayoutResourceID());
        }
        // 通过注解绑定控件
        bind = ButterKnife.bind(this);

        setUpView();
        setUpData();
    }
    protected void setUpData() {
    }
    /***
     * 用于在初始化View之前做一些事
     */
    protected void init(Bundle savedInstanceState) {

    }
    protected abstract int setLayoutResourceID();

    protected abstract void setUpView();


    protected <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }


    protected void startActivityWithoutExtras(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void startActivityWithExtras(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(extras);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    /**
     * 申请指定的权限.
     */
    public void requestPermission(int code, String... permissions) {

        ActivityCompat.requestPermissions(this, permissions, code);
    }

    /**
     * 判断是否有指定的权限
     */
    public boolean hasPermission(String... permissions) {

        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(this, permisson)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.WRITE_READ_EXTERNAL_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doSDCardPermission();
                }
                break;
        }
    }

    /**
     * 处理整个应用用中的SDCard业务
     */
    public void doSDCardPermission() {
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }

    /**
     * 隐藏状态栏
     */
    public void hiddenStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 改变状态栏颜色
     *
     * @param color
     */
    public void changeStatusBarColor(@ColorRes int color) {
        StatusBarUtil.setStatusBarColor(this, color);
    }

    /**
     * 调整状态栏为亮模式，这样状态栏的文字颜色就为深模式了。
     */
    private void reverseStatusColor() {
        StatusBarUtil.statusBarLightMode(this);
    }
}
