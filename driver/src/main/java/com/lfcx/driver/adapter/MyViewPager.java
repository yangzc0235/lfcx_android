package com.lfcx.driver.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by LaiYingtang on 2016/5/22.
 * 主页面左右滑动
 */
public class MyViewPager extends ViewPager {
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewPager(Context context) {
        super(context);
    }
    //判断menu在x,y的位置
    public void scrollTo(int x,int y){
        if(getAdapter()==null||x>getWidth()*(getAdapter().getCount()-2)){
            return;
        }
        super.scrollTo(x,y);
    }

}
