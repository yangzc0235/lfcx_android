package com.lfcx.driver.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/19.
 */

public class UserOrderEntity implements Serializable {


    /**
     * fromlatitude : 39.947234
     * reservatedate : 2017-11-20 13:00:00
     * toaddress : 北京西站(地铁站)
     * mobile : 15901126195
     * tolatitude : 39.894763
     * actioncode : 1-400
     * fromlongitude : 116.453643
     * pk_user : 752b85a9-6ca0-4467-93e8-5fbf9d1c2f90
     * fromaddress : 京城大厦
     * tolongitude : 116.321262
     * pk_userorder : 7cc6524d-0901-4a31-a5be-87fd3aa3ac4f
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
    private String basetravelrecord;

    public String getBasetravelrecord() {
        return basetravelrecord;
    }

    public void setBasetravelrecord(String basetravelrecord) {
        this.basetravelrecord = basetravelrecord;
    }

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
