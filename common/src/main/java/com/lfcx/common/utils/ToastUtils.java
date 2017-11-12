package com.lfcx.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * author: drawthink
 * desc  : (该类描述)
 */

public class ToastUtils {

    public static void shortToast(Context ctx, String msg){
        if(TextUtils.isEmpty(msg) || null == ctx){
            return;
        }
        try {
            Toast.makeText(ctx.getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            LogUtils.e(ctx.getClass().getSimpleName(),ex.getMessage());
        }
    }

    public static void longToast(Context ctx, String msg){
        if(TextUtils.isEmpty(msg) || null == ctx){
            return;
        }
        try {
            Toast.makeText(ctx.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            LogUtils.e(ctx.getClass().getSimpleName(),ex.getMessage());
        }
    }
}
