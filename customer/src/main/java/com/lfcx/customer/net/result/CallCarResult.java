package com.lfcx.customer.net.result;

import com.lfcx.common.net.result.BaseResultBean;

/**
 * author: drawthink
 * date  : 2017/10/21
 * des   :
 */

public class CallCarResult extends BaseResultBean{
    /**
     * {","mobile":"","title":"用户123预约您","pk_user":"f94352ef-d33b-47b5-b533-877a40dc6676"}
     */
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
