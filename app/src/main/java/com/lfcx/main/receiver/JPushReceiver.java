package com.lfcx.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * author: drawthink
 * date  : 2017/10/14
 * des   : 极光推送消息接受类
 */

public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPushReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
//        try {
//            Bundle bundle = intent.getExtras();
//            LogUtils.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
//
//            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//                LogUtils.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//                //send the Registration Id to your server...
//
//            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//
//            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知");
//
//                /**
//                 * 向司机推送的通知里面content包括{actioncode；pk_userorder}这两个字段
//                 * {"content":"{\"pk_userorder\":\"123445\",\"actoincode\":\"0-400\"}"}
//                 *
//                 * 向客户推送的已被接单通知content包含{actioncode；pk_user；istruename}三个属性
//                 */
//                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//                String content = bundle.getString(JPushInterface.EXTRA_EXTRA);
//                JSONObject obj = new JSONObject(content);
//                if(null != obj && !TextUtils.isEmpty(obj.getString("content"))){
//                    JSONObject cb = new JSONObject(obj.getString("content").replace("\\",""));
//                    String code =cb.getString("actioncode");
//                    if(!TextUtils.isEmpty(code)){
//                        Intent bIntent;
//                        //推送给用户
//                        if(code.startsWith("0")){
//                            bIntent = new Intent("Customer.BROADCAST");
//                            Bundle bundle1 = new Bundle();
//                            bundle1.putString("code",code);
//                            bundle.putString("pk_user",cb.getString("pk_user"));
//                            bundle.putInt("istruename",cb.getInt("istruename"));
//                            bIntent.putExtras(bundle);
//                            context.sendBroadcast(bIntent);
//                        }else if(code.startsWith("1")){
//                            bIntent = new Intent("Driver.BROADCAST");
//                            Bundle bundle1 = new Bundle();
//                            bundle1.putString("code",code);
//                            bundle.putString("pk_userorder",cb.getString("pk_userorder"));
//                            bIntent.putExtras(bundle);
//                            context.sendBroadcast(bIntent);
//                        }
//                    }
//                }
//
//                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//
//
//            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//                LogUtils.d(TAG, "[MyReceiver] 用户点击打开了通知");
//
//
//            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//                LogUtils.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
//
//            } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//                LogUtils.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
//            } else {
//                LogUtils.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
//            }
//        } catch (Exception e){
//            LogUtils.e(TAG, e.getMessage());
//        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
//        StringBuilder sb = new StringBuilder();
//        for (String key : bundle.keySet()) {
//            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
//                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
//            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
//                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
//                    LogUtils.i(TAG, "This message has no Extra data");
//                    continue;
//                }
//
//                try {
//                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
//                    Iterator<String> it =  json.keys();
//
//                    while (it.hasNext()) {
//                        String myKey = it.next();
//                        sb.append("\nkey:" + key + ", value: [" +
//                                myKey + " - " +json.optString(myKey) + "]");
//                    }
//                } catch (JSONException e) {
//                    LogUtils.e(TAG, "Get message extra JSON error!");
//                }
//
//            } else {
//                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
//            }
//        }
//        return sb.toString();
        return null;
    }

}
