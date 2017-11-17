package com.lfcx.driver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * author: drawthink
 * date  : 2017/10/14
 * des   : 极光推送消息接受类
 */

public class DriverJPushReceiver extends BroadcastReceiver {
    private static final String TAG = "CustomerJPushReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String action = intent.getAction();
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
            String codeStyle = (String)bundle.get("code");

            Log.v("system---codeStyle-->",codeStyle);
            switch (codeStyle){
                case "0-1"://增加
                    break;
                case "0-2"://删除
                    break;
                case "0-3"://修改
                    break;
                case "0-4"://查询
                    break;
            }
        } catch (Exception e){

        }

    }


}
