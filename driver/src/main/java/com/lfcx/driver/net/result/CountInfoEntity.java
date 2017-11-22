package com.lfcx.driver.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/20.
 */

public class CountInfoEntity implements Serializable {

    /**
     * msg : 获取数据成功
     * inCome : 0.21
     * code : 0
     * acceptCount : 21
     */

    private String msg;
    private double inCome;
    private String code;
    private int acceptCount;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public double getInCome() {
        return inCome;
    }

    public void setInCome(double inCome) {
        this.inCome = inCome;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAcceptCount() {
        return acceptCount;
    }

    public void setAcceptCount(int acceptCount) {
        this.acceptCount = acceptCount;
    }
}
