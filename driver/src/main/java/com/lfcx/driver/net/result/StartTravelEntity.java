package com.lfcx.driver.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/20.
 */

public class StartTravelEntity implements Serializable {


    /**
     * msg : 系统已确认出发,请提醒乘客确认上车
     * code : 0
     * pk_basetravelrecord : 8cc6165c-d599-4a47-b5ef-923bc5279704
     */

    private String msg;
    private String code;
    private String pk_basetravelrecord;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPk_basetravelrecord() {
        return pk_basetravelrecord;
    }

    public void setPk_basetravelrecord(String pk_basetravelrecord) {
        this.pk_basetravelrecord = pk_basetravelrecord;
    }
}
