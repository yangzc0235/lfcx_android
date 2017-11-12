package com.lfcx.customer.net.result;

import com.lfcx.common.net.result.BaseResultBean;

/**
 * author: drawthink
 * date  : 2017/10/8
 * des   :
 */

public class LoginResult extends BaseResultBean {
    private String pk_user;

    public String getPk_user() {
        return pk_user;
    }

    public void setPk_user(String pk_user) {
        this.pk_user = pk_user;
    }
}
