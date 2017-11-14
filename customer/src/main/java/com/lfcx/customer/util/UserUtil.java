package com.lfcx.customer.util;

import android.content.Context;
import android.text.TextUtils;

import com.lfcx.common.utils.SPUtils;
import com.lfcx.customer.consts.SPConstants;


/**
 * Created by yzc on 2016/11/9.
 */
public class UserUtil {

    /**
     * 用户是否登录
     *
     * @return
     */
    public static boolean isLogin(Context context) {
        String key= (String) SPUtils.getParam(context, SPConstants.KEY_CUSTOMER_PK_USER,"");
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return true;
    }

    /**
     * 清空用户信息
     *
     * @param context
     */
    public static void cleanUserInfo(Context context) {
        SPUtils.setParam(context, SPConstants.KEY_CUSTOMER_PK_USER,"");
        SPUtils.setParam(context, SPConstants.KEY_CUSTOMER_MOBILE,"");
        SPUtils.setParam(context, SPConstants.KEY_CUSTOMER_PWD,"");
    }
}
