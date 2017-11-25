package com.lfcx.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.main.R;
import com.lfcx.main.consts.SPConstants;
import com.lfcx.main.event.EventUtil;
import com.lfcx.main.net.api.UserAPI;
import com.lfcx.main.net.result.LoginResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   : 乘客登录页面
 */
public class CustomerLoginActivity extends CustomerBaseActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private TextView mTitleBar;
    private ImageView mIvIcon;
    private ImageView mIvUser;
    private EditText mEtPhone;
    private ImageView mIvPwd;
    private EditText mEtPwd;
    private Button mBtnLogin;
    private TextView mTvRegister;
    private TextView mTvFindPwd;
    private UserAPI userAPI;
    private boolean mHidePasswd = true;
    private ImageView mImgvClearNumActivityLogin;
    private ImageView mImgvEyeActivityLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_customer_login);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvUser = (ImageView) findViewById(R.id.iv_user);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        mIvUser = (ImageView) findViewById(R.id.iv_user);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mImgvClearNumActivityLogin = (ImageView) findViewById(R.id.imgv_clear_num_activity_login);
        mIvPwd = (ImageView) findViewById(R.id.iv_pwd);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mImgvEyeActivityLogin = (ImageView) findViewById(R.id.imgv_eye_activity_login);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mTvRegister = (TextView) findViewById(R.id.tv_register);
        mTvFindPwd = (TextView) findViewById(R.id.tv_findPwd);
        mIvBack.setOnClickListener(this);
        mImgvClearNumActivityLogin.setOnClickListener(this);
        mImgvEyeActivityLogin.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvUser.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mTvFindPwd.setOnClickListener(this);
        userAPI = APIFactory.create(UserAPI.class);
    }


    public void onClick(View view) {
        Intent intent;
        int i = view.getId();
        if (i == R.id.btn_login) {
            String moible = mEtPhone.getText().toString().trim();
            String pwd = mEtPwd.getText().toString().trim();
            if (TextUtils.isEmpty(moible)) {
                showToast("请输入手机号");
                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                showToast("请输入密码");
                return;
            }
            goLogin(moible, pwd);

        } else if (i == R.id.tv_register) {
            goToActivity(CustomerRegisterActivity.class);
        } else if (i == R.id.imgv_clear_num_activity_login) {
            mEtPhone.setText("");
        } else if (i == R.id.imgv_eye_activity_login) {
            isHidePwd();
        } else if (i == R.id.iv_back) {
            finish();
        }else if (i == R.id.tv_findPwd) {
            goToActivity(CustomerForgotPwdActivity.class);
        }
    }


    /**
     * 是否影藏密码
     */
    public void isHidePwd() {
        if (mHidePasswd) {
            mImgvEyeActivityLogin.setBackgroundResource(R.mipmap.icon_visual);
            mHidePasswd = false;
            mEtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mImgvEyeActivityLogin.setBackgroundResource(R.mipmap.icon_notvisible);
            mHidePasswd = true;
        }
        //切换后将EditText光标置于末尾
        CharSequence charSequence = mEtPwd.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    /**
     * 登录
     *
     * @param moible
     * @param pwd
     */
    private void goLogin(final String moible, final String pwd) {
        Map<String, String> param = new HashMap<>();
        param.put("user", moible);
        param.put("pwd", pwd);
        param.put("isdriver", "0");
//        showLoading();
        userAPI.customerLogin(param).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                LoginResult result = null;
                try {
                    result = response.body();
                    LogUtils.e(CustomerLoginActivity.this.getClass().getSimpleName(), response.body().toString());
                    if ("0".equals(result.getCode())) {
                        SPUtils.setParam(CustomerLoginActivity.this, SPConstants.KEY_CUSTOMER_PK_USER, result.getPk_user());
                        SPUtils.setParam(CustomerLoginActivity.this, SPConstants.KEY_CUSTOMER_MOBILE, moible);
                        SPUtils.setParam(CustomerLoginActivity.this, SPConstants.KEY_CUSTOMER_PWD, pwd);
                        showToast(result.getMsg());
                        finish();
                    } else {
                        showToast(result.getMsg());
                    }

                } catch (Exception e) {
//                     hideLoading();
                    LogUtils.e(CustomerLoginActivity.this.getClass().getSimpleName(), e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
//                 hideLoading();
                LogUtils.e(CustomerLoginActivity.this.getClass().getSimpleName(), t.getMessage());
                showToast("网络错误，请稍后再试!");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //在ui线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventUtil event) {
        if (event.getMsg().equals("reg_success")) {
            finish();
        }
    }
}



