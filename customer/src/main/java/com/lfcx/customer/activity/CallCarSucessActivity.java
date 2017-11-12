package com.lfcx.customer.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.maphelper.LocationTask;
import com.lfcx.customer.maphelper.OnLocationGetListener;
import com.lfcx.customer.maphelper.PositionEntity;
import com.lfcx.customer.maphelper.RegeocodeTask;
import com.lfcx.customer.maphelper.RouteTask;
import com.lfcx.customer.maphelper.Utils;
import com.lfcx.customer.net.api.UserAPI;
import com.lfcx.customer.net.result.LoginResult;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallCarSucessActivity extends CustomerBaseActivity  implements AMap.OnCameraChangeListener,
        AMap.OnMapLoadedListener, OnLocationGetListener, View.OnClickListener,
        RouteTask.OnRouteCalculateListener {

    public static final String TAG = CallCarSucessActivity.class.getSimpleName();

    private MapView mMapView;

    private AMap mAmap;


    private Marker mPositionMark;

    private LatLng mStartPosition;

    private RegeocodeTask mRegeocodeTask;

    private LocationTask mLocationTask;

    private boolean mIsFirst = true;

    private boolean mIsRouteSuccess = false;

    private UserAPI userAPI;

    private String pk_user;

    public interface OnGetLocationListener {
        public void getLocation(String locationAddress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_car_sucess);
        init(savedInstanceState);
        mLocationTask = LocationTask.getInstance( getApplicationContext());
        mLocationTask.setOnLocationGetListener(this);
        mRegeocodeTask = new RegeocodeTask( getApplicationContext());
        RouteTask.getInstance( getApplicationContext())
                .addRouteCalculateListener(this);
        try{
            pk_user = getIntent().getStringExtra("pk_user");
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }
    private void init(Bundle savedInstanceState) {
        mMapView = (MapView)  findViewById(R2.id.map);
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
        mAmap.getUiSettings().setZoomControlsEnabled(false);
        mAmap.setOnMapLoadedListener(this);
        mAmap.setOnCameraChangeListener(this);
        userAPI = APIFactory.create(UserAPI.class);
        getDriverInfo();
    }

    private void getDriverInfo(){
        showLoading();
        Map<String,String> param = new HashMap<>();
        param.put("pk_user",pk_user);
        userAPI.getDriverInfo(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                showToast("获取司机信息成功");

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
            }
        });

    }

    @Override
    public void onCameraChange(CameraPosition arg0) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mStartPosition = cameraPosition.target;
        mRegeocodeTask.setOnLocationGetListener(this);
        mRegeocodeTask
                .search(mStartPosition.latitude, mStartPosition.longitude);
        if (mIsFirst) {
            Utils.addEmulateData(mAmap, mStartPosition);
            if (mPositionMark != null) {
                mPositionMark.setToTop();
            }
            mIsFirst = false;
        }
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
        RouteTask.getInstance( getApplicationContext()).removeRouteCalculateListener(this);

    }

    @Override
    public void onMapLoaded() {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(new LatLng(0, 0));
        markerOptions
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),
                                R2.drawable.c_map_location)));
        mPositionMark = mAmap.addMarker(markerOptions);

        mPositionMark.setPositionByPixels(mMapView.getWidth() / 2,
                mMapView.getHeight() / 2);
        mLocationTask.startSingleLocate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R2.id.destination_button:
//                Intent intent = new Intent(this, DestinationActivity.class);
//                startActivity(intent);
//                break;
            case R2.id.location_image:
                mLocationTask.startSingleLocate();
                break;
            case R2.id.destination_text:
                Intent destinationIntent = new Intent(this,
                        DestinationActivity.class);
                startActivity(destinationIntent);
                break;
        }
    }

    @Override
    public void onLocationGet(PositionEntity entity) {
        // todo 这里在网络定位时可以减少一个逆地理编码
        RouteTask.getInstance( getApplicationContext()).setStartPoint(entity);

        mStartPosition = new LatLng(entity.latitue, entity.longitude);
        CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                mStartPosition, mAmap.getCameraPosition().zoom);
        mAmap.animateCamera(cameraUpate);

    }

    @Override
    public void onRegecodeGet(PositionEntity entity) {
        entity.latitue = mStartPosition.latitude;
        entity.longitude = mStartPosition.longitude;
        RouteTask.getInstance( getApplicationContext()).setStartPoint(entity);
        RouteTask.getInstance( getApplicationContext()).search();
    }

    @Override
    public void onRouteCalculate(float cost, float distance, int duration) {
        mIsRouteSuccess = true;
//        mRouteCostText.setVisibility(View.VISIBLE);
//        llCarContainer.setVisibility(View.VISIBLE);
//        mDesitinationText.setText(RouteTask
//                .getInstance( getApplicationContext()).getEndPoint().address);
//        mRouteCostText.setText(String.format("预估费用%.2f元，距离%.1fkm", cost,
//                distance));
////        mDestinationButton.setText("我要用车");
////        mCancelButton.setVisibility(View.VISIBLE);
//        llCarContainer.setOnClickListener(null);
    }
}
