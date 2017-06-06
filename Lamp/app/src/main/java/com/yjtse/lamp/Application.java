package com.yjtse.lamp;

import com.cxs.androidlib.CommonApplication;
import com.mob.mobapi.MobAPI;

import cn.bmob.sms.BmobSMS;


/**
 * Created by XieYingjie on 2017/6/4/0004.
 */

public class Application extends CommonApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MobAPI.initSDK(this, Config.NEWS_APP_KEY);
        BmobSMS.initialize(this, Config.BmobSMSToken);
    }
}
