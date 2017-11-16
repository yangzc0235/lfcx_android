package com.lfcx.driver.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfcx.driver.R;
import com.lfcx.driver.event.EventUtil;
import com.lfcx.driver.maphelper.LocationTask;
import com.lfcx.driver.maphelper.RouteTask;

import org.greenrobot.eventbus.EventBus;

/**
 *
 */

public class ReceiptFragment extends Fragment  {
    private ImageView mTvBack;
    private TextView mTvTime;
    private ImageView mIvNowAddress;
    private EditText mEtStartAddress;
    private ImageView mIvToAddress;
    private EditText mEtEndAddress;
    private TextView mTvGrab;
    LocationTask mLocation;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_point_fragment, container, false);
        mTvBack = (ImageView) view.findViewById(R.id.tv_back);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        mIvNowAddress = (ImageView) view.findViewById(R.id.iv_now_address);
        mEtStartAddress = (EditText) view.findViewById(R.id.et_start_address);
        mIvToAddress = (ImageView) view.findViewById(R.id.iv_to_address);
        mEtEndAddress = (EditText) view.findViewById(R.id.et_end_address);
        mTvGrab = (TextView) view.findViewById(R.id.tv_grab);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();

    }


    public void onRouteFinish() {
        setDepartureAddress(RouteTask.getInstance(getActivity()).getStartPoint().address);
        setDestinationAddress(RouteTask.getInstance(getActivity()).getEndPoint().address);
    }

    private void setDestinationAddress(String address) {
        mEtStartAddress.setText(address+"");
    }

    private void setDepartureAddress(String address) {
        mEtStartAddress.setText(address+"");
        mEtEndAddress.setText(address+"");
    }


    private void init() {
        mTvGrab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要请求接单接口
                EventBus.getDefault().post(new EventUtil("receipt"));
            }
        });
    }





}
