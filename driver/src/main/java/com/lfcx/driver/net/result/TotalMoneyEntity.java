package com.lfcx.driver.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/12/1.
 */

public class TotalMoneyEntity implements Serializable {
    private String code;
    private String msg;
    private String cashnum;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCashnum() {
        return cashnum;
    }

    public void setCashnum(String cashnum) {
        this.cashnum = cashnum;
    }
}
