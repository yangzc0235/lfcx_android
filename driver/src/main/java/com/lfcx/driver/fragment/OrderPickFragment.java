package com.lfcx.driver.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lfcx.driver.R;
import com.lfcx.driver.activity.DriverOrderActivity;
import com.lfcx.driver.event.EventUtil;

import org.greenrobot.eventbus.EventBus;

/**
 *接单之后的流程
 */

public class OrderPickFragment extends Fragment implements View.OnClickListener {
    private TextView mTvMoible;
    private ImageView mImageView1;
    private TextView mTvStart;
    private ImageView mImageView2;
    private TextView mTvEnd;
    private Button mBtnNow;
    private ImageView mImgvCall;
    private TextView mTvTime;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_order_pick_content_fragment, container, false);

        mTvMoible = (TextView) view.findViewById(R.id.tv_mobile);
        mImageView1 = (ImageView) view.findViewById(R.id.imageView1);
        mTvStart = (TextView) view.findViewById(R.id.tv_start);
        mImageView2 = (ImageView) view.findViewById(R.id.imageView2);
        mTvEnd = (TextView) view.findViewById(R.id.tv_end);
        mBtnNow = (Button) view.findViewById(R.id.btn_now);
        mImgvCall = (ImageView) view.findViewById(R.id.imgv_call);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mImgvCall.setOnClickListener(this);
        mBtnNow.setOnClickListener(this);
        try {
            mTvStart.setText(DriverOrderActivity.userOrderEntity.getFromaddress()+"");
            mTvEnd.setText(DriverOrderActivity.userOrderEntity.getToaddress()+"");
            mTvMoible.setText(DriverOrderActivity.userOrderEntity.getMobile()+"");
            mTvTime.setText(DriverOrderActivity.userOrderEntity.getReservatedate()+"");
        }catch (Exception e){

        }
    }


    /**
     * 打电话
     */
    private void callPhone(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(getContext(), "号码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            // 拨号：激活系统的拨号组件
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + mobile);
            intent.setData(data);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.imgv_call) {
            callPhone(DriverOrderActivity.userOrderEntity.getMobile());
        } else if (i == R.id.btn_now) {
            if(mBtnNow.getText().toString().trim().equals("接到乘客")){
                //Received the passengers
                EventBus.getDefault().post(new EventUtil("received_passengers"));
                return;
            }
            if (mBtnNow.getText().toString().trim().equals("开始行程")) {
                EventBus.getDefault().post(new EventUtil("start_travel"));
            }
            if (mBtnNow.getText().toString().trim().equals("结束行程")) {
                EventBus.getDefault().post(new EventUtil("arrive_point"));

            }
        }
    }

    /**
     * 设置文字为开始行程
     */
    public void setButtonTextFirst(){
        mBtnNow.setText("开始行程");
    }


    /**
     * 设置文字为结束行程
     */
    public void setButtonText(){
        mBtnNow.setText("结束行程");
    }
}
