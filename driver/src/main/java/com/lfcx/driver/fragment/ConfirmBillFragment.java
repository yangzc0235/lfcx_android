package com.lfcx.driver.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lfcx.driver.R;
import com.lfcx.driver.event.MoneyEvent;
import com.lfcx.driver.util.EdtUtil;

import org.greenrobot.eventbus.EventBus;

/**
 *
 */

public class ConfirmBillFragment extends Fragment  {
    private TextView mTvMoney;
    private EditText mEdtBalanceGs;
    private EditText mEdtBalanceLq;
    private EditText mEdtBalanceTc;
    private TextView mTvCollection;
    private LinearLayout mLlOne;
    private RelativeLayout mLlWait;
    private TextView mTvCancelOrderFirst;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_bill_fragment, container, false);
        mTvMoney = (TextView) view.findViewById(R.id.tv_money);
        mEdtBalanceGs = (EditText) view.findViewById(R.id.edt_balance_gs);
        mEdtBalanceLq = (EditText) view.findViewById(R.id.edt_balance_lq);
        mEdtBalanceTc = (EditText) view.findViewById(R.id.edt_balance_tc);
        mTvCollection = (TextView) view.findViewById(R.id.tv_collection);
        mLlOne = (LinearLayout) view.findViewById(R.id.ll_one);
        mLlWait = (RelativeLayout) view.findViewById(R.id.ll_wait);
        mTvCancelOrderFirst = (TextView) view.findViewById(R.id.tv_cancel_order_first);
        mLlOne.setVisibility(View.VISIBLE);
        mLlWait.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        mTvMoney.setText("0.01元");
    }

    private void init() {
        mTvCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gsMoney= EdtUtil.getEdtText(mEdtBalanceGs);
                String lqMoney= EdtUtil.getEdtText(mEdtBalanceLq);
                String tcMoney= EdtUtil.getEdtText(mEdtBalanceTc);
                EventBus.getDefault().post(new MoneyEvent("order_finish",gsMoney,lqMoney,tcMoney));

            }
        });
    }


    public void hide(){
        mLlOne.setVisibility(View.GONE);
        mLlWait.setVisibility(View.VISIBLE);
    }



}
