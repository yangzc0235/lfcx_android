package com.lfcx.driver.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.driver.R;
import com.lfcx.driver.R2;
import com.lfcx.driver.net.api.DUserAPI;
import com.zaaach.citypicker.CityPickerActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRegistAfterActivity extends DriverBaseActivity {

    private Unbinder unbinder;

    @BindView(R2.id.d_tv_pick_city)
    TextView tvPickCity;

    @BindView(R2.id.d_et_driver_name)
    EditText etDriverName;

    private DUserAPI userAPI;
    private String city;
    ImageView iv_back;
    Button btn_save;

    /**
     * 城市选择返回
     */
    private static final int REQUEST_CODE_PICK_CITY = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_regist_after);
        unbinder = ButterKnife.bind(this);
        init();
    }

    private void init(){
        userAPI = APIFactory.create(DUserAPI.class);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverRegistAfterActivity.this.finish();
            }
        });
        btn_save = (Button)findViewById(R.id.d_btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverRegistAfterActivity.this, DriverUploadActivity.class);
                DriverRegistAfterActivity.this.startActivity(intent);
            }
        });
    }


    /**
     * 保存
     * @param v
     */
    @OnClick(R2.id.d_btn_save)
    public void onClickSave(View v){
        Intent intent = new Intent(this,DriverUploadActivity.class);
        startActivity(intent);
        finish();
    }

    private void goRegist(String moible,String pwd){
        Map<String,Object> param = new HashMap<>();
        param.put("mobile",moible);
        param.put("city",city);
        param.put("pwd",pwd);
        param.put("drivername",etDriverName.getText().toString().trim());
        param.put("drivercard","");
        param.put("firstdetcreddate","");
        param.put("type","私家车");
        param.put("regitdate","");
        param.put("engineno","");
        param.put("carcode","");//车牌号
        param.put("styletype",0);
        userAPI.driverRegist(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(null != response && !TextUtils.isEmpty(response.body())){
                    BaseResultBean res =  new Gson().fromJson(response.body(),BaseResultBean.class);
                    if("0".equals(res.getCode())){
                        startActivity(new Intent(DriverRegistAfterActivity.this,DriverUploadActivity.class));
                    }else {
                        showToast(res.getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("注册失败！！");
            }
        });
    }

    /**
     * 初次领取驾照时间
     * @param v
     */
    @OnClick(R2.id.d_rl_carcode)
    public void onClickCarCode(View v){
        finish();
    }
    /**
     * 车型选择
     * @param v
     */
    @OnClick(R2.id.d_rl_car_type)
    public void onClickCarType(View v){
        finish();
    }
    /**
     * 车辆注册时间
     * @param v
     */
    @OnClick(R2.id.d_rl_car_regist)
    public void onClickCarRegist(View v){
        finish();
    }

    /**
     * 选择城市
     * @param v
     */
    @OnClick(R2.id.d_tv_pick_city)
    public void onClickPickCity(View v){
        //启动
        startActivityForResult(new Intent(DriverRegistAfterActivity.this, CityPickerActivity.class),
                REQUEST_CODE_PICK_CITY);
    }
    //重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                tvPickCity.setText("当前选择：" + city);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
