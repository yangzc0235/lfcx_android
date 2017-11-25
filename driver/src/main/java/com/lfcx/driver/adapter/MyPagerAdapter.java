package com.lfcx.driver.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 分页显示
 * Created by LaiYingtang on 2016/5/24.
 */
public class MyPagerAdapter extends PagerAdapter {
    private List<View> viewList;//创建view集合

    public MyPagerAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        if(viewList!=null){
            return viewList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    /**
     * 每次滑动pager的position
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(View container, int position) {
        ((MyViewPager)container).addView(viewList.get(position),0);
        return viewList.get(position);
    }

    /**
     *删除item
     * 当滑动到第三个item时，第一个item就会被destroy
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((MyViewPager)container).removeView(viewList.get(position));
    }

    /**
     * pager的宽度
     * @param position
     * @return
     */
    @Override
    public float getPageWidth(int position) {
        return 0.5f;
    }
}
