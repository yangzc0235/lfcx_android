package com.lfcx.main.net;

/**
 * author: drawthink
 * date  : 2017/9/24
 * des   :
 */

public interface NetConfig {

    /**
     * 发短信
     */
    String CUSTOMER_SEND_MESSAGE = "http://v.juhe.cn/sms/send";


    /**
     * 用户设置
     */
    String USER_SETTER = "http://101.200.58.37:8899/LFCar/mycenter/build/pages/my-center/index.html";

    /**
     * 获取用户信息
     */
    String CUSTOMER_GET_DRIVER_INFO = "user/getdriverinfo";

    /**
     * 判断用户是否注册
     */
    String CHECK_REGISTER = "user/checkmobile";
    /**
     * 用户注册
     */
    String CUSTOMER_REGISTER = "user/registUser";
    /**
     * 忘记密码
     */
    String FORGET_PWD = "user/rememberpwd";
    /**
     * 用户登录
     */
    String CUSTOMER_LOGIN = "user/verifyLogin";
    /**
     * 生成订单
     */
    String GENERATE_ORDER = "userOrder/generateOrder";
    /**
     * 生成订单
     */
    String CANCEL_ORDER = "userOrder/cancelorder";
    /**
     * 用户确认上车
     */
    String CONFIRM_CAR = "travelrecord/checkgeton";

    /**
     * 立即叫车、预约用车、接机、送机 输入起始位置，得到预计价格
     * datetime  ，fromaddress，toaddress，fromlongitude，fromlatitude，tolongitude，
     * Tolatitude，styletype（舒适型 0，豪华型 1，七座商务 2）
     */
    String GUSS_NOW_COST = "countcost/countimmediately";

    /**
     * 预约用车、接机、送机 输入起始位置，得到预计价格
     * datetime  ，fromaddress，toaddress，fromlongitude，fromlatitude，tolongitude，
     * Tolatitude，styletype（舒适型 0，豪华型 1，七座商务 2）
     */
    String GUSS_AFTER_COST = "countcost/countreservate";

    /**
     * 包车的预估价格
     * begintime （上车时间） ，hour,fromaddress，toaddress，fromlongitude，fromlatitude，tolongitude，
     * tolatitude，styletype（舒适型 0，豪华型 1，七座商务 2），combotype（1：4小时套餐；2：8小时套餐）
     */
    String GUSS_BAO_COST = "countcost/countprivate";

    /**
     * 包车估算价格
     */
    String GUSS_PRIVATE_COST = "countcost/countprivate";

    /**
     * 获取当前用户信息
     * 参数：mobile
     */
    String GET_USERINFO = "user/getUserInfoByUserMobile";
    /**
     * 微信,支付宝支付
     * 参数：mobile
     */
    String WX_PAY = "apppay/pay";

    /**
     * 生成交易记录
     */
    String TRADE_RECORD = "basetraderecord/generatetraderecord";
    /**
     * 获取附近车辆信息
     */
    String GET_CAR_INFO = "dvlct/querynearby";
    /**
     * 获取附近车辆信息
     */
    String GET_ORDER_INFO = "userOrder/getperorderlist";


}
