package com.lfcx.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.R2;
import com.lfcx.driver.consts.SPConstants;
import com.lfcx.driver.net.api.DUserAPI;
import com.lfcx.driver.net.result.LoginResult;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   : 司机登录页面
 */
public class DriverLoginActivity extends DriverBaseActivity implements View.OnClickListener {

    @BindView(R2.id.btn_login)
    Button btnLogin;
    @BindView(R2.id.tv_register)
    TextView tvRegister;

    Unbinder unbinder;
    private ImageView mIvIcon;
    private ImageView mIvUser;
    private EditText mEtPhone;
    private ImageView mIvPwd;
    private EditText mEtPwd;
    private Button mBtnLogin;
    private TextView mTvRegister;
    private TextView mTvForget;
    private ImageView mImgvSelectActivityCardRegister;
    private TextView mTvLmAgreenmentActivityCardRegister;
    private ImageView mImgvClearNumActivityLogin;
    private ImageView mImgvEyeActivityLogin;
    private DUserAPI userAPI;
    private boolean mHidePasswd = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_login);
        unbinder = ButterKnife.bind(this);
        userAPI = APIFactory.create(DUserAPI.class);
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        mIvUser = (ImageView) findViewById(R.id.iv_user);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mIvPwd = (ImageView) findViewById(R.id.iv_pwd);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mTvRegister = (TextView) findViewById(R.id.tv_register);
        mTvForget = (TextView) findViewById(R.id.tv_forget);
        mImgvClearNumActivityLogin = (ImageView) findViewById(R.id.imgv_clear_num_activity_login);
        mImgvEyeActivityLogin = (ImageView) findViewById(R.id.imgv_eye_activity_login);
        mImgvSelectActivityCardRegister = (ImageView) findViewById(R.id.imgv_select_activity_card_register);
        mTvLmAgreenmentActivityCardRegister = (TextView) findViewById(R.id.tv_lm_agreenment_activity_card_register);
        mImgvEyeActivityLogin.setOnClickListener(this);
        mImgvClearNumActivityLogin.setOnClickListener(this);
    }

    @OnClick(R2.id.btn_login)
    public void onClickLogin(View v) {
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

    }

    @OnClick(R2.id.tv_register)
    public void onClickGoRegister(View v) {
        Intent intent = new Intent(this, DriverRegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.tv_forget)
    public void onClickGoForgetPwd(View v) {
        Intent intent = new Intent(this, DriverForgotPwdActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * 登录
     *
     * @param moible
     * @param pwd
     */
    private void goLogin(final String moible, final String pwd) {
        showLoading();
        Map<String, String> param = new HashMap<>();
        param.put("user", moible);
        param.put("pwd", pwd);
        param.put("isdriver", "1");
        userAPI.customerLogin(param).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                hideLoading();
                LoginResult result = null;
                try {
                    result = response.body();
                    Log.v("system------->", response.body().toString());
                    if ("0".equals(result.getCode())) {
                        SPUtils.setParam(DriverLoginActivity.this, SPConstants.KEY_DRIVER_PK_USER, result.getPk_user());
                        SPUtils.setParam(DriverLoginActivity.this, SPConstants.DRIVER_MOBILE, moible);
                        SPUtils.setParam(DriverLoginActivity.this, SPConstants.KEY_DRIVER_PWD, pwd);
                        finish();
                    } else {
                        showToast(result.getMsg());
                    }

                } catch (Exception e) {
                    LogUtils.e(DriverLoginActivity.this.getClass().getSimpleName(), e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                 hideLoading();
                LogUtils.e(DriverLoginActivity.this.getClass().getSimpleName(), t.getMessage());
                showToast("网络错误，请稍后再试!");
            }
        });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.imgv_clear_num_activity_login) {
            mEtPhone.setText("");
        } else if (i == R.id.imgv_eye_activity_login) {
            isHidePwd();
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
}



