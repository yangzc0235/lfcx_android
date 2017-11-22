package com.lfcx.driver.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/21.
 */

public class IncomeEntity implements Serializable {


    /**
     * msg : 收到款项0.009000000000000001元
     * code : 0
     * orderCode : LF201711211807484483
     * actioncode : 1-401
     * pk_user : 752b85a9-6ca0-4467-93e8-5fbf9d1c2f90
     * pk_userorder : 42713d34-06fc-4fc2-ac80-5afbd09b3188
     * pk_userdriver : e03399f3-8d34-4136-8cee-a99b9a738910
     * istruename : 0
     */

    private String msg;
    private String code;
    private String orderCode;
    private String actioncode;
    private String pk_user;
    private String pk_userorder;
    private String pk_userdriver;
    private int istruename;
    private double totalfee;

    public double getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(double totalfee) {
        this.totalfee = totalfee;
    }

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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getActioncode() {
        return actioncode;
    }

    public void setActioncode(String actioncode) {
        this.actioncode = actioncode;
    }

    public String getPk_user() {
        return pk_user;
    }

    public void setPk_user(String pk_user) {
        this.pk_user = pk_user;
    }

    public String getPk_userorder() {
        return pk_userorder;
    }

    public void setPk_userorder(String pk_userorder) {
        this.pk_userorder = pk_userorder;
    }

    public String getPk_userdriver() {
        return pk_userdriver;
    }

    public void setPk_userdriver(String pk_userdriver) {
        this.pk_userdriver = pk_userdriver;
    }

    public int getIstruename() {
        return istruename;
    }

    public void setIstruename(int istruename) {
        this.istruename = istruename;
    }
}
