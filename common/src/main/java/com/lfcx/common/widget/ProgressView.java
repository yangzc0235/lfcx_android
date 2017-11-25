package com.lfcx.common.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.lfcx.common.R;


/**
 * Created by yzc on 2016/11/25.
 */
public class ProgressView extends ImageView {
    public ProgressView(Context context) {
        super(context);
        initView();
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context,attrs);
        initView();
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        initView();
    }

    private void initView() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                AnimationDrawable drawable = (AnimationDrawable) getContext().getResources().getDrawable(R.drawable.anim_dialog_progress);
                setImageDrawable(drawable);
                if (drawable != null) {
                    drawable.start();
                }
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }
    }

