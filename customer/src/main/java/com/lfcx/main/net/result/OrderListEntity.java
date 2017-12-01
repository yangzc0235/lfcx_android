package com.lfcx.main.net.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yzc on 2017/11/29.
 */

public class OrderListEntity implements Serializable {


    /**
     * msg : 查询成功
     * code : 0
     * non_paymentlist : [{"PK_UserOrder":"cb3b7ffc-1033-4b00-85fc-54bc24c3e02c","PK_User":"22072dac-6e1b-4112-9578-0dcc75d9b218","PK_UserDriver":"fe6e9fc4-cf6b-4dd4-b446-d6e5a0f10754","OrderCode":"LF201711301340055345","orderStatus":"待付款","CarCode":"123456","CarOwner":"杨志财","PK_VehiRegitCert":"1cb77a26-d973-40b1-a2fa-ec752d3c2464","PK_DriveLicense":"f80b86c9-a127-4c01-a637-74da5920d07c","GenerateDate":"2017-11-30 13:40:05","Mobile":"15901126195","Money":0.01,"FromAddress":"京城大厦","ToAddress":"北京银行ATM(广场西路)","factMoney":0.01,"OrderType":"专车"}]
     * paymentlist : [{"PK_UserOrder":"c1beb709-a2f5-4bc8-ac11-0f428bd1d981","PK_User":"22072dac-6e1b-4112-9578-0dcc75d9b218","PK_UserDriver":"fe6e9fc4-cf6b-4dd4-b446-d6e5a0f10754","OrderCode":"LF201711301339056764","orderStatus":"已完成","CarCode":"123456","CarOwner":"杨志财","PK_VehiRegitCert":"1cb77a26-d973-40b1-a2fa-ec752d3c2464","PK_DriveLicense":"f80b86c9-a127-4c01-a637-74da5920d07c","GenerateDate":"2017-11-30 13:39:05","Mobile":"15901126195","Money":0.01,"FromAddress":"京城大厦","ToAddress":"乐视大厦","factMoney":0.01,"OrderType":"专车"}]
     */

    private String msg;
    private String code;
    private List<NonPaymentlistBean> nonpaymentlist;
    private List<PaymentlistBean> paymentlist;

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

    public List<NonPaymentlistBean> getNon_paymentlist() {
        return nonpaymentlist;
    }

    public void setNon_paymentlist(List<NonPaymentlistBean> non_paymentlist) {
        this.nonpaymentlist = non_paymentlist;
    }

    public List<PaymentlistBean> getPaymentlist() {
        return paymentlist;
    }

    public void setPaymentlist(List<PaymentlistBean> paymentlist) {
        this.paymentlist = paymentlist;
    }

    public static class NonPaymentlistBean {
        /**
         * PK_UserOrder : cb3b7ffc-1033-4b00-85fc-54bc24c3e02c
         * PK_User : 22072dac-6e1b-4112-9578-0dcc75d9b218
         * PK_UserDriver : fe6e9fc4-cf6b-4dd4-b446-d6e5a0f10754
         * OrderCode : LF201711301340055345
         * orderStatus : 待付款
         * CarCode : 123456
         * CarOwner : 杨志财
         * PK_VehiRegitCert : 1cb77a26-d973-40b1-a2fa-ec752d3c2464
         * PK_DriveLicense : f80b86c9-a127-4c01-a637-74da5920d07c
         * GenerateDate : 2017-11-30 13:40:05
         * Mobile : 15901126195
         * Money : 0.01
         * FromAddress : 京城大厦
         * ToAddress : 北京银行ATM(广场西路)
         * factMoney : 0.01
         * OrderType : 专车
         */

        private String PK_UserOrder;
        private String PK_User;
        private String PK_UserDriver;
        private String OrderCode;
        private String orderStatus;
        private String CarCode;
        private String CarOwner;
        private String PK_VehiRegitCert;
        private String PK_DriveLicense;
        private String GenerateDate;
        private String Mobile;
        private double Money;
        private String FromAddress;
        private String ToAddress;
        private double factMoney;
        private String OrderType;

        public String getPK_UserOrder() {
            return PK_UserOrder;
        }

        public void setPK_UserOrder(String PK_UserOrder) {
            this.PK_UserOrder = PK_UserOrder;
        }

        public String getPK_User() {
            return PK_User;
        }

        public void setPK_User(String PK_User) {
            this.PK_User = PK_User;
        }

        public String getPK_UserDriver() {
            return PK_UserDriver;
        }

        public void setPK_UserDriver(String PK_UserDriver) {
            this.PK_UserDriver = PK_UserDriver;
        }

        public String getOrderCode() {
            return OrderCode;
        }

