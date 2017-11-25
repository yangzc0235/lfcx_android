package com.lfcx.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.common.utils.LogUtils;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.common.utils.StringUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.R2;
import com.lfcx.driver.consts.Constants;
import com.lfcx.driver.consts.SPConstants;
import com.lfcx.driver.net.NetConfig;
import com.lfcx.driver.net.api.DUserAPI;
import com.lfcx.driver.net.result.SendMessageResult;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverForgotPwdActivity extends DriverBaseActivity {
    public static final String TAG = DriverForgotPwdActivity.class.getSimpleName();


    @BindView(R2.id.btn_getcode)
    Button btnCode;
    @BindView(R2.id.et_pwd)
    EditText etPwd;
    @BindView(R2.id.et_phone)
    EditText etPhone;
    @BindView(R2.id.et_code)
    EditText etCode;
    private ImageView mIvBack;
    private TextView mTitleBar;
    private DUserAPI userAPI;
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
        setContentView(R.layout.activity_driver_forget_pwd);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        mIvBack.setVisibility(View.VISIBLE);
        mTitleBar.setText("忘记密码");
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        unbinder =  ButterKnife.bind(this);
        userAPI = APIFactory.create(DUserAPI.class);
    }

    @OnClick({R2.id.btn_register, R2.id.btn_getcode})
    public void onClick(View view) {
        Intent intent;
        int i = view.getId();
        if (i == R.id.btn_register) {
            String moible = etPhone.getText().toString().trim();
            String pwd = etPwd.getText().toString().trim();
            String code = etCode.getText().toString().trim();
            if (TextUtils.isEmpty(moible)) {
                showToast("请输入手机号");
                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                showToast("请输入密码");
                return;
            }
            if (TextUtils.isEmpty(code)) {
                showToast("请输入验证码");
                return;
            }

            Log.v("checkCode---------->",checkCode);
            if (!checkCode.equals(code)) {
                showToast("验证码输入错误");
                return;
            }
            requestForgetPwd(moible, pwd);

        } else if (i == R.id.tv_gologin) {
            finish();
        } else if (i == R.id.btn_getcode) {
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

        }
    }

    /**
     * 完成修改密码
     * @param moible
     * @param pwd
     */
    private void requestForgetPwd(final String moible, final String pwd){
        showLoading();
        Map<String,String> param = new HashMap<>();
        param.put("mobile",moible);
        param.put("pwd",pwd);
        userAPI.fortgetPwd(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                try {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        SPUtils.setParam(DriverForgotPwdActivity.this, SPConstants.DRIVER_MOBILE,etPhone.getText().toString());
                        Toast.makeText(DriverForgotPwdActivity.this, res.getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
//                        goLogin(moible,pwd);
                    }else {
                        showToast(res.getMsg());
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
            }
        });
    }

    /**
     * 获取验证码
     * @param phone
     */
    private void sendCheckCode(String phone){
        showLoading();
        Map<String,Object> param = new HashMap<>();
        param.put("mobile",phone);
        param.put("key", Constants.D_CHECK_ID_CARD_KEY);
        param.put("tpl_id", Constants.D_REGIST_CHECKCODE_MESSAGE_ID);
        checkCode = StringUtils.generateCheckCode();
        param.put("tpl_value","%23code%23%3D"+checkCode);
        userAPI.customerSendMessage(NetConfig.DRIVER_SEND_MESSAGE,param).enqueue(new Callback<SendMessageResult>() {
            @Override
            public void onResponse(Call<SendMessageResult> call, Response<SendMessageResult> response) {
                if(response.body().getError_code() == 0){
                    hideLoading();
                    showToast("验证码已发送成功，请注意查收");
                }
            }

            @Override
            public void onFailure(Call<SendMessageResult> call, Throwable t) {
                hideLoading();
                Toast.makeText(DriverForgotPwdActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
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
