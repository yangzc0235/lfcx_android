package com.lfcx.common.utils;

import android.text.TextUtils;
import android.util.Log;

import com.lfcx.common.BuildConfig;

/**
 * author: drawthink
 * desc  : (日志工具类)
 */

public class LogUtils {

    public static void e(String tag,String msg){
        if(TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)){
            return;
        }
        if(BuildConfig.LOG_DEBUG){
            Log.e(tag,msg);
        }
    }

    public static void i(String tag,String msg){
        if(TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)){
            return;
        }
        if(BuildConfig.LOG_DEBUG){
            Log.i(tag,msg);
        }
    }

    public static void w(String tag,String msg){
        if(TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)){
            return;
        }
        if(BuildConfig.LOG_DEBUG){
            Log.w(tag,msg);
        }
    }

    public static void d(String tag,String msg){
        if(TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)){
            return;
        }
        if(BuildConfig.LOG_DEBUG){
            Log.d(tag,msg);
        }
    }
}
