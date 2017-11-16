package com.lfcx.main.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.iflytek.cloud.SpeechUtility;
import com.lfcx.common.crashhandler.CrashHandler;
import com.lfcx.main.R;

import cn.jpush.android.api.JPushInterface;


/**
 * author: drawthink
 * desc  : (该类描述)
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);

        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
