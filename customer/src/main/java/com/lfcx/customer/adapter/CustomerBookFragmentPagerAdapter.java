package com.lfcx.customer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   :  專車
 */
public class CustomerBookFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] titles = new String[]{"预约","包车","接机","送机"};
    private Context context;

    private List<Fragment> fragments;
    public CustomerBookFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return null ==  fragments ?0 :fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}