package com.lfcx.main.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/20.
 */

public class PayEntity implements Serializable {


    /**
     * pk_userorder : 88e86146-68f3-496c-b094-91f53a1e14b7
     * cashnum : 3.01
     * actioncode : 0-402
     * drivermobile : 18801117356
     * pk_userdriver : e03399f3-8d34-4136-8cee-a99b9a738910
     */

    private String pk_userorder;
    private double cashnum;
    private String actioncode;
    private String drivermobile;
    private String pk_userdriver;

    public String getPk_userorder() {
        return pk_userorder;
    }

    public void setPk_userorder(String pk_userorder) {
        this.pk_userorder = pk_userorder;
    }

    public double getCashnum() {
        return cashnum;
    }

    public void setCashnum(double cashnum) {
        this.cashnum = cashnum;
    }

    public String getActioncode() {
        return actioncode;
    }

    public void setActioncode(String actioncode) {
        this.actioncode = actioncode;
    }

    public String getDrivermobile() {
        return drivermobile;
    }

    public void setDrivermobile(String drivermobile) {
        this.drivermobile = drivermobile;
    }

    public String getPk_userdriver() {
        return pk_userdriver;
    }

    public void setPk_userdriver(String pk_userdriver) {
        this.pk_userdriver = pk_userdriver;
    }
}
