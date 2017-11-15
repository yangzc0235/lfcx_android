package com.lfcx.customer.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.lfcx.common.utils.MapMarkerUtils;
import com.lfcx.common.utils.ToastUtils;
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.activity.CustomerBookActivity;
import com.lfcx.customer.activity.CustomerOrderActivity;
import com.lfcx.customer.activity.DestinationActivity;
import com.lfcx.customer.consts.Constants;
import com.lfcx.customer.maphelper.LocationTask;
import com.lfcx.customer.maphelper.OnLocationGetListener;
import com.lfcx.customer.maphelper.PositionEntity;
import com.lfcx.customer.maphelper.RegeocodeTask;
import com.lfcx.customer.maphelper.RouteTask;
import com.lfcx.customer.maphelper.Utils;
import com.lfcx.customer.util.LocationUtils;
import com.lfcx.customer.widget.pop.CustomerCarPop;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   : 乘客打车首页面，
 */
public class CustomerIndexFragment extends Fragment implements AMap.OnCameraChangeListener,
        AMap.OnMapLoadedListener, OnLocationGetListener, RouteTask.OnRouteCalculateListener{

    private static final String TAG = CustomerMainFragment.class.getSimpleName();

    @BindView(R2.id.mapview)
    MapView mMapView;

    private AMap mAmap;

    @BindView(R2.id.btn_now)
    Button btnNow;

    @BindView(R2.id.ll_car_container)
    LinearLayout llCarContainer;

    @BindView(R2.id.btn_after)
    Button btnAfter;

    @BindView(R2.id.iv_car)
    ImageView ivCar;

    private Marker mPositionMark;

    //记录每次移动地图的坐标
    private LatLng mStartPosition;
    //记录当前位置
    private LatLng mInitPosition;

    private RegeocodeTask mRegeocodeTask;
    private LocationTask mLocationTask;

    @BindView(R2.id.location_image)
    ImageView mLocationImage;

    private boolean mIsFirst = true;

    private boolean mIsRouteSuccess = false;
    private Unbinder unbinder;

    /**
     * 车型选择
     */
    private CustomerCarPop carPop;

    private GeocodeSearch geocoderSearch ;

    /**
     * 默认为舒适型
     */
    private int styletype = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.c_fragment_customer_index, container, false);
        unbinder = ButterKnife.bind(this,contentView);
        init(contentView, savedInstanceState);
        initPermisson();
        mLocationTask = LocationTask.getInstance(getActivity().getApplicationContext());
        mLocationTask.setOnLocationGetListener(this);
        mRegeocodeTask = new RegeocodeTask(getActivity().getApplicationContext());
        RouteTask.getInstance(getActivity().getApplicationContext())
                .addRouteCalculateListener(this);
        return contentView;
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
                ToastUtils.shortToast(getActivity(),"已拒绝授权定位，暂不能获取您当前位置");
            }
        }
    };
    private void init(View contentView, Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
        mAmap.getUiSettings().setZoomControlsEnabled(false);
        mAmap.setOnMapLoadedListener(this);
        mAmap.setOnCameraChangeListener(this);
        //首次进入地图设置地图的缩放级别
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(Constants.MAP_INIT_ZOOM));
//        geocoderSearch = new GeocodeSearch(getActivity());
//        geocoderSearch.setOnGeocodeSearchListener(this);
        mAmap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setMarker(latLng.latitude,latLng.longitude);
                LatLonPoint point = new LatLonPoint(latLng.latitude,latLng.longitude);
                RegeocodeQuery query = new RegeocodeQuery(point, 100,GeocodeSearch.AMAP);
                LocationUtils.getLocation().latitue = latLng.latitude;
                LocationUtils.getLocation().longitude = latLng.longitude;
                geocoderSearch.getFromLocationAsyn(query);
            }
        });
    }


    @Override
    public void onCameraChange(CameraPosition arg0) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        //挪动地图，不改变指针位置
