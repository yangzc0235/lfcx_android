package com.lfcx.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.consts.SPConstants;
import com.lfcx.customer.event.EventUtil;
import com.lfcx.customer.net.api.UserAPI;
import com.lfcx.customer.net.result.LoginResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * des   : 乘客登录页面
 */
public class CustomerLoginActivity extends CustomerBaseActivity {

    @BindView(R2.id.btn_login)
    Button btnLogin;
    @BindView(R2.id.tv_register)
    TextView tvRegister;

    @BindView(R2.id.et_phone)
    EditText etPhone;
    @BindView(R2.id.et_pwd)
    EditText etPwd;

    Unbinder unbinder;
    private UserAPI userAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_customer_login);
        unbinder = ButterKnife.bind(this);
        userAPI = APIFactory.create(UserAPI.class);
    }

    @OnClick({R2.id.btn_login, R2.id.tv_register})
    public void onClick(View view) {
        Intent intent;
        int i = view.getId();
        if (i == R.id.btn_login) {
            String moible = etPhone.getText().toString().trim();
            String pwd = etPwd.getText().toString().trim();
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
        }
    }

    private void goLogin(final String moible, final String pwd) {
        Map<String, String> param = new HashMap<>();
        param.put("user", moible);
        param.put("pwd", pwd);
        param.put("isdriver", "0");
//        showLoading();
        userAPI.customerLogin(param).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
//                hideLoading();
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
        unbinder.unbind();
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



