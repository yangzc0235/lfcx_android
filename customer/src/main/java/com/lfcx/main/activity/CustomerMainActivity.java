package com.lfcx.main.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static com.lfcx.common.utils.SPUtils.getParam;

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
    private SharingCarFragment mSharingCarFragment;
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
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.v("system---------->","刷新数据");
                if(mSharingCarFragment!=null){
                    mSharingCarFragment.refreshStartPos();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
        checkFirstInApp();
        checkIsSuccessCar();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //checkIsSuccessCar();
    }

    /**
     * 是否叫车成功
     */
    private void checkIsSuccessCar() {
        String car= (String) SPUtils.getParam(this,SPConstants.KEY_SUCCESS_CAR,"");
        if("true".equals(car)){
            goToActivity(CallCarSucessActivity.class);
        }
    }


    private void setAlias(String alias) {
        if (TextUtils.isEmpty(alias)) {
//            Toast.makeText(this, "别名为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
//            Toast.makeText(this, "别名为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
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
//            ExampleUtil.showToast(logs, getApplicationContext());
        }
    };


    /**
     * 判断是否首次进入应用
     */
    private void checkFirstInApp() {
        if (!UserUtil.isLogin(this)) {
            goToActivity(CustomerLoginActivity.class);
        }else {
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
            //JPushInterface.setAlias(getApplicationContext(),Integer.parseInt(rid), (String) SPUtils.getParam(this, SPConstants.KEY_CUSTOMER_MOBILE, ""));
//        JPushInterface.resumePush(getApplicationContext());
            setAlias((String) getParam(this, SPConstants.KEY_CUSTOMER_MOBILE, ""));
        }
    }

    public void setiBackListener(IBackListener iBackListener) {
        this.iBackListener = iBackListener;
    }

    private void init() {
        Fragment f1 = new CustomerIndexFragment();
        fragments.add(f1);
        mSharingCarFragment = new SharingCarFragment();
        fragments.add(mSharingCarFragment);
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


