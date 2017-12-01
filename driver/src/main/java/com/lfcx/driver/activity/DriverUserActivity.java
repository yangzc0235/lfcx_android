package com.lfcx.driver.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lfcx.common.utils.SPUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.consts.SPConstants;
import com.lfcx.driver.dialog.ExitLoginDialog;

public class DriverUserActivity extends DriverBaseActivity implements View.OnClickListener {

    private TextView mTvDriverMobile;
    private TextView mTvExitLoginActivityCustomerPersonCenter;
    private ImageView mIvBack;
    private TextView mTitleBar;
    private ImageView mDIvWallet;
    private ImageView mDIvTrip;
    private ImageView mDIvCar;
    private ImageView mDIvTuijian;
    private ImageView mDIvShensu;
    private RelativeLayout mRlWallet;
    private RelativeLayout mRlTrip;
    private RelativeLayout mRlCar;
    private RelativeLayout mRlGift;
    private RelativeLayout mRlComplain;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_driver_user);
        mTvDriverMobile = (TextView) findViewById(R.id.tv_driver_mobile);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        mDIvWallet = (ImageView) findViewById(R.id.d_iv_wallet);
        mDIvTrip = (ImageView) findViewById(R.id.d_iv_trip);
        mDIvCar = (ImageView) findViewById(R.id.d_iv_car);
        mDIvTuijian = (ImageView) findViewById(R.id.d_iv_tuijian);
        mDIvShensu = (ImageView) findViewById(R.id.d_iv_shensu);
        mTvExitLoginActivityCustomerPersonCenter = (TextView) findViewById(R.id.tv_exit_login_activity_customer_person_center);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mRlWallet = (RelativeLayout) findViewById(R.id.rl_wallet);
        mRlTrip = (RelativeLayout) findViewById(R.id.rl_trip);
        mRlCar = (RelativeLayout) findViewById(R.id.rl_car);
        mRlGift = (RelativeLayout) findViewById(R.id.rl_gift);
        mRlComplain = (RelativeLayout) findViewById(R.id.rl_complain);
        mTvExitLoginActivityCustomerPersonCenter.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mRlWallet.setOnClickListener(this);
        mRlTrip.setOnClickListener(this);
        mRlComplain.setOnClickListener(this);
        mRlCar.setOnClickListener(this);
        mRlGift.setOnClickListener(this);
        mTitleBar.setText("个人中心");
        mTvDriverMobile.setText((CharSequence) SPUtils.getParam(this, SPConstants.DRIVER_MOBILE, ""));
        mIvBack.setVisibility(View.VISIBLE);
        init();
    }

    private void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.tv_exit_login_activity_customer_person_center:
                //退出登录
                ExitLoginDialog exitLoginDialog = new ExitLoginDialog(this, R.style.customDialog);
                exitLoginDialog.show();
                break;
            case R.id.rl_wallet:
                //我的钱包

                break;
            case R.id.rl_trip:
                //我的行程
                goToActivity(DriverTravelActivity.class);
                break;
            case R.id.rl_car:
                //我的车辆
                break;
            case R.id.rl_gift:
                //推荐有奖
                break;
            case R.id.rl_complain:
                //申诉中心

                break;
        }
    }
}
