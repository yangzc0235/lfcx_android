package com.lfcx.driver.net;

/**
 * author: drawthink
 * date  : 2017/9/24
 * des   :
 */

public interface NetConfig {

    /**
     * 发短信
     */
    String DRIVER_SEND_MESSAGE = "http://v.juhe.cn/sms/send";
    /**
     * 司机注册
     */
    String DRIVER_REGISTER = "user/registDriver";
    /**
     * 司机实时上传位置到服务器
     */
    String INSERT_LOCATION = "dvlct/insertlocation";
    /**
     * 忘记密码
     */
    String FORGET_PWD = "user/rememberpwd";

    /**
     * 司机接单类型设置
     */
    String ORDER_TYPE_SETTING = "Driver/insertDriverSet";

    /**
     * 判断用户是否注册
     */
    String CHECK_REGISTER = "user/checkmobile";
    /**
     * 用户登录
     */
    String CUSTOMER_LOGIN = "user/verifyLogin";
    /**
     * 司机注册上传照片
     * cardfrontphoto  身份证正面照
     * ,cardbackphoto, 身份证反面照
     * cardwithhandphoto,手持身份证照片
     * vehiregitcertphoto1 行驶证
     * vehiregitcertphoto2,行驶证副页
     * drivelicephoto, 驾驶证
     * driverandcarphoto,人车合影
     * car45photo车辆45度
     * ,carfacephoto,车正面照
     * Safedocphoto保险单
     */

    String UPLOAD_PHOTO = "upload/uploadPhoto";

    /**
     * 获取个人信息
     * 参数：mobile
     */
    String DRIVER_GET_USERINFO = "user/getUserInfoByUserMobile";

    /**
     * 登录接口
     * 参数：user 手机号 ， pwd
     */
    String DRIVER_LOGIN = "user/verifyLogin ";

    /**
     * 参数 ： pk_userOder，pk_user，pk_userDriver，title，
     */
    String DRIVER_ACCEPT_ORDER = "userOrder/acceptOrder";

    /**
     * pk_userorder: 订单PK
     */
    String DRIVER_GET_ORDER_INFO = "userOrder/getorderinfobyorderpk";

    /**
     * 聚合数据 车辆信息校验
     */
    String DRIVER_CAR_INFO = "http://v.juhe.cn/carInfoCheck/query.php";

    /**
     * 聚合数据 身份证实名认证
     */

    String DRIVER_SHIMING = "http://op.juhe.cn/idcard/query";


    /**
     * 驾驶证核查
     */
    String DRIVER_JIASHI = "http://v.juhe.cn/jszhc/query.php";


    /**
     * 用户上车,开始行程
     */
    String START_DRIVER = "travelrecord/starttravel";


    /**
     * 结束行程
     */
    String FINISH_DRIVER = "travelrecord/finishtravel";
    /**
     * 获取今日订单量
     */
    String CURRENT_COUNT_INFO = "userOrder/getCurrentCountInfo";


    /**
     * 附加费
     */
    String UPDATE_FINISH_TRAVEL = "userOrder/updateOrderForFinishTravel";
    /**
     * 生成交易记录
     */
    String TRADE_RECORD = "basetraderecord/generatetraderecord";

    /**
     * 确认乘客上车
     */
    String CONFIRM_CUSTOMER = "travelrecord/receivePassengers";


    /**
     * 注册的步骤
     */
    String REG_STEP = "user/getRegDriverPoint";


    /**
     * 设置
     */
    String USER_SETTER = "http://101.200.58.37:8899/LFCar/mycenter/build/pages/my-center/index.html";

    /**
     * oktttputils上传图片
     */
    String UPLOAD_PHOTO_OK = "http://101.200.58.37:8899/LFCar/upload/uploadPhoto";


}
