package com.lfcx.customer.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.activity.CallCarSucessActivity;
import com.lfcx.customer.activity.DestinationActivity;
import com.lfcx.customer.consts.Constants;
import com.lfcx.customer.maphelper.LocationTask;
import com.lfcx.customer.maphelper.OnLocationGetListener;
import com.lfcx.customer.maphelper.PositionEntity;
import com.lfcx.customer.maphelper.RegeocodeTask;
import com.lfcx.customer.maphelper.RouteTask;
import com.lfcx.customer.maphelper.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   :  专车（包括预约和立即）
 */
public class CustomerMainFragment extends Fragment implements AMap.OnCameraChangeListener,
        AMap.OnMapLoadedListener, OnLocationGetListener,
        RouteTask.OnRouteCalculateListener {

    private static final String TAG = CustomerMainFragment.class.getSimpleName();

    @BindView(R2.id.mapview)
    MapView mMapView;

    private AMap mAmap;

    @BindView(R2.id.address_text)
    TextView mAddressTextView;

    @BindView(R2.id.btn_now)
    Button btnNow;

    @BindView(R2.id.ll_car_container)
    LinearLayout llCarContainer;

    @BindView(R2.id.btn_after)
    Button btnAfter;

    private Marker mPositionMark;

    //记录每次移动地图的坐标
    private LatLng mStartPosition;
    //记录当前位置
    private LatLng mInitPosition;

    private RegeocodeTask mRegeocodeTask;

    @BindView(R2.id.destination_container)
    LinearLayout mDestinationContainer;

    @BindView(R2.id.routecost_text)
    TextView mRouteCostText;

    @BindView(R2.id.destination_text)
    TextView mDesitinationText;

    private LocationTask mLocationTask;

    @BindView(R2.id.location_image)
    ImageView mLocationImage;

    @BindView(R2.id.fromto_container)
    LinearLayout mFromToContainer;

    private boolean mIsFirst = true;

    private boolean mIsRouteSuccess = false;
    private Unbinder unbinder;

    public interface OnGetLocationListener {
        public void getLocation(String locationAddress);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_customer_main, container, false);
        unbinder = ButterKnife.bind(this,contentView);

        init(contentView, savedInstanceState);
        mLocationTask = LocationTask.getInstance(getActivity().getApplicationContext());
        mLocationTask.setOnLocationGetListener(this);
        mRegeocodeTask = new RegeocodeTask(getActivity().getApplicationContext());
        RouteTask.getInstance(getActivity().getApplicationContext())
                .addRouteCalculateListener(this);
        return contentView;
    }

    private void init(View contentView, Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
        mAmap.getUiSettings().setZoomControlsEnabled(false);
        mAmap.setOnMapLoadedListener(this);
        mAmap.setOnCameraChangeListener(this);
        //首次进入地图设置地图的缩放级别
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(Constants.MAP_INIT_ZOOM));
//        mAddressTextView = (TextView) contentView.findViewById(R.id.address_text);

//        btnNow = (Button) contentView.findViewById(R.id.btn_now);
//        btnNow.setOnClickListener(this);

//        btnAfter = (Button) contentView.findViewById(R.id.btn_after);
//        btnAfter.setOnClickListener(this);

//        llCarContainer = (LinearLayout) contentView.findViewById(R.id.ll_car_container);

//        mMapView = (MapView) contentView.findViewById(R.id.map);


//        mDestinationContainer = (LinearLayout) contentView.findViewById(R.id.destination_container);
//        mRouteCostText = (TextView) contentView.findViewById(R.id.routecost_text);
//        mDesitinationText = (TextView) contentView.findViewById(R.id.destination_text);
//        mDesitinationText.setOnClickListener(this);
//        mLocationImage = (ImageView) contentView.findViewById(R.id.location_image);
//        mLocationImage.setOnClickListener(this);
//        mFromToContainer = (LinearLayout) contentView.findViewById(R.id.fromto_container);

    }

    private void hideView() {
        mFromToContainer.setVisibility(View.GONE);
        llCarContainer.setVisibility(View.GONE);
    }

    private void showView() {
        mFromToContainer.setVisibility(View.VISIBLE);
        if (mIsRouteSuccess) {
        }
    }

    @Override
    public void onCameraChange(CameraPosition arg0) {
        hideView();
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        showView();
        mStartPosition = cameraPosition.target;
        mInitPosition = cameraPosition.target;
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
        RouteTask.getInstance(getActivity().getApplicationContext()).removeRouteCalculateListener(this);
        unbinder.unbind();
    }

    @Override
    public void onMapLoaded() {
        MarkerOptions markerOptions = MapMarkerUtils.instance().getMarkerOptions(new LatLng(0,0)
                ,BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.c_map_location));
        mPositionMark = mAmap.addMarker(markerOptions);
        mPositionMark.setPositionByPixels(mMapView.getWidth() / 2,
                mMapView.getHeight() / 2);
        mLocationTask.startSingleLocate();
    }

    @OnClick({R2.id.location_image,R2.id.btn_now,R2.id.btn_after,R2.id.destination_text})
    public void onClick(View v) {

        if (v.getId() == R.id.location_image)
            mLocationTask.startSingleLocate();
        else if (v.getId() == R.id.destination_text) {
            Intent destinationIntent = new Intent(getActivity(),
                    DestinationActivity.class);
            startActivity(destinationIntent);
        }
        else if (v.getId() == R.id.btn_now) {
            Intent callIntent = new Intent(getActivity(),
                    CallCarSucessActivity.class);
            startActivity(callIntent);
        }
        else if (v.getId() == R.id.btn_after) {
            Intent allIntent = new Intent(getActivity(),
                    CallCarSucessActivity.class);
            startActivity(allIntent);
        }

//        switch (v.getId()){
//            case R2.id.location_image:
//                mLocationTask.startSingleLocate();
//                break;
//            case R2.id.btn_now:
//                Intent callIntent = new Intent(getActivity(),
//                        CallCarSucessActivity.class);
//                startActivity(callIntent);
//                break;
//            case R2.id.btn_after:
//                Intent allIntent = new Intent(getActivity(),
//                        CallCarSucessActivity.class);
//                startActivity(allIntent);
//                break;
//            case R2.id.destination_text:
//                Intent destinationIntent = new Intent(getActivity(),
//                        DestinationActivity.class);
//                startActivity(destinationIntent);
//                break;
//        }

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
        Log.i(TAG,"onLocationGet-->"+entity.address);
        mAddressTextView.setText(entity.address);
        RouteTask.getInstance(getActivity().getApplicationContext()).setStartPoint(entity);

        mStartPosition = new LatLng(entity.latitue, entity.longitude);
        CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                mStartPosition, mAmap.getCameraPosition().zoom);
        mAmap.animateCamera(cameraUpate);

    }

    @Override
    public void onRegecodeGet(PositionEntity entity) {
        Log.i(TAG,"onRegecodeGet-->"+entity.address);
        mAddressTextView.setText(entity.address);
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
        RouteTask.getInstance(getActivity().getApplicationContext()).setStartPoint(entity);
        RouteTask.getInstance(getActivity().getApplicationContext()).search();
    }

    @Override
    public void onRouteCalculate(float cost, float distance, int duration) {
        mIsRouteSuccess = true;
        mRouteCostText.setVisibility(View.VISIBLE);
        llCarContainer.setVisibility(View.VISIBLE);
        mDesitinationText.setText(RouteTask
                .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
        mRouteCostText.setText(String.format("预估费用%.2f元，距离%.1fkm", cost,
                distance));
        llCarContainer.setOnClickListener(null);
    }


}
