package com.lfcx.driver.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/19.
 */

public class UserOrderEntity implements Serializable {
    /**
     * fromlatitude : 39.947784
     * reservatedate :
     * toaddress : 北京银行ATM
     * mobile : 15901126195
     * tolatitude : 39.881763
     * actioncode : 1-400
     * fromlongitude : 116.454027
     * pk_user : 22072dac-6e1b-4112-9578-0dcc75d9b218
     * fromaddress : 京城大厦
     * tolongitude : 116.298112
     * pk_userorder : af3e2888-d23c-4727-b44f-d1a49f9254c0
     */

    private double fromlatitude;
    private String reservatedate;
    private String toaddress;
    private String mobile;
    private double tolatitude;
    private String actioncode;
    private double fromlongitude;
    private String pk_user;
    private String fromaddress;
    private double tolongitude;
    private String pk_userorder;

    public double getFromlatitude() {
        return fromlatitude;
    }

    public void setFromlatitude(double fromlatitude) {
        this.fromlatitude = fromlatitude;
    }

    public String getReservatedate() {
        return reservatedate;
    }

    public void setReservatedate(String reservatedate) {
        this.reservatedate = reservatedate;
    }

    public String getToaddress() {
        return toaddress;
    }

    public void setToaddress(String toaddress) {
        this.toaddress = toaddress;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public double getTolatitude() {
        return tolatitude;
    }

    public void setTolatitude(double tolatitude) {
        this.tolatitude = tolatitude;
    }

    public String getActioncode() {
        return actioncode;
    }

    public void setActioncode(String actioncode) {
        this.actioncode = actioncode;
    }

    public double getFromlongitude() {
        return fromlongitude;
    }

    public void setFromlongitude(double fromlongitude) {
        this.fromlongitude = fromlongitude;
    }

    public String getPk_user() {
        return pk_user;
    }

    public void setPk_user(String pk_user) {
        this.pk_user = pk_user;
    }

    public String getFromaddress() {
        return fromaddress;
    }

    public void setFromaddress(String fromaddress) {
        this.fromaddress = fromaddress;
    }

    public double getTolongitude() {
        return tolongitude;
    }

    public void setTolongitude(double tolongitude) {
        this.tolongitude = tolongitude;
    }

    public String getPk_userorder() {
        return pk_userorder;
    }

    public void setPk_userorder(String pk_userorder) {
        this.pk_userorder = pk_userorder;
    }



}
