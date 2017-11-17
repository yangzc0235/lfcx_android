package com.lfcx.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lfcx.common.utils.SPUtils;
import com.lfcx.customer.R;
import com.lfcx.customer.consts.SPConstants;
import com.lfcx.customer.net.NetConfig;
import com.lfcx.customer.widget.dialog.ExitLoginDialog;

public class CustomerUserCenterActivity extends CustomerBaseActivity implements View.OnClickListener {
    private LinearLayout mLlBarActivityCustomerPersonCenter;
    private TextView mTitleLeftActivityCustomerPersonCenter;
    private TextView mTitleCenterActivityCustomerPersonCenter;
    private TextView mTitleRightActivityCustomerPersonCenter;
    private TextView mTvMoneyActivityCustomerPersonCenter;
    private TextView mTvStrokeActivityCustomerPersonCenter;
    private TextView mTvRecommendActivityCustomerPersonCenter;
    private TextView mTvComplaintActivityCustomerPersonCenter;
    private TextView mTvMobileActivityCustomerPersonCenter;
    private TextView mTvExitLoginActivityCustomerPersonCenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_person_center);
        mLlBarActivityCustomerPersonCenter = (LinearLayout) findViewById(R.id.ll_bar_activity_customer_person_center);
        mTitleLeftActivityCustomerPersonCenter = (TextView) findViewById(R.id.title_left_activity_customer_person_center);
        mTitleCenterActivityCustomerPersonCenter = (TextView) findViewById(R.id.title_center_activity_customer_person_center);
        mTitleRightActivityCustomerPersonCenter = (TextView) findViewById(R.id.title_right_activity_customer_person_center);
        mTvMoneyActivityCustomerPersonCenter = (TextView) findViewById(R.id.tv_money_activity_customer_person_center);
        mTvStrokeActivityCustomerPersonCenter = (TextView) findViewById(R.id.tv_stroke_activity_customer_person_center);
        mTvRecommendActivityCustomerPersonCenter = (TextView) findViewById(R.id.tv_recommend_activity_customer_person_center);
        mTvComplaintActivityCustomerPersonCenter = (TextView) findViewById(R.id.tv_complaint_activity_customer_person_center);
        mTvMobileActivityCustomerPersonCenter = (TextView) findViewById(R.id.tv_mobile_activity_customer_person_center);
        mTvExitLoginActivityCustomerPersonCenter = (TextView) findViewById(R.id.tv_exit_login_activity_customer_person_center);
        mTitleLeftActivityCustomerPersonCenter.setOnClickListener(this);
        mTitleRightActivityCustomerPersonCenter.setOnClickListener(this);
        mTvMoneyActivityCustomerPersonCenter.setOnClickListener(this);
        mTvStrokeActivityCustomerPersonCenter.setOnClickListener(this);
        mTvRecommendActivityCustomerPersonCenter.setOnClickListener(this);
        mTvComplaintActivityCustomerPersonCenter.setOnClickListener(this);
        mTvExitLoginActivityCustomerPersonCenter.setOnClickListener(this);
        mTvMobileActivityCustomerPersonCenter.setText((CharSequence) SPUtils.getParam(this, SPConstants.KEY_CUSTOMER_MOBILE, ""));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.title_right_activity_customer_person_center) {
            Intent intent = new Intent(this, CustomerWebviewActivity.class);
            intent.putExtra(CustomerWebviewActivity.WEB_URL, NetConfig.USER_SETTER);
            startActivity(intent);

        } else if (i == R.id.tv_money_activity_customer_person_center) {
            goToActivity(CustomerMeWalletActivity.class);

        } else if (i == R.id.tv_stroke_activity_customer_person_center) {
        } else if (i == R.id.tv_recommend_activity_customer_person_center) {
        } else if (i == R.id.tv_complaint_activity_customer_person_center) {
        } else if (i == R.id.title_left_activity_customer_person_center) {
            finish();

        } else if (i == R.id.tv_exit_login_activity_customer_person_center) {//退出登录

            ExitLoginDialog exitLoginDialog=new ExitLoginDialog(this, R.style.customDialog);
            exitLoginDialog.show();

        }
    }
}
