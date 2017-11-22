package com.lfcx.main.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.main.R;
import com.lfcx.main.event.ReceiptResultEvent;
import com.lfcx.main.maphelper.LocationTask;
import com.lfcx.main.maphelper.OnLocationGetListener;
import com.lfcx.main.maphelper.PositionEntity;
import com.lfcx.main.maphelper.RegeocodeTask;
import com.lfcx.main.maphelper.RouteTask;
import com.lfcx.main.maphelper.Utils;
import com.lfcx.main.net.api.UserAPI;
import com.lfcx.main.net.result.DriverOrderEntity;
import com.lfcx.main.net.result.PayEntity;
import com.lfcx.main.net.result.StartTravelEntity;
import com.lfcx.main.widget.dialog.PayDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallCarSucessActivity extends CustomerBaseActivity implements AMap.OnCameraChangeListener,
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
    private String mPk_userOder;
    private RelativeLayout mActivityMain;
    private ImageView mIvBack;
    private ImageView mIvUser;
    private TextView mTitleBar;
    private MapView mMap;
    private LinearLayout mLlOne;
    private TextView mTvName;
    private TextView mCardId;
    private TextView mCardName;
    private TextView mTvCancelOrder;
    private TextView mTvCallPhone;
    private TextView mTvComplain;
    private RelativeLayout mLlWait;
    private FrameLayout mFlDriverInfo;
    private TextView mTvConfirm;
    private TextView mTvCancelOrderFirst;
    private String userOrderid;

    public interface OnGetLocationListener {
        public void getLocation(String locationAddress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_call_car_sucess);
        init(savedInstanceState);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
        mLlWait = (RelativeLayout) findViewById(R.id.ll_wait);
        mFlDriverInfo = (FrameLayout) findViewById(R.id.fl_driver_info);
        mActivityMain = (RelativeLayout) findViewById(R.id.activity_main);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvUser = (ImageView) findViewById(R.id.iv_user);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        mMap = (MapView) findViewById(R.id.map);
        mLlOne = (LinearLayout) findViewById(R.id.ll_one);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mCardId = (TextView) findViewById(R.id.card_id);
        mCardName = (TextView) findViewById(R.id.card_name);
        mTvCancelOrder = (TextView) findViewById(R.id.tv_cancel_order);
        mTvCallPhone = (TextView) findViewById(R.id.tv_call_phone);
        mTvComplain = (TextView) findViewById(R.id.tv_complain);
        mTvCancelOrderFirst = (TextView) findViewById(R.id.tv_cancel_order_first);
        mLocationTask = LocationTask.getInstance(getApplicationContext());
        mLocationTask.setOnLocationGetListener(this);
        mFlDriverInfo.setVisibility(View.GONE);
        mLlWait.setVisibility(View.VISIBLE);
        mIvBack.setVisibility(View.GONE);
        mTvCancelOrderFirst.setOnClickListener(this);
        mRegeocodeTask = new RegeocodeTask(getApplicationContext());
        RouteTask.getInstance(getApplicationContext())
                .addRouteCalculateListener(this);
        try {
            mPk_userOder = getIntent().getStringExtra("pk_userOder");
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }

    private void init(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
        mAmap.getUiSettings().setZoomControlsEnabled(false);
        mAmap.setOnMapLoadedListener(this);
        mAmap.setOnCameraChangeListener(this);
        userAPI = APIFactory.create(UserAPI.class);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R2.id.destination_button:
//                Intent intent = new Intent(this, DestinationActivity.class);
//                startActivity(intent);
//                break;
            case R.id.location_image:
                mLocationTask.startSingleLocate();
                break;
            case R.id.destination_text:
                Intent destinationIntent = new Intent(this,
                        DestinationActivity.class);
                startActivity(destinationIntent);
                break;

            case R.id.tv_cancel_order_first:
                //取消订单
                cancelOrderInfo(mPk_userOder);
                break;
        }
    }


    //wait_receipt
    //在ui线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReceiptResultEvent event) {
        final DriverOrderEntity driverOrderEntity;
        if (event.getKey().equals("receiveDriverInfo")) {
            mFlDriverInfo.setVisibility(View.VISIBLE);
            mLlWait.setVisibility(View.GONE);
            String value = event.getValue();
            Log.v("system---value--->", value);
            Gson gson = new Gson();
            driverOrderEntity = gson.fromJson(value, DriverOrderEntity.class);
            mCardId.setText("车牌号:"+driverOrderEntity.getCarCode());
            mCardName.setText("车型:"+driverOrderEntity.getType());
            mTvName.setText("司机:"+driverOrderEntity.getDrivername());
            userOrderid = driverOrderEntity.getPk_userorder();
            mTvCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消订单
                    cancelOrderInfo(driverOrderEntity.getPk_userorder());
                }
            });

            mTvCallPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //联系客服
                    callPhone(driverOrderEntity.getMobile());
                }
            });

            mTvComplain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //投诉电话
                    callPhone(driverOrderEntity.getMobile());
                }
            });

            //getDriverInfo(driverOrderEntity.getPk_user(),String.valueOf(driverOrderEntity.getIstruename()));
        } else if (event.getKey().equals("startConfirm")) {
            mTvConfirm.setVisibility(View.VISIBLE);
            mTvCancelOrder.setVisibility(View.GONE);
            String message = event.getValue();
            final StartTravelEntity startTravelEntity = new Gson().fromJson(message, StartTravelEntity.class);
            mTvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmStartTravel(startTravelEntity.getPk_basetravelrecord());
                }
            });
        } else if (event.getKey().equals("receiveFinshOrder")) {
            String message = event.getValue();
            Log.v("system---value-->", message);
            Gson gson = new Gson();
            PayEntity payEntity = gson.fromJson(message, PayEntity.class);
            PayDialog payDialog = new PayDialog(this, R.style.customDialog, payEntity);
            payDialog.show();
        }else if (event.getKey().equals("receivePayFinish")) {
            Toast.makeText(this, "您已经付款成功,感谢您下次乘坐雷风专车", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * 用户确认上车
     *
     * @param pk_basetravelrecord
     */
    private void confirmStartTravel(String pk_basetravelrecord) {
        showLoading();
        Map<String, String> param = new HashMap<>();
        Log.v("system-----record-->", pk_basetravelrecord);
        param.put("pk_basetravelrecord", pk_basetravelrecord);
        userAPI.userConfirmCar(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    hideLoading();
                    Gson gson = new Gson();
                    BaseResultBean resultBean = gson.fromJson(response.body(), BaseResultBean.class);
                    if (resultBean.getCode().equals("0")) {
                        Toast.makeText(CallCarSucessActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
                        mTvConfirm.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(CallCarSucessActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    Log.v("system----确认上车--->", response.body());

                } catch (Exception e) {
                    Toast.makeText(CallCarSucessActivity.this, "确认上车异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
                showToast("确认上车异常!");
            }
        });
    }


    /**
     * 取消订单
     *
     * @param pk_userorder
     */
    private void cancelOrderInfo(String pk_userorder) {
        showLoading();
        Map<String, String> param = new HashMap<>();
        param.put("isdriver", "0");
        param.put("pk_userorder", pk_userorder);
        userAPI.cancelOrderInfo(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    Gson gson = new Gson();
                    BaseResultBean resultBean = gson.fromJson(response.body(), BaseResultBean.class);
                    if (resultBean.getCode().equals("0")) {
                        Toast.makeText(CallCarSucessActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CallCarSucessActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    Log.v("system----取消订单信息--->", response.body());

                } catch (Exception e) {
                    Toast.makeText(CallCarSucessActivity.this, "订单取消异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
                showToast("订单取消失败!");
            }
        });

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
        EventBus.getDefault().unregister(this);
        Utils.removeMarkers();
        mMapView.onDestroy();
        mLocationTask.onDestroy();
        RouteTask.getInstance(getApplicationContext()).removeRouteCalculateListener(this);

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
                                R.drawable.c_map_location)));
        mPositionMark = mAmap.addMarker(markerOptions);

        mPositionMark.setPositionByPixels(mMapView.getWidth() / 2,
                mMapView.getHeight() / 2);
        mLocationTask.startSingleLocate();
    }


    @Override
    public void onLocationGet(PositionEntity entity) {
        // todo 这里在网络定位时可以减少一个逆地理编码
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
        RouteTask.getInstance(getApplicationContext()).setStartPoint(entity);
        RouteTask.getInstance(getApplicationContext()).search();
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


    /**
     * 打电话
     */
    private void callPhone(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "号码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            // 拨号：激活系统的拨号组件
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + mobile);
            intent.setData(data);
            startActivity(intent);
        }
    }


}
