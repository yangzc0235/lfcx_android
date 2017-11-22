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
import android.widget.TextView;
import android.widget.Toast;

import com.lfcx.driver.R;
import com.lfcx.driver.activity.DriverOrderActivity;
import com.lfcx.driver.event.EventUtil;

import org.greenrobot.eventbus.EventBus;

/**
 *
 */

public class OrderFinishFragment extends Fragment implements View.OnClickListener {
    private TextView mTvMoney;
    private TextView mTvCarid;
    private TextView mTvStart;
    private TextView mTvEnd;
    private TextView mTvCollect;
    private TextView mTvFinish;
    private TextView mTvTime;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_finish_fragment, container, false);
        mTvMoney = (TextView) view.findViewById(R.id.tv_money);
        mTvCarid = (TextView) view.findViewById(R.id.tv_carid);
        mTvStart = (TextView) view.findViewById(R.id.tv_start);
        mTvEnd = (TextView) view.findViewById(R.id.tv_end);
        mTvCollect = (TextView) view.findViewById(R.id.tv_collect);
        mTvFinish = (TextView) view.findViewById(R.id.tv_finish);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();

    }


    private void init() {
        mTvFinish.setOnClickListener(this);
        mTvTime.setText(DriverOrderActivity.userOrderEntity.getReservatedate());
        mTvCarid.setText(DriverOrderActivity.userOrderEntity.getMobile());
        mTvStart.setText(DriverOrderActivity.userOrderEntity.getFromaddress());
        mTvEnd.setText(DriverOrderActivity.userOrderEntity.getToaddress());
        mTvMoney.setText(DriverOrderActivity.incomeEntity.getTotalfee()+"");

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_collect) {
            EventBus.getDefault().post(new EventUtil("collect_car"));
        } else if (i == R.id.tv_finish) {
            Toast.makeText(getContext(), "完成了", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new EventUtil("collect_car"));
        } else if (i == R.id.tv_carid) {
           callPhone(DriverOrderActivity.userOrderEntity.getMobile());
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

}
