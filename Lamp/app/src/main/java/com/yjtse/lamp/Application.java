package com.yjtse.lamp;

import com.mob.mobapi.MobAPI;

import cn.bmob.sms.BmobSMS;


/**
 * Created by XieYingjie on 2017/6/4/0004.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        MobAPI.initSDK(this, Config.MOB_APP_KEY);
        BmobSMS.initialize(this, Config.BmobSMSToken);
    }
}
