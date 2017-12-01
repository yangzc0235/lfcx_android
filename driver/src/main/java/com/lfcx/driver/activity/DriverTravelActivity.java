package com.lfcx.driver.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfcx.common.utils.ViewUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.adapter.CustomerMainFragmentPagerAdapter;
import com.lfcx.driver.fragment.FinishPaymentFragment;
import com.lfcx.driver.fragment.WaitPaymentFragment;
import com.lfcx.driver.widget.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class DriverTravelActivity extends DriverBaseActivity {
    private FinishPaymentFragment mFinishPaymentFragment;
    private WaitPaymentFragment mWaitPaymentFragment;
    private TabLayout mTabLayout;
    private NoScrollViewPager mViewPager;
    private ImageView mIvBack;
    private ImageView mIvUser;
    private TextView mTitleBar;
    private CustomerMainFragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_travel);
        initViews();
    }
    protected void initViews() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvUser = (ImageView) findViewById(R.id.iv_user);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        initDatas();
    }



    protected void initDatas() {
        mTitleBar.setText("我的行程");
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                ViewUtils.setIndicator(mTabLayout, 60, 60);
            }
        });
        fragments = new ArrayList<>();
        mFinishPaymentFragment = new FinishPaymentFragment();
        mWaitPaymentFragment = new WaitPaymentFragment();
        fragments.add(mWaitPaymentFragment);
        fragments.add(mFinishPaymentFragment);
        fragmentPagerAdapter = new CustomerMainFragmentPagerAdapter(getSupportFragmentManager(), this, fragments);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
