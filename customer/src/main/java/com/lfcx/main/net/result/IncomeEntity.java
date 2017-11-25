package com.lfcx.main.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/21.
 */

public class IncomeEntity implements Serializable {


    /**
     * msg : 收到付款0.01元，欢迎下次使用
     * income : 0
     * tradetype : 1
     * code : 0
     * totalfee : 0.01
     * pay : 0.01
     * orderCode : LF201711251449222596
     * fromtype : 0
     * actioncode : 0-403
     * type : 2
     * pk_user : 22072dac-6e1b-4112-9578-0dcc75d9b218
     * pk_userorder : 2da35c2d-3647-4b00-ad26-2b4273eeff8f
     * pk_userdriver : fe6e9fc4-cf6b-4dd4-b446-d6e5a0f10754
     * istruename : 0
     */

    private String msg;
    private int income;
    private int tradetype;
    private String code;
    private double totalfee;
    private double pay;
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

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
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

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
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
