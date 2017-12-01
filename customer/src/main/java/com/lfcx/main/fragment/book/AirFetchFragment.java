package com.lfcx.main.fragment.book;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.common.utils.ToastUtils;
import com.lfcx.common.widget.LoadingDialog;
import com.lfcx.main.R;
import com.lfcx.main.R2;
import com.lfcx.main.activity.CallCarSucessActivity;
import com.lfcx.main.activity.DestinationActivity;
import com.lfcx.main.consts.SPConstants;
import com.lfcx.main.fragment.BaseFragment;
import com.lfcx.main.maphelper.PositionEntity;
import com.lfcx.main.maphelper.RouteTask;
import com.lfcx.main.net.api.CarAPI;
import com.lfcx.main.net.result.CallCarResult;
import com.lfcx.main.util.Distance;
import com.lfcx.main.util.EdtUtil;
import com.lfcx.main.util.LocationUtils;
import com.lfcx.main.util.TimeSelectUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amap.api.navi.AmapNaviPage.TAG;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   : 接机
 */
public class AirFetchFragment extends BaseFragment implements RouteTask.OnRouteCalculateListener {

    //***车型选择
    @BindView(R2.id.ll_one_container)
    LinearLayout llOne;
    @BindView(R2.id.ll_two_container)
    LinearLayout llTwo;
    @BindView(R2.id.ll_three_container)
    LinearLayout llThree;
    @BindView(R2.id.c_guss_cost)
    TextView tvGussCost;
    //***
    @BindView(R2.id.et_time)
    EditText etTime;
    @BindView(R2.id.et_start_address)
    EditText etStartAddress;
    @BindView(R2.id.et_end_address)
    EditText etEndAddress;
    @BindView(R2.id.et_user)
    EditText etUser;
    @BindView(R2.id.et_air_code)
    EditText etAirCode;
    @BindView(R2.id.btn_register)
    Button mButton;

