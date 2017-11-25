package com.lfcx.driver.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lfcx.driver.R;
import com.lfcx.driver.adapter.LvItemActivityStatementAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzc on 2017/11/23.
 */

public class CarNumPopWindow extends PopupWindow {
    private View conentView;
    private Activity context;
    private LvItemActivityStatementAdapter lvItemActivityStatementAdapter;
    private List<StateEntity> mStateEntities = new ArrayList<>();
    private TextView mTextView;
    public CarNumPopWindow(final Activity context, final TextView textView) {
        super(context);
        this.context = context;
        this.initPopupWindow();
        mTextView=textView;
    }

    private void initPopupWindow() {
        //使用view来引入布局
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.more_popup_dialog, null);
        //获取popupwindow的高度与宽度
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(280);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果，设置动画，一会会讲解
        this.setAnimationStyle(R.style.AnimationPreview);
        //布局控件初始化与监听设置
        ListView lv = (ListView) conentView
                .findViewById(R.id.lv_pop_dialog);
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(context.getAssets().open(
                    "ProvinceCode.json"), "UTF-8");

            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            StateEntity stateEntity =null;
            JSONArray jsonArray=null;
            JSONObject jsonObject=null;
            try {
                jsonArray=new JSONArray(stringBuilder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);
                    String code = (String) jsonObject.get("code");
                    Log.v("system--code-->",code);
                    stateEntity=new StateEntity();
                    stateEntity.setCode(code);
                    mStateEntities.add(stateEntity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            lvItemActivityStatementAdapter = new LvItemActivityStatementAdapter(context, mStateEntities,mTextView,this);
            lv.setAdapter(lvItemActivityStatementAdapter);
            Log.i("system---所有车牌号", stringBuilder.toString());
            inputStreamReader.close();
            bufferedReader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 显示popupWindow的方式设置，当然可以有别的方式。
     * 一会会列出其他方法
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, -parent.getLayoutParams().width+600,-800);
        } else {
            this.dismiss();
        }
    }

}
