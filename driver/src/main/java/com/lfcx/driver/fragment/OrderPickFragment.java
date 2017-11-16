package com.lfcx.driver.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfcx.driver.R;
import com.lfcx.driver.event.EventUtil;

import org.greenrobot.eventbus.EventBus;

/**
 *
 */

public class OrderPickFragment extends Fragment {
    private TextView mTvCarid;
    private ImageView mImageView1;
    private TextView mTvStart;
    private ImageView mImageView2;
    private TextView mTvEnd;
    private Button mBtnNow;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_order_pick_content_fragment, container, false);

        mTvCarid = (TextView) view.findViewById(R.id.tv_carid);
        mImageView1 = (ImageView) view.findViewById(R.id.imageView1);
        mTvStart = (TextView) view.findViewById(R.id.tv_start);
        mImageView2 = (ImageView) view.findViewById(R.id.imageView2);
        mTvEnd = (TextView) view.findViewById(R.id.tv_end);
        mBtnNow = (Button) view.findViewById(R.id.btn_now);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mBtnNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBtnNow.getText().toString().trim().equals("到达约定地点")){
                    mBtnNow.setText("接到乘客");
                    return;
                }
                if(mBtnNow.getText().toString().trim().equals("接到乘客")){
                    //EventBus.getDefault().post(new EventUtil("arrive_point"));
                    mBtnNow.setText("开始行程");
                    return;
                }
                if(mBtnNow.getText().toString().trim().equals("开始行程")){
//                    EventBus.getDefault().post(new EventUtil("arrive_point"));
                    mBtnNow.setText("结束行程");

                }
                if(mBtnNow.getText().toString().trim().equals("结束行程")){
                    EventBus.getDefault().post(new EventUtil("arrive_point"));
                    mBtnNow.setText("订单完成");
                }


            }
        });
    }
}
