package com.lfcx.customer.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.lfcx.common.utils.ViewUtils;
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.adapter.CustomerMainFragmentPagerAdapter;
import com.lfcx.customer.fragment.CustomerIndexFragment;
import com.lfcx.customer.fragment.SharingCarFragment;
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
        JPushInterface.setAlias(getApplicationContext(),1,"18511004876");
        JPushInterface.resumePush(getApplicationContext());
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
            goToActivity(CustomerPersonCenterActivity.class);
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
}

