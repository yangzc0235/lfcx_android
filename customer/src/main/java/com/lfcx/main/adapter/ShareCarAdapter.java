package com.lfcx.main.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * author: drawthink
 * date  : 2017/10/9
 * des   :
 */

public class ShareCarAdapter extends PagerAdapter {
    private List<View> mViews;

    public ShareCarAdapter(List<View> mViews) {
        this.mViews = mViews;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return null == mViews ?0 : mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
