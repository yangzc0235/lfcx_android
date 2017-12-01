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
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.main.R;
import com.lfcx.main.consts.SPConstants;
import com.lfcx.main.event.EventUtil;
import com.lfcx.main.event.ReceiptResultEvent;
import com.lfcx.main.fragment.SharingCarFragment;
import com.lfcx.main.maphelper.LocationTask;
import com.lfcx.main.maphelper.OnLocationGetListener;
import com.lfcx.main.maphelper.PositionEntity;
import com.lfcx.main.maphelper.RegeocodeTask;
import com.lfcx.main.maphelper.RouteTask;
import com.lfcx.main.maphelper.Utils;
import com.lfcx.main.net.api.UserAPI;
import com.lfcx.main.net.result.DriverOrderEntity;
import com.lfcx.main.net.result.IncomeEntity;
import com.lfcx.main.net.result.PayEntity;
import com.lfcx.main.net.result.StartTravelEntity;
import com.lfcx.main.util.LocationUtils;
import com.lfcx.main.widget.dialog.PayDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lfcx.common.utils.SPUtils.getParam;

public class CallCarSucessActivity extends BaseActivity implements AMap.OnCameraChangeListener,
        AMap.OnMapLoadedListener, OnLocationGetListener, View.OnClickListener,
        RouteTask.OnRouteCalculateListener, SharingCarFragment.CallBackValue {

    public static final String TAG = CallCarSucessActivity.class.getSimpleName();
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
    private ImageView mImgvDriverMobile;
    private DriverOrderEntity driverOrderEntity;
    private PayDialog payDialog;
    private int strategy=0;

    @Override
    public void SendMessageValue(String strValue) {
        mPk_userOder = strValue;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_call_car_sucess);
        initMap(savedInstanceState);
        userAPI = APIFactory.create(UserAPI.class);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
        mLlWait = (RelativeLayout) findViewById(R.id.ll_wait);
        mFlDriverInfo = (FrameLayout) findViewById(R.id.fl_driver_info);
        mActivityMain = (RelativeLayout) findViewById(R.id.activity_main);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvUser = (ImageView) findViewById(R.id.iv_user);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        mLlOne = (LinearLayout) findViewById(R.id.ll_one);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mCardId = (TextView) findViewById(R.id.card_id);
        mCardName = (TextView) findViewById(R.id.card_name);
        mTvCancelOrder = (TextView) findViewById(R.id.tv_cancel_order);
        mTvCallPhone = (TextView) findViewById(R.id.tv_call_phone);
        mTvComplain = (TextView) findViewById(R.id.tv_complain);
        mTvCancelOrderFirst = (TextView) findViewById(R.id.tv_cancel_order_first);
        mImgvDriverMobile = (ImageView) findViewById(R.id.imgv_driver_mobile);
        mLocationTask = LocationTask.getInstance(getApplicationContext());
        mLocationTask.setOnLocationGetListener(this);
        mFlDriverInfo.setVisibility(View.GONE);
        mLlWait.setVisibility(View.VISIBLE);
        mIvBack.setVisibility(View.GONE);
        mIvUser.setVisibility(View.VISIBLE);
        mTvCancelOrderFirst.setOnClickListener(this);
        mTvCancelOrder.setOnClickListener(this);
        mTvCallPhone.setOnClickListener(this);
        mTvComplain.setOnClickListener(this);
        mImgvDriverMobile.setOnClickListener(this);
        mIvUser.setOnClickListener(this);
        mRegeocodeTask = new RegeocodeTask(getApplicationContext());
        RouteTask.getInstance(getApplicationContext())
                .addRouteCalculateListener(this);
        SPUtils.setParam(this, SPConstants.KEY_SUCCESS_CAR, "true");
        String carCode = (String) SPUtils.getParam(this, SPConstants.KEY_CAR_CODE, "");
        String carType = (String) SPUtils.getParam(this, SPConstants.KEY_CAR_TYPE, "");
        String carDriverName = (String) SPUtils.getParam(this, SPConstants.KEY_DRIVER_NAME, "");
        if (null != carCode && !"".equals(carCode)) {
            mFlDriverInfo.setVisibility(View.VISIBLE);
            mTvCancelOrder.setVisibility(View.GONE);
            mLlWait.setVisibility(View.GONE);
            mCardId.setText("车牌号:  " + carCode);
            mCardName.setText("车型:  " + carType);
            mTvName.setText("司机:  " + carDriverName);

        }
    }

    /**
     * 初始化导航
     *
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        options.setCarBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.move_car2));
//        options.setFourCornersBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.lane00));
//        options.setStartPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.navi_start));
//        options.setWayPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.navi_way));
//        options.setEndPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.navi_end));
        mAMapNaviView.setViewOptions(options);

    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();


    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);

    }

    @Override
    public void onNaviViewLoaded() {
        super.onNaviViewLoaded();
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

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_image:
                mLocationTask.startSingleLocate();
                break;
            case R.id.destination_text:
                //
                Intent destinationIntent = new Intent(this,
                        DestinationActivity.class);
                startActivity(destinationIntent);
                break;

            case R.id.tv_cancel_order_first:
                //取消订单
                SharingCarFragment sharingCarFragment = new SharingCarFragment();
                sharingCarFragment.setCallBackValue(new SharingCarFragment.CallBackValue() {
                    @Override
                    public void SendMessageValue(String strValue) {
                        cancelOrderInfo(strValue);
                    }
                });

                break;
            case R.id.imgv_driver_mobile:
                //给司机打电话
                String diverMobile = (String) SPUtils.getParam(CallCarSucessActivity.this, SPConstants.KEY_DRIVER_MOBILE, "");
                try {
                    if (diverMobile == null || "".equals(diverMobile)) {
                        callPhone(driverOrderEntity.getMobile());
                    } else {
                        callPhone(diverMobile);
                    }
                } catch (Exception e) {

                }
                break;
            case R.id.tv_call_phone:
                //给客服打电话
                callPhone("10086");
                break;
            case R.id.tv_complain:
                //投诉
                callPhone("10086");
                break;
            case R.id.tv_cancel_order:
                //司机接单取消订单
                String carUserOrder = (String) SPUtils.getParam(CallCarSucessActivity.this, SPConstants.KEY_USER_ORDER, "");
                try {
                    if (carUserOrder == null || "".equals(carUserOrder)) {
                        cancelOrderInfo(driverOrderEntity.getPk_userorder());
                    } else {
                        cancelOrderInfo(carUserOrder);
                    }
                } catch (Exception e) {
                    Toast.makeText(CallCarSucessActivity.this, "订单取消异常", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_user:
                //个人中心
                goToActivity(CustomerUserCenterActivity.class);
                break;
        }
    }


    //在ui线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReceiptResultEvent event) {

        switch (event.getKey()) {
            case "receiveDriverInfo": {
                //接收到司机信息
                mFlDriverInfo.setVisibility(View.VISIBLE);
                mLlWait.setVisibility(View.GONE);
                String value = event.getValue();
                Log.v("system---value--->", value);
                Gson gson = new Gson();
                driverOrderEntity = gson.fromJson(value, DriverOrderEntity.class);
                mStartLatlng.setLongitude(driverOrderEntity.getLongitude());
                mStartLatlng.setLatitude(driverOrderEntity.getLatitude());
                mEndLatlng.setLongitude(LocationUtils.getLocation().getLongitude());
                mEndLatlng.setLatitude(LocationUtils.getLocation().getLatitue());
                sList.add(mStartLatlng);
                eList.add(mEndLatlng);
                try {
                    //再次强调，最后一个参数为true时代表多路径，否则代表单路径
                    strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mAMapNavi.setCarNumber("宁", driverOrderEntity.getCarCode());
                mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);



                SPUtils.setParam(this, SPConstants.KEY_CAR_CODE, driverOrderEntity.getCarCode());
                SPUtils.setParam(this, SPConstants.KEY_CAR_TYPE, driverOrderEntity.getType());
                SPUtils.setParam(this, SPConstants.KEY_DRIVER_NAME, driverOrderEntity.getDrivername());
                SPUtils.setParam(this, SPConstants.KEY_USER_ORDER, driverOrderEntity.getPk_userorder());
                SPUtils.setParam(this, SPConstants.KEY_DRIVER_MOBILE, driverOrderEntity.getMobile());
                mCardId.setText("车牌号:  " + driverOrderEntity.getCarCode());
                mCardName.setText("车型:  " + driverOrderEntity.getType());
                mTvName.setText("司机:  " + driverOrderEntity.getDrivername());
                break;
            }
            case "startConfirm": {
                //确认上车
                mTvConfirm.setVisibility(View.VISIBLE);
                mTvCancelOrder.setVisibility(View.GONE);
                String message = event.getValue();
                final StartTravelEntity startTravelEntity = new Gson().fromJson(message, StartTravelEntity.class);
                mTvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mStartLatlng.setLongitude(LocationUtils.getLocation().getLongitude());
                            mStartLatlng.setLatitude(LocationUtils.getLocation().getLatitue());
                            mEndLatlng.setLongitude(RouteTask
                                    .getInstance(getApplicationContext()).getEndPoint().longitude);
                            mEndLatlng.setLatitude(RouteTask
                                    .getInstance(getApplicationContext()).getEndPoint().latitue);
                            sList.add(mStartLatlng);
                            eList.add(mEndLatlng);
                            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
                            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mAMapNavi.setCarNumber("宁", driverOrderEntity.getCarCode());
                        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
                        confirmStartTravel(startTravelEntity.getPk_basetravelrecord());
                    }
                });

                break;
            }
            case "receiveFinshOrder": {
                //开始支付
                String message = event.getValue();
                Log.v("system---value-->", message);
                Gson gson = new Gson();
                PayEntity payEntity = gson.fromJson(message, PayEntity.class);
                payDialog = new PayDialog(this, R.style.customDialog, payEntity);
                payDialog.show();
                break;
            }
            case "receivePayFinish":
                //支付完成
                // payDialog.dismiss();

                IncomeEntity incomeEntity = new Gson().fromJson(event.getValue(), IncomeEntity.class);
                //requestRradeRecord(incomeEntity.getPk_userorder(), incomeEntity.getPk_user(), incomeEntity.getIncome(), incomeEntity.getPay(), incomeEntity.getType(), incomeEntity.getTradetype());
                break;
        }

    }

    //在ui线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventUtil event) {
        try {
            switch (event.getMsg()) {
                case "close_carsuccess":
                    //goToActivity(CustomerMainActivity.class);
                    finish();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "发送消息异常", Toast.LENGTH_SHORT).show();
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
        Log.v("system---order----->", pk_userorder + "");
        userAPI.cancelOrderInfo(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.v("system-----取消订单信息--->", response.body() + "");
                hideLoading();
                try {
                    Gson gson = new Gson();
                    BaseResultBean resultBean = gson.fromJson(response.body(), BaseResultBean.class);
                    if (resultBean.getCode().equals("0")) {
                        SPUtils.setParam(CallCarSucessActivity.this, SPConstants.KEY_SUCCESS_CAR, "");
                        SPUtils.setParam(CallCarSucessActivity.this, SPConstants.KEY_CAR_CODE, "");
                        SPUtils.setParam(CallCarSucessActivity.this, SPConstants.KEY_CAR_TYPE, "");
                        SPUtils.setParam(CallCarSucessActivity.this, SPConstants.KEY_DRIVER_NAME, "");
                        SPUtils.setParam(CallCarSucessActivity.this, SPConstants.KEY_USER_ORDER, "");
                        SPUtils.setParam(CallCarSucessActivity.this, SPConstants.KEY_DRIVER_MOBILE, "");
                        Toast.makeText(CallCarSucessActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CallCarSucessActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(CallCarSucessActivity.this, "订单取消异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
                showToast("订单取消失败!");
                SPUtils.setParam(CallCarSucessActivity.this, SPConstants.KEY_SUCCESS_CAR, "");
            }
        });

    }

    /**
     * 生成交易记录
     *
     * @param pk_userorder
     * @param pk_user
     * @param income
     * @param pay
     * @param type
     * @param tradetype
     */
    private void requestRradeRecord(String pk_userorder, String pk_user, double income, double pay, int type, int tradetype) {
        Map<String, Object> param = new HashMap<>();
        param.put("pk_userorder", pk_userorder);
        param.put("pk_user", pk_user);
        param.put("income", income);
        param.put("pay", pay);
        param.put("type", type);
        param.put("fromtype", "0");
        param.put("mobile", getParam(this, SPConstants.KEY_CUSTOMER_MOBILE, ""));
        param.put("tradetype", tradetype);
        userAPI.generateTradeRecord(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    Log.v("system-用户生成交易记录--->", response.body());
                    try {
                        BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                        if ("0".equals(res.getCode())) {
                            //Toast.makeText(CallCarSucessActivity.this, res.getMsg(), Toast.LENGTH_SHORT).show();
                            Log.v("system---交易记录------>", res.getMsg());

                        } else {
                            Toast.makeText(CallCarSucessActivity.this, res.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(CallCarSucessActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Utils.removeMarkers();
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
        mLocationTask.startSingleLocate();
    }


    @Override
    public void onLocationGet(PositionEntity entity) {
        // todo 这里在网络定位时可以减少一个逆地理编码
        RouteTask.getInstance(getApplicationContext()).setStartPoint(entity);

        mStartPosition = new LatLng(entity.latitue, entity.longitude);


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
