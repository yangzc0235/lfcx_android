package com.lfcx.main.widget.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lfcx.common.utils.DensityUtils;
import com.lfcx.main.R;

/**
 * author: drawthink
 * desc  : (该类描述)
 */

public class CustomerCarPop implements View.OnClickListener{

    public static final int POSITION_ONE = 1;
    public static final int POSITION_TWO = 2;
    public static final int POSITION_THREE = 3;
    public static final int POSITION_FOUR = 4;

    private PopupWindow mPop;
    private Context ctx;
    private ICarSelectListener listener;

    private TextView tvCarOne;
    private TextView tvCarTwo;
    private TextView tvCarThree;
    private TextView tvCarFour;

    public CustomerCarPop(Context ctx){
        this.ctx = ctx;
        init();
    }

    public void setCarSelectListener(ICarSelectListener listener) {
        this.listener = listener;
    }

    public PopupWindow getmPop() {
        return mPop;
    }

    public void show(View parent, int x, int y, int grivity){
        if(mPop == null){
            init();
        }
        mPop.showAsDropDown(parent,x,y, grivity);
    }

    public void dissmiss(){
        if(mPop == null){
            return;
        }
        if (mPop.isShowing()){
            mPop.dismiss();
        }
    }

    public boolean isShowing(){
        return mPop.isShowing();
    }

    private void init(){
        View view = LayoutInflater.from(ctx).inflate(R.layout.c_car_select_pop,null,false);
        tvCarOne = (TextView) view.findViewById(R.id.tv_car_one);
        tvCarTwo = (TextView) view.findViewById(R.id.tv_car_two);
        tvCarThree = (TextView) view.findViewById(R.id.tv_car_three);
        tvCarFour = (TextView) view.findViewById(R.id.tv_car_four);

        tvCarOne.setOnClickListener(this);
        tvCarTwo.setOnClickListener(this);
        tvCarThree.setOnClickListener(this);
        tvCarFour.setOnClickListener(this);
        //主动去调用view的measure方法，不然在后面获取pop的宽高时，可能为0
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mPop = new PopupWindow(view, DensityUtils.dip2px(ctx,180),ViewGroup.LayoutParams.WRAP_CONTENT,true);
        mPop.setBackgroundDrawable(new BitmapDrawable());
        mPop.setOutsideTouchable(true);
        mPop.setFocusable(true);
    }

    @Override
    public void onClick(View v) {
        if(null == listener){
            return;
        }
       if(v.getId() == R.id.tv_car_one){
           listener.onSelected(tvCarOne,POSITION_ONE);
       }else  if(v.getId() == R.id.tv_car_two){
           listener.onSelected(tvCarTwo,POSITION_TWO);
       } else  if(v.getId() == R.id.tv_car_three){
           listener.onSelected(tvCarThree,POSITION_THREE);
       }else  if(v.getId() == R.id.tv_car_four){
           listener.onSelected(tvCarFour,POSITION_FOUR);
       }
    }

    public interface ICarSelectListener{
        void onSelected(View view,int position);
    }

}
