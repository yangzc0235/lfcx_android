package com.lfcx.driver.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfcx.driver.R;
import com.lfcx.driver.activity.DriverOrderActivity;
import com.lfcx.driver.event.EventUtil;
import com.lfcx.driver.maphelper.LocationTask;

import org.greenrobot.eventbus.EventBus;

/**
 *
 */

public class ReceiptFragment extends Fragment implements View.OnClickListener {
    private ImageView mIvNowAddress;
    private EditText mEtStartAddress;
    private ImageView mIvToAddress;
    private EditText mEtEndAddress;
    private TextView mTvGrab;
    LocationTask mLocation;
    private TextView mTvTime;
    private int mCount=10;
    private boolean mIsSkip=true;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                mCount--;
                mTvTime.setText(mCount+"s后订单自动取消");
                mHandler.sendEmptyMessageDelayed(1,1000);
                if(mCount==0&&mIsSkip){
                    EventBus.getDefault().post(new EventUtil("collect_car"));
                }
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_point_fragment, container, false);
        mIvNowAddress = (ImageView) view.findViewById(R.id.iv_now_address);
        mEtStartAddress = (EditText) view.findViewById(R.id.et_start_address);
        mIvToAddress = (ImageView) view.findViewById(R.id.iv_to_address);
        mEtEndAddress = (EditText) view.findViewById(R.id.et_end_address);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        mTvGrab = (TextView) view.findViewById(R.id.tv_grab);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();

    }

    private void init() {
        mTvGrab.setOnClickListener(this);
        setStartReciept();
        mTvTime.setText(mCount+"s后订单自动取消");
        mHandler.sendEmptyMessageDelayed(1,1000);


    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_grab){
            mIsSkip=false;
            EventBus.getDefault().post(new EventUtil("receipt"));
        }
    }

    public void setStartReciept(){
        mEtStartAddress.setText(DriverOrderActivity.userOrderEntity.getFromaddress());
        mEtEndAddress.setText(DriverOrderActivity.userOrderEntity.getToaddress());
    }
}
