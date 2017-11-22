package com.lfcx.driver.net.api;

import com.lfcx.driver.net.NetConfig;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * author: drawthink
 * date  : 2017/10/8
 * des   :
 */

public interface DriverCarAPI {

    /**
     * latitude , longitude pk_user
     *
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.INSERT_LOCATION)
    Call<String> insertLocation(@FieldMap Map<String, Object> param);

    @Multipart
    @POST(NetConfig.UPLOAD_PHOTO)
    Call<String> uploadPhoto(@PartMap Map<String, RequestBody> param);



    @FormUrlEncoded
    @POST(NetConfig.DRIVER_ACCEPT_ORDER)
    Call<String> acceptOrder(@FieldMap Map<String, Object> param);

    @FormUrlEncoded
    @POST(NetConfig.DRIVER_GET_ORDER_INFO)
    Call<String> getOrderInfo(@FieldMap Map<String, Object> param);

    @FormUrlEncoded
    @POST(NetConfig.ORDER_TYPE_SETTING)
    Call<String> setOrderType(@FieldMap Map<String, Object> param);


    @FormUrlEncoded
    @POST(NetConfig.START_DRIVER)
    Call<String> startTravel(@FieldMap Map<String, Object> param);


    @FormUrlEncoded
    @POST(NetConfig.FINISH_DRIVER)
    Call<String> finishTravel(@FieldMap Map<String, Object> param);

    @FormUrlEncoded
    @POST(NetConfig.CURRENT_COUNT_INFO)
    Call<String> getCurrentCountInfo(@FieldMap Map<String, Object> param);

    @FormUrlEncoded
    @POST(NetConfig.UPDATE_FINISH_TRAVEL)
    Call<String> updateOrderForFinishTravel(@FieldMap Map<String, Object> param);

    @FormUrlEncoded
    @POST(NetConfig.CONFIRM_CUSTOMER)
    Call<String> confirmCustomer(@FieldMap Map<String, Object> param);
}
