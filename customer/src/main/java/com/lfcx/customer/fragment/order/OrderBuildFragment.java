package com.lfcx.customer.fragment.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.common.utils.ToastUtils;
import com.lfcx.customer.R;
import com.lfcx.customer.activity.CallCarSucessActivity;
import com.lfcx.customer.activity.CustomerOrderActivity;
import com.lfcx.customer.consts.SPConstants;
import com.lfcx.customer.maphelper.RouteTask;
import com.lfcx.customer.net.api.CarAPI;
import com.lfcx.customer.net.result.CallCarResult;
import com.lfcx.customer.util.LocationUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */

public class OrderBuildFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = OrderBuildFragment.class.getSimpleName();

    TextView mAddressTextView;

    LinearLayout mDestinationContainer;

    TextView mRouteCostText;

    TextView mDesitinationText;

    ImageView mLocationImage;

    Button mConfirm;

    private int styletype = 0;

    CarAPI carAPI;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_order_build_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mAddressTextView = (TextView) getActivity().findViewById(R.id.address_text);
        mDestinationContainer = (LinearLayout) getActivity().findViewById(R.id.destination_container);
        mRouteCostText = (TextView) getActivity().findViewById(R.id.routecost_text);
        mDesitinationText = (TextView) getActivity().findViewById(R.id.destination_text);
        mLocationImage = (ImageView) getActivity().findViewById(R.id.location_image);
        mConfirm = (Button) getActivity().findViewById(R.id.c_btn_confirm);

        mConfirm.setOnClickListener(this);
        mDesitinationText.setOnClickListener(this);
        mLocationImage.setOnClickListener(this);

        carAPI = APIFactory.create(CarAPI.class);
        if(getActivity().getIntent() != null) {
            getActivity().getIntent().getIntExtra("styletype",0);
        }
    }

    //设置起始位置
    public void setDepartureAddress(String address) {
        mAddressTextView.setText(address);
    }

    //设置终点位置
    public void setDestinationAddress(String address) {
        mDesitinationText.setText(address);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.location_image) {
            ((CustomerOrderActivity)getActivity()).mLocationTask.startSingleLocate();
        }
        else if (v.getId() == R.id.destination_text) {
            ((CustomerOrderActivity)getActivity()).switchFragment(CustomerOrderActivity.SELECT);
        }
        else if(v.getId() == R.id.c_btn_confirm) {
            //发送订单
            bookCar();
        }
    }


    public void onRouteFinish() {
        mRouteCostText.setVisibility(View.VISIBLE);
        setDepartureAddress(RouteTask.getInstance(getActivity()).getStartPoint().address);
        setDestinationAddress(RouteTask.getInstance(getActivity()).getEndPoint().address);
        getCost();
    }

    private void getCost(){
        ((CustomerOrderActivity)getActivity()).showLoading();
        mRouteCostText.setVisibility(View.VISIBLE);
        Map<String,Object> param = new HashMap<>();
        param.put("fromaddress",LocationUtils.getLocation().address);
        param.put("toaddress",RouteTask
                .getInstance(getActivity()).getEndPoint().address);
        param.put("fromlongitude",LocationUtils.getLocation().longitude);
        param.put("fromlatitude",LocationUtils.getLocation().latitue);
        param.put("tolongitude",RouteTask
                .getInstance(getActivity()).getEndPoint().longitude);
        param.put("tolatitude",RouteTask
                .getInstance(getActivity()).getEndPoint().latitue);
        param.put("styletype",styletype);
        param.put("datetime","2017-09-08");

        carAPI.getNowCost(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try{
                    String cost = response.body();
                    mRouteCostText.setText(String.format("预估费用%.2f元", Float.valueOf(cost)));
                }catch (Exception e){
                    LogUtils.e(TAG,e.getMessage());
                }
                ((CustomerOrderActivity)getActivity()).hideLoading();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ((CustomerOrderActivity)getActivity()).hideLoading();
            }
        });
    }

    public void bookCar(){
        Map<String,Object> param = new HashMap<>();
        param.put("pk_user", SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_PK_USER,""));
        param.put("latitude", LocationUtils.getLocation().latitue);
        param.put("longitude",LocationUtils.getLocation().longitude);
        param.put("title","用户123预约您");
        param.put("content","用户123预约您");
        param.put("reservatedate","2017-10-23 20:00:00");
        param.put("ridertel",SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE,""));
        param.put("fromaddress","银川金凤区六盘水中学");
        param.put("toaddress","银川市绿地21城");
        param.put("ordertype",1);//ordertype 0 顺风车 1 专车 2 专车（预约），3专车（包车）4 专车（接机）5 专车（送机）
        param.put("status",0);// 0 待付款 1 订单完成 2 订单取消
        param.put("cartype",styletype);// 0 舒适型 1 豪华型 7做商务2 其他9

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
                        SPUtils.setParam(getActivity(),SPConstants.KEY_CUSTOMER_MOBILE,result.getPk_userOder());
                        Intent intent = new Intent(getActivity(), CallCarSucessActivity.class);
                        startActivity(intent);
                    }else {
                        ToastUtils.shortToast(getActivity(),result.getMsg());
                    }
                }catch (Exception e){
                    LogUtils.e(TAG,e.getMessage());
                }

                ((CustomerOrderActivity)getActivity()).switchFragment(CustomerOrderActivity.WAIT);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if(t != null)
                    Log.i(CustomerOrderActivity.class.getSimpleName(), t.getMessage());
            }
        });
    }


}
