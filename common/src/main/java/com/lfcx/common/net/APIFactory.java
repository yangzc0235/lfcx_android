package com.lfcx.common.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * author: drawthink
 * date  : 2017/9/23
 * des   :
 */

public class APIFactory {
    private static Retrofit retrofit;

    public static <T> T create(Class<T> clazz){
        init();
       return retrofit.create(clazz);
    }
    private static Retrofit init(){
        if(null == retrofit){
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://101.200.58.37:8899/LFCar/")
                    //增加返回值为String的支持
                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addConverterFactory(new StringConverterFactory())
                    //增加返回值为Gson的支持(以实体类返回)
                    .addConverterFactory(GsonConverterFactory.create())
                    //增加返回值为Oservable<T>的支持
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
       return retrofit;
    }
}
