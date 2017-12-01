package com.lfcx.driver.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lfcx.driver.R;
import com.lfcx.driver.activity.DriverOrderActivity;
import com.lfcx.driver.event.EventUtil;

import org.greenrobot.eventbus.EventBus;

/**
 *
 */

public class OrderTitleFragment extends Fragment {
    private TextView mTvStartAddress;
    private TextView mTvStartNavigation;
    private TextView mTvTime;
    private LinearLayout mLlStartNavigation;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_order_title_fragment, container, false);
        mTvStartAddress = (TextView) view.findViewById(R.id.tv_start_address);
        mTvStartNavigation = (TextView) view.findViewById(R.id.tv_start_navigation);
        mLlStartNavigation = (LinearLayout) view.findViewById(R.id.ll_start_navigation);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        try {
            mTvStartAddress.setText(DriverOrderActivity.userOrderEntity.getToaddress());
            mTvTime.setText("乘客预约"+DriverOrderActivity.userOrderEntity.getReservatedate()+"用车，请前往指定地点");
            mLlStartNavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventUtil("start_gps"));
                }
            });
        }catch (Exception e){

        }
    }
}
