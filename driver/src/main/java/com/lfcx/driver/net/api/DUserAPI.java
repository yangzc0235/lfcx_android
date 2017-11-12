package com.lfcx.driver.net.api;

import com.lfcx.driver.net.NetConfig;
import com.lfcx.driver.net.result.SendMessageResult;

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

public interface DUserAPI {
    /**
     * [{"key":"city","value":"银川","description":""},
     * {"key":"mobile","value":"13910350994","description":""},
     * {"key":"pwd","value":"123456","description":""},
     * {"key":"drivername","value":"杨宗炜","description":""},
     * {"key":"drivercard","value":"642221197802193410","description":""},
     * {"key":"firstdetcreddate","value":"2013-5-9","description":""},{
     * "key":"type","value":"私家车","description":""},
     * {"key":"regitdate","value":"2013-05-09","description":""},
     * {"key":"engineno","value":"wwrr","description":""},
     * {"key":"carcode","value":"冀FJE174","description":""} ] StyleType ：0：舒适型 ; 1：豪华型 ;2：七座商务; 9：其他型 推荐人手机号： recommobile：手机号码
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST(NetConfig.DRIVER_REGISTER)
    Call<String> driverRegist(@FieldMap Map<String, Object> param);

    /**
     * 调用聚合接口发送短信
     * @param param
     * @return
     */
    @GET
    Call<SendMessageResult> customerSendMessage(@Url String url, @QueryMap Map<String,Object> param);
}
