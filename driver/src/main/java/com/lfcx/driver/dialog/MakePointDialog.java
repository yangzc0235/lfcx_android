package com.lfcx.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfcx.driver.R;


/**
 * Created by yzc on 2016/12/3.
 * 自定义验证码dialog
 */
public class MakePointDialog extends Dialog implements View.OnClickListener {

    private TextView mTvDistance;
    private ImageView mIvNowAddress;
    private EditText mEtStartAddress;
    private ImageView mIvToAddress;
    private EditText mEtEndAddress;
    private TextView mTvAll;
    private TextView mTvGrab;
    private TextView mTvCollect;



    private String mMobile;
    private Context mContext;
    private TextView mTextView;
    private CheckOnclickLisetner mCheckOnclickLisetner;
    public MakePointDialog(Context context) {
        super(context);
        mContext = context;
    }
    /**
     * 自定义布局的构造方法
     * @param context
     * @param moblie
     */
    public MakePointDialog(Context context, String moblie){
        super(context);
        mContext = context;
        mMobile=moblie;
    }
    /**
     * 自定义主题及布局的构造方法
     * @param context
     * @param theme
     * @param moblie
     */
    public MakePointDialog(Context context, int theme, String moblie, TextView textView){
        super(context, theme);
        mContext = context;
        mMobile=moblie;
        mTextView=textView;
    }


    public interface CheckOnclickLisetner extends View.OnClickListener {
        @Override
        void onClick(View v);
    }

    public void setOnclickLisetner(CheckOnclickLisetner lisjOnclickLisetner) {
        this.mCheckOnclickLisetner = lisjOnclickLisetner;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.make_point_dialog);
        setCancelable(false);
        mTvDistance = (TextView) findViewById(R.id.tv_distance);
        mIvNowAddress = (ImageView) findViewById(R.id.iv_now_address);
        mEtStartAddress = (EditText) findViewById(R.id.et_start_address);
        mIvToAddress = (ImageView) findViewById(R.id.iv_to_address);
        mEtEndAddress = (EditText) findViewById(R.id.et_end_address);
        mTvAll = (TextView) findViewById(R.id.tv_all);
        mTvGrab = (TextView) findViewById(R.id.tv_grab);
        mTvCollect = (TextView) findViewById(R.id.tv_collect);
        mTvGrab.setOnClickListener(this);
        mTvCollect.setOnClickListener(this);
        mTvAll.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_all) {//更换验证码


        } else if (i == R.id.tv_grab) {//取消
            dismiss();

        } else if (i == R.id.tv_collect) {//确定


        }
    }




}
