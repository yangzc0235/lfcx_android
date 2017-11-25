package com.lfcx.driver.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.amap.api.navi.enums.NaviType;
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
import com.lfcx.driver.event.MoneyEvent;
import com.lfcx.driver.event.ReceiptEvent;
import com.lfcx.driver.fragment.ConfirmBillFragment;
import com.lfcx.driver.fragment.OrderFinishFragment;
import com.lfcx.driver.fragment.OrderPickFragment;
import com.lfcx.driver.fragment.OrderTitleFragment;
import com.lfcx.driver.fragment.ReceiptFragment;
import com.lfcx.driver.maphelper.LocationTask;
import com.lfcx.driver.maphelper.OnLocationGetListener;
import com.lfcx.driver.maphelper.PositionEntity;
import com.lfcx.driver.maphelper.RouteTask;
import com.lfcx.driver.net.api.DriverCarAPI;
import com.lfcx.driver.net.result.IncomeEntity;
import com.lfcx.driver.net.result.StartTravelEntity;
import com.lfcx.driver.net.result.UserOrderEntity;
import com.lfcx.driver.util.Distance;
import com.lfcx.driver.util.DriverLocationUtils;
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
 *
 */

public class DriverOrderActivity extends DriverBaseActivity implements AMap.OnMapLoadedListener, AMap.OnCameraChangeListener, OnLocationGetListener {

