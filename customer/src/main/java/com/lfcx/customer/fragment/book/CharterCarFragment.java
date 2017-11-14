package com.lfcx.customer.fragment.book;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
 * des   :  包車
 */
public class CharterCarFragment extends Fragment implements  RouteTask.OnRouteCalculateListener{

    public static final String TAG = CharterCarFragment.class.getSimpleName();

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

    @BindView(R2.id.c_time_range)
    RadioGroup timeRange;

    @BindView(R2.id.btn_register)
    Button bt;

    private Unbinder unbinder;

    /**
     * //1 4小时  2 8小时
     */
    private int combotype = 4;
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
    private CarAPI carAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cv = inflater.inflate(R.layout.fragment_charter_car, container, false);
        unbinder = ButterKnife.bind(this, cv);
        init();
        return cv;
    }

    @Override
    public void onResume() {
        super.onResume();
        PositionEntity location = LocationUtils.getLocation();
        if(null != location && !isSelectStartAdress){
            etStartAddress.setText(location.getAddress());
        }
    }

    private void init(){
        RouteTask.getInstance( getActivity().getApplicationContext())
                .addRouteCalculateListener(this);
        carAPI = APIFactory.create(CarAPI.class);
        timeRange.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R2.id.c_range_one){
                    combotype = 4;
                }else if(checkedId == R2.id.c_range_two){
                    combotype = 8;
                }
            }
        });
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

    @OnClick(R2.id.btn_register)
    public void onClickConfirm(View view) {
        //确认用车
        bookCar();
    }
    //**
    /**
     * 是否需要去刷新估算价格接口
     */
    private void shouldGetCost(){
        //当出发，和目的地都选择完毕，去估算价格
        if(!TextUtils.isEmpty(etStartAddress.getText().toString())
                && !TextUtils.isEmpty(etEndAddress.getText().toString())
//                && !TextUtils.isEmpty(etTime.getText().toString())
                ){
            getCost();
        }

    }


    /**
     * 计算价格
     */
    private void getCost(){
        showLoading();

        //参数 (都是必填项):datetime （预约时间） ，fromaddress，toaddress，fromlongitude，fromlatitude，tolongitude， Tolatitude，styletype（舒适型 0，豪华型 1，七座商务 2），
        // privatetype（0：4小时套餐；1：8小时套餐）;distance:(导航距离)
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
        param.put("privatetype",combotype);
        param.put("distance",4);


        carAPI.getPrivateCost(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try{
                    String cost = response.body();
                    Toast.makeText(getContext().getApplicationContext(), response.body(), Toast.LENGTH_SHORT).show();
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


    /**
     * 确认用车
     */
    public void bookCar(){

//        pk_user:(用户主键 必填);fromlatitude:(纬度 必填); fromlongitude:(经度 必填);title:订单标题 (必填);
//        content:订单内容(必填) ;fromaddress: 开始位置(必填) ; toaddress: 目的地 (必填);tolatitude:(纬度 必填);
//        tolongitude:(经度 必填);ordertype : 订单类型 (必填)(0 顺风车 1 专车 ，2 专车-〉预约 3 专车-〉包车 4 专车-〉接机 5 专车-〉送机);
//        status:订单状态(*)（0：待付款；1订单完成2 ：订单取消）, cancelreason(取消原因),personcount 乘车人数，
//        begintime（包车下单开始时间）, privatetype(0:4小时套餐 ；1 ：8小时套餐);aircode（航班号）;
//        ispacket :( 是否带小件 0:是;1:否);packetmoney;小件金额；consignee 收货人; consigneetel 收货人电话;
//        ishelpother(是否替人叫车 0:是;1:否);Name :(乘车人姓名);ridertel(乘车人电话);
//        carstyletype:车辆类型(0:舒适型，1:豪华型，2:7座商务) ;isprivatecar:是否专车(0:是专车;1:顺风车)
        Map<String,Object> param = new HashMap<>();
        param.put("pk_user", SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_PK_USER,""));
        param.put("fromaddress",LocationUtils.getLocation().address);
        param.put("toaddress",RouteTask
                .getInstance( getActivity().getApplicationContext()).getEndPoint().address);
        param.put("fromlongitude",LocationUtils.getLocation().longitude);
        param.put("fromlatitude",LocationUtils.getLocation().latitue);
        param.put("tolongitude",RouteTask
                .getInstance(  getActivity().getApplicationContext()).getEndPoint().longitude);
        param.put("tolatitude",RouteTask
                .getInstance(  getActivity().getApplicationContext()).getEndPoint().latitue);
        param.put("title","用户123预约您");
        param.put("content","用户123预约您");
        param.put("reservatedate","2017-10-22 20:00:00");
        param.put("ridertel", SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE,""));
        param.put("fromaddress","银川金凤区六盘水中学");
        param.put("toaddress","银川市绿地21城");
        param.put("carstyletype",styletype);//ordertype 0 顺风车 1 专车 2 专车（预约），3专车（包车）4 专车（接机）5 专车（送机）
        param.put("ordertype",3);//ordertype 0 顺风车 1 专车 2 专车（预约），3专车（包车）4 专车（接机）5 专车（送机）
        param.put("status",0);// 0 待付款 1 订单完成 2 订单取消
        carAPI.generateOrder(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try{
                    CallCarResult result = new Gson().fromJson(response.body(),CallCarResult.class);

                    Toast.makeText(getActivity().getApplicationContext(), response.body(), Toast.LENGTH_SHORT).show();
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
        unbinder.unbind();
    }
}
