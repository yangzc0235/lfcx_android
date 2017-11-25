package com.lfcx.driver.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/21.
 */

public class IncomeEntity implements Serializable {


    /**
     * msg : 收到款项0.01元
     * income : 0.01
     * tradetype : 1
     * code : 0
     * totalfee : 0.01
     * pay : 0
     * orderCode : LF201711251426523213
     * fromtype : 1
     * actioncode : 1-401
     * type : 1
     * pk_user : 22072dac-6e1b-4112-9578-0dcc75d9b218
     * pk_userorder : 944f6c6d-a329-4b9c-9b93-96d8cf4440ad
     * pk_userdriver : fe6e9fc4-cf6b-4dd4-b446-d6e5a0f10754
     * istruename : 0
     */

    private String msg;
    private double income;
    private int tradetype;
    private String code;
    private double totalfee;
    private int pay;
    private String orderCode;
    private int fromtype;
    private String actioncode;
    private int type;
    private String pk_user;
    private String pk_userorder;
    private String pk_userdriver;
    private int istruename;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public int getTradetype() {
        return tradetype;
    }

    public void setTradetype(int tradetype) {
        this.tradetype = tradetype;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(double totalfee) {
        this.totalfee = totalfee;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public int getFromtype() {
        return fromtype;
    }

    public void setFromtype(int fromtype) {
        this.fromtype = fromtype;
    }

    public String getActioncode() {
        return actioncode;
    }

    public void setActioncode(String actioncode) {
        this.actioncode = actioncode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
