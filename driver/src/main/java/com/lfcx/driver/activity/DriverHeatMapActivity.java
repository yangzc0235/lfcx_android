package com.lfcx.driver.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.lfcx.common.utils.MapMarkerUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.consts.Constants;
import com.lfcx.driver.maphelper.LocationTask;
import com.lfcx.driver.maphelper.OnLocationGetListener;
import com.lfcx.driver.maphelper.PositionEntity;
import com.lfcx.driver.maphelper.RegeocodeTask;
import com.lfcx.driver.maphelper.RouteTask;
import com.lfcx.driver.maphelper.Utils;

public class DriverHeatMapActivity extends DriverBaseActivity implements AMap.OnMapLoadedListener, AMap.OnCameraChangeListener, RouteTask.OnRouteCalculateListener, OnLocationGetListener, View.OnClickListener {
    private MapView mMapView;
    private AMap mAmap;
    private LocationTask mLocationTask;
    private RegeocodeTask mRegeocodeTask;
    private Marker mPositionMark;
    private LatLng mStartPosition;
    private ImageView mIvBack;
    private ImageView mIvUser;
    private TextView mTitleBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_heat_map);
        mMapView = (MapView) findViewById(R.id.mapview);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvUser = (ImageView) findViewById(R.id.iv_user);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        init(savedInstanceState);
        mLocationTask = LocationTask.getInstance(getApplicationContext());
        mLocationTask.setOnLocationGetListener(this);
        mRegeocodeTask = new RegeocodeTask(getApplicationContext());
        RouteTask.getInstance(getApplicationContext())
                .addRouteCalculateListener(this);
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(this);
        mTitleBar.setText("热力图");
    }


    private void init(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
        mAmap.getUiSettings().setZoomControlsEnabled(false);
        mAmap.setOnMapLoadedListener(this);
        mAmap.setOnCameraChangeListener(this);
        //首次进入地图设置地图的缩放级别
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(Constants.MAP_INIT_ZOOM));
        Intent intent=getIntent();
        Bundle bundleExtra = intent.getBundleExtra(BUNDLE_KEY);
        if(bundleExtra!=null){
            goToActivity(DriverModeActivity.class);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onRouteCalculate(float cost, float distance, int duration) {

    }

    @Override
    public void onLocationGet(PositionEntity entity) {
        // todo 这里在网络定位时可以减少一个逆地理编码
        //第一次获得位置
        if(null != mPositionMark && !mPositionMark.isRemoved()){
            mPositionMark.remove();
        }
        MarkerOptions markerOptions = MapMarkerUtils.instance().getMarkerOptions(new LatLng(0,0)
                ,BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.c_map_location));
        mPositionMark = mAmap.addMarker(markerOptions);
        try{
            mPositionMark.setPosition(new LatLng(entity.latitue,entity.longitude));
        }catch (Exception e){

        }

        RouteTask.getInstance(getApplicationContext()).setStartPoint(entity);
        mStartPosition = new LatLng(entity.latitue, entity.longitude);
        CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                mStartPosition, mAmap.getCameraPosition().zoom);
        mAmap.animateCamera(cameraUpate);
    }

    @Override
    public void onRegecodeGet(PositionEntity entity) {
        entity.latitue = mStartPosition.latitude;
        entity.longitude = mStartPosition.longitude;

        if(null != mPositionMark && !mPositionMark.isRemoved()){
            mPositionMark.remove();
        }
        MarkerOptions markerOptions = MapMarkerUtils.instance().getMarkerOptions(new LatLng(mStartPosition.latitude,mStartPosition.longitude)
                ,BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.c_map_location));
        mPositionMark = mAmap.addMarker(markerOptions);
        try{
            mPositionMark.setPosition(new LatLng(entity.latitue,entity.longitude));
        }catch (Exception e){

        }
        RouteTask.getInstance(getApplicationContext()).setStartPoint(entity);
        RouteTask.getInstance(getApplicationContext()).search();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.removeMarkers();
        mMapView.onDestroy();
        mLocationTask.onDestroy();
        RouteTask.getInstance(getApplicationContext()).removeRouteCalculateListener(this);
    }

    @Override
    public void onMapLoaded() {
        MarkerOptions markerOptions = MapMarkerUtils.instance().getMarkerOptions(new LatLng(0,0)
                , BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.c_map_location));
        mPositionMark = mAmap.addMarker(markerOptions);
        mPositionMark.setPositionByPixels(mMapView.getWidth() / 2,
                mMapView.getHeight() / 2);
        mLocationTask.startSingleLocate();
    }


}