        public void setOrderCode(String OrderCode) {
            this.OrderCode = OrderCode;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getCarCode() {
            return CarCode;
        }

        public void setCarCode(String CarCode) {
            this.CarCode = CarCode;
        }

        public String getCarOwner() {
            return CarOwner;
        }

        public void setCarOwner(String CarOwner) {
            this.CarOwner = CarOwner;
        }

        public String getPK_VehiRegitCert() {
            return PK_VehiRegitCert;
        }

        public void setPK_VehiRegitCert(String PK_VehiRegitCert) {
            this.PK_VehiRegitCert = PK_VehiRegitCert;
        }

        public String getPK_DriveLicense() {
            return PK_DriveLicense;
        }

        public void setPK_DriveLicense(String PK_DriveLicense) {
            this.PK_DriveLicense = PK_DriveLicense;
        }

        public String getGenerateDate() {
            return GenerateDate;
        }

        public void setGenerateDate(String GenerateDate) {
            this.GenerateDate = GenerateDate;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public double getMoney() {
            return Money;
        }

        public void setMoney(double Money) {
            this.Money = Money;
        }

        public String getFromAddress() {
            return FromAddress;
        }

        public void setFromAddress(String FromAddress) {
            this.FromAddress = FromAddress;
        }

        public String getToAddress() {
            return ToAddress;
        }

        public void setToAddress(String ToAddress) {
            this.ToAddress = ToAddress;
        }

        public double getFactMoney() {
            return factMoney;
        }

        public void setFactMoney(double factMoney) {
            this.factMoney = factMoney;
        }

        public String getOrderType() {
            return OrderType;
        }

        public void setOrderType(String OrderType) {
            this.OrderType = OrderType;
        }
    }

    public static class PaymentlistBean {
        /**
         * PK_UserOrder : c1beb709-a2f5-4bc8-ac11-0f428bd1d981
         * PK_User : 22072dac-6e1b-4112-9578-0dcc75d9b218
         * PK_UserDriver : fe6e9fc4-cf6b-4dd4-b446-d6e5a0f10754
         * OrderCode : LF201711301339056764
         * orderStatus : 已完成
         * CarCode : 123456
         * CarOwner : 杨志财
         * PK_VehiRegitCert : 1cb77a26-d973-40b1-a2fa-ec752d3c2464
         * PK_DriveLicense : f80b86c9-a127-4c01-a637-74da5920d07c
         * GenerateDate : 2017-11-30 13:39:05
         * Mobile : 15901126195
         * Money : 0.01
         * FromAddress : 京城大厦
         * ToAddress : 乐视大厦
         * factMoney : 0.01
         * OrderType : 专车
         */

        private String PK_UserOrder;
        private String PK_User;
        private String PK_UserDriver;
        private String OrderCode;
        private String orderStatus;
        private String CarCode;
        private String CarOwner;
        private String PK_VehiRegitCert;
        private String PK_DriveLicense;
        private String GenerateDate;
        private String Mobile;
        private double Money;
        private String FromAddress;
        private String ToAddress;
        private double factMoney;
        private String OrderType;

        public String getPK_UserOrder() {
            return PK_UserOrder;
        }

        public void setPK_UserOrder(String PK_UserOrder) {
            this.PK_UserOrder = PK_UserOrder;
        }

        public String getPK_User() {
            return PK_User;
        }

        public void setPK_User(String PK_User) {
            this.PK_User = PK_User;
        }

        public String getPK_UserDriver() {
            return PK_UserDriver;
        }

        public void setPK_UserDriver(String PK_UserDriver) {
            this.PK_UserDriver = PK_UserDriver;
        }

        public String getOrderCode() {
            return OrderCode;
        }

        public void setOrderCode(String OrderCode) {
            this.OrderCode = OrderCode;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getCarCode() {
            return CarCode;
        }

        public void setCarCode(String CarCode) {
            this.CarCode = CarCode;
        }

        public String getCarOwner() {
            return CarOwner;
        }

        public void setCarOwner(String CarOwner) {
            this.CarOwner = CarOwner;
        }

        public String getPK_VehiRegitCert() {
            return PK_VehiRegitCert;
        }

        public void setPK_VehiRegitCert(String PK_VehiRegitCert) {
            this.PK_VehiRegitCert = PK_VehiRegitCert;
        }

        public String getPK_DriveLicense() {
            return PK_DriveLicense;
        }

        public void setPK_DriveLicense(String PK_DriveLicense) {
            this.PK_DriveLicense = PK_DriveLicense;
        }

        public String getGenerateDate() {
            return GenerateDate;
        }

        public void setGenerateDate(String GenerateDate) {
            this.GenerateDate = GenerateDate;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public double getMoney() {
            return Money;
        }

        public void setMoney(double Money) {
            this.Money = Money;
        }

        public String getFromAddress() {
            return FromAddress;
        }

        public void setFromAddress(String FromAddress) {
            this.FromAddress = FromAddress;
        }

        public String getToAddress() {
            return ToAddress;
        }

        public void setToAddress(String ToAddress) {
            this.ToAddress = ToAddress;
        }

        public double getFactMoney() {
            return factMoney;
        }

        public void setFactMoney(double factMoney) {
            this.factMoney = factMoney;
        }

        public String getOrderType() {
            return OrderType;
        }

        public void setOrderType(String OrderType) {
            this.OrderType = OrderType;
        }
    }
}
