package com.lfcx.driver.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfcx.driver.R;
import com.lfcx.driver.R2;
import com.lfcx.driver.service.LocationService;
import com.lfcx.driver.util.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DriverMainActivity extends DriverBaseActivity {

    @BindView(R2.id.d_iv_message)
    ImageView ivMessage;

    ImageView iv_user;
    TextView titleBar;
    ImageView iv_message;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_main);
        unbinder = ButterKnife.bind(this);
        LocationService.startService(this);
        init();
    }

    private void init() {
        checkFirstInApp();
        iv_user = (ImageView) findViewById(R.id.iv_user);
        iv_user.setVisibility(View.VISIBLE);
        iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtil.isLogin(DriverMainActivity.this)) {
                    goToActivity(DriverLoginActivity.class);

                }else {
                    goToActivity( DriverUserActivity.class);
                }
            }
        });
        titleBar = (TextView) findViewById(R.id.title_bar);
        titleBar.setText("雷风司机");
        iv_message = (ImageView) findViewById(R.id.d_iv_message);
        iv_message.setVisibility(View.VISIBLE);

    }


    @OnClick(R2.id.d_iv_message)
    public void onClickMessage(View view) {
        if (!UserUtil.isLogin(this)) {
            goToActivity(DriverLoginActivity.class);

        }else {
            goToActivity(DriverNewsActivity.class);
        }

    }

    @OnClick(R2.id.d_mode_select)
    public void onClickMode(View view) {
        if (!UserUtil.isLogin(this)) {
            goToActivity(DriverLoginActivity.class);

        }else {
            goToActivity(DriverModeActivity.class);
        }

    }

    @OnClick(R2.id.btn_start_work)
    public void onClickStart(View view) {
        if (!UserUtil.isLogin(this)) {
            goToActivity(DriverLoginActivity.class);

        }else {
            goToActivity(DriverOrderActivity.class);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkFirstInApp();
    }

    /**
     * 判断是否首次进入应用
     */
    private void checkFirstInApp() {
        if (!UserUtil.isLogin(this)) {
            goToActivity(DriverLoginActivity.class);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private long exitTime = 0;

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
