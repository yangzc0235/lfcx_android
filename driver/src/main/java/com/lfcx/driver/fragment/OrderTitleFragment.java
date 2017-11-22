package com.lfcx.driver.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lfcx.driver.R;
import com.lfcx.driver.activity.DriverGPSNaviActivity;
import com.lfcx.driver.activity.DriverOrderActivity;

/**
 *
 */

public class OrderTitleFragment extends Fragment {
    private TextView mTvStartAddress;
    private TextView mTvStartNavigation;
    private TextView mTvTime;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_order_title_fragment, container, false);
        mTvStartAddress = (TextView) view.findViewById(R.id.tv_start_address);
        mTvStartNavigation = (TextView) view.findViewById(R.id.tv_start_navigation);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mTvStartAddress.setText(DriverOrderActivity.userOrderEntity.getToaddress());
        mTvTime.setText("乘客预约"+DriverOrderActivity.userOrderEntity.getReservatedate()+"用车，请前往指定地点");
        mTvStartNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), DriverGPSNaviActivity.class);
                startActivity(intent);
            }
        });
    }
}
