package com.lfcx.customer.fragment.book;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.common.utils.ToastUtils;
import com.lfcx.common.widget.LoadingDialog;
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.activity.CallCarSucessActivity;
import com.lfcx.customer.activity.DestinationActivity;
import com.lfcx.customer.consts.SPConstants;
import com.lfcx.customer.maphelper.PositionEntity;
import com.lfcx.customer.maphelper.RouteTask;
import com.lfcx.customer.net.api.CarAPI;
import com.lfcx.customer.net.result.CallCarResult;
import com.lfcx.customer.util.LocationUtils;
import com.lfcx.customer.util.TimeSelectUtils;

import java.util.HashMap;
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
 * des   :  预约
 */
public class CustomerBookCarFragment extends Fragment implements  RouteTask.OnRouteCalculateListener{

    public static final String TAG = CustomerBookCarFragment.class.getSimpleName();

    @BindView(R2.id.btn_confirm)
    Button btnConfirm;
    @BindView(R2.id.et_user)
    EditText etUser;
    @BindView(R2.id.et_start_address)
    EditText etStartAddress;
    @BindView(R2.id.et_end_address)
    EditText etEndAddress;
    @BindView(R2.id.et_time)
    EditText etTime;

    //***车型选择
    @BindView(R2.id.ll_one_container)
    LinearLayout llOne;
    @BindView(R2.id.ll_two_container)
    LinearLayout llTwo;
    @BindView(R2.id.ll_three_container)
    LinearLayout llThree;
    /**
     * 大约多少钱
     */
    @BindView(R2.id.c_guss_cost)
    TextView tvGussCost;
    //***

    private CarAPI carAPI;
    private Unbinder mUnBinder;

    private String fromLatitude;
    private String fromLongitude;

    private String toLatitude;
    private String toLongitude;

    /**
     * 1 为点击开始位置  2为点击结束位置
     */
    private int clickType = -1;
    /**
     * 如果已经重新选择了开始位置，则onResume里则不再修改
     */
    private boolean isSelectStartAdress = false;

    private LoadingDialog mLoadDialog;

