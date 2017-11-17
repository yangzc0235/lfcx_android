package com.lfcx.customer.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.lfcx.common.crashhandler.CrashHandler;

import cn.jpush.android.api.JPushInterface;

/**
 * author: drawthink
 * desc  : (该类描述)
 */

public class CustomerApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志

        //极光推送
        JPushInterface.init(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
