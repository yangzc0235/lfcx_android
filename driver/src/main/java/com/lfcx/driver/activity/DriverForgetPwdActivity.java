package com.lfcx.driver.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lfcx.driver.R;
import com.lfcx.driver.event.EventUtil;
import com.lfcx.driver.util.CommonUtil;
import com.lfcx.driver.util.EdtUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 忘记密码界面
 */
public class DriverForgetPwdActivity extends DriverBaseActivity implements View.OnClickListener{
    private EditText mEdtPhoneActivityPhoneNumCheck;
    private EditText mEdtPwdActivityPhoneNumCheck;
    private TextView mTvYzmActivityPhoneNumCheck;
    private Button mBtCommitActivityPhoneNumCheck;
    private int countDownTime = 60;
    private final int COUNT_DOWN_TASK = 20001;
    private final int COUNT_DOWN_OVER = 10001;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNT_DOWN_TASK:
                    //点击获取验证码后的倒计时任务
                    mTvYzmActivityPhoneNumCheck.setText(msg.arg1 + "s");
                    if (countDownTime > 0) {
                        Message message = new Message();
                        countDownTime -= 1;
                        message.what = COUNT_DOWN_TASK;
                        message.arg1 = countDownTime;
                        handler.sendMessageDelayed(message, 1000);
                    } else {
                        Message message2 = new Message();
                        message2.what = COUNT_DOWN_OVER;
                        handler.sendMessageDelayed(message2, 0);
                    }
                    break;
                case COUNT_DOWN_OVER:
                    //倒计时结束，启用获取验证码按钮
                    enableGetVerifyButton();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        mEdtPhoneActivityPhoneNumCheck = (EditText) findViewById(R.id.edt_phone_activity_phone_num_check);
        mEdtPwdActivityPhoneNumCheck = (EditText) findViewById(R.id.edt_pwd_activity_phone_num_check);
        mTvYzmActivityPhoneNumCheck = (TextView) findViewById(R.id.tv_yzm_activity_phone_num_check);
        mBtCommitActivityPhoneNumCheck = (Button) findViewById(R.id.bt_commit_activity_phone_num_check);
        mTvYzmActivityPhoneNumCheck.setOnClickListener(this);
        mBtCommitActivityPhoneNumCheck.setOnClickListener(this);
        mTvYzmActivityPhoneNumCheck.setEnabled(false);
        mBtCommitActivityPhoneNumCheck.setEnabled(false);
        mEdtPhoneActivityPhoneNumCheck.addTextChangedListener(textWatcher);
        mEdtPhoneActivityPhoneNumCheck.addTextChangedListener(textWatcherOne);
        mEdtPwdActivityPhoneNumCheck.addTextChangedListener(textWatcherOne);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (CommonUtil.isMobileNO(s.toString())) {
                mTvYzmActivityPhoneNumCheck.setEnabled(true);
                mTvYzmActivityPhoneNumCheck.setTextColor(getResources().getColor(R.color.finish_color));
            } else {
                mTvYzmActivityPhoneNumCheck.setEnabled(false);
                mTvYzmActivityPhoneNumCheck.setTextColor(getResources().getColor(R.color.lyt_color));
            }
        }
    };

    TextWatcher textWatcherOne=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!EdtUtil.isEdtEmpty(mEdtPhoneActivityPhoneNumCheck)&&!EdtUtil.isEdtEmpty(mEdtPwdActivityPhoneNumCheck)){
                mBtCommitActivityPhoneNumCheck.setBackgroundResource(R.drawable.btn_blue_bg);
                mBtCommitActivityPhoneNumCheck.setEnabled(true);
            }else {
                mBtCommitActivityPhoneNumCheck.setBackgroundResource(R.drawable.bt_login_corner);
                mBtCommitActivityPhoneNumCheck.setEnabled(false);
            }

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_yzm_activity_phone_num_check:
                mTvYzmActivityPhoneNumCheck.setEnabled(false);
                mTvYzmActivityPhoneNumCheck.setTextColor(getResources().getColor(R.color.lyt_color));
                break;

            case R.id.bt_commit_activity_phone_num_check:
                checkInfo();
                break;
        }
    }

    /**
     * 时间递减
     */
    private void getCode() {
        // 倒计时60s
        countDownTime = 60;
        Message message = new Message();
        countDownTime -= 1;
        message.what = COUNT_DOWN_TASK;
        message.arg1 = countDownTime;
        handler.sendMessageDelayed(message, 0);
    }

    /**
     * 检查是否验证完成
     */
    private void checkInfo() {
        String mobile= EdtUtil.getEdtText(mEdtPhoneActivityPhoneNumCheck);
        if(EdtUtil.isEdtEmpty(mEdtPhoneActivityPhoneNumCheck)){
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!CommonUtil.isMobileNO(mobile)){
            Toast.makeText(this, "手机号格式有误,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = EdtUtil.getEdtText(mEdtPwdActivityPhoneNumCheck);
        if (EdtUtil.isEdtEmpty(mEdtPwdActivityPhoneNumCheck)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        requestCheckPhoneAndCode(mobile, code.toUpperCase());
        mBtCommitActivityPhoneNumCheck.setEnabled(false);

    }

    /**
     * 验证手机号和验证码
     *
     * @param mobile 手机号
     * @param code   验证码
     */
    private void requestCheckPhoneAndCode(final String mobile, String code) {


    }

    private void enableGetVerifyButton() {
        mTvYzmActivityPhoneNumCheck.setEnabled(true);
        mTvYzmActivityPhoneNumCheck.setText("获取验证码");
        mTvYzmActivityPhoneNumCheck.setTextColor(getResources().getColor(R.color.finish_color));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEvent(EventUtil event) {
        if(event.getMsg().equals("forget_code")){
            getCode();
        }
        if (event.getMsg().equals("killpwd")){
            finish();
        }
    }
}