    /**
     * 车类型
     */
    private int styletype = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  cv = inflater.inflate(R.layout.c_fragment_customer_book_car, container, false);
        mUnBinder = ButterKnife.bind(this,cv);
        RouteTask.getInstance( getActivity().getApplicationContext())
                .addRouteCalculateListener(this);
        return cv;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        carAPI = APIFactory.create(CarAPI.class);
        String mobile = (String)SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE,"");
        if(!TextUtils.isEmpty(mobile)){
            etUser.setText(mobile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PositionEntity location = LocationUtils.getLocation();
        if(null != location && !isSelectStartAdress){
            etStartAddress.setText(location.getAddress());
        }
    }
    //**车型选择处理
    @OnClick(R2.id.ll_one_container)
    public void onClickOne(View v){
        styletype=0;
        llThree.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llTwo.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llOne.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        shouldGetCost();
    }
    @OnClick(R2.id.ll_two_container)
    public void onClickTwo(View v){
        styletype = 1;
        llThree.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llOne.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llTwo.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        shouldGetCost();
    }
    @OnClick(R2.id.ll_three_container)
    public void onClickThree(View v){
        styletype = 2;
        llTwo.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llOne.setBackgroundColor(getActivity().getResources().getColor(R.color.c_gray_bg));
        llThree.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        shouldGetCost();
    }
    //**

    @OnClick(R2.id.btn_confirm)
    public void onClickConfirm(View view) {
        bookCar();
    }

    @OnClick(R2.id.et_end_address)
    public void onClickEndAdress(View v){
        clickType = 2;
        Intent destinationIntent = new Intent(getActivity(), DestinationActivity.class);
        startActivity(destinationIntent);
    }
    @OnClick(R2.id.et_start_address)
    public void onClickStartAdress(View v){
        clickType = 1;
        Intent destinationIntent = new Intent(getActivity(), DestinationActivity.class);
        startActivity(destinationIntent);
    }

    @OnClick(R2.id.et_time)
    public void onClickSelectTime(View v){
        TimeSelectUtils timeSelectUtils = new TimeSelectUtils(getActivity(), null, null, null, new TimeSelectUtils.GetSubmitTime() {
            @Override
            public void selectTime(String startDate, String entDate) {
               etTime.setText(startDate);
            }
        });
        //显示我们的时间选择器
        timeSelectUtils.dateTimePicKDialog();
    }

    private void getCost(){
        showLoading();
        Map<String,Object> param = new HashMap<>();
        param.put("fromaddress",LocationUtils.getLocation().address);
        param.put("toaddress",RouteTask
                .getInstance( getActivity().getApplicationContext()).getEndPoint().address);
        param.put("fromlongitude",LocationUtils.getLocation().longitude);
        param.put("fromlatitude",LocationUtils.getLocation().latitue);
        param.put("tolongitude",RouteTask
                .getInstance(  getActivity().getApplicationContext()).getEndPoint().longitude);
        param.put("tolatitude",RouteTask
                .getInstance(  getActivity().getApplicationContext()).getEndPoint().latitue);
        param.put("styletype",styletype);
        param.put("datetime",etTime.getText().toString());

        carAPI.getAfterCost(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try{
                    String cost = response.body();
                    tvGussCost.setText(String.format("%.2f元", Float.valueOf(cost)));
                }catch (Exception e){
                    LogUtils.e(TAG,e.getMessage());
                }
                hideLoading();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
            }
        });
    }
    public void bookCar(){
        Map<String,Object> param = new HashMap<>();
        param.put("pk_user", SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_PK_USER,""));
        param.put("latitude","38.46667");
        param.put("longitude","106.26667");
        param.put("title","用户123预约您");
        param.put("content","用户123预约您");
        param.put("reservatedate","2017-10-22 20:00:00");
        param.put("ridertel",SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE,""));
        param.put("fromaddress","银川金凤区六盘水中学");
        param.put("toaddress","银川市绿地21城");
        param.put("ordertype",2);//ordertype 0 顺风车 1 专车 2 专车（预约），3专车（包车）4 专车（接机）5 专车（送机）
        param.put("status",0);// 0 待付款 1 订单完成 2 订单取消
        param.put("cartype",0);// 0 舒适型 1 豪华型 7做商务2 其他9
//        param.put("cancelreason","");
//        param.put("personcount","");
//        param.put("begintime","");//包车下单时间
//        param.put("privatetype",0);//0 4小时  1 8小时
//        param.put("aircode","");//航班号
//        param.put("ispacket",0);//是否小件
//        param.put("packetmoney",0);//小件金额
//        param.put("consignee","");//收货人
//        param.put("consigneetel","");
        carAPI.generateOrder(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try{
                    CallCarResult result = new Gson().fromJson(response.body(),CallCarResult.class);
                    //下单成功
                    if("0".equals(result.getCode())){
                        Intent intent = new Intent(getActivity(), CallCarSucessActivity.class);
                        startActivity(intent);
                    }else {
                        ToastUtils.shortToast(getActivity(),result.getMsg());
                    }
                }catch (Exception e){
                    LogUtils.e(TAG,e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRouteCalculate(float cost, float distance, int duration) {
        if(clickType == 2){
            etEndAddress.setText(RouteTask
                .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
        }else if(clickType == 1){
            etStartAddress.setText(RouteTask
                    .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
            isSelectStartAdress = true;
        }
        shouldGetCost();
    }

    /**
     * 是否需要去刷新估算价格接口
     */
    private void shouldGetCost(){
        //当出发，和目的地都选择完毕，去估算价格
        if(!TextUtils.isEmpty(etStartAddress.getText().toString())&&
                !TextUtils.isEmpty(etEndAddress.getText().toString())){
            getCost();
        }
    }

    public void showLoading(){
        if(null == mLoadDialog){
            mLoadDialog = new LoadingDialog(getActivity());
        }
        mLoadDialog.show();
    }

    public void hideLoading(){
        if(null == mLoadDialog){
            return;
        }
        mLoadDialog.hide();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

}