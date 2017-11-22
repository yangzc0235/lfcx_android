package com.lfcx.main.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/20.
 */

public class PayResultEntity implements Serializable {


    /**
     * code : 0
     * msg : 生成订单成功
     * result : {"appid":"wxa7329e064ae58113","noncestr":"IWZvNDYCBL0bo4EWUJFN8ODB095u077U","package":"Sign=WXPay","partnerid":"1488615222","prepayid":"wx201711202005285e66f8fe960985662337","sign":"BA66A8E3785F7C919C38EFD96F2BFE5C","timestamp":"1511179528"}
     * out_trade_no : TNO2017112020052766100000001
     */

    private int code;
    private String msg;
    private String result;
    private String out_trade_no;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }
}
