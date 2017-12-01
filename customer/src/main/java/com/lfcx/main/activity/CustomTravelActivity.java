package com.lfcx.main.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lfcx.common.utils.ViewUtils;
import com.lfcx.main.R;
import com.lfcx.main.adapter.CustomerOrderFragmentPagerAdapter;
import com.lfcx.main.event.EventUtil;
import com.lfcx.main.fragment.FinishPaymentFragment;
import com.lfcx.main.fragment.WaitPaymentFragment;
import com.lfcx.main.widget.view.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CustomTravelActivity extends CustomerBaseActivity {
    private FinishPaymentFragment mFinishPaymentFragment;
    private WaitPaymentFragment mWaitPaymentFragment;
    private TabLayout mTabLayout;
    private NoScrollViewPager mViewPager;
    private ImageView mIvBack;
    private ImageView mIvUser;
    private TextView mTitleBar;
    private CustomerOrderFragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_customer_travel);
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
        fragments.add(mFinishPaymentFragment);
        fragments.add(mWaitPaymentFragment);
        fragmentPagerAdapter = new CustomerOrderFragmentPagerAdapter(getSupportFragmentManager(), this, fragments);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //在ui线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventUtil event) {
        try {
            switch (event.getMsg()) {
                case "close_carsuccess":
                    finish();
                    goToActivity(CustomerMainActivity.class);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "发送消息异常", Toast.LENGTH_SHORT).show();
        }
    }
}
