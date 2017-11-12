package com.lfcx.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.StringUtils;
import com.lfcx.driver.R;
import com.lfcx.driver.R2;
import com.lfcx.driver.consts.Constants;
import com.lfcx.driver.net.NetConfig;
import com.lfcx.driver.net.api.DUserAPI;
import com.lfcx.driver.net.result.SendMessageResult;

import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRegisterActivity extends DriverBaseActivity {


    private String checkCode;
    private DUserAPI userAPI;

    EditText et_phone;
    EditText et_code;
    Button btn_getcode;
    EditText et_pwd;
    EditText et_confirm_pwd;
    EditText et_introduce_phone;
    Button btn_register;
    TextView title_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_register_activity);
        userAPI = APIFactory.create(DUserAPI.class);
        init();
    }

    private void init() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_getcode = (Button) findViewById(R.id.btn_getcode);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
        et_introduce_phone = (EditText) findViewById(R.id.et_introduce_phone);
        btn_register = (Button) findViewById(R.id.btn_register);
        title_bar = (TextView) findViewById(R.id.title_bar);
        title_bar.setText("注册用户");
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverRegisterActivity.this, DriverRegistAfterActivity.class);
                DriverRegisterActivity.this.startActivity(intent);
            }
        });
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


    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
