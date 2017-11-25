package com.lfcx.driver.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * 用于adapter显示的viewHolder
 * Created by LaiYingtang on 2016/5/24.
 */
public class ViewHolder {
    //无参构造
    public ViewHolder() {
    }

    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();//j节省内存，提高性能，使用SparseArray
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView=view.findViewById(id);
            viewHolder.put(id,childView);
        }
        return (T) childView;
    }
}
