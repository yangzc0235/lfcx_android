package com.lfcx.customer.application;

import android.app.Application;

/**
 * author: drawthink
 * desc  : (该类描述)
 */

public class CustomerApplication extends Application {

//    private RefWatcher mRefWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
//        mRefWatcher = LeakCanary.install(this);
    }
}
