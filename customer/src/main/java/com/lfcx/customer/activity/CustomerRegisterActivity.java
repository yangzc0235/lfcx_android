package com.lfcx.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.common.utils.StringUtils;
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.consts.Constants;
import com.lfcx.customer.consts.SPConstants;
import com.lfcx.customer.net.NetConfig;
import com.lfcx.customer.net.api.UserAPI;
import com.lfcx.customer.net.result.SendMessageResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRegisterActivity extends CustomerBaseActivity {
    public static final String TAG = CustomerRegisterActivity.class.getSimpleName();

    @BindView(R2.id.tv_gologin)
    TextView tvLogin;
    @BindView(R2.id.btn_getcode)
    TextView btnCode;

    @BindView(R2.id.et_pwd)
    EditText etPwd;
    @BindView(R2.id.et_phone)
    EditText etPhone;
    @BindView(R2.id.et_code)
    EditText etCode;
    @BindView(R2.id.title_bar)
    TextView title_bar;
    @BindView(R2.id.iv_back)
    ImageView btn_back;

    private UserAPI userAPI;

    private int count = 60;

    private String checkCode;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            btnCode.setText((--count)+"s");
            if(count >= 0 ){
                 mHandler.sendEmptyMessageDelayed(0,1000);
            }else {
                btnCode.setText("获取验证码");
                btnCode.setEnabled(true);
            }
        }
    };

    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);
        unbinder =  ButterKnife.bind(this);
        userAPI = APIFactory.create(UserAPI.class);
        title_bar.setText("注册用户");
        btn_back.setVisibility(View.VISIBLE);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerRegisterActivity.this, CustomerLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick({R2.id.btn_register,R2.id.tv_gologin,R2.id.btn_getcode,R2.id.iv_back})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R2.id.btn_register:
                String moible = etPhone.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                String code = etCode.getText().toString().trim();
                if(TextUtils.isEmpty(moible)){
                    showToast("请输入手机号");
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    showToast("请输入密码");
                    return;
                }
                if(TextUtils.isEmpty(code)){
                    showToast("请输入验证码");
                    return;
                }
                if(!"666666".equals(code)){
                    showToast("验证码输入错误");
                    return;
                }
                goRegist(moible,pwd);
                break;
            case R2.id.tv_gologin:
                intent = new Intent(this, CustomerLoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R2.id.btn_getcode:
                if (btnCode.isEnabled()) {
                    String phone = etPhone.getText().toString().trim();
                    if (TextUtils.isEmpty(phone) && phone.length() < 11) {
                        showToast("请输入完整的手机号");
                        return;
                    }
                    sendCheckCode(phone);
                    mHandler.sendEmptyMessage(0);
                    btnCode.setEnabled(false);
                }
                break;
        }
    }

    private void goRegist(String moible,String pwd){
        showLoading();
        Map<String,String> param = new HashMap<>();
        param.put("mobile",moible);
        param.put("pwd",pwd);
        userAPI.customerRegist(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    hideLoading();
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        showToast("注册成功");
                        SPUtils.setParam(CustomerRegisterActivity.this, SPConstants.KEY_CUSTOMER_MOBILE,etPhone.getText().toString());
                        Intent intent = new Intent(CustomerRegisterActivity.this, CustomerLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        showToast(res.getMsg());
                    }
                } catch (Exception e) {
                    hideLoading();
                    LogUtils.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
            }
        });
    }

    private void sendCheckCode(String phone){
        Map<String,Object> param = new HashMap<>();
        param.put("mobile",phone);
        param.put("key", Constants.C_SEND_CHECKCODE_KEY);
        param.put("tpl_id",Constants.C_REGIST_CHECKCODE_MESSAGE_ID);
        checkCode = StringUtils.generateCheckCode();
        param.put("tpl_value","%23code%23%3D"+checkCode);
        userAPI.customerSendMessage(NetConfig.CUSTOMER_SEND_MESSAGE,param).enqueue(new Callback<SendMessageResult>() {
            @Override
            public void onResponse(Call<SendMessageResult> call, Response<SendMessageResult> response) {
                    if(response.body().getError_code() == 0){
                        showToast("验证码已发送成功，请注意查收");
                    }
            }

            @Override
            public void onFailure(Call<SendMessageResult> call, Throwable t) {

            }
        });
    }
    @Override
    public void onDestroy(){
        mHandler.removeMessages(0);
        mHandler = null;
        super.onDestroy();
        unbinder.unbind();
    }
}
