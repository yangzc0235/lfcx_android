package com.lfcx.main.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.MapMarkerUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.common.utils.ToastUtils;
import com.lfcx.main.R;
import com.lfcx.main.R2;
import com.lfcx.main.activity.CallCarSucessActivity;
import com.lfcx.main.activity.CustomerBookActivity;
import com.lfcx.main.activity.CustomerLoginActivity;
import com.lfcx.main.activity.DestinationActivity;
import com.lfcx.main.consts.Constants;
import com.lfcx.main.consts.SPConstants;
import com.lfcx.main.maphelper.LocationTask;
import com.lfcx.main.maphelper.OnLocationGetListener;
import com.lfcx.main.maphelper.PositionEntity;
import com.lfcx.main.maphelper.RegeocodeTask;
import com.lfcx.main.maphelper.RouteTask;
import com.lfcx.main.maphelper.Utils;
import com.lfcx.main.net.api.CarAPI;
import com.lfcx.main.net.result.CallCarResult;
import com.lfcx.main.util.EdtUtil;
import com.lfcx.main.util.LocationUtils;
import com.lfcx.main.util.Md5Util;
import com.lfcx.main.util.UserUtil;
import com.lfcx.main.widget.dialog.BottomDialog;
import com.lfcx.main.widget.pop.CustomerCarPop;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   : 乘客打车首页面，
 */
