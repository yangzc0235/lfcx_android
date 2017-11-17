package com.lfcx.customer.fragment.order;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.common.utils.ToastUtils;
import com.lfcx.customer.R;
import com.lfcx.customer.activity.CustomerOrderActivity;
import com.lfcx.customer.consts.SPConstants;
import com.lfcx.customer.maphelper.RouteTask;
import com.lfcx.customer.net.api.CarAPI;
import com.lfcx.customer.net.result.CallCarResult;
import com.lfcx.customer.util.LocationUtils;
import com.lfcx.customer.util.Md5Util;
import com.lfcx.customer.util.StringEmptyUtil;

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
            try {
                String from_address = LocationUtils.getLocation().address;
                String to_address = RouteTask
                        .getInstance(getActivity()).getEndPoint().address;
                if(StringEmptyUtil.isEmpty(from_address)){
                    Toast.makeText(getActivity(), "正在获取您当前位置", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringEmptyUtil.isEmpty(to_address)){
                    Toast.makeText(getActivity(), "请输入终点位置", Toast.LENGTH_SHORT).show();
                    return;
                }
                bookCar(from_address,to_address);
            }catch (Exception e){
                Toast.makeText(getContext(), "正在获取您的位置信息,请稍等", Toast.LENGTH_SHORT).show();
            }
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
        param.put("datetime",Md5Util.getTimestamp());

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

    public void bookCar(String from_address,String to_address){
        Map<String,Object> param = new HashMap<>();
        param.put("pk_user", SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_PK_USER,""));
        param.put("fromlatitude",LocationUtils.getLocation().latitue+"");
        param.put("fromlongitude",LocationUtils.getLocation().longitude+"");
        param.put("tolatitude",RouteTask
                .getInstance(getActivity()).getEndPoint().latitue+"");
        param.put("tolongitude",RouteTask
                .getInstance(getActivity()).getEndPoint().longitude+"");
//        param.put("latitude","38.46667");
//        param.put("longitude","106.26667");
        param.put("title","用户123预约您");
        param.put("content","用户123预约您");
        param.put("reservatedate",System.currentTimeMillis());
        param.put("ridertel",SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE,""));
        param.put("fromaddress",from_address);
        param.put("toaddress",to_address);
        param.put("carstyletype",styletype);//ordertype 0 顺风车 1 专车 2 专车（预约），3专车（包车）4 专车（接机）5 专车（送机）
        param.put("ordertype",1);//ordertype 0 顺风车 1 专车 2 专车（预约），3专车（包车）4 专车（接机）5 专车（送机）
        param.put("status",0);// 0 待付款 1 订单完成 2 订单取消
        Log.v("pk_user----------",SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_PK_USER,"")+"");
        Log.v("fromlatitude----------",LocationUtils.getLocation().latitue+"");
        Log.v("fromlongitude----------",LocationUtils.getLocation().longitude+"");
        Log.v("tolatitude----------",RouteTask
                .getInstance(getActivity()).getEndPoint().latitue+"");
        Log.v("tolongitude----------",RouteTask
                .getInstance(getActivity()).getEndPoint().longitude+"");
        Log.v("title----------","用户123预约您");
        Log.v("content----------","用户123预约您");
        Log.v("ridertel----------",SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE,"")+"");
        Log.v("status----------",0+"");
        Log.v("fromaddress----------",from_address);
        Log.v("toaddress----------",to_address);
        Log.v("ordertype----------",1+"");
        Log.v("carstyletype----------",styletype+"");
        Log.v("isprivatecar----------",0+"");
        carAPI.generateOrder(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try{
                    CallCarResult result = new Gson().fromJson(response.body(),CallCarResult.class);
                    Toast.makeText(getActivity(), response.body(), Toast.LENGTH_SHORT).show();
                    //下单成功
                    if("0".equals(result.getCode())){
//                        SPUtils.setParam(getActivity(),SPConstants.KEY_CUSTOMER_MOBILE,result.getPk_userOder());
//                        Intent intent = new Intent(getActivity(), CallCarSucessActivity.class);
//                        startActivity(intent);
                        ToastUtils.shortToast(getActivity(),result.getMsg());
//                        ((CustomerOrderActivity)getActivity()).switchFragment(CustomerOrderActivity.WAIT);

                    }else {
                        ToastUtils.shortToast(getActivity(),result.getMsg());
                        //((CustomerOrderActivity)getActivity()).switchFragment(CustomerOrderActivity.WAIT);
                    }

                }catch (Exception e){
                    LogUtils.e(TAG,e.getMessage());
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if(t != null)
                    Log.i(CustomerOrderActivity.class.getSimpleName(), t.getMessage());
            }
        });
    }


}
