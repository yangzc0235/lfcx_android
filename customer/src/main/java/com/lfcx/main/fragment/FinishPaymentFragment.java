package com.lfcx.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.main.R;
import com.lfcx.main.adapter.LvItemActivityTravelFinishAdapter;
import com.lfcx.main.application.CustomerApplication;
import com.lfcx.main.consts.SPConstants;
import com.lfcx.main.net.api.CarAPI;
import com.lfcx.main.net.result.OrderListEntity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *
 */

public class FinishPaymentFragment extends BaseFragment {


    private CarAPI carAPI;
    private LvItemActivityTravelFinishAdapter mLvItemActivityTravelAdapter;
    private ListView mListviewCustomerTravel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_finish_payment, container, false);
        mListviewCustomerTravel= (ListView) view.findViewById(R.id.listview_customer_travel);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        carAPI= APIFactory.create(CarAPI.class);
        requestgetOrderInfo((String) SPUtils.getParam(CustomerApplication.getInstance(), SPConstants.KEY_CUSTOMER_PK_USER,""),1,0);
    }

    /**
     * 获取订单列表
     * @param pk_user
     * @param pagecount
     * @param pagesize
     */
    private void requestgetOrderInfo(String pk_user,int pagecount,int pagesize) {
        showLoading();
        Map<String, Object> param = new HashMap<>();
        param.put("pk_user", pk_user);
        param.put("pagecount", pagecount);
        param.put("pagesize", pagesize);
        carAPI.getOrderInfo(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                Log.v("system--订单信息----",response.body()+"");
                try {
                    Gson gson = new Gson();
                    OrderListEntity orderListEntity = gson.fromJson(response.body(), OrderListEntity.class);
                    if(orderListEntity.getCode().equals("0")){
                        mLvItemActivityTravelAdapter=new LvItemActivityTravelFinishAdapter(CustomerApplication.getInstance(),orderListEntity.getPaymentlist());
                        mListviewCustomerTravel.setAdapter(mLvItemActivityTravelAdapter);
                    }else {
                        Toast.makeText(getContext(), "您暂时没有行程,尽快下单出发吧", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(CustomerApplication.getInstance(), "获取订单列表异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
            }
        });
    }



}
