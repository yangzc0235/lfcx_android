package com.lfcx.customer.net.result;

import com.lfcx.common.net.result.BaseResultBean;

/**
 * author: drawthink
 * date  : 2017/10/8
 * des   :
 */

public class BookCarResult extends BaseResultBean {
    private String pk_userOder;
    private String mobile;
    private String title;
    private String pk_user;

    public String getPk_userOder() {
        return pk_userOder;
    }

    public void setPk_userOder(String pk_userOder) {
        this.pk_userOder = pk_userOder;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPk_user() {
        return pk_user;
    }

    public void setPk_user(String pk_user) {
        this.pk_user = pk_user;
    }
}
