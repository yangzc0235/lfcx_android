package com.lfcx.main.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lfcx.common.utils.SPUtils;
import com.lfcx.common.utils.ViewUtils;
import com.lfcx.main.R;
import com.lfcx.main.adapter.CustomerMainFragmentPagerAdapter;
import com.lfcx.main.consts.SPConstants;
import com.lfcx.main.fragment.CustomerIndexFragment;
import com.lfcx.main.fragment.SharingCarFragment;
import com.lfcx.main.util.ExampleUtil;
import com.lfcx.main.util.UserUtil;
import com.lfcx.main.widget.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   : 乘客首页
 */
public class CustomerMainActivity extends CustomerBaseActivity {
    private IBackListener iBackListener;
    private CustomerMainFragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragments;
    public static boolean isForeground = false;
    private LinearLayout mActivityMain;
    private ImageView mIvBack;
    private ImageView mIvUser;
    private TextView mTitleBar;
    private TabLayout mTabLayout;
    private NoScrollViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_main);
        mActivityMain = (LinearLayout) findViewById(R.id.activity_main);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvUser = (ImageView) findViewById(R.id.iv_user);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        mIvUser.setVisibility(View.VISIBLE);
        mIvBack.setVisibility(View.GONE);
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                ViewUtils.setIndicator(mTabLayout, 60, 60);
            }
        });
        fragments = new ArrayList<>();
        init();
        fragmentPagerAdapter = new CustomerMainFragmentPagerAdapter(getSupportFragmentManager(), this, fragments);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        JPushInterface.setAlias(getApplicationContext(), 1, (String) SPUtils.getParam(this, SPConstants.KEY_CUSTOMER_MOBILE, ""));
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
        checkFirstInApp();
    }

    /**
     * 判断是否首次进入应用
     */
    private void checkFirstInApp() {
        if (!UserUtil.isLogin(this)) {
            goToActivity(CustomerLoginActivity.class);
        }
    }

    public void setiBackListener(IBackListener iBackListener) {
        this.iBackListener = iBackListener;
    }

    private void init() {
        Fragment f1 = new CustomerIndexFragment();
        fragments.add(f1);
//        Fragment f2 = new CharterCarFragment();
//        fragments.add(f2);
        Fragment f3 = new SharingCarFragment();
        fragments.add(f3);
        mIvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserUtil.isLogin(getApplicationContext())) {
                    goToActivity(CustomerLoginActivity.class);
                } else {
                    goToActivity(CustomerUserCenterActivity.class);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JPushInterface.stopPush(getApplicationContext());
    }

    public interface IBackListener {
        boolean onBack();
    }

    @Override
    public void onBackPressed() {
        //当实现了此接口的类onBack返回true的时候，证明它需要处理返回键，则此时Activty交出处理权
        if (null != iBackListener && iBackListener.onBack()) {
            return;
        }
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


