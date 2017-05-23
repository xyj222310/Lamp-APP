package com.yjtse.lamp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;


import com.yjtse.lamp.Config;
import com.yjtse.lamp.domain.Login;
import com.yjtse.lamp.utils.SharedPreferencesUtil;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Config.writeToDebug("app Splash...");

        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2));
        String token = (String) SharedPreferencesUtil.query(this, Config.KEY_TOKEN, "String");
        boolean isRemember = (boolean) SharedPreferencesUtil.query(this, Config.KEY_REMEMBER_PWD, "boolean");
        if (token != null && !TextUtils.isEmpty(token) && isRemember) {//判断token是否为空或者没有意义,并且记住了密码
            Config.loginState = Login.State.LOGIN;
            Config.loginStyle = Login.Style.LOGIN_REMOTE;
//            Intent intent = new Intent(this, TabFragmentActivity.class);
//            startActivity(intent);//跳转页面
        } else {//如果token值过期就直接重新登录
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();//结束此activity

    }
}
