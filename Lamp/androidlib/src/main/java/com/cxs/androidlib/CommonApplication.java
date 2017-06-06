package com.cxs.androidlib;

import android.app.Application;
import android.os.Environment;

import com.cxs.androidlib.utils.ToastUtils;

import java.io.File;


/**
 * Created by _CXS
 * Date:2016/3/30
 * Time:20:59
 */
public class CommonApplication extends Application {
    private static CommonApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ToastUtils.init(this);
    }

    public static CommonApplication getInstance() {
        return mInstance;
    }

    @Override
    public File getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }
        return super.getCacheDir();
    }
}
