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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.lfcx.common.utils.SPUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.consts.Constants;
import com.lfcx.driver.consts.SPConstants;
import com.lfcx.driver.event.EventUtil;
import com.lfcx.driver.fragment.OrderPickFragment;
import com.lfcx.driver.fragment.OrderTitleFragment;
import com.lfcx.driver.fragment.ReceiptFragment;
import com.lfcx.driver.maphelper.LocationTask;
import com.lfcx.driver.maphelper.OnLocationGetListener;
import com.lfcx.driver.maphelper.PositionEntity;
import com.lfcx.driver.maphelper.RouteTask;
import com.lfcx.driver.net.api.DriverCarAPI;
import com.lfcx.driver.util.DriverLocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */

public class DriverOrderActivity extends DriverBaseActivity implements AMap.OnMapLoadedListener, AMap.OnCameraChangeListener, OnLocationGetListener {

    MapView mapView;
    LinearLayout content_container;
    LinearLayout title_container;
    private ImageView mIvBack;
    private TextView mTitleBar;


    private FrameLayout mMainContainer;
    AMap mAmap;
    LocationTask mLocation;
    Marker mPositionMark;
    FragmentManager fragmentManager;
    private DriverCarAPI mDriverCarAPI;
    ReceiptFragment receiptFragment;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                requestUploadPosition(mLatitue, mLongitude);


            }
        }
    };
    private double mLatitue;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_order_activity);
        EventBus.getDefault().register(this);
        initMap(savedInstanceState);
        initView();
        mDriverCarAPI = APIFactory.create(DriverCarAPI.class);
        initLocation();

    }

    private void initView() {

        mMainContainer = (FrameLayout) findViewById(R.id.main_container);
        content_container = (LinearLayout) findViewById(R.id.order_content);
        title_container = (LinearLayout) findViewById(R.id.order_title);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        mIvBack.setVisibility(View.VISIBLE);
        mTitleBar.setText("订单详情");
        receiptFragment = new ReceiptFragment();
        Fragment orderTitleFragment = new OrderTitleFragment();
        Fragment orderContentFragment = new OrderPickFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, receiptFragment);
        transaction.commit();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.order_content, orderContentFragment);
        transaction.commit();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.order_title, orderTitleFragment).commit();
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                LatLonPoint point = new LatLonPoint(latLng.latitude, latLng.longitude);
            }
        });

    }


    @Override
    public void onMapLoaded() {
        //初始化地图完成后开始定位
        setMarker(0, 0);
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
        if (mPositionMark != null && !mPositionMark.isRemoved()) {
            mPositionMark.remove();
        }

        mLatitue = entity.getLatitue();
        mLongitude = entity.getLongitude();
        setMarker(mLatitue, mLongitude);
        DriverLocationUtils.setLocation(entity);
        RouteTask.getInstance(getApplicationContext()).setStartPoint(entity);
        Log.v("system----latitude--->", entity.getLatitue() + "");
        Log.v("system----longitude--->", entity.getLongitude() + "");
        if (mLatitue != 0.0 && mLongitude != 0.0) {
            requestUploadPosition(mLatitue, mLongitude);
        }
        CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(entity.getLatitue(), entity.getLongitude()), mAmap.getCameraPosition().zoom);
        mAmap.animateCamera(cameraUpate);
        if (receiptFragment != null) {
            receiptFragment.onRouteFinish();
        }

    }


    /**
     * 实施上传位置
     *
     * @param latitue
     * @param longitude
     */
    private void requestUploadPosition(double latitue, double longitude) {
        Map<String, Object> param = new HashMap<>();
        param.put("latitude", latitue);
        param.put("longitude ", longitude);
        param.put("pk_user", "752b85a9-6ca0-4467-93e8-5fbf9d1c2f90");
        mDriverCarAPI.insertLocation(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        //showToast(res.getMsg());
                        Log.v("system-------->", res.getMsg());
                        mHandler.sendEmptyMessageDelayed(1, 30000);
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

        MarkerOptions markerOptions = MapMarkerUtils.instance().getMarkerOptions(new LatLng(latitude, longitude)
                , BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.c_map_location));
        mPositionMark = mAmap.addMarker(markerOptions);
//        try{
//            mPositionMark.setPosition(new LatLng(latitude, longitude));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        if (latitude == 0 && longitude == 0) {
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
        EventBus.getDefault().unregister(this);
        mapView.onDestroy();
        if (null != mHandler) {
            mHandler.removeMessages(1);
        }
    }

    //在ui线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventUtil event) {
        if (event.getMsg().equals("receipt")) {
            requestAcceptOrder("推送过来的订单号", (String) SPUtils.getParam(DriverOrderActivity.this, SPConstants.KEY_DRIVER_PK_USER, ""), "752b85a9-6ca0-4467-93e8-5fbf9d1c2f90");
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (receiptFragment != null) {
                transaction.hide(receiptFragment);
                transaction.commit();
            }
            mTitleBar.setText("接乘客");

        }else if(event.getMsg().equals("arrive_point")){
//            Toast.makeText(this, "接到乘客开始行程啦", Toast.LENGTH_SHORT).show();
//            mTitleBar.setText("乘客已经上车");
            Toast.makeText(this, "结束行程", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 司机接单
     *
     * @param pk_userOder
     * @param pk_userDriver
     * @param pk_user
     */
    private void requestAcceptOrder(String pk_userOder, String pk_userDriver, String pk_user) {
        Map<String, Object> param = new HashMap<>();
        param.put("pk_userOder", pk_userOder);
        param.put("pk_userDriver", pk_userDriver);
        param.put("pk_user", pk_user);
        mDriverCarAPI.acceptOrder(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        showToast(res.getMsg());
                        Log.v("system-------->", res.getMsg());
                        //成功之后显示去接乘车的界面
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        if (receiptFragment != null) {
                            transaction.hide(receiptFragment);
                            transaction.commit();
                        }
                        mTitleBar.setText("接乘客");
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

    /**
     * 开始行程
     * @param pk_userOder
     * @param pk_userDriver
     * @param pk_user
     * @param fromaddress
     * @param fromlongitude
     * @param fromlatitude
     * @param aboutdistance
     * @param aboutminutes
     */
    private void requestStartTravel(String pk_userOder, String pk_userDriver, String pk_user,String fromaddress,String fromlongitude,String fromlatitude,String aboutdistance,String aboutminutes) {
        Map<String, Object> param = new HashMap<>();
        param.put("pk_userOder", pk_userOder);
        param.put("pk_userDriver", pk_userDriver);
        param.put("pk_user", pk_user);
        param.put("fromaddress", fromaddress);
        param.put("fromlongitude", fromlongitude);
        param.put("fromlatitude", fromlatitude);
        param.put("aboutdistance", aboutdistance);
        param.put("aboutminutes", aboutminutes);
        mDriverCarAPI.startTravel(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        showToast(res.getMsg());
                        Log.v("system-------->", res.getMsg());
                        //成功之后显示去接乘车的界面
                        mTitleBar.setText("乘客已经上车");
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


    /**
     * 结束行程
     * @param pk_basetravelrecord
     * @param toaddress
     * @param tolatitude
     * @param tolongitude
     * @param traveldistance
     * @param travelminutes
     */
    private void requestFinishTravel(String pk_basetravelrecord,String toaddress,String tolatitude,String tolongitude,String traveldistance,String travelminutes) {
        Map<String, Object> param = new HashMap<>();
        param.put("pk_basetravelrecord", pk_basetravelrecord);
        param.put("toaddress", toaddress);
        param.put("tolatitude", tolatitude);
        param.put("tolongitude", tolongitude);
        param.put("traveldistance", traveldistance);
        param.put("travelminutes", travelminutes);
        mDriverCarAPI.finishTravel(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        showToast(res.getMsg());
                        Log.v("system-------->", res.getMsg());
                        //成功之后显示去接乘车的界面
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        if (receiptFragment != null) {
                            transaction.hide(receiptFragment);
                            transaction.commit();
                        }
                        mTitleBar.setText("接乘客");
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


}
