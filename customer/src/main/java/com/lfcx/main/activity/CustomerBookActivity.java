package com.lfcx.main.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.lfcx.common.utils.ViewUtils;
import com.lfcx.main.R;
import com.lfcx.main.R2;
import com.lfcx.main.adapter.CustomerBookFragmentPagerAdapter;
import com.lfcx.main.fragment.book.AirFetchFragment;
import com.lfcx.main.fragment.book.AirSendFragment;
import com.lfcx.main.fragment.book.CharterCarFragment;
import com.lfcx.main.fragment.book.CustomerBookCarFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   : 预约界面
 */
public class CustomerBookActivity extends CustomerBaseActivity {

    @BindView(R2.id.iv_back)
    ImageView ivBack;

    @BindView(R2.id.viewPager)
    ViewPager vp;

    @BindView(R2.id.tabLayout)
    TabLayout tableLayout;

    private Unbinder unbinder;
    private CustomerBookFragmentPagerAdapter fragmentPagerAdapter;

    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_customer_book);
        unbinder = ButterKnife.bind(this);
        ivBack.setVisibility(View.VISIBLE);
        tableLayout.post(new Runnable() {
            @Override
            public void run() {
                ViewUtils.setIndicator(tableLayout,20,20);
            }
        });
        fragments = new ArrayList<>();
        init();
        fragmentPagerAdapter = new CustomerBookFragmentPagerAdapter(getSupportFragmentManager(),this,fragments);
        vp.setAdapter(fragmentPagerAdapter);
        tableLayout.setupWithViewPager(vp);
    }

    private void init(){
        Fragment f1 = new CustomerBookCarFragment();
        fragments.add(f1);
        Fragment f2 = new CharterCarFragment();
        fragments.add(f2);
        Fragment f3 = new AirFetchFragment();
        fragments.add(f3);
        Fragment f4 = new AirSendFragment();
        fragments.add(f4);
    }

    @OnClick({R2.id.iv_back})
    public void OnClick(View view){
        if(view.getId() == R.id.iv_back){
            finish();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unbinder.unbind();
    }
}
