package com.lfcx.customer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.lfcx.common.utils.LogUtils;
import com.lfcx.customer.event.CallCarSuccessEvent;
import org.greenrobot.eventbus.EventBus;

/**
 * author: drawthink
 * date  : 2017/10/14
 * des   : 极光推送消息接受类
 */

public class CustomerJPushReceiver extends BroadcastReceiver {
    private static final String TAG = "CustomerJPushReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
            String codeStyle = (String)bundle.get("code");
            Toast.makeText(context, codeStyle, Toast.LENGTH_SHORT).show();
            switch (codeStyle){
                case "0-1"://增加
                    break;
                case "0-2"://删除
                    break;
                case "0-3"://修改
                    break;
                case "0-400"://查看接单司机信息
                    String pk_user = bundle.getString("pk_user");
                    EventBus.getDefault().post(new CallCarSuccessEvent(pk_user));
                    break;
            }
        } catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }

    }


}
