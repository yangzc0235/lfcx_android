package com.lfcx.driver.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfcx.common.utils.SPUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.R2;
import com.lfcx.driver.consts.SPConstants;
import com.lfcx.driver.dialog.ExitLoginDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DriverUserActivity extends DriverBaseActivity implements View.OnClickListener {

    @BindView(R2.id.title_bar)
    TextView title_bar;
    private Unbinder unbinder;
    ImageView iv_back;
    private TextView mTvDriverMobile;
    private TextView mTvExitLoginActivityCustomerPersonCenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_driver_user);
        mTvDriverMobile = (TextView) findViewById(R.id.tv_driver_mobile);
        mTvExitLoginActivityCustomerPersonCenter = (TextView) findViewById(R.id.tv_exit_login_activity_customer_person_center);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mTvExitLoginActivityCustomerPersonCenter.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        unbinder = ButterKnife.bind(this);
        init();
    }
    private void init(){
        title_bar.setText("个人中心");
        mTvDriverMobile.setText((CharSequence) SPUtils.getParam(this, SPConstants.DRIVER_MOBILE,""));
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.tv_exit_login_activity_customer_person_center:
                //退出登录
                ExitLoginDialog exitLoginDialog=new ExitLoginDialog(this, R.style.customDialog);
                exitLoginDialog.show();
                break;
        }
    }
}
