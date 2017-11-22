package com.lfcx.main.event;

/**
 * author: drawthink
 * date  : 2017/10/22
 * des   :
 */

public class CallCarSuccessEvent {
    private String pk_user;

    public CallCarSuccessEvent(String pk_user) {
        this.pk_user = pk_user;
    }

    public String getPk_user() {
        return pk_user;
    }
}
