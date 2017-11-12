package com.lfcx.customer.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.DriveStep;
import com.lfcx.common.utils.MapMarkerUtils;
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.consts.Constants;
import com.lfcx.customer.fragment.CustomerMainFragment;
import com.lfcx.customer.fragment.order.OrderBuildFragment;
import com.lfcx.customer.fragment.order.OrderCancelFragment;
import com.lfcx.customer.fragment.order.OrderJourneyFragment;
import com.lfcx.customer.fragment.order.OrderPayFragment;
import com.lfcx.customer.fragment.order.OrderPickFragment;
import com.lfcx.customer.fragment.order.OrderSelectFragment;
import com.lfcx.customer.fragment.order.OrderWaitFragment;
import com.lfcx.customer.maphelper.LocationTask;
import com.lfcx.customer.maphelper.OnLocationGetListener;
import com.lfcx.customer.maphelper.PositionEntity;
import com.lfcx.customer.maphelper.RegeocodeTask;
import com.lfcx.customer.maphelper.RouteTask;
import com.lfcx.customer.maphelper.Utils;
import com.lfcx.customer.util.LocationUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 立马叫车界面
 */
public class CustomerOrderActivity extends CustomerBaseActivity  implements AMap.OnCameraChangeListener,
        AMap.OnMapLoadedListener, OnLocationGetListener,RouteTask.OnRouteCalculateListener,
        RouteTask.OnRoutePolyLineCalculateListener {

    private static final String TAG = CustomerMainFragment.class.getSimpleName();

    @BindView(R2.id.mapview)
    MapView mMapView;

    private AMap mAmap;

    private Marker mPositionMark;
    private Polyline mPolyLine;
    private Circle mPositionCircle;

    //记录每次移动地图的坐标
    private LatLng mStartPosition;
    //记录当前位置
    private LatLng mInitPosition;

    private RegeocodeTask mRegeocodeTask;

    @BindView(R2.id.iv_back)
    ImageView ivBack;

    public LocationTask mLocationTask;

    @BindView(R2.id.fromto_container)
    LinearLayout mFromToContainer;

    @BindView(R2.id.title_bar)
    TextView mTitleBar;

    private boolean mIsFirst = true;
    private Unbinder unbinder;
    private GeocodeSearch geocoderSearch;

    FragmentManager fragmentManager;
    Map<String, Fragment> fragmentMap;

    private String orderState;

    public static final String BUILD = "BUILD";
    public static final String CANCEL = "CANCEL";
    public static final String SELECT = "SELECT";
    public static final String WAIT = "WAIT";
    public static final String PICK = "PICK";
    public static final String JOURNEY = "JOURNEY";
    public static final String PAY = "PAY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        unbinder = ButterKnife.bind(this);
        initView();
        initMap(savedInstanceState);
        initPermisson();
        mLocationTask = LocationTask.getInstance(getApplicationContext());
        mLocationTask.setOnLocationGetListener(this);
        mRegeocodeTask = new RegeocodeTask(getApplicationContext());
        RouteTask.getInstance(getApplicationContext())
                .addRouteCalculateListener(this);
        RouteTask.getInstance(getApplicationContext())
                .addRoutePolyLineCalculateListener(this);
    }


    private void initPermisson(){
        AndPermission.with(this)
                .requestCode(200)
                .permission(Permission.LOCATION)
                .callback(listener)
                .start();
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if(requestCode == 200) {
                // TODO ...
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if(requestCode == 200) {
                showToast("已拒绝授权定位，暂不能获取您当前位置");
            }
        }
    };

    private void initMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
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
                RegeocodeQuery query = new RegeocodeQuery(point, 100,GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);
            }
        });
    }

    private void initView() {
        ivBack.setVisibility(View.VISIBLE);
        fragmentManager = this.getSupportFragmentManager();

        fragmentMap = new HashMap<>();
        Fragment orderBuildFragment = new OrderBuildFragment();
        Fragment orderCancelFragment = new OrderCancelFragment();
        Fragment orderSelectFragment = new OrderSelectFragment();
        Fragment orderWaitFragment = new OrderWaitFragment();
        Fragment orderPickFragment = new OrderPickFragment();
        Fragment orderJourneyFragment = new OrderJourneyFragment();
        Fragment orderPayFragment = new OrderPayFragment();
        fragmentMap.put(BUILD, orderBuildFragment);
        fragmentMap.put(CANCEL, orderCancelFragment);
        fragmentMap.put(SELECT, orderSelectFragment);
        fragmentMap.put(WAIT, orderWaitFragment);
        fragmentMap.put(PICK, orderPickFragment);
        fragmentMap.put(JOURNEY, orderJourneyFragment);
        fragmentMap.put(PAY, orderPayFragment);
        //初始化在第一个界面
        switchFragment(BUILD);

    }

    public void switchFragment(String key) {
        Fragment fragment = fragmentMap.get(key);
        if(fragment == null) {
            //创建fragment
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fromto_container, fragment);
        transaction.commit();

        //切换当前界面状态
        orderState = key;
        switchState(key);
    }

    /**
     * 当fragment切换时修改界面中相关内容
     *
     * @param key
     */
    private void switchState(String key) {
        //先清除之前界面的遗留效果
        if(mPolyLine != null) {
            mPolyLine.remove();
        }
        if(mPositionCircle != null) {
            mPositionCircle.remove();
        }

        if(WAIT.equals(key)) {
            setTitleText("等待应答");
            setMarkerText("正在为您寻找附近车辆");
            setCircle();
        }
        else if(CANCEL.equals(key)) {
            setTitleText("行程结束");
        }
        else if(PICK.equals(key)) {
            setTitleText("等待接驾");
            setMarkerText("请在指定位置上车");
            RouteTask.getInstance(getApplicationContext()).search();
        }
        else if(PAY.equals(key)) {
            setTitleText("等待支付");
        }
    }

    private void setContainerVisibility(boolean isShow) {
        if(isShow) {
            mFromToContainer.setVisibility(View.VISIBLE);
        }
        else{
            mFromToContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCameraChange(CameraPosition arg0) {
//        setContainerVisibility(false);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
//        setContainerVisibility(true);
        mRegeocodeTask.setOnLocationGetListener(this);
        mRegeocodeTask.search(mStartPosition.latitude, mStartPosition.longitude);
        if (mIsFirst) {
            Utils.addEmulateData(mAmap, mStartPosition);
            if (mPositionMark != null) {
                mPositionMark.setToTop();
            }
            mIsFirst = false;
        }
    }


    @Override
    public void onMapLoaded() {
        //初始化地图
        setMarker(0,0);
        mLocationTask.startSingleLocate();
    }


    @Override
    public void onLocationGet(PositionEntity entity) {
        // todo 这里在网络定位时可以减少一个逆地理编码
        LocationUtils.setLocation(entity);
        //第一次获得位置
        if(null != mPositionMark && !mPositionMark.isRemoved()){
            mPositionMark.remove();
        }
        setMarker(entity.latitue,entity.longitude);
        setMarkerText(entity.address);
        Log.i(TAG,"onLocationGet-->"+entity.address);
        ((OrderBuildFragment)fragmentMap.get(BUILD)).setDepartureAddress(entity.address);
        RouteTask.getInstance( getApplicationContext()).setStartPoint(entity);
//        RouteTask.getInstance(getApplicationContext()).search();

        mStartPosition = new LatLng(entity.latitue, entity.longitude);
        CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                mStartPosition, mAmap.getCameraPosition().zoom);
        mAmap.animateCamera(cameraUpate);

    }

    @Override
    public void onRegecodeGet(PositionEntity entity) {
        Log.i(TAG,"onRegecodeGet-->"+entity.address);
    }

    private void setMarker(double latitude, double longitude) {
        MarkerOptions markerOptions = MapMarkerUtils.instance().getMarkerOptions(new LatLng(latitude,longitude)
                ,BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.c_map_location));
        mPositionMark = mAmap.addMarker(markerOptions);
//        try{
//            mPositionMark.setPosition(new LatLng(latitude, longitude));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        if(latitude == 0 && longitude == 0){
            mPositionMark.setPositionByPixels(mMapView.getWidth() / 2,
                    mMapView.getHeight() / 2);
        }
    }

    private void setMarkerText(String text) {
        mPositionMark.setSnippet(text);
        mPositionMark.showInfoWindow();
    }

    private void setCircle() {
        LatLng latLng = new LatLng(LocationUtils.getLocation().getLatitue(),
                LocationUtils.getLocation().getLongitude());
        mPositionCircle = mAmap.addCircle(new CircleOptions().
                center(latLng).
                radius(50).
                fillColor(Color.parseColor("#ACACAC")).
                strokeColor(Color.parseColor("#ACACAC")).
                strokeWidth(15));
    }


    @Override
    public void onRouteCalculate(float cost, float distance, int duration) {
        if(BUILD.equals(orderState)) {
            ((OrderBuildFragment)fragmentMap.get(BUILD)).onRouteFinish();
        }
    }

    @Override
    public void onRoutePolyLineCalculate(List<DriveStep> stepList) {
        if(PICK.equals(orderState) || JOURNEY.equals(orderState)) {
            //绘制路径线段
            List<LatLng> latLngs = new ArrayList<LatLng>();
            for(int i=0; i<stepList.size(); i++) {
                List<LatLonPoint> pointList = stepList.get(i).getPolyline();
                for(int j=0; j<pointList.size(); j++) {
                    latLngs.add(new LatLng(pointList.get(j).getLatitude(),
                            pointList.get(j).getLongitude()));
                }
            }
            mPolyLine =mAmap.addPolyline(new PolylineOptions().
                    addAll(latLngs).width(10).color(Color.parseColor("#0074A6")));
        }

    }

    public void setTitleText(String name) {
        mTitleBar.setText(name);
    }

    @OnClick({R2.id.iv_back})
    public void onClick(View v) {
       if (v.getId() == R.id.iv_back) {
            finish();
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
        RouteTask.getInstance( getApplicationContext()).removeRoutePolyLineCalculateListener(this);
        unbinder.unbind();
    }

}
