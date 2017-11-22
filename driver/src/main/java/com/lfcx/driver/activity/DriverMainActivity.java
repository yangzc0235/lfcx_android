package com.lfcx.driver.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.consts.SPConstants;
import com.lfcx.driver.event.ReceiptEvent;
import com.lfcx.driver.net.api.DriverCarAPI;
import com.lfcx.driver.net.result.CountInfoEntity;
import com.lfcx.driver.service.LocationService;
import com.lfcx.driver.util.ExampleUtil;
import com.lfcx.driver.util.UserUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverMainActivity extends DriverBaseActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private ImageView mIvUser;
    private TextView mTitleBar;
    private ImageView mDIvMessage;
    private TextView mTvSevicePoint;
    private TextView mTvTodayRecepiet;
    private TextView mTvTodayIncome;
    private Button mTvRewards;
    private Button mTvRlt;
    private TextView mTvNews;
    private LinearLayout mDModeSelect;
    private Button mBtnStartWork;
    private long exitTime = 0;
    private DriverCarAPI mDriverCarAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.d_activity_main);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvUser = (ImageView) findViewById(R.id.iv_user);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        mDIvMessage = (ImageView) findViewById(R.id.d_iv_message);
        mTvSevicePoint = (TextView) findViewById(R.id.tv_sevice_point);
        mTvTodayRecepiet = (TextView) findViewById(R.id.tv_today_recepiet);
        mTvTodayIncome = (TextView) findViewById(R.id.tv_today_income);
        mTvRewards = (Button) findViewById(R.id.tv_rewards);
        mTvRewards = (Button) findViewById(R.id.tv_rewards);
        mTvRlt = (Button) findViewById(R.id.tv_rlt);
        mTvNews = (TextView) findViewById(R.id.tv_news);
        mDModeSelect = (LinearLayout) findViewById(R.id.d_mode_select);
        mBtnStartWork = (Button) findViewById(R.id.btn_start_work);
        //每三分钟上传一次司机的位置
        LocationService.startService(this);
        init();
    }

    private void init() {
        mDriverCarAPI = APIFactory.create(DriverCarAPI.class);
        checkFirstInApp();
        mIvUser.setVisibility(View.VISIBLE);
        mIvUser.setOnClickListener(this);
        mTvNews.setOnClickListener(this);
        mBtnStartWork.setOnClickListener(this);
        mDModeSelect.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mDIvMessage.setOnClickListener(this);
        mTvRewards.setOnClickListener(this);
        mTvRlt.setOnClickListener(this);
        mBtnStartWork.setText("点击出车");
        mTitleBar.setText("雷风司机");
        mDIvMessage.setVisibility(View.VISIBLE);
        JPushInterface.setAlias(getApplicationContext(), 1, (String) SPUtils.getParam(this, SPConstants.DRIVER_MOBILE, ""));
        JPushInterface.resumePush(getApplicationContext());
        String appKey = ExampleUtil.getAppKey(getApplicationContext());
        String packageName = getPackageName();
        String deviceId = ExampleUtil.getDeviceId(getApplicationContext());
        String udid = ExampleUtil.getImei(getApplicationContext(), "");
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        Log.v("system--appKey---->", appKey);
        Log.v("system---pac--->", packageName);
        Log.v("system---deviceId--->", deviceId);
        Log.v("system---udid--->", udid);
        Log.v("system---rid--->", rid);
        String param = (String) SPUtils.getParam(this, SPConstants.KEY_DRIVER_PK_USER, "");
        if(param!=null){
            getCurrentCountInfo(param);
        }
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    //在ui线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReceiptEvent event) {
        if (event.getKey().equals("startReceipt") && mBtnStartWork.getText().toString().trim().equals("正在接单中")) {
            Bundle bundle = new Bundle();
            bundle.putString("orderInfo", event.getValue());
            goToActivity(DriverOrderActivity.class, bundle);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_work:
                if (!UserUtil.isLogin(this)) {
                    goToActivity(DriverLoginActivity.class);
                } else {
                    if (mBtnStartWork.getText().toString().trim().equals("点击出车")) {
                        mBtnStartWork.setText("正在接单中");
                    }
                }
                break;

            case R.id.d_mode_select:
                if (!UserUtil.isLogin(this)) {
                    goToActivity(DriverLoginActivity.class);

                } else {
                    goToActivity(DriverModeActivity.class);
                }

                break;

            case R.id.d_iv_message:
                if (!UserUtil.isLogin(this)) {
                    goToActivity(DriverLoginActivity.class);

                } else {
                    goToActivity(DriverNewsActivity.class);
                }

                break;
            case R.id.iv_user:
                if (!UserUtil.isLogin(DriverMainActivity.this)) {
                    goToActivity(DriverLoginActivity.class);

                } else {
                    goToActivity(DriverUserActivity.class);
                }
                break;

            case R.id.tv_rewards:
                if (!UserUtil.isLogin(DriverMainActivity.this)) {
                    goToActivity(DriverLoginActivity.class);

                } else {
                    //奖励
                    goToActivity(DriverGPSNaviActivity.class);
                }
                break;

            case R.id.tv_rlt:
                if (!UserUtil.isLogin(DriverMainActivity.this)) {
                    goToActivity(DriverLoginActivity.class);

                } else {
                    //热力图
                    goToActivity(DriverHeatMapActivity.class);
                }
                break;
        }
    }

    /**
     * 获取司机接单信息
     * @param pk_userdriver
     */
    private void getCurrentCountInfo(String pk_userdriver) {
        Map<String, Object> param = new HashMap<>();
        param.put("pk_userdriver", pk_userdriver);
        Log.v("system--pk_userdriver->", pk_userdriver);
        mDriverCarAPI.getCurrentCountInfo(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    Log.v("system---接单信息--->", response.body());
                    try {
                        CountInfoEntity res = new Gson().fromJson(response.body(), CountInfoEntity.class);
                        if ("0".equals(res.getCode())) {
                            mTvTodayRecepiet.setText(res.getAcceptCount()+"");
                            mTvTodayIncome.setText(res.getInCome()+"");
                        } else {
                            showToast(res.getMsg());
                        }
                    } catch (Exception e) {
                        showToast("服务器异常");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("获取信息失败！！");
            }
        });
    }

}
