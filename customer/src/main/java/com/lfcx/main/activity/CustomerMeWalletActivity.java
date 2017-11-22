package com.lfcx.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lfcx.main.R;

public class CustomerMeWalletActivity extends CustomerBaseActivity implements View.OnClickListener {

    private TextView mTitleLeftActivityCustomerPersonCenter;
    private TextView mTitleCenterActivityCustomerPersonCenter;
    private LinearLayout mLlBalanceActivityMeWallet;
    private TextView mTvBalanceActivityMeWallet;
    private LinearLayout mLlCouponsActivityMeWallet;
    private TextView mTvCouponsActivityMeWallet;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_wallet);
        mTitleLeftActivityCustomerPersonCenter = (TextView) findViewById(R.id.title_left_activity_customer_person_center);
        mTitleCenterActivityCustomerPersonCenter = (TextView) findViewById(R.id.title_center_activity_customer_person_center);
        mLlBalanceActivityMeWallet = (LinearLayout) findViewById(R.id.ll_balance_activity_me_wallet);
        mTvBalanceActivityMeWallet = (TextView) findViewById(R.id.tv_balance_activity_me_wallet);
        mLlCouponsActivityMeWallet = (LinearLayout) findViewById(R.id.ll_coupons_activity_me_wallet);
        mTvCouponsActivityMeWallet = (TextView) findViewById(R.id.tv_coupons_activity_me_wallet);
        mTitleLeftActivityCustomerPersonCenter.setOnClickListener(this);
        mLlBalanceActivityMeWallet.setOnClickListener(this);
        mTvBalanceActivityMeWallet.setOnClickListener(this);
        mLlCouponsActivityMeWallet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_balance_activity_me_wallet) {
            Toast.makeText(this, "点击了", Toast.LENGTH_SHORT).show();

            //余额

        } else if (i == R.id.title_left_activity_customer_person_center) {//返回
            finish();
        } else if (i == R.id.ll_coupons_activity_me_wallet) {//优惠券

        }
    }
}
