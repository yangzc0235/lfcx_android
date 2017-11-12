package com.lfcx.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.common.widget.LoadingDialog;
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.consts.SPConstants;
import com.lfcx.customer.net.api.UserAPI;
import com.lfcx.customer.net.result.LoginResult;

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
    @BindView(R2.id.title_bar)
    TextView title_bar;

    Unbinder unbinder;
    private UserAPI userAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        unbinder = ButterKnife.bind(this);
        userAPI = APIFactory.create(UserAPI.class);
    }

    @OnClick({R2.id.btn_login,R2.id.tv_register})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R2.id.btn_login:
                String moble = etPhone.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                if(TextUtils.isEmpty(moble)){
                    showToast("请输入手机号");
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    showToast("请输入密码");
                    return;
                }
                goLogin(moble,pwd);
                break;
            case R2.id.tv_register:
                intent = new Intent(this, CustomerRegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void goLogin(final String moble, final String pwd){
        Map<String,String> param = new HashMap<>();
        param.put("user",moble);
        param.put("pwd",pwd);
        param.put("isdriver", "0");//普通用户登录为0
        showLoading();
        userAPI.customerLogin(param).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                hideLoading();
                LoginResult result = null;
                try{
                    result = response.body();
                    if("0".equals(result.getCode())){
                        SPUtils.setParam(CustomerLoginActivity.this, SPConstants.KEY_CUSTOMER_PK_USER,result.getPk_user());
                        SPUtils.setParam(CustomerLoginActivity.this, SPConstants.KEY_CUSTOMER_MOBILE,moble);
                        SPUtils.setParam(CustomerLoginActivity.this, SPConstants.KEY_CUSTOMER_PWD,pwd);
                        startActivity(new Intent(CustomerLoginActivity.this,CustomerMainActivity.class));
                        finish();
                    }else {
                        showToast(result.getMsg());
                    }

                }catch (Exception e){
                    hideLoading();
                    LogUtils.e(CustomerLoginActivity.this.getClass().getSimpleName(),e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                hideLoading();
                LogUtils.e(CustomerLoginActivity.this.getClass().getSimpleName(),t.getMessage());
                showToast("网络错误，请稍后再试!");
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}



