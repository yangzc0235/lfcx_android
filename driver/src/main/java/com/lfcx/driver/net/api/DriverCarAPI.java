package com.lfcx.driver.net.api;

import com.lfcx.driver.net.NetConfig;
import com.squareup.okhttp.RequestBody;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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


    /**
     * 上传头像
     */
    @Multipart
    @POST(NetConfig.UPLOAD_PHOTO)
    Call<String> uploadMemberIcon(@Part List<MultipartBody.Part> partList);

    /**
     * 上传一张图片
     *
     * @param description
     * @param imgs
     * @return
     */
    @Multipart
    @POST(NetConfig.UPLOAD_PHOTO)
    Call<String> uploadImage(@Part("fileName") String description, @Part("file\"; filename=\"15901126195_cardfrontphoto.jpg\"") RequestBody imgs);

    /**
     * 上传三张图片
     *
     * @param description
     * @param imgs
     * @param imgs1
     * @param imgs3
     * @return
     */
    @Multipart
    @POST(NetConfig.UPLOAD_PHOTO)
    Call<String> uploadImage(@Part("fileName") String description,
                             @Part("file\"; filename=\"15901126195_cardfrontphoto.jpg\"") RequestBody imgs,
                             @Part("file\"; filename=\"15901126195_cardbackphoto.jpg\"") RequestBody imgs1,
                             @Part("file\"; filename=\"15901126195_cardwithhandphoto.jpg\"") RequestBody imgs3);

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
    @POST(NetConfig.TRADE_RECORD)
    Call<String> generateTradeRecord(@FieldMap Map<String, Object> param);

    @FormUrlEncoded
    @POST(NetConfig.CONFIRM_CUSTOMER)
    Call<String> confirmCustomer(@FieldMap Map<String, Object> param);


}