    private LinearLayout content_container;
    private LinearLayout title_container;
    private ImageView mIvBack;
    private TextView mTitleBar;
    private MapView mMapView;
    private FrameLayout mMainContainer;
    AMap mAmap;
    LocationTask mLocation;
    Marker mPositionMark;
    FragmentManager fragmentManager;
    private DriverCarAPI mDriverCarAPI;
    private ReceiptFragment receiptFragment;
    private OrderPickFragment mOrderPickFragment;
    private double mLatitue;
    private double mLongitude;
    public static UserOrderEntity userOrderEntity;
    private String mBasetravelrecord;
    ConfirmBillFragment confirmBillFragment;
    public static IncomeEntity incomeEntity;
    private long mStartTime;
    private long mEndTime;

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
        mTitleBar.setText("正在接单");
        receiptFragment = new ReceiptFragment();
        Fragment orderTitleFragment = new OrderTitleFragment();
        mOrderPickFragment = new OrderPickFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, receiptFragment);
        transaction.commit();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.order_content, mOrderPickFragment);
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
        mMapView = (MapView) findViewById(R.id.mapview);
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


    }

    //在ui线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventUtil event) {
        try {
            if (event.getMsg().equals("receipt")) {
                Log.v("system--------->", "司机开始接单");
                requestAcceptOrder(userOrderEntity.getPk_userorder(), (String) SPUtils.getParam(DriverOrderActivity.this, SPConstants.KEY_DRIVER_PK_USER, ""), userOrderEntity.getPk_user());
            } else if (event.getMsg().equals("received_passengers")) {
                mTitleBar.setText("确认接到乘客");
                String pk_userDriver = (String) SPUtils.getParam(DriverOrderActivity.this, SPConstants.KEY_DRIVER_PK_USER, "");
                double distance = Distance.getDistance(userOrderEntity.getFromlongitude(), userOrderEntity.getFromlatitude(), userOrderEntity.getTolongitude(), userOrderEntity.getTolatitude());
                requestConfirmCustomer(userOrderEntity.getPk_userorder(), pk_userDriver, userOrderEntity.getPk_user(), userOrderEntity.getFromaddress(), userOrderEntity.getFromlongitude(), userOrderEntity.getFromlatitude(), distance, "50");
            } else if (event.getMsg().equals("start_travel")) {
                mTitleBar.setText("开始行程");
                requestStartTravel(mBasetravelrecord);
            } else if (event.getMsg().equals("arrive_point")) {
                Log.v("system------------>", "结束行程");
                mEndTime = System.currentTimeMillis();
                double distance = Distance.getDistance(userOrderEntity.getFromlongitude(), userOrderEntity.getFromlatitude(), LocationUtils.getLocation().getLongitude(), LocationUtils.getLocation().getLatitude());
                Log.v("system---time--->", (mEndTime - mStartTime) + "");
                Log.v("system---实际时间--->", String.valueOf((mEndTime - mStartTime) / 60000) + "");
                Log.v("system--- distance--->", distance / 1000 + "");
                requestFinishTravel(mBasetravelrecord, userOrderEntity.getToaddress(), userOrderEntity.getTolongitude(), userOrderEntity.getTolatitude(), distance / 1000, (mEndTime - mStartTime) / 60000 + "");
            } else if (event.getMsg().equals("collect_car")) {
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "发送消息异常", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MoneyEvent event) {
        if (event.getKey().equals("order_finish")) {
            Log.v("system-------->", "开始请求附加费接口");
            String arg1 = event.getArg1();
            String arg2 = event.getArg2();
            String arg3 = event.getArg3();
            requestOtherexpenses(userOrderEntity.getPk_userorder(), "0.01", arg1, arg2, arg3, "0");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReceiptEvent event) {
        if (event.getKey().equals("pay_finish")) {
            Log.v("system----->", "跳支付完成");
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            OrderFinishFragment orderFinishFragment = new OrderFinishFragment();
            Log.v("system--支付显示-->", event.getValue());
            incomeEntity = new Gson().fromJson(event.getValue(), IncomeEntity.class);
            transaction.replace(R.id.main_container, orderFinishFragment);
            transaction.commit();
            //生成交易记录
            requestRradeRecord(incomeEntity.getPk_userorder(),incomeEntity.getPk_user(),incomeEntity.getIncome(),incomeEntity.getPay(),incomeEntity.getType(),incomeEntity.getTradetype());

        } else if (event.getKey().equals("begain_travel")) {
            Log.v("system---------->", "收到乘客已经确认上车");
            if (mOrderPickFragment != null) {
                mOrderPickFragment.setButtonTextFirst();
            }
        }else if (event.getKey().equals("startReceipt")) {
            Gson gson = new Gson();
            userOrderEntity = gson.fromJson(event.getValue(), UserOrderEntity.class);
            double distance = Distance.getDistance(userOrderEntity.getFromlongitude(), userOrderEntity.getFromlatitude(), userOrderEntity.getTolongitude(), userOrderEntity.getTolatitude());
//            int time= (int) (distance/(60*1000));
            mTts.startSpeaking("实时从" + userOrderEntity.getFromaddress() + "到" + userOrderEntity.getToaddress() + "订单," + "距您" + distance / 1000+"公里" , mTtsListener);
            if (receiptFragment!=null) {
                receiptFragment.setStartReciept();
            }
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
        param.put("longitude", longitude);
        param.put("pk_user", SPUtils.getParam(this, SPConstants.KEY_DRIVER_PK_USER, ""));
        Log.v("system----latitue---->", latitue + "");
        Log.v("system----longitude-->", longitude + "");
        Log.v("system----pk_user-->", SPUtils.getParam(this, SPConstants.KEY_DRIVER_PK_USER, "") + "");
        mDriverCarAPI.insertLocation(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        Log.v("system-------->", res.getMsg());
                    } else {
                        showToast(res.getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("上传失败！！");
            }
        });
    }

    /**
     * 司机接单
     *
     * @param pk_userorder
     * @param pk_userdriver
     * @param pk_user
     */
    private void requestAcceptOrder(String pk_userorder, String pk_userdriver, String pk_user) {
        Map<String, Object> param = new HashMap<>();
        param.put("pk_userorder", pk_userorder);
        param.put("pk_userdriver", pk_userdriver);
        param.put("pk_user", pk_user);
        Log.v("system--pk_userorder->", pk_userorder);
        Log.v("system--pk_userdriver->", pk_userdriver);
        Log.v("system--pk_user->", pk_user);
        showLoading();
        mDriverCarAPI.acceptOrder(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    Log.v("system---接单返回的信息--->", response.body());
                    try {
                        BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                        if ("0".equals(res.getCode())) {
                            showToast(res.getMsg());
                            mTitleBar.setText("接乘客");
                            mTts.startSpeaking(res.getMsg() , mTtsListener);
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            if (receiptFragment != null) {
                                transaction.hide(receiptFragment);
                                transaction.commit();
                            }
                        } else {
                            showToast(res.getMsg());
                            finish();
                        }
                    } catch (Exception e) {
                        finish();
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("接单失败！！");
                hideLoading();
            }
        });
    }


    /**
     * 确认用户上车
     *
     * @param pk_userOder
     * @param pk_userDriver
     * @param pk_user
     * @param fromaddress
     * @param fromlongitude
     * @param fromlatitude
     * @param aboutdistance
     * @param aboutminutes
     */
    private void requestConfirmCustomer(String pk_userOder, String pk_userDriver, String pk_user, String fromaddress, double fromlongitude, double fromlatitude, double aboutdistance, String aboutminutes) {
        Map<String, Object> param = new HashMap<>();
        param.put("pk_userorder", pk_userOder);
        param.put("pk_userdriver", pk_userDriver);
        param.put("pk_user", pk_user);
        param.put("fromaddress", fromaddress);
        param.put("fromlongitude", fromlongitude);
        param.put("fromlatitude", fromlatitude);
        param.put("aboutdistance", aboutdistance);
        param.put("aboutminutes", aboutminutes);
        showLoading();
        mDriverCarAPI.confirmCustomer(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    StartTravelEntity res = new Gson().fromJson(response.body(), StartTravelEntity.class);
                    Log.v("system---开始行程返回信息--->", response.body());
                    if ("0".equals(res.getCode())) {
                        mBasetravelrecord = res.getPk_basetravelrecord();
                        showToast(res.getMsg());
                        mTts.startSpeaking(res.getMsg() , mTtsListener);
                        Log.v("system-------->", "司机确认用户上车,等待用户确认上车,用户确认上车后,司机就能开始行程");
                        //成功之后显示去接乘车的界面
                        mTitleBar.setText("乘客已经上车");
                    } else {
                        showToast(res.getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("请求失败！！");
                hideLoading();
            }
        });
    }

    /**
     * 开始行程
     *
     * @param pk_basetravelrecord
     */
    private void requestStartTravel(String pk_basetravelrecord) {
        Map<String, Object> param = new HashMap<>();
        param.put("pk_basetravelrecord", pk_basetravelrecord);
        showLoading();
        mDriverCarAPI.startTravel(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    StartTravelEntity res = new Gson().fromJson(response.body(), StartTravelEntity.class);
                    Log.v("system---开始行程返回信息--->", response.body());
                    if ("0".equals(res.getCode())) {
                        mStartTime = System.currentTimeMillis();
                        mBasetravelrecord = res.getPk_basetravelrecord();
                        showToast(res.getMsg());
                        mTts.startSpeaking(res.getMsg() , mTtsListener);
                        if (mOrderPickFragment != null) {
                            mOrderPickFragment.setButtonText();
                        }
                        Log.v("system-------->", res.getMsg());
                        //成功之后显示去接乘车的界面
                        mTitleBar.setText("开始行程");
                    } else {
                        showToast(res.getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("请求失败！！");
                hideLoading();
            }
        });
    }


    /**
     * 结束行程
     *
     * @param pk_basetravelrecord 订单号
     * @param toaddress           目的地
     * @param tolatitude
     * @param tolongitude
     * @param traveldistance      实际距离
     * @param travelminutes       实际行车时间
     */
    private void requestFinishTravel(String pk_basetravelrecord, String toaddress, double tolatitude, double tolongitude, double traveldistance, String travelminutes) {
        Map<String, Object> param = new HashMap<>();
        param.put("pk_basetravelrecord", pk_basetravelrecord);
        param.put("toaddress", toaddress);
        param.put("tolatitude", tolatitude);
        param.put("tolongitude", tolongitude);
        param.put("traveldistance", traveldistance);
        param.put("travelminutes", travelminutes);
        Log.v("system----record----->", pk_basetravelrecord);
        Log.v("system----toaddress->", toaddress);
        Log.v("system----tolatitude->", tolatitude + "");
        Log.v("system----tolongitude->", tolongitude + "");
        Log.v("system----distance->", traveldistance + "");
        Log.v("system----minutes->", travelminutes + "");
        showLoading();
        mDriverCarAPI.finishTravel(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        showToast(res.getMsg());
                        mTts.startSpeaking(res.getMsg() , mTtsListener);
                        Log.v("system---结束行程后的信息---->", response.body());
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        confirmBillFragment = new ConfirmBillFragment();
                        transaction.replace(R.id.main_container, confirmBillFragment);
                        transaction.commit();
                        mTitleBar.setText("行程结束");
                    } else {
                        showToast(res.getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("结束行程失败");
                hideLoading();
            }
        });
    }


    /**
     * 其他费用
     *
     * @param pk_userorder
     * @param factmoney
     * @param highspeedfee
     * @param otherfee
     * @param roadbridgefee
     * @param parkingfee
     */
    private void requestOtherexpenses(String pk_userorder, String factmoney, String highspeedfee, String otherfee, String roadbridgefee, String parkingfee) {
        Map<String, Object> param = new HashMap<>();
        param.put("pk_userorder", pk_userorder);
        param.put("factmoney", factmoney);
        param.put("highspeedfee", highspeedfee);
        param.put("otherfee", otherfee);
        param.put("roadbridgefee", roadbridgefee);
        param.put("parkingfee", parkingfee);
        Log.v("system--pk_userorder->", pk_userorder);
        showLoading();
        mDriverCarAPI.updateOrderForFinishTravel(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    Log.v("system---所有费用--->", response.body());
                    try {
                        BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                        if ("0".equals(res.getCode())) {
                            showToast(res.getMsg());
                            Log.v("system---->", res.getMsg());
                            if (confirmBillFragment != null) {
                                confirmBillFragment.hide();
                            }
                            mTts.startSpeaking("请稍后,用户正在支付" , mTtsListener);
                        } else {
                            showToast(res.getMsg());
                        }
                    } catch (Exception e) {
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("请求失败！！");
                hideLoading();
            }
        });
    }

    /**
     * 生成交易记录
     * @param pk_userorder
     * @param pk_user
     * @param income
     * @param pay
     * @param type
     * @param tradetype
     */
    private void requestRradeRecord(String pk_userorder, String pk_user, double income, int pay, int type, int tradetype) {
        Map<String, Object> param = new HashMap<>();
        param.put("pk_userorder", pk_userorder);
        param.put("pk_user", pk_user);
        param.put("income", income);
        param.put("pay", pay);
        param.put("type", type);
        param.put("fromtype", "1");
        param.put("mobile", SPUtils.getParam(this, SPConstants.DRIVER_MOBILE, ""));
        param.put("tradetype", tradetype);
        showLoading();
        mDriverCarAPI.generateTradeRecord(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    Log.v("system---所有费用--->", response.body());
                    try {
                        BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                        if ("0".equals(res.getCode())) {
                            showToast(res.getMsg());
                            Log.v("system---->", res.getMsg());
                        } else {
                            showToast(res.getMsg());
                        }
                    } catch (Exception e) {
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("生成交易记录失败！！");
                hideLoading();
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
            mPositionMark.setPositionByPixels(mMapView.getWidth() / 2,
                    mMapView.getHeight() / 2);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mMapView.onDestroy();

    }


    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }


}
