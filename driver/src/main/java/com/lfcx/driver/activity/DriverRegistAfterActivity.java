package com.lfcx.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.driver.R;
import com.lfcx.driver.consts.Constants;
import com.lfcx.driver.dialog.BottomDialog;
import com.lfcx.driver.event.ReceiptEvent;
import com.lfcx.driver.net.NetConfig;
import com.lfcx.driver.net.api.DUserAPI;
import com.lfcx.driver.net.result.IdCardResult;
import com.lfcx.driver.net.result.NameEntity;
import com.lfcx.driver.popwindow.CarNumPopWindow;
import com.lfcx.driver.util.EdtUtil;
import com.lfcx.driver.util.StringEmptyUtil;
import com.zaaach.citypicker.CityPickerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
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
    private TextView mTvSelectCarType;
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
    private LinearLayout mLl;
    private static final int REQUEST_CODE = 100;//请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        mTvSelectCarType = (TextView) findViewById(R.id.tv_select_car_type);
        mLl = (LinearLayout) findViewById(R.id.ll);
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
        mTvSelectCarType.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle bundleExtra = intent.getBundleExtra(BUNDLE_KEY);
        if (bundleExtra != null) {
            mMobile = bundleExtra.getString("mobile");
            mPwd = bundleExtra.getString("pwd");
            mRecommandMobile = bundleExtra.getString("recommandMobile");
            if (mRecommandMobile == null) {
                mRecommandMobile = "";
            }
            Log.v("system----mMobile---->", mMobile);
            Log.v("system------mPwd----->", mPwd);
            Log.v("system---推荐人--->", mRecommandMobile);
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        //初次领取驾照的日期
        if (i == R.id.tv_primary_date) {
            final DatePickDialog dialog = new DatePickDialog(this);
            //设置上下年分限制
            dialog.setYearLimt(100);
            //设置标题
            dialog.setTitle("选择时间");
            //设置类型
            dialog.setType(DateType.TYPE_YMD);
            //设置消息体的显示格式，日期格式
            dialog.setMessageFormat("yyyy-MM-dd");
            dialog.show();
            //设置点击确定按钮回调
            dialog.setOnSureLisener(new OnSureLisener() {
                @Override
                public void onSure(Date date) {
                    java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    String rq = simpleDateFormat.format(date);
                    mTvPrimaryDate.setText(rq);
                    dialog.dismiss();
                }
            });


        } else if (i == R.id.tv_register_date) {
            final DatePickDialog dialog = new DatePickDialog(this);
            //设置上下年分限制
            dialog.setYearLimt(100);
            //设置标题
            dialog.setTitle("选择时间");
            //设置类型
            dialog.setType(DateType.TYPE_YMD);
            //设置消息体的显示格式，日期格式
            dialog.setMessageFormat("yyyy-MM-dd");
            dialog.show();
            //设置点击确定按钮回调
            dialog.setOnSureLisener(new OnSureLisener() {
                @Override
                public void onSure(Date date) {
                    java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    String rq = simpleDateFormat.format(date);
                    mTvRegisterDate.setText(rq);
                    dialog.dismiss();
                }
            });
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
            startActivityForResult(new Intent(DriverRegistAfterActivity.this, CarNameSelectActivity.class),
                    REQUEST_CODE);
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
        }else if(i==R.id.tv_select_car_type){
            //选择车辆类型
            CarNumPopWindow morePopWindow = new CarNumPopWindow(this,mTvSelectCarType);
            morePopWindow.showPopupWindow(mTvSelectCarType);
        }
    }

    /**
     * 选择汽车类型
     */
    private void showButtomSelectDialog() {
        mDialogSelect = BottomDialog.create(getSupportFragmentManager()).setCancelOutside(false);
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

        checkCardIdandReallyName(EdtUtil.getEdtText(mDEtDriverId), EdtUtil.getEdtText(mDEtDriverName), Constants.D_CHECK_ID_CARD_KEY);

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
        showLoading();
        userAPI.customerCheckIdCard(NetConfig.DRIVER_SHIMING, param).enqueue(new Callback<IdCardResult>() {
            @Override
            public void onResponse(Call<IdCardResult> call, Response<IdCardResult> response) {
                hideLoading();
                if (response.body().getError_code() == 0) {
                    String idCard = response.body().getResult().getIdcard();
                    String realName = response.body().getResult().getRealname();
                    requestRegistDatas(idCard, realName);
                    Log.v("system-----reson----->", response.body().getReason());
                } else {
                    Toast.makeText(DriverRegistAfterActivity.this, response.body().getReason(), Toast.LENGTH_SHORT).show();
                    Log.v("system-----reson----->", response.body().getReason());
                }
            }

            @Override
            public void onFailure(Call<IdCardResult> call, Throwable t) {
                showToast("注册失败！！");
                hideLoading();
            }
        });
    }

    /**
     * 注册填入信息
     *
     * @param idcard   身份证号
     * @param realname 真实姓名
     */
    private void requestRegistDatas(String idcard, String realname) {
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
        param.put("carcode", mTvSelectCarType.getText().toString().trim()+EdtUtil.getEdtText(mDEtCarId));//车牌号
        param.put("StyleType", mStyleType);
        param.put("recommobile", mRecommandMobile);
        param.put("istruename", 1);
        param.put("usercharacter", 0);
        param.put("truename", 0);
        showLoading();
        userAPI.driverRegist(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        showToast(res.getMsg());
                        Bundle bundle=new Bundle();
                        bundle.putString("mobile",mMobile);
                        bundle.putString("pwd",mPwd);
                        goToActivity(DriverUploadActivity.class,bundle);
                    } else {
                        showToast(res.getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("注册失败！！");
                hideLoading();
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
        if(requestCode==REQUEST_CODE&& resultCode == RESULT_OK) {
            if (data != null) {
                NameEntity nameEntity = (NameEntity) data.getSerializableExtra("menu");
                Log.v("system----fff--->", nameEntity.getType());
                mTvCarType.setText(nameEntity.getCarmodels() + nameEntity.getType());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //在ui线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReceiptEvent event) {
        if (event.getKey().equals("card_id")) {
           mTvSelectCarType.setText(event.getValue());
        }
    }
}
