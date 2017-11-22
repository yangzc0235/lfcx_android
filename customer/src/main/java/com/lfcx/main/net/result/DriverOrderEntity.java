package com.lfcx.main.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/19.
 */

public class DriverOrderEntity implements Serializable {

    /**
     * IsTrueName : true
     * createtime : 2017-11-04 11:02:47
     * drivername : 姚鹏飞
     * latitude : 39.947236
     * mobile : 18801117356
     * actioncode : 0-400
     * type : 大众高尔夫银色
     * Longitude : 116.453645
     * PK_User : e03399f3-8d34-4136-8cee-a99b9a738910
     * CarCode : 宁A85L63
     * pk_user : 752b85a9-6ca0-4467-93e8-5fbf9d1c2f90
     * pk_userorder : 761ed68c-867a-434e-8539-d076b9ff2d02
     * pk_userdriver : e03399f3-8d34-4136-8cee-a99b9a738910
     * istruename : 0
     */

    private boolean IsTrueName;
    private String createtime;
    private String drivername;
    private double latitude;
    private String mobile;
    private String actioncode;
    private String type;
    private double Longitude;
    private String PK_User;
    private String CarCode;
    private String pk_user;
    private String pk_userorder;
    private String pk_userdriver;
    private int istruename;

    public boolean isIsTrueName() {
        return IsTrueName;
    }

    public void setIsTrueName(boolean IsTrueName) {
        this.IsTrueName = IsTrueName;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getActioncode() {
        return actioncode;
    }

    public void setActioncode(String actioncode) {
        this.actioncode = actioncode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public String getPK_User() {
        return PK_User;
    }

    public void setPK_User(String PK_User) {
        this.PK_User = PK_User;
    }

    public String getCarCode() {
        return CarCode;
    }

    public void setCarCode(String CarCode) {
        this.CarCode = CarCode;
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
