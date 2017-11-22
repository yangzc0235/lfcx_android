package com.lfcx.main.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/20.
 */

public class StartTravelEntity implements Serializable {


    /**
     * actioncode : 0-401
     * drivermobile : 18801117356
     * pk_userdriver : e03399f3-8d34-4136-8cee-a99b9a738910
     * pk_userorder : 27a2a63c-cb69-48f0-b71e-8e0bf353dae5
     * pk_basetravelrecord : 4b1b23cb-e5b6-405f-99c4-84ea425452dd
     */

    private String actioncode;
    private String drivermobile;
    private String pk_userdriver;
    private String pk_userorder;
    private String pk_basetravelrecord;

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

    public String getPk_userorder() {
        return pk_userorder;
    }

    public void setPk_userorder(String pk_userorder) {
        this.pk_userorder = pk_userorder;
    }

    public String getPk_basetravelrecord() {
        return pk_basetravelrecord;
    }

    public void setPk_basetravelrecord(String pk_basetravelrecord) {
        this.pk_basetravelrecord = pk_basetravelrecord;
    }
}
