package com.lfcx.driver.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.driver.consts.SPConstants;
import com.lfcx.driver.event.LoginOutEvent;
import com.lfcx.driver.net.api.DriverCarAPI;
import com.lfcx.driver.util.LocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用于定位及每1分钟上传车辆位置
 */

public class LocationService extends IntentService {

    private static Context ctx;
    public static final String TAG = LocationService.class.getSimpleName();

    private boolean isCancel = false;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            LocationUtils.setLocation(amapLocation);
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private DriverCarAPI driverCarAPI;


    public LocationService() {
        super("LocationService");
        EventBus.getDefault().register(this);
        //初始化定位
        mLocationClient = new AMapLocationClient(ctx.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2500);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        driverCarAPI = APIFactory.create(DriverCarAPI.class);
    }

    public static void startService(Context context) {
        ctx = context;
        Intent intent = new Intent(context, LocationService.class);
        context.startService(intent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallCarSuccess(LoginOutEvent event){
        isCancel = true;
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (!isCancel){
            try{
                requestUploadPosition();
                SystemClock.sleep(1000*10);//每10s上传一次位置
            //上传车辆位置信息
            }catch (Exception e){
                LogUtils.e(TAG,e.getMessage());
            }
        }
    }



    /**
     * 实施上传位置
     */
    private void requestUploadPosition() {
        Map<String, Object> param = new HashMap<>();
        param.put("latitude", LocationUtils.getLocation().getLatitude());
        param.put("longitude", LocationUtils.getLocation().getLongitude());
        param.put("pk_user", SPUtils.getParam(this,SPConstants.KEY_DRIVER_PK_USER,""));
        Log.v("system----latitue---->",LocationUtils.getLocation().getLatitude()+"");
        Log.v("system----longitude-->",LocationUtils.getLocation().getLongitude()+"");
        Log.v("system----pk_user-->",SPUtils.getParam(this,SPConstants.KEY_DRIVER_PK_USER,"")+"");
        driverCarAPI.insertLocation(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
//                        Toast.makeText(LocationService.this, res.getMsg(), Toast.LENGTH_SHORT).show();
                        Log.v("system-------->", res.getMsg());
                    } else {
                        //Toast.makeText(LocationService.this, res.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LocationService.this, "上传失败!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
