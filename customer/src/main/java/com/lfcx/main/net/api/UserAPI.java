package com.lfcx.main.net.api;

import com.lfcx.main.net.NetConfig;
import com.lfcx.main.net.result.LoginResult;
import com.lfcx.main.net.result.SendMessageResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * author: drawthink
 * date  : 2017/9/24
 * des   :
 */

public interface UserAPI {
    @FormUrlEncoded
    @POST(NetConfig.CUSTOMER_REGISTER)
    Call<String> customerRegist(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST(NetConfig.CHECK_REGISTER)
    Call<String> checkRegist(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST(NetConfig.FORGET_PWD)
    Call<String> forgetPwd(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST(NetConfig.CUSTOMER_LOGIN)
    Call<LoginResult> customerLogin(@FieldMap Map<String, String> param);

    /**
     * 调用聚合接口发送短信
     *
     * @param param
     * @return
     */
    @GET
    Call<SendMessageResult> customerSendMessage(@Url String url, @QueryMap Map<String, Object> param);


    /**
     * pk_user
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.CUSTOMER_GET_DRIVER_INFO)
    Call<String> getDriverInfo(@FieldMap Map<String, String> param);

    /**
     * 取消订单
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.CANCEL_ORDER)
    Call<String> cancelOrderInfo(@FieldMap Map<String, String> param);

    /**
     * 用户确认上车
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.CONFIRM_CAR)
    Call<String> userConfirmCar(@FieldMap Map<String, String> param);

    /**
     * 微信支付
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.WX_PAY)
    Call<String> wxPay(@FieldMap Map<String, String> param);


    @FormUrlEncoded
    @POST(NetConfig.TRADE_RECORD)
    Call<String> generateTradeRecord(@FieldMap Map<String, Object> param);
}
