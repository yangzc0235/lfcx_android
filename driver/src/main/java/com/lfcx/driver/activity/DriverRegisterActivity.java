package com.lfcx.driver.activity;

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

import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.StringUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.consts.Constants;
import com.lfcx.driver.net.NetConfig;
import com.lfcx.driver.net.api.DUserAPI;
import com.lfcx.driver.net.result.SendMessageResult;
import com.lfcx.driver.util.EdtUtil;
import com.lfcx.driver.util.StringEmptyUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRegisterActivity extends DriverBaseActivity implements View.OnClickListener {
    private String checkCode;
    private DUserAPI userAPI;
    private ImageView mIvBack;
    private ImageView mIvUser;
    private TextView mTitleBar;
    private ImageView mDIvMessage;
    private EditText mEtPhone;
    private EditText mEtCode;
    private Button mBtnGetcode;
    private EditText mEtPwd;
    private EditText mEtConfirmPwd;
    private EditText mEtIntroducePhone;
    private Button mBtnRegister;

    private int count = 60;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mBtnGetcode.setText((--count)+"s");
            if(count >= 0 ){
                mHandler.sendEmptyMessageDelayed(0,1000);
            }else {
                mBtnGetcode.setText("获取验证码");
                mBtnGetcode.setEnabled(true);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_register_activity);
        userAPI = APIFactory.create(DUserAPI.class);
        init();
    }

    private void init() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvUser = (ImageView) findViewById(R.id.iv_user);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        mDIvMessage = (ImageView) findViewById(R.id.d_iv_message);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtCode = (EditText) findViewById(R.id.et_code);
        mBtnGetcode = (Button) findViewById(R.id.btn_getcode);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mEtConfirmPwd = (EditText) findViewById(R.id.et_confirm_pwd);
        mEtIntroducePhone = (EditText) findViewById(R.id.et_introduce_phone);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mTitleBar.setText("注册用户");
        mBtnRegister.setOnClickListener(this);
        mBtnGetcode.setOnClickListener(this);
        mBtnGetcode.setEnabled(true);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_getcode) {
            String mobile= EdtUtil.getEdtText(mEtPhone);
            if (mBtnGetcode.isEnabled()) {
                String phone = EdtUtil.getEdtText(mEtPhone);
                if (TextUtils.isEmpty(phone) && phone.length() < 11) {
                    showToast("请输入完整的手机号");
                    return;
                }
                sendCheckCode(phone);
                mHandler.sendEmptyMessage(0);
                mBtnGetcode.setEnabled(false);
            }

        } else if (i == R.id.btn_register) {
            checkRegister();
        }
    }

    //获取验证码
    private void sendCheckCode(String phone){
        Map<String,Object> param = new HashMap<>();
        param.put("mobile",phone);
        param.put("key", Constants.D_SEND_CHECKCODE_KEY);
        param.put("tpl_id",Constants.D_REGIST_CHECKCODE_MESSAGE_ID);
        checkCode = StringUtils.generateCheckCode();
        param.put("tpl_value","%23code%23%3D"+checkCode);
        userAPI.customerSendMessage(NetConfig.DRIVER_SEND_MESSAGE,param).enqueue(new Callback<SendMessageResult>() {
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


    /**
     * 检查手机号和密码
     */
    private void checkRegister() {
        String moible =EdtUtil.getEdtText(mEtPhone);
        String pwd = EdtUtil.getEdtText(mEtPwd);
        String againPwd = EdtUtil.getEdtText(mEtConfirmPwd);
        String code = EdtUtil.getEdtText(mEtCode);
        String recommandMobile=EdtUtil.getEdtText(mEtIntroducePhone);
        if (TextUtils.isEmpty(moible)) {
            showToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }
        if(!pwd.equals(againPwd)){
            showToast("两次输入密码不一致");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            showToast("请输入验证码");
            return;
        }
        if(StringEmptyUtil.isEmpty(checkCode)){
            Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.v("checkCode---------->",checkCode);
        if (!checkCode.equals(code)) {
            showToast("验证码输入错误");
            return;
        }
        Bundle bundle=new Bundle();
        bundle.putString("mobile",moible);
        bundle.putString("pwd",pwd);
        bundle.putString("recommandMobile",recommandMobile);
        goToActivity(DriverRegistAfterActivity.class,bundle);

    }

    @Override
    public void onDestroy(){
        mHandler.removeMessages(0);
        mHandler = null;
        super.onDestroy();
    }


}
