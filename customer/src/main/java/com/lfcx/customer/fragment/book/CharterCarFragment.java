package com.lfcx.customer.fragment.book;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.widget.LoadingDialog;
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.activity.DestinationActivity;
import com.lfcx.customer.maphelper.PositionEntity;
import com.lfcx.customer.maphelper.RouteTask;
import com.lfcx.customer.net.api.CarAPI;
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


    private Unbinder unbinder;

    /**
     * //1 4小时  2 8小时
     */
    private int combotype = 1;
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
        EditText et_duration = (EditText) getActivity().findViewById(R.id.et_duration);

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
        param.put("begintime",etTime.getText().toString());
        param.put("comboType",combotype);
        param.put("hours",4);


        carAPI.getPrivateCost(param).enqueue(new Callback<String>() {
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
