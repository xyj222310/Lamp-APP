package com.yjtse.lamp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;

import com.yjtse.lamp.Config;
import com.yjtse.lamp.utils.SharedPreferencesUtil;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Config.writeToDebug("app Splash...");

        SystemClock.sleep(TimeUnit.SECONDS.toSeconds(10));
        String userId = (String) SharedPreferencesUtil.query(this, Config.KEY_USERNAME, "String");
        String userPass = (String) SharedPreferencesUtil.query(this, Config.KEY_PASSWORD, "String");
        boolean isRemember = (boolean) SharedPreferencesUtil.query(this, Config.KEY_REMEMBER_PWD, "boolean");
        if (userId != null && !TextUtils.isEmpty(userId) && isRemember && userPass != null && !TextUtils.isEmpty(userPass)) {//判断token是否为空或者没有意义,并且记住了密码
            Intent intent = new Intent(this, TabFragmentActivity.class);
            startActivity(intent);//跳转页面

        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();//结束此activity

    }
}
