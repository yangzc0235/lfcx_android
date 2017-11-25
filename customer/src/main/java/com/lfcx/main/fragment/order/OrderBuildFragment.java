package com.lfcx.main.fragment.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.common.utils.ToastUtils;
import com.lfcx.main.R;
import com.lfcx.main.activity.CallCarSucessActivity;
import com.lfcx.main.activity.CustomerOrderActivity;
import com.lfcx.main.consts.SPConstants;
import com.lfcx.main.fragment.BaseFragment;
import com.lfcx.main.maphelper.RouteTask;
import com.lfcx.main.net.api.CarAPI;
import com.lfcx.main.net.result.CallCarResult;
import com.lfcx.main.util.EdtUtil;
import com.lfcx.main.util.LocationUtils;
import com.lfcx.main.util.Md5Util;

import java.util.HashMap;
import java.util.Map;

import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */

public class OrderBuildFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = OrderBuildFragment.class.getSimpleName();

    EditText mAddressTextView;

    LinearLayout mDestinationContainer;

    TextView mRouteCostText;

    EditText mDesitinationText;

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
        mAddressTextView = (EditText) getActivity().findViewById(R.id.address_text);
        mDestinationContainer = (LinearLayout) getActivity().findViewById(R.id.destination_container);
        mRouteCostText = (TextView) getActivity().findViewById(R.id.routecost_text);
        mDesitinationText = (EditText) getActivity().findViewById(R.id.destination_text);
        mLocationImage = (ImageView) getActivity().findViewById(R.id.location_image);
        mConfirm = (Button) getActivity().findViewById(R.id.c_btn_confirm);

        mConfirm.setOnClickListener(this);
        mDesitinationText.setOnClickListener(this);
        mLocationImage.setOnClickListener(this);
        carAPI = APIFactory.create(CarAPI.class);
        if (getActivity().getIntent() != null) {
            getActivity().getIntent().getIntExtra("styletype", 0);
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
            ((CustomerOrderActivity) getActivity()).mLocationTask.startSingleLocate();
        } else if (v.getId() == R.id.destination_text) {
            ((CustomerOrderActivity) getActivity()).switchFragment(CustomerOrderActivity.SELECT);
        } else if (v.getId() == R.id.c_btn_confirm) {
            //发送订单
            try {

                if (EdtUtil.isEdtEmpty(mAddressTextView)) {
                    Toast.makeText(getActivity(), "获取您的位置失败,请输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (EdtUtil.isEdtEmpty(mDesitinationText)) {
                    Toast.makeText(getActivity(), "请输入终点位置", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.v("system--longitude----->",LocationUtils.getLocation().longitude+"");
                Log.v("system--latitue----->",LocationUtils.getLocation().latitue+"");
                Log.v("system--tolongitude--->",RouteTask
                        .getInstance(getActivity()).getEndPoint().longitude+"");
                Log.v("system--tolongitude--->",RouteTask
                        .getInstance(getActivity()).getEndPoint().latitue+"");
                bookCar(EdtUtil.getEdtText(mAddressTextView), EdtUtil.getEdtText(mDesitinationText));
            } catch (Exception e) {
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

    /**
     * 获取预估费用
     */
    private void getCost() {
        ((CustomerOrderActivity) getActivity()).showLoading();
        mRouteCostText.setVisibility(View.VISIBLE);
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
        param.put("datetime", Md5Util.getTimestamp());

        carAPI.getNowCost(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String cost = response.body();
                    mRouteCostText.setText(String.format("预估费用%.2f元", Float.valueOf(cost)));
                } catch (Exception e) {
                    LogUtils.e(TAG, e.getMessage());
                }
                ((CustomerOrderActivity) getActivity()).hideLoading();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ((CustomerOrderActivity) getActivity()).hideLoading();
            }
        });
    }


    /**
     * 专车下单
     * @param fromAddress
     * @param toAddress
     */
    public void bookCar(String fromAddress,String toAddress) {
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
        param.put("title", "用户"+SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE, "")+"预约您");
        param.put("content", "用户"+SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE, "")+"预约您");
        param.put("reservatedate", "");
        param.put("ridertel", SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE, ""));
        param.put("ordertype", 1);//专车
        param.put("status", 0);// 0 待付款 1 订单完成 2 订单取消
        param.put("isprivatecar", 0);//专车
        param.put("carstyletype", styletype);//类型:舒适型
        Log.v("fromlatitude----------",LocationUtils.getLocation().latitue+"");
        Log.v("fromlongitude----------",LocationUtils.getLocation().longitude+"");
        Log.v("tolatitude----------",RouteTask
                .getInstance(getActivity()).getEndPoint().latitue+"");
        Log.v("tolongitude----------",RouteTask
                .getInstance(getActivity()).getEndPoint().longitude+"");
        carAPI.generateOrder(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                Log.v("system---下单信息-->",response.body()+"");
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

}
