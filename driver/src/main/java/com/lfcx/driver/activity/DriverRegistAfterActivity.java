package com.lfcx.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.driver.R;
import com.lfcx.driver.consts.Constants;
import com.lfcx.driver.dialog.BottomDialog;
import com.lfcx.driver.net.NetConfig;
import com.lfcx.driver.net.api.DUserAPI;
import com.lfcx.driver.net.result.IdCardResult;
import com.lfcx.driver.util.DriveTimeSelectUtils;
import com.lfcx.driver.util.EdtUtil;
import com.lfcx.driver.util.StringEmptyUtil;
import com.zaaach.citypicker.CityPickerActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRegistAfterActivity extends DriverBaseActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private ImageView mIvUser;
    private TextView mTitleBar;
    private ImageView mDIvMessage;
    private TextView mDTvPickCity;
    private TextView mDTvDriverName;
    private EditText mDEtDriverName;
    private TextView mDTvDriverID;
    private EditText mDEtDriverId;
    private RelativeLayout mDRlCarcode;
    private TextView mTvPrimaryDate;
    private TextView mDTvCarId;
    private EditText mDEtCarId;
    private TextView mDTvCarOwner;
    private EditText mDEtCarOwner;
    private RelativeLayout mDRlCarType;
    private TextView mTvCarType;
    private RelativeLayout mDRlCarRegist;
    private TextView mTvRegisterDate;
    private TextView mDTvCarMac;
    private EditText mDEtCarMac;
    private TextView mDTvCarType;
    private TextView mDEtCarType;
    private Button mDBtnSave;
    private DUserAPI userAPI;
    private String city;
    private int mStyleType = 0;
    /**
     * 城市选择返回
     */
    private static final int REQUEST_CODE_PICK_CITY = 0;
    private String mMobile;
    private String mPwd;
    private String mRecommandMobile;
    private BottomDialog mDialogSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_regist_after);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvUser = (ImageView) findViewById(R.id.iv_user);
        mTitleBar = (TextView) findViewById(R.id.title_bar);
        mDIvMessage = (ImageView) findViewById(R.id.d_iv_message);
        mDTvPickCity = (TextView) findViewById(R.id.d_tv_pick_city);
        mDTvDriverName = (TextView) findViewById(R.id.d_tv_driver_name);
        mDEtDriverName = (EditText) findViewById(R.id.d_et_driver_name);
        mDTvDriverID = (TextView) findViewById(R.id.d_tv_driver_ID);
        mDEtDriverId = (EditText) findViewById(R.id.d_et_driver_id);
        mDRlCarcode = (RelativeLayout) findViewById(R.id.d_rl_carcode);
        mTvPrimaryDate = (TextView) findViewById(R.id.tv_primary_date);
        mDTvCarId = (TextView) findViewById(R.id.d_tv_car_id);
        mDEtCarId = (EditText) findViewById(R.id.d_et_car_id);
        mDTvCarOwner = (TextView) findViewById(R.id.d_tv_car_owner);
        mDEtCarOwner = (EditText) findViewById(R.id.d_et_car_owner);
        mDRlCarType = (RelativeLayout) findViewById(R.id.d_rl_car_type);
        mTvCarType = (TextView) findViewById(R.id.tv_car_type);
        mDRlCarRegist = (RelativeLayout) findViewById(R.id.d_rl_car_regist);
        mTvRegisterDate = (TextView) findViewById(R.id.tv_register_date);
        mDTvCarMac = (TextView) findViewById(R.id.d_tv_car_mac);
        mDEtCarMac = (EditText) findViewById(R.id.d_et_car_mac);
        mDTvCarType = (TextView) findViewById(R.id.d_tv_car_type);
        mDEtCarType = (TextView) findViewById(R.id.d_et_car_type);
        mDBtnSave = (Button) findViewById(R.id.d_btn_save);
        init();
    }

    private void init() {
        userAPI = APIFactory.create(DUserAPI.class);
        mDBtnSave.setOnClickListener(this);
        mDTvCarType.setOnClickListener(this);
        mDTvCarMac.setOnClickListener(this);
        mTvRegisterDate.setOnClickListener(this);
        mTvCarType.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvUser.setOnClickListener(this);
        mDIvMessage.setOnClickListener(this);
        mDTvPickCity.setOnClickListener(this);
        mDTvDriverName.setOnClickListener(this);
        mDTvDriverID.setOnClickListener(this);
        mTvPrimaryDate.setOnClickListener(this);
        mDRlCarcode.setOnClickListener(this);
        mDEtCarType.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle bundleExtra = intent.getBundleExtra(BUNDLE_KEY);
        if (bundleExtra != null) {
            mMobile = bundleExtra.getString("mobile");
            mPwd = bundleExtra.getString("pwd");
            mRecommandMobile = bundleExtra.getString("recommandMobile");
            if (mRecommandMobile == null) {
                mRecommandMobile = "";
            }
        }

        Log.v("system----mMobile---->", mMobile);
        Log.v("system------mPwd----->", mPwd);
        Log.v("system---推荐人--->", mRecommandMobile);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();

        //初次领取驾照的日期
        if (i == R.id.tv_primary_date) {
            DriveTimeSelectUtils timeSelectUtils = new DriveTimeSelectUtils(this, null, null, null, new DriveTimeSelectUtils.GetSubmitTime() {
                @Override
                public void selectTime(String startDate, String entDate) {
                    mTvPrimaryDate.setText(startDate);
                }
            });
            timeSelectUtils.dateTimePicKDialog();

        } else if (i == R.id.tv_register_date) {
            DriveTimeSelectUtils timeSelectUtils = new DriveTimeSelectUtils(this, null, null, null, new DriveTimeSelectUtils.GetSubmitTime() {
                @Override
                public void selectTime(String startDate, String entDate) {
                    mTvRegisterDate.setText(startDate);
                }
            });
            timeSelectUtils.dateTimePicKDialog();
        } else if (i == R.id.iv_back) {
            finish();
            //保存
        } else if (i == R.id.d_btn_save) {
            saveDate();
            //用户中心
        } else if (i == R.id.iv_user) {
            goToActivity(DriverUserActivity.class);

            //消息
        } else if (i == R.id.d_iv_message) {
            goToActivity(DriverNewsActivity.class);

            //车型
        } else if (i == R.id.tv_car_type) {
            mTvCarType.setText("迈腾");
            //选择城市
        } else if (i == R.id.d_tv_pick_city) {//启动
            startActivityForResult(new Intent(DriverRegistAfterActivity.this, CityPickerActivity.class),
                    REQUEST_CODE_PICK_CITY);

        } else if (i == R.id.d_et_car_type) {
            //选择汽车类型
            showButtomSelectDialog();
        } else if (i == R.id.tv_ss_type) {
            mDEtCarType.setText("舒适型");
            mStyleType = 0;
            mDialogSelect.dismiss();
        } else if (i == R.id.tv_hh_type) {
            mDEtCarType.setText("豪华型");
            mStyleType = 1;
            mDialogSelect.dismiss();
        } else if (i == R.id.tv_seven_type) {
            mDEtCarType.setText("七座商务");
            mStyleType = 2;
            mDialogSelect.dismiss();
        } else if (i == R.id.tv_other_type) {
            mDEtCarType.setText("其他型");
            mStyleType = 9;
            mDialogSelect.dismiss();
        } else if (i == R.id.tv_cancel_select_account) {
            mDialogSelect.dismiss();
        }
    }


    /**
     * 选择联名卡账号或简易积分账户
     */
    private void showButtomSelectDialog() {
        mDialogSelect = BottomDialog.create(getSupportFragmentManager());
        mDialogSelect.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {

                TextView mTvSsType = (TextView) v.findViewById(R.id.tv_ss_type);
                TextView mTvHhType = (TextView) v.findViewById(R.id.tv_hh_type);
                TextView mTvSevenType = (TextView) v.findViewById(R.id.tv_seven_type);
                TextView mTvOtherType = (TextView) v.findViewById(R.id.tv_other_type);
                TextView mTvCancelSelectAccount = (TextView) v.findViewById(R.id.tv_cancel_select_account);

                mTvSsType.setOnClickListener(DriverRegistAfterActivity.this);
                mTvHhType.setOnClickListener(DriverRegistAfterActivity.this);
                mTvSevenType.setOnClickListener(DriverRegistAfterActivity.this);
                mTvOtherType.setOnClickListener(DriverRegistAfterActivity.this);
                mTvCancelSelectAccount.setOnClickListener(DriverRegistAfterActivity.this);
            }
        })
                .setLayoutRes(R.layout.select_car_type_pop)
                .setDimAmount(0.6f)
                .setTag("BottomDialog")
                .show();
    }


    /**
     * 保存数据
     */
    private void saveDate() {

        if (StringEmptyUtil.isEmpty(city)) {
            Toast.makeText(this, "请选择您所在的城市", Toast.LENGTH_SHORT).show();
            return;
        }

        if (EdtUtil.isEdtEmpty(mDEtDriverName)) {
            Toast.makeText(this, "请输入司机姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (EdtUtil.isEdtEmpty(mDEtDriverId)) {
            Toast.makeText(this, "请输入身份证号", Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringEmptyUtil.isEmpty(mTvPrimaryDate.getText().toString())) {
            Toast.makeText(this, "请选择领取驾照日期", Toast.LENGTH_SHORT).show();
            return;
        }

        if (EdtUtil.isEdtEmpty(mDEtCarId)) {
            Toast.makeText(this, "请输入车牌号", Toast.LENGTH_SHORT).show();
            return;
        }

        if (EdtUtil.isEdtEmpty(mDEtCarOwner)) {
            Toast.makeText(this, "请输入车辆所有人姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringEmptyUtil.isEmpty(mTvCarType.getText().toString())) {
            Toast.makeText(this, "请选择您的车型", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringEmptyUtil.isEmpty(mTvRegisterDate.getText().toString())) {
            Toast.makeText(this, "请选择车辆注册日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (EdtUtil.isEdtEmpty(mDEtCarMac)) {
            Toast.makeText(this, "请输入发动机型号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringEmptyUtil.isEmpty(mDEtCarType.getText().toString())) {
            Toast.makeText(this, "请选择汽车类型", Toast.LENGTH_SHORT).show();
            return;
        }


        checkCardIdandReallyName(EdtUtil.getEdtText(mDEtDriverId), EdtUtil.getEdtText(mDEtDriverName), Constants.D_SEND_CHECKCODE_KEY);

        Intent intent = new Intent(this, DriverUploadActivity.class);
        startActivity(intent);
    }


    /**
     * 实名认证
     *
     * @param idcard   身份证号
     * @param realname 真实姓名
     * @param key      key值
     */
    private void checkCardIdandReallyName(final String idcard, final String realname, String key) {
        Map<String, Object> param = new HashMap<>();
        param.put("idcard", idcard);
        param.put("realname", realname);
        param.put("key", key);
        userAPI.customerCheckIdCard(NetConfig.DRIVER_SHIMING, param).enqueue(new Callback<IdCardResult>() {
            @Override
            public void onResponse(Call<IdCardResult> call, Response<IdCardResult> response) {
                if (response.body().getError_code() == 0) {
                    String idCard = response.body().getResult().getIdcard();
                    String realName = response.body().getResult().getRealname();
                    requestRegistDatas(idCard, realName);
                } else {
                    Toast.makeText(DriverRegistAfterActivity.this, response.body().getReason(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<IdCardResult> call, Throwable t) {
                showToast("注册失败！！");
            }
        });
    }


    private void requestRegistDatas(String idcard, String realname) {

//        参数: city:城市; mobile:电话;pwd : 密码; drivername:姓名;drivercard:身份证;firstdetcreddate:首次登记日期; type:车型号 ;
//        regitdate :注册日期; engineno:发动机号; carcode:车牌号 ; StyleType:(0：舒适型 ; 1：豪华型 ;2：七座商务; 9：其他型 );
//        recommobile：推荐人手机;istruename:是否实名(0:否;1:是);
//        usercharacter:车辆使用性质 必填项 (0:个人;1:企业;2:其他) truename:实名称;card:身份证
        Map<String, Object> param = new HashMap<>();
        param.put("city", city);
        param.put("mobile", mMobile);
        param.put("pwd", mPwd);
        param.put("drivername", realname);
        param.put("drivercard", idcard);
        param.put("firstdetcreddate", mTvPrimaryDate.getText().toString().trim());
        param.put("type", mTvCarType.getText().toString());
        param.put("regitdate", mTvRegisterDate.getText().toString().trim());
        param.put("engineno", EdtUtil.getEdtText(mDEtCarMac));
        param.put("carcode", EdtUtil.getEdtText(mDEtCarId));//车牌号
        param.put("StyleType", mStyleType);
        param.put("recommobile", mRecommandMobile);
        param.put("istruename", 1);
        param.put("usercharacter", 0);
        param.put("truename", 0);
        userAPI.driverRegist(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        startActivity(new Intent(DriverRegistAfterActivity.this, DriverUploadActivity.class));
                    } else {
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


    //重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK) {
            if (data != null) {
                city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                mDTvPickCity.setText("当前选择：" + city);
            }
        }
    }

}