public class CustomerIndexFragment extends BaseFragment implements AMap.OnCameraChangeListener,
        AMap.OnMapLoadedListener, OnLocationGetListener, RouteTask.OnRouteCalculateListener {

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
//
//    @BindView(R2.id.location_image)
//    ImageView mLocationImage;

    private boolean mIsFirst = true;

    private boolean mIsRouteSuccess = false;
    private Unbinder unbinder;

    /**
     * 车型选择
     */
    private CustomerCarPop carPop;

    private GeocodeSearch geocoderSearch;

    /**
     * 默认为舒适型
     */
    private int styletype = 0;
    private BottomDialog mDialogSelect;
    private ImageView mLocationImage;
    private ImageView mImageView1;
    private EditText mAddressText;
    private LinearLayout mDestinationContainer;
    private EditText mDestinationText;
    private TextView mRoutecostText;
    private Button mCBtnConfirm;
    private String destinationAddress;
    private ImageView mImgvClose;
    private FrameLayout mFragmentAcontainer;
    private LinearLayout mLlBottom;


    /**
     * 如果已经重新选择了开始位置，则onResume里则不再修改
     */
    private boolean isSelectStartAdress = false;
    private int clickType = -1;
    private CarAPI carAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.c_fragment_customer_index, container, false);
        mFragmentAcontainer = (FrameLayout) contentView.findViewById(R.id.fragment_acontainer);
        mLlBottom = (LinearLayout) contentView.findViewById(R.id.ll_bottom);
        mLlBottom.setVisibility(View.VISIBLE);
        unbinder = ButterKnife.bind(this, contentView);
        carAPI = APIFactory.create(CarAPI.class);
        init(contentView, savedInstanceState);
        initPermisson();
        mLocationTask = LocationTask.getInstance(getActivity().getApplicationContext());
        mLocationTask.setOnLocationGetListener(this);
        mRegeocodeTask = new RegeocodeTask(getActivity().getApplicationContext());
        RouteTask.getInstance(getActivity().getApplicationContext())
                .addRouteCalculateListener(this);
        return contentView;
    }

    private void initPermisson() {
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
            if (requestCode == 200) {
                // TODO ...
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                ToastUtils.shortToast(getActivity(), "已拒绝授权定位，暂不能获取您当前位置");
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
               try {
                   setMarker(latLng.latitude, latLng.longitude);
                   LatLonPoint point = new LatLonPoint(latLng.latitude, latLng.longitude);
                   RegeocodeQuery query = new RegeocodeQuery(point, 100, GeocodeSearch.AMAP);
                   LocationUtils.getLocation().latitue = latLng.latitude;
                   LocationUtils.getLocation().longitude = latLng.longitude;
                   geocoderSearch.getFromLocationAsyn(query);
               }catch (Exception e){

               }
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
        try {
            mRegeocodeTask.setOnLocationGetListener(this);
            mRegeocodeTask.search(mStartPosition.latitude, mStartPosition.longitude);
            if (mIsFirst) {
                Utils.addEmulateData(mAmap, mStartPosition);
                carAPI= APIFactory.create(CarAPI.class);
                if (mPositionMark != null) {
                    mPositionMark.setToTop();
                }
                mIsFirst = false;
            }
        } catch (Exception e) {

        }
    }


    @OnClick({R2.id.location_image, R2.id.btn_now, R2.id.btn_after, R2.id.iv_car})
    public void onClick(View v) {

        if (v.getId() == R.id.location_image)
            mLocationTask.startSingleLocate();
        else if (v.getId() == R.id.destination_text) {
            if (!UserUtil.isLogin(getActivity().getApplicationContext())) {
                Intent destinationIntent = new Intent(getActivity(),
                        CustomerLoginActivity.class);
                startActivity(destinationIntent);
            } else {
                Intent destinationIntent = new Intent(getActivity(),
                        DestinationActivity.class);
                startActivity(destinationIntent);
            }

        } else if (v.getId() == R.id.btn_now) {
            if (!UserUtil.isLogin(getActivity().getApplicationContext())) {
                Intent destinationIntent = new Intent(getActivity(),
                        CustomerLoginActivity.class);
                startActivity(destinationIntent);
            } else {
                //马上用车,弹出底部dialog
                showButtomSelectDialog();
            }

        } else if (v.getId() == R.id.btn_after) {

            //预约车
            if (!UserUtil.isLogin(getActivity().getApplicationContext())) {
                Intent destinationIntent = new Intent(getActivity(),
                        CustomerLoginActivity.class);
                startActivity(destinationIntent);
            } else {
                Intent allIntent = new Intent(getActivity(),
                        CustomerBookActivity.class);
                startActivity(allIntent);
            }

        } else if (v.getId() == R.id.iv_car) {
            int[] location = new int[2];
            int popHeight = 0;
            ivCar.getLocationOnScreen(location);
            if (null == carPop) {
                carPop = new CustomerCarPop(getActivity());
                carPop.setCarSelectListener(new CustomerCarPop.ICarSelectListener() {
                    @Override
                    public void onSelected(View view, int position) {
                        switch (position) {
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
                carPop.show(ivCar, 0, 0 - (ivCar.getMeasuredHeight() + popHeight), Gravity.NO_GRAVITY);
            } else {
                carPop.dissmiss();
            }
        }
    }


    /**
     * 选择汽车类型
     */
    private void showButtomSelectDialog() {
        mDialogSelect = BottomDialog.create(getActivity().getSupportFragmentManager()).setCancelOutside(false);
        mDialogSelect.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                mImgvClose = (ImageView)  v.findViewById(R.id.imgv_close);
                mLocationImage = (ImageView) v.findViewById(R.id.location_image);
                mImageView1 = (ImageView) v.findViewById(R.id.imageView1);
                mAddressText = (EditText) v.findViewById(R.id.address_text);
                mDestinationContainer = (LinearLayout) v.findViewById(R.id.destination_container);
                mDestinationText = (EditText) v.findViewById(R.id.destination_text);
                mRoutecostText = (TextView) v.findViewById(R.id.routecost_text);
                mCBtnConfirm = (Button) v.findViewById(R.id.c_btn_confirm);
                mRoutecostText.setVisibility(View.GONE);
               try {
                   mAddressText.setText(LocationUtils.getLocation().address + "");
               }catch (Exception e){

               }
                mCBtnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //开始下单
                        mDialogSelect.dismiss();
                        try {
                            mLocationTask.startLocate();
                            if (EdtUtil.isEdtEmpty(mAddressText)) {
                                Toast.makeText(getActivity(), "获取您的位置失败,请输入", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (EdtUtil.isEdtEmpty(mDestinationText)) {
                                Toast.makeText(getActivity(), "请输入终点位置", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Log.v("system--longitude----->", LocationUtils.getLocation().longitude + "");
                            Log.v("system--latitue----->", LocationUtils.getLocation().latitue + "");
                            Log.v("system--tolongitude--->", RouteTask
                                    .getInstance(getActivity()).getEndPoint().longitude + "");
                            Log.v("system--tolongitude--->", RouteTask
                                    .getInstance(getActivity()).getEndPoint().latitue + "");
                            bookCar(EdtUtil.getEdtText(mAddressText), EdtUtil.getEdtText(mDestinationText));
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "正在获取您的位置信息,请稍等", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mImgvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogSelect.dismiss();
                    }
                });
                mDestinationText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //选择结束位置
                        clickType = 2;
                        Intent intent = new Intent(getActivity(), DestinationActivity.class);
                        startActivity(intent);
                    }
                });

                mAddressText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //选择开始位置
                        clickType = 1;
                        Intent intent = new Intent(getActivity(), DestinationActivity.class);
                        startActivity(intent);
                    }
                });

            }
        })
                .setLayoutRes(R.layout.customer_order_build_fragment)
                .setDimAmount(0.6f)
                .setTag("BottomDialog")
                .show();
    }


    /**
     * 选择之后设置位置
     */
    public void onRouteFinish() {
        if (clickType == 2) {
            mDestinationText.setText(RouteTask
                    .getInstance(getContext().getApplicationContext()).getEndPoint().address);
            getCost();
        } else if (clickType == 1) {
            mAddressText.setText(RouteTask
                    .getInstance(getContext().getApplicationContext()).getEndPoint().address);
            isSelectStartAdress = true;
        }

        Log.v("system---start------>", LocationUtils.getLocation().address);
        Log.v("system----end----->", RouteTask.getInstance(getActivity()).getEndPoint().address);

    }

    /**
     * 获取预估费用
     */
    private void getCost() {
        mRoutecostText.setVisibility(View.VISIBLE);
        showLoading();
        Map<String, Object> param = new HashMap<>();
        param.put("fromaddress", LocationUtils.getLocation().address);
        param.put("toaddress", RouteTask
                .getInstance(getActivity()).getEndPoint().address);
        Log.v("system---start------>", LocationUtils.getLocation().address);
        Log.v("system----end----->", RouteTask.getInstance(getActivity()).getEndPoint().address);
        param.put("fromlongitude", LocationUtils.getLocation().longitude);
        param.put("fromlatitude", LocationUtils.getLocation().latitue);
        param.put("tolongitude", RouteTask
                .getInstance(getActivity()).getEndPoint().longitude);
        param.put("tolatitude", RouteTask
                .getInstance(getActivity()).getEndPoint().latitue);
        param.put("styletype", styletype);
        param.put("datetime", Md5Util.getTimestamp());

        carAPI.getNowCost(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String cost = response.body();
                    mRoutecostText.setText(String.format("预估费用%.2f元", Float.valueOf(cost)));
                } catch (Exception e) {
                    LogUtils.e(TAG, e.getMessage());
                }
                hideLoading();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
            }
        });
    }

    /**
     * 专车下单
     *
     * @param fromAddress
     * @param toAddress
     */
    public void bookCar(String fromAddress, String toAddress) {
        showLoading();
        Map<String, Object> param = new HashMap<>();
        param.put("pk_user", SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_PK_USER, ""));
        param.put("fromaddress", fromAddress);
        param.put("toaddress", toAddress);
        param.put("fromlongitude", LocationUtils.getLocation().longitude);
        param.put("fromlatitude", LocationUtils.getLocation().latitue);
        param.put("tolongitude", RouteTask
                .getInstance(getActivity().getApplicationContext()).getEndPoint().longitude);
        param.put("tolatitude", RouteTask
                .getInstance(getActivity().getApplicationContext()).getEndPoint().latitue);
        param.put("title", "用户" + SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE, "") + "预约您");
        param.put("content", "用户" + SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE, "") + "预约您");
        param.put("reservatedate", "");
        param.put("ridertel", SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE, ""));
        param.put("ordertype", 1);//专车
        param.put("status", 0);// 0 待付款 1 订单完成 2 订单取消
        param.put("isprivatecar", 0);//专车
        param.put("carstyletype", styletype);//类型:舒适型
        Log.v("fromlatitude----------", LocationUtils.getLocation().latitue + "");
        Log.v("fromlongitude----------", LocationUtils.getLocation().longitude + "");
        Log.v("tolatitude----------", RouteTask
                .getInstance(getActivity()).getEndPoint().latitue + "");
        Log.v("tolongitude----------", RouteTask
                .getInstance(getActivity()).getEndPoint().longitude + "");
        carAPI.generateOrder(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                Log.v("system---下单信息-->", response.body() + "");
                try {
                    CallCarResult result = new Gson().fromJson(response.body(), CallCarResult.class);

                    //下单成功
                    if ("0".equals(result.getCode())) {
                        Intent intent = new Intent(getActivity(), CallCarSucessActivity.class);
                        startActivity(intent);
//                        mLlBottom.setVisibility(View.GONE);
//                        OrderWaitFragment orderWaitFragment = new OrderWaitFragment();
//                        FragmentManager manager = getChildFragmentManager();
//                        FragmentTransaction transaction = manager.beginTransaction();
//                        transaction.replace(R.id.fragment_acontainer, orderWaitFragment);
//                        transaction.commit();
                        Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        ToastUtils.shortToast(getActivity(), result.getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "叫车失败", Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        });
    }


    @Override
    public void onLocationGet(PositionEntity entity) {
        // todo 这里在网络定位时可以减少一个逆地理编码
        //第一次获得位置
        if (null != mPositionMark && !mPositionMark.isRemoved()) {
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

        if (null != mPositionMark && !mPositionMark.isRemoved()) {
            mPositionMark.remove();
        }
        setMarker(entity.latitue, entity.longitude);
        RouteTask.getInstance(getActivity().getApplicationContext()).setStartPoint(entity);
        RouteTask.getInstance(getActivity().getApplicationContext()).search();

    }

    private void setMarker(double latitude, double longitude) {
        MarkerOptions markerOptions = MapMarkerUtils.instance().getMarkerOptions(new LatLng(latitude, longitude)
                , BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.c_map_location));
        markerOptions.snippet("当前位置");
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
    public void onRouteCalculate(float cost, float distance, int duration) {
        mIsRouteSuccess = true;
        llCarContainer.setOnClickListener(null);
        onRouteFinish();

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        try {
            PositionEntity location = LocationUtils.getLocation();
            if (null != location && !isSelectStartAdress) {
                mAddressText.setText(location.getAddress() + "");
            }
            mLocationTask.startLocate();
        } catch (Exception e) {

        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        try {
            mLocationTask.startLocate();
        }catch (Exception e){

        }
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
        setMarker(0, 0);
        mLocationTask.startSingleLocate();
    }


}
