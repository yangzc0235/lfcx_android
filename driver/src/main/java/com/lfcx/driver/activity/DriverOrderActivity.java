package com.lfcx.driver.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.common.utils.MapMarkerUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.consts.Constants;
import com.lfcx.driver.fragment.OrderPickFragment;
import com.lfcx.driver.fragment.OrderTitleFragment;
import com.lfcx.driver.maphelper.LocationTask;
import com.lfcx.driver.maphelper.OnLocationGetListener;
import com.lfcx.driver.maphelper.PositionEntity;
import com.lfcx.driver.maphelper.RouteTask;
import com.lfcx.driver.net.api.DriverCarAPI;
import com.lfcx.driver.util.DriverLocationUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */

public class DriverOrderActivity extends DriverBaseActivity implements AMap.OnMapLoadedListener,AMap.OnCameraChangeListener,OnLocationGetListener{

    MapView mapView;
    LinearLayout content_container;
    LinearLayout title_container;
    AMap mAmap;
    LocationTask mLocation;
    Marker mPositionMark;
    FragmentManager fragmentManager;
    private DriverCarAPI mDriverCarAPI;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                requestUploadPosition(mLatitue,mLongitude);
            }
        }
    };
    private double mLatitue;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_order_activity);
        initMap(savedInstanceState);
        initView();
        mDriverCarAPI = APIFactory.create(DriverCarAPI.class);
        initLocation();
    }

    private void initView() {
        content_container = (LinearLayout) findViewById(R.id.order_content);
        title_container = (LinearLayout) findViewById(R.id.order_title);
        Fragment orderTitleFragment = new OrderTitleFragment();
        Fragment orderContentFragment = new OrderPickFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.order_content, orderContentFragment);
        transaction.commit();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.order_title, orderTitleFragment).commit();
    }

    private void initLocation() {
        mLocation = LocationTask.getInstance(this);
        mLocation.setOnLocationGetListener(this);
    }

    private void initMap(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mAmap = mapView.getMap();
        mAmap.getUiSettings().setZoomControlsEnabled(false);
        mAmap.setOnMapLoadedListener(this);
        mAmap.setOnCameraChangeListener(this);
        //首次进入地图设置地图的缩放级别
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(Constants.MAP_INIT_ZOOM));
//        geocoderSearch = new GeocodeSearch(this);
//        geocoderSearch.setOnGeocodeSearchListener(this);
        mAmap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                setMarker(latLng.latitude,latLng.longitude);
                LatLonPoint point = new LatLonPoint(latLng.latitude,latLng.longitude);
            }
        });

    }



    @Override
    public void onMapLoaded() {
        //初始化地图完成后开始定位
        setMarker(0,0);
        mLocation.startSingleLocate();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onLocationGet(PositionEntity entity) {
        if(mPositionMark != null && !mPositionMark.isRemoved()){
            mPositionMark.remove();
        }

        mLatitue=entity.getLatitue();
        mLongitude=entity.getLongitude();
        setMarker(mLatitue, mLongitude);
        DriverLocationUtils.setLocation(entity);
        RouteTask.getInstance(getApplicationContext()).setStartPoint(entity);
        Log.v("system----latitude--->",entity.getLatitue()+"");
        Log.v("system----longitude--->",entity.getLongitude()+"");
        if(mLatitue!=0.0&&mLongitude!=0.0){
            requestUploadPosition(mLatitue,mLongitude);
        }
        CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(entity.getLatitue(), entity.getLongitude()), mAmap.getCameraPosition().zoom);
        mAmap.animateCamera(cameraUpate);
    }


    /**
     * 实施上传位置
     * @param latitue
     * @param longitude
     */
    private void requestUploadPosition(double latitue, double longitude) {
        Map<String, Object> param = new HashMap<>();
        param.put("latitude", latitue);
        param.put("longitude ", longitude );
        param.put("pk_user ", "752b85a9-6ca0-4467-93e8-5fbf9d1c2f90");
        mDriverCarAPI.insertLocation(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        showToast(res.getMsg());
                        Log.v("system-------->",res.getMsg());
                        mHandler.sendEmptyMessageDelayed(1,30000);
                    } else {
                        showToast(res.getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("注册失败！！");
            }
        });
    }

    @Override
    public void onRegecodeGet(PositionEntity entity) {

    }

    private void setMarker(double latitude, double longitude) {

        MarkerOptions markerOptions = MapMarkerUtils.instance().getMarkerOptions(new LatLng(latitude,longitude)
                , BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.c_map_location));
        mPositionMark = mAmap.addMarker(markerOptions);
//        try{
//            mPositionMark.setPosition(new LatLng(latitude, longitude));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        if(latitude == 0 && longitude == 0){
            mPositionMark.setPositionByPixels(mapView.getWidth() / 2,
                    mapView.getHeight() / 2);
        }



    }



    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null!=mHandler){
            mHandler.removeMessages(1);
        }
    }

}
