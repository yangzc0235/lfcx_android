package com.lfcx.main.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.lfcx.main.R;
import com.lfcx.main.activity.CustomerUserCenterActivity;
import com.lfcx.main.util.UserUtil;


/**
 * Created by yzc on 2016/12/3.
 * 解绑dialog
 */
public class ExitLoginDialog extends Dialog implements View.OnClickListener {
    private TextView mTvCancelDialogCheck;
    private TextView mTvOkDialogCheck;
    private TextView mTvHintDialogCheck;
    private CustomerUserCenterActivity mContext;

    public ExitLoginDialog(CustomerUserCenterActivity context) {
        super(context);
        mContext = context;
    }

    /**
     * 自定义主题及布局的构造方法
     *
     * @param context
     * @param theme
     */
    public ExitLoginDialog(CustomerUserCenterActivity context, int theme) {
        super(context, theme);
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.exit_login_dialog);
        mTvCancelDialogCheck = (TextView) findViewById(R.id.tv_cancel_dialog_check);
        mTvOkDialogCheck = (TextView) findViewById(R.id.tv_ok_dialog_check);
        mTvHintDialogCheck = (TextView) findViewById(R.id.tv_hint_dialog_check);
        mTvCancelDialogCheck.setOnClickListener(this);
        mTvOkDialogCheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel_dialog_check) {//取消
            dismiss();

        } else if (i == R.id.tv_ok_dialog_check) {//退出登录
            UserUtil.cleanUserInfo(mContext);
            dismiss();
            mContext.finish();

        }
    }



}
