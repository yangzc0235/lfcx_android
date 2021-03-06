package com.lfcx.driver.activity;

import android.os.Bundle;
import android.os.Handler;
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
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lfcx.common.utils.SPUtils.getParam;

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
    private TextView mTvClose;
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
        mTvClose = (TextView) findViewById(R.id.tv_close);
        //每1分钟上传一次司机的位置
        LocationService.startService(this);
        init();
    }

    private void init() {
        mDriverCarAPI = APIFactory.create(DriverCarAPI.class);
        checkFirstInApp();
        mIvUser.setVisibility(View.VISIBLE);
        mIvUser.setOnClickListener(this);
        mTvNews.setOnClickListener(this);
        //mBtnStartWork.setOnClickListener(this);
        mDModeSelect.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mDIvMessage.setOnClickListener(this);
        mTvRewards.setOnClickListener(this);
        mTvRlt.setOnClickListener(this);
        mTvClose.setOnClickListener(this);
        mTitleBar.setText("雷风司机");
        mDIvMessage.setVisibility(View.VISIBLE);

    }

    /**
     * 设置别名
     *
     * @param alias
     */
    private void setAlias(String alias) {
        if (TextUtils.isEmpty(alias)) {
            Log.v("system------>", "别名设置失败");
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            return;
        }

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
            }
            //ExampleUtil.showToast(logs, getApplicationContext());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
            }
        }
    };

    /**
     * 判断是否首次进入应用
     */
    private void checkFirstInApp() {
        if (!UserUtil.isLogin(this)) {
            goToActivity(DriverLoginActivity.class);
        } else {
            setAlias((String) getParam(this, SPConstants.DRIVER_MOBILE, ""));
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
            String param = (String) getParam(this, SPConstants.KEY_DRIVER_PK_USER, "");
            if (param != null) {
                getCurrentCountInfo(param);
            }
        }
//        checkReceiptSuccess();
    }

    //是否接单成功
    private void checkReceiptSuccess() {
        String receipt = (String) SPUtils.getParam(this, SPConstants.KEY_RECEIPT_SUCCESS, "");
        if ("true".equals(receipt)) {
            goToActivity(DriverOrderActivity.class);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //checkFirstInApp();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_work:

                //开始接单
                if (!UserUtil.isLogin(this)) {
                    goToActivity(DriverLoginActivity.class);
                } else {
                    mTts.startSpeaking("您已开始接单,请注意查收订单", mTtsListener);
                    goToActivity(DriverOrderActivity.class);
                }
                break;

            case R.id.d_mode_select:
                //模式选择
                if (!UserUtil.isLogin(this)) {
                    goToActivity(DriverLoginActivity.class);

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("setmode", "setmode");
                    goToActivity(DriverHeatMapActivity.class, bundle);

                }

                break;

            case R.id.d_iv_message:
                //消息
                if (!UserUtil.isLogin(this)) {
                    goToActivity(DriverLoginActivity.class);

                } else {
                    goToActivity(DriverNewsActivity.class);
                }

                break;
            case R.id.iv_user:
                //个人中心
                if (!UserUtil.isLogin(DriverMainActivity.this)) {
                    goToActivity(DriverLoginActivity.class);

                } else {
                    goToActivity(DriverUserActivity.class);
                }
                break;

            case R.id.tv_rewards:

                //奖励
                if (!UserUtil.isLogin(DriverMainActivity.this)) {
                    goToActivity(DriverLoginActivity.class);

                } else {
                    //奖励
//                    goToActivity(DriverGPSNaviActivity.class);
                    goToActivity(OverviewModeActivity.class);

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
            case R.id.tv_close:
                finish();
                System.exit(0);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReceiptEvent event) {
        switch (event.getKey()) {
            case "start":
                Bundle bundle = new Bundle();
                bundle.putString("startReceipt", event.getValue());
                goToActivity(DriverOrderActivity.class, bundle);
                break;
            case "finsish_car":
                finish();
                System.exit(0);
                break;
        }
    }

    /**
     * 获取司机接单信息
     *
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
                            mTvTodayRecepiet.setText(res.getAcceptCount() + "");
                            mTvTodayIncome.setText(res.getInCome() + "");
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
