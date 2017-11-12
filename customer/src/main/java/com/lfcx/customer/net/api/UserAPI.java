package com.lfcx.customer.net.api;

import com.lfcx.customer.net.NetConfig;
import com.lfcx.customer.net.result.LoginResult;
import com.lfcx.customer.net.result.SendMessageResult;

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
    Call<String> customerRegist(@FieldMap Map<String,String> param);

    @FormUrlEncoded
    @POST(NetConfig.CUSTOMER_LOGIN)
    Call<LoginResult> customerLogin(@FieldMap Map<String,String> param);

    /**
     * 调用聚合接口发送短信
     * @param param
     * @return
     */
    @GET
    Call<SendMessageResult> customerSendMessage(@Url String url,@QueryMap Map<String,Object> param);


    /**
     * pk_user
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.CUSTOMER_GET_DRIVER_INFO)
    Call<String> getDriverInfo(@FieldMap Map<String,String> param);
}
