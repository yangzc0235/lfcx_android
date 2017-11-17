package com.lfcx.customer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lfcx.common.utils.ViewUtils;
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.adapter.CustomerMainFragmentPagerAdapter;
import com.lfcx.customer.fragment.CustomerIndexFragment;
import com.lfcx.customer.fragment.SharingCarFragment;
import com.lfcx.customer.util.ExampleUtil;
import com.lfcx.customer.util.UserUtil;
import com.lfcx.customer.widget.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   : 乘客首页
 */
public class CustomerMainActivity extends CustomerBaseActivity {

    @BindView(R2.id.viewPager)
    NoScrollViewPager vp;
    @BindView(R2.id.iv_user)
    ImageView ivUser;

    @BindView(R2.id.tabLayout)
    TabLayout tableLayout;

    private Unbinder unbinder;
    private IBackListener iBackListener;

    private CustomerMainFragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragments;
    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_main);
        checkFirstInApp();
        unbinder = ButterKnife.bind(this);
        ivUser.setVisibility(View.VISIBLE);
        tableLayout.post(new Runnable() {
            @Override
            public void run() {
                ViewUtils.setIndicator(tableLayout,60,60);
            }
        });
        fragments = new ArrayList<>();
        init();
        fragmentPagerAdapter = new CustomerMainFragmentPagerAdapter(getSupportFragmentManager(),this,fragments);
        vp.setAdapter(fragmentPagerAdapter);
        tableLayout.setupWithViewPager(vp);
//        JPushInterface.setAlias(getApplicationContext(),1,"18511004876");
//        JPushInterface.resumePush(getApplicationContext());
        String appKey = ExampleUtil.getAppKey(getApplicationContext());
        String packageName =  getPackageName();
        String deviceId = ExampleUtil.getDeviceId(getApplicationContext());
        String udid =  ExampleUtil.getImei(getApplicationContext(), "");
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        Log.v("system--appKey---->",appKey);
        Log.v("system---pac--->",packageName);
        Log.v("system---deviceId--->",deviceId);
        Log.v("system---udid--->",udid);
        Log.v("system---rid--->",rid);
    }

    /**
     * 判断是否首次进入应用
     */
    private void checkFirstInApp() {
        if(!UserUtil.isLogin(this)){
            goToActivity(CustomerLoginActivity.class);
        }
    }

    public void setiBackListener(IBackListener iBackListener) {
        this.iBackListener = iBackListener;
    }

    private void init(){
        Fragment f1 = new CustomerIndexFragment();
        fragments.add(f1);
//        Fragment f2 = new CharterCarFragment();
//        fragments.add(f2);
        Fragment f3 = new SharingCarFragment();
        fragments.add(f3);
    }

    @OnClick(R2.id.iv_user)
    public void onClickUser(View v){
        if(!UserUtil.isLogin(this)){
            goToActivity(CustomerLoginActivity.class);
        }else {
            goToActivity(CustomerUserCenterActivity.class);
        }

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        unbinder.unbind();
        JPushInterface.stopPush(getApplicationContext());
    }

    public interface IBackListener{
        boolean onBack();
    }

    @Override
    public void onBackPressed() {
        //当实现了此接口的类onBack返回true的时候，证明它需要处理返回键，则此时Activty交出处理权
        if(null != iBackListener && iBackListener.onBack()){
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

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!UserUtil.isLogin(this)){
           finish();
        }
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }




    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }

    private void setCostomMsg(String msg){

    }
}

