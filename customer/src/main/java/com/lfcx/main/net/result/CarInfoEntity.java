package com.lfcx.main.net.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yzc on 2017/11/29.
 */

public class CarInfoEntity implements Serializable {

    /**
     * msg : 查询成功
     * code : 0
     * list : [{"latitude":39.947271,"longitude":116.453691,"pk_carlocation_record":"2a8ddb9d-8bb4-4487-b265-a23ce1c53a4a","pk_user":"fe6e9fc4-cf6b-4dd4-b446-d6e5a0f10754","uploadDatetime":"2017-11-29 13:33:11.0"}]
     */

    private String msg;
    private String code;
    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * latitude : 39.947271
         * longitude : 116.453691
         * pk_carlocation_record : 2a8ddb9d-8bb4-4487-b265-a23ce1c53a4a
         * pk_user : fe6e9fc4-cf6b-4dd4-b446-d6e5a0f10754
         * uploadDatetime : 2017-11-29 13:33:11.0
         */

        private double latitude;
        private double longitude;
        private String pk_carlocation_record;
        private String pk_user;
        private String uploadDatetime;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getPk_carlocation_record() {
            return pk_carlocation_record;
        }

        public void setPk_carlocation_record(String pk_carlocation_record) {
            this.pk_carlocation_record = pk_carlocation_record;
        }

        public String getPk_user() {
            return pk_user;
        }

        public void setPk_user(String pk_user) {
            this.pk_user = pk_user;
        }

        public String getUploadDatetime() {
            return uploadDatetime;
        }

        public void setUploadDatetime(String uploadDatetime) {
            this.uploadDatetime = uploadDatetime;
        }
    }
}
