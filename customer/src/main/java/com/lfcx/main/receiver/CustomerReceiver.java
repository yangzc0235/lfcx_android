package com.lfcx.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lfcx.main.activity.CallCarSucessActivity;
import com.lfcx.main.event.ReceiptResultEvent;
import com.lfcx.main.net.result.ResultCodeEntity;
import com.lfcx.main.util.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class CustomerReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[CustomerReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                bundle.getString(JPushInterface.EXTRA_MESSAGE);
                Logger.d(TAG, "[CustomerReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                Gson gson = new Gson();
                ResultCodeEntity resultCodeEntity = gson.fromJson(message, ResultCodeEntity.class);
                if (resultCodeEntity.getActioncode().equals("0-400")) {
                    Log.v("system----司机信息---->",message);
                    EventBus.getDefault().post(new ReceiptResultEvent("receiveDriverInfo", message));
                } else if (resultCodeEntity.getActioncode().equals("0-401")) {
                    Log.v("system----开始行程---->",message);
                    EventBus.getDefault().post(new ReceiptResultEvent("startConfirm", message));
                } else if (resultCodeEntity.getActioncode().equals("0-402")) {
                    Log.v("system----支付数据---->",message);
                    EventBus.getDefault().post(new ReceiptResultEvent("receiveFinshOrder", message));
                }else if (resultCodeEntity.getActioncode().equals("0-403")) {
                    Log.v("system----支付完成---->",message);
                    EventBus.getDefault().post(new ReceiptResultEvent("receivePayFinish", message));
                }

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.v("system---------", "接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                int anInt = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                int anInt1 = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);
                Log.v("system---------", "[CustomerReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                Log.v("system---------", "[CustomerReceiver] 接收到推送下来的通知的title: " + anInt);
                Log.v("system---------", "[CustomerReceiver] EXTRA: " + anInt1);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[CustomerReceiver] 用户点击打开了通知");
                //打开自定义的Activity
				Intent i = new Intent(context, CallCarSucessActivity.class);
				i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[CustomerReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[CustomerReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.d(TAG, "[CustomerReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                // sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                // sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
//                        sb.append("\nkey:" + key + ", value: [" +
//                                myKey + " - " + json.optString(myKey) + "]");
                        String s = json.optString(myKey);
                        Log.v("system-后台返回的订单信息----->", s);
                        sb.append(json.optString(myKey));

                    }
                } catch (JSONException e) {
                }

            } else {
                //sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
                //Log.v("system---ordertitle---", bundle.getString(key));
            }
        }
        return sb.toString();
    }


}
