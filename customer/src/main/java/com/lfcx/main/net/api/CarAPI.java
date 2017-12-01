package com.lfcx.main.net.api;

import com.lfcx.main.net.NetConfig;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * author: drawthink
 * date  : 2017/10/8
 * des   :
 */

public interface CarAPI {

    @FormUrlEncoded
    @POST(NetConfig.GENERATE_ORDER)
    Call<String> generateOrder(@FieldMap Map<String, Object> param);

    /**
     * 立即估算价格
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.GUSS_NOW_COST)
    Call<String> getNowCost(@FieldMap Map<String, Object> param);

    /**
     * 预约估算价格
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.GUSS_AFTER_COST)
    Call<String> getAfterCost(@FieldMap Map<String, Object> param);

    /**
     * 包车估算价格
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.GUSS_PRIVATE_COST)
    Call<String> getPrivateCost(@FieldMap Map<String, Object> param);

    /**
     * 获取附近车辆信息
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.GET_CAR_INFO)
    Call<String> getCarInfo(@FieldMap Map<String, Object> param);

    /**
     * 获取订单列表
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.GET_ORDER_INFO)
    Call<String> getOrderInfo(@FieldMap Map<String, Object> param);
}