//        mStartPosition = cameraPosition.target;
//        mInitPosition = cameraPosition.target;
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
        RouteTask.getInstance(getActivity().getApplicationContext()).removeRouteCalculateListener(this);
        unbinder.unbind();
    }

    @Override
    public void onMapLoaded() {
        //地图初始化
        setMarker(0,0);

        mLocationTask.startSingleLocate();
    }

    @OnClick({R2.id.location_image,R2.id.btn_now,R2.id.btn_after,R2.id.iv_car})
    public void onClick(View v) {

        if (v.getId() == R.id.location_image)
            mLocationTask.startSingleLocate();
        else if (v.getId() == R.id.destination_text) {
            Intent destinationIntent = new Intent(getActivity(),
                    DestinationActivity.class);
            startActivity(destinationIntent);
        }
        else if (v.getId() == R.id.btn_now) {

            //马上用车
            Intent callIntent = new Intent(getActivity(),
                    CustomerOrderActivity.class);
            callIntent.putExtra("styletype",styletype);
            startActivity(callIntent);
        }
        else if (v.getId() == R.id.btn_after) {

            //预约
            Intent allIntent = new Intent(getActivity(),
                    CustomerBookActivity.class);
            startActivity(allIntent);
        } else if (v.getId() == R.id.iv_car) {
            int[] location = new int[2];
            int popHeight = 0;
            ivCar.getLocationOnScreen(location);
            if (null == carPop) {
                carPop = new CustomerCarPop(getActivity());
                carPop.setCarSelectListener(new CustomerCarPop.ICarSelectListener() {
                    @Override
                    public void onSelected(View view, int position) {
                        switch (position){
                            case CustomerCarPop.POSITION_ONE:
                                ivCar.setBackground(getResources().getDrawable(R.drawable.but_index_box_one));
                                break;
                            case CustomerCarPop.POSITION_TWO:
                                ivCar.setBackground(getResources().getDrawable(R.drawable.but_index_box_two));
                                styletype = 0;
                                break;
                            case CustomerCarPop.POSITION_THREE:
                                ivCar.setBackground(getResources().getDrawable(R.drawable.but_index_box_three));
                                styletype = 1;
                                break;
                            case CustomerCarPop.POSITION_FOUR:
                                ivCar.setBackground(getResources().getDrawable(R.drawable.but_index_box_four));
                                styletype = 2;
                                break;
                        }
                        carPop.dissmiss();
                    }
                });
            }
            popHeight = carPop.getmPop().getContentView().getMeasuredHeight();
            if (!carPop.isShowing()) {
                carPop.show(ivCar,  0, 0-(ivCar.getMeasuredHeight()+popHeight),Gravity.NO_GRAVITY);
            } else {
                carPop.dissmiss();
            }
        }
    }

    @Override
    public void onLocationGet(PositionEntity entity) {
        // todo 这里在网络定位时可以减少一个逆地理编码
        //第一次获得位置
        if(null != mPositionMark && !mPositionMark.isRemoved()){
            mPositionMark.remove();
        }
        setMarker(entity.latitue, entity.longitude);
        LocationUtils.setLocation(entity);
        RouteTask.getInstance(getActivity().getApplicationContext()).setStartPoint(entity);
        mStartPosition = new LatLng(entity.latitue, entity.longitude);
        CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                mStartPosition, mAmap.getCameraPosition().zoom);
        mAmap.animateCamera(cameraUpate);
    }

    @Override
    public void onRegecodeGet(PositionEntity entity) {
        entity.latitue = mStartPosition.latitude;
        entity.longitude = mStartPosition.longitude;
        LocationUtils.setLocation(entity);

        if(null != mPositionMark && !mPositionMark.isRemoved()){
            mPositionMark.remove();
        }
        setMarker(entity.latitue, entity.longitude);
        RouteTask.getInstance(getActivity().getApplicationContext()).setStartPoint(entity);
        RouteTask.getInstance(getActivity().getApplicationContext()).search();
    }

    private void setMarker(double latitude, double longitude) {
        MarkerOptions markerOptions = MapMarkerUtils.instance().getMarkerOptions(new LatLng(latitude,longitude)
                ,BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.c_map_location));
        markerOptions.snippet("当前位置");
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

    @Override
    public void onRouteCalculate(float cost, float distance, int duration) {
        mIsRouteSuccess = true;
        llCarContainer.setOnClickListener(null);
    }

}