    /**
     * 1 为点击开始位置  2为点击结束位置
     */
    private int clickType = -1;
    /**
     * 如果已经重新选择了开始位置，则onResume里则不再修改
     */
    private boolean isSelectStartAdress = false;
    private Unbinder unbinder;
    private LoadingDialog mLoadDialog;
    /**
     * 车类型
     */
    private int styletype = 0;
    private CarAPI carAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cv = inflater.inflate(R.layout.c_fragment_air_fetch, container, false);
        unbinder = ButterKnife.bind(this, cv);
        RouteTask.getInstance(getActivity().getApplicationContext())
                .addRouteCalculateListener(this);
        return cv;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        carAPI = APIFactory.create(CarAPI.class);
        String mobile = (String) SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE, "");
        if (!TextUtils.isEmpty(mobile)) {
            etUser.setText(mobile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PositionEntity location = LocationUtils.getLocation();
        if (null != location && !isSelectStartAdress) {
            etStartAddress.setText(location.getAddress());
        }
    }

    @OnClick(R2.id.et_end_address)
    public void onClickEndAdress(View v) {
        clickType = 222;
        Intent destinationIntent = new Intent(getActivity(), DestinationActivity.class);
        startActivity(destinationIntent);
    }


    @OnClick(R2.id.et_start_address)
    public void onClickStartAdress(View v) {
        clickType = 221;
        Intent destinationIntent = new Intent(getActivity(), DestinationActivity.class);
        startActivity(destinationIntent);
    }

    @OnClick(R2.id.et_time)
    public void onClickSelectTime(View v) {
        TimeSelectUtils timeSelectUtils = new TimeSelectUtils(getActivity(), null, null, null, new TimeSelectUtils.GetSubmitTime() {
            @Override
            public void selectTime(String startDate, String entDate) {
                etTime.setText(startDate);
            }
        });
        //显示我们的时间选择器
        timeSelectUtils.dateTimePicKDialog();
    }

    @OnClick(R2.id.btn_register)
    public void onClickConfirm(View view) {
        //确认用车
        try {
            if (EdtUtil.isEdtEmpty(etAirCode)) {
                Toast.makeText(getActivity(), "请输入航班号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (EdtUtil.isEdtEmpty(etTime)) {
                Toast.makeText(getActivity(), "请选择时间", Toast.LENGTH_SHORT).show();
                return;
            }
            if (EdtUtil.isEdtEmpty(etStartAddress)) {
                Toast.makeText(getActivity(), "定位失败,请选择开始位置", Toast.LENGTH_SHORT).show();
                return;
            }

            if (EdtUtil.isEdtEmpty(etEndAddress)) {
                Toast.makeText(getActivity(), "请选择目的地", Toast.LENGTH_SHORT).show();
                return;
            }
            bookCar(EdtUtil.getEdtText(etStartAddress), EdtUtil.getEdtText(etEndAddress));
        } catch (Exception e) {
            Toast.makeText(getContext(), "请补全用车信息", Toast.LENGTH_SHORT).show();
        }
    }

    //**车型选择处理
    @OnClick(R2.id.ll_one_container)
    public void onClickOne(View v) {
        styletype = 0;
        llThree.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llTwo.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llOne.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        shouldGetCost();
    }

    @OnClick(R2.id.ll_two_container)
    public void onClickTwo(View v) {
        styletype = 1;
        llThree.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llOne.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llTwo.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        shouldGetCost();
    }

    @OnClick(R2.id.ll_three_container)
    public void onClickThree(View v) {
        styletype = 2;
        llTwo.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llOne.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llThree.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        shouldGetCost();
    }

    /**
     * 是否需要去刷新估算价格接口
     */
    private void shouldGetCost(){
        try {
            if (EdtUtil.isEdtEmpty(etTime)) {
                Toast.makeText(getContext(), "请选择时间", Toast.LENGTH_SHORT).show();
                return;
            }
            if (EdtUtil.isEdtEmpty(etStartAddress)) {
                Toast.makeText(getContext(), "定位失败,请选择开始位置", Toast.LENGTH_SHORT).show();
                return;
            }

            if (EdtUtil.isEdtEmpty(etEndAddress)) {
                Toast.makeText(getContext(), "请选择目的地", Toast.LENGTH_SHORT).show();
                return;
            }
            double distance = Distance.getDistance(LocationUtils.getLocation().longitude, LocationUtils.getLocation().latitue, RouteTask
                    .getInstance(getActivity().getApplicationContext()).getEndPoint().longitude, RouteTask
                    .getInstance(getActivity().getApplicationContext()).getEndPoint().latitue) / 1000;
            getCost(distance);
        }catch (Exception e){
        }
    }
    /**
     * 获取预估费用
     */
    private void getCost(double distance) {
        showLoading();
        Map<String, Object> param = new HashMap<>();
        param.put("fromaddress", LocationUtils.getLocation().address);
        param.put("toaddress", RouteTask
                .getInstance(getActivity()).getEndPoint().address);
        param.put("fromlongitude", LocationUtils.getLocation().longitude);
        param.put("fromlatitude", LocationUtils.getLocation().latitue);
        param.put("tolongitude", RouteTask
                .getInstance(getActivity()).getEndPoint().longitude);
        param.put("tolatitude", RouteTask
                .getInstance(getActivity()).getEndPoint().latitue);
        param.put("styletype", styletype);
        param.put("datetime", etTime.getText().toString());
        param.put("distance",distance);
        carAPI.getNowCost(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String cost = response.body();
                    tvGussCost.setText(String.format("预估费用%.2f元", Float.valueOf(cost)));
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
     * 专车接机下单
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
        param.put("ordertype", 4);//专车-送机
        param.put("aircode", EdtUtil.getEdtText(etAirCode));//专车-送机
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
//                        pk_userOder = result.getPk_userOder();
                        Intent intent = new Intent(getActivity(), CallCarSucessActivity.class);
                        startActivity(intent);
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
    public void onRouteCalculate(float cost, float distance, int duration) {
        try {
            if (clickType == 222) {
                etEndAddress.setText(RouteTask
                        .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
            } else if (clickType == 221) {
                etStartAddress.setText(RouteTask
                        .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
                isSelectStartAdress = true;
            }
            shouldGetCost();
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
