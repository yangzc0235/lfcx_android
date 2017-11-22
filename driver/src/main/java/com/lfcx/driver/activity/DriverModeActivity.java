package com.lfcx.driver.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.driver.R;
import com.lfcx.driver.maphelper.RouteTask;
import com.lfcx.driver.net.api.DriverCarAPI;
import com.lfcx.driver.util.DriveTimeSelectUtils;
import com.lfcx.driver.util.StringEmptyUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverModeActivity extends DriverBaseActivity implements RouteTask.OnRouteCalculateListener, View.OnClickListener {

    private Unbinder unbinder;
    TextView tvConfrim;
    LinearLayout real_container;
    LinearLayout book_container;
    Button btn_current;
    Button btn_book;
    Button btn_all;
    private RelativeLayout mRlToaddressActivityDriverMode;
    private TextView mEdtStartActivityDriverMode;
    private TextView mEdtEndActivityDriverMode;
    private TextView mTvToaddressActivityDriverMode;
    private DriverCarAPI mDriverCarAPI;
    private String toAddress, startTime, endTime;
    protected Handler mainHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_mode);
        mainHandler = new Handler() {
            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

            }

        };
        unbinder = ButterKnife.bind(this);
        mDriverCarAPI = APIFactory.create(DriverCarAPI.class);
        RouteTask.getInstance(this)
                .addRouteCalculateListener(this);
        init();
        mTts.startSpeaking("设置模式", mTtsListener);
    }

    private void init() {
        real_container = (LinearLayout) findViewById(R.id.real_container);
        book_container = (LinearLayout) findViewById(R.id.book_container);
        tvConfrim = (TextView) findViewById(R.id.tv_confirm);
        btn_current = (Button) findViewById(R.id.btn_current);
        mRlToaddressActivityDriverMode = (RelativeLayout) findViewById(R.id.rl_toaddress_activity_driver_mode);
        mEdtStartActivityDriverMode = (TextView) findViewById(R.id.edt_start_activity_driver_mode);
        mEdtEndActivityDriverMode = (TextView) findViewById(R.id.edt_end_activity_driver_mode);
        mTvToaddressActivityDriverMode = (TextView) findViewById(R.id.tv_toaddress_activity_driver_mode);
        btn_current.setOnClickListener(this);
        btn_book = (Button) findViewById(R.id.btn_book);
        btn_book.setOnClickListener(this);
        btn_all = (Button) findViewById(R.id.btn_all);
        btn_all.setOnClickListener(this);
        tvConfrim.setOnClickListener(this);
        mRlToaddressActivityDriverMode.setOnClickListener(this);
        mEdtStartActivityDriverMode.setOnClickListener(this);
        mEdtEndActivityDriverMode.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_current) {
            mTts.startSpeaking("只听实时订单", mTtsListener);
            real_container.setVisibility(View.VISIBLE);
            book_container.setVisibility(View.GONE);
            btn_current.setBackgroundResource(R.drawable.btn_yellow_shape_bg);
            btn_book.setBackgroundResource(android.R.color.transparent);
            btn_all.setBackgroundResource(android.R.color.transparent);
            String toAddress = mTvToaddressActivityDriverMode.getText().toString().trim();
            if (StringEmptyUtil.isEmpty(toAddress)) {
                Toast.makeText(this, "请选择目的地", Toast.LENGTH_SHORT).show();
                return;
            }
            requestSetOrderType("", "", toAddress, 1);
        } else if (view.getId() == R.id.btn_book) {
            mTts.startSpeaking("只听预约订单", mTtsListener);
            real_container.setVisibility(View.GONE);
            book_container.setVisibility(View.VISIBLE);
            btn_book.setBackgroundResource(R.drawable.btn_yellow_shape_bg);
            btn_current.setBackgroundResource(android.R.color.transparent);
            btn_all.setBackgroundResource(android.R.color.transparent);
            //requestSetOrderType("", "", "", 2);
        } else if (view.getId() == R.id.btn_all) {
            mTts.startSpeaking("听全部订单", mTtsListener);
            real_container.setVisibility(View.VISIBLE);
            book_container.setVisibility(View.VISIBLE);
            btn_all.setBackgroundResource(R.drawable.btn_yellow_shape_bg);
            btn_book.setBackgroundResource(android.R.color.transparent);
            btn_current.setBackgroundResource(android.R.color.transparent);
            toAddress = mTvToaddressActivityDriverMode.getText().toString().trim();
            startTime = mEdtStartActivityDriverMode.getText().toString().trim();
            endTime = mEdtEndActivityDriverMode.getText().toString().trim();
            if (StringEmptyUtil.isEmpty(toAddress)) {
                Toast.makeText(this, "请选择目的地", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringEmptyUtil.isEmpty(startTime)) {
                Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringEmptyUtil.isEmpty(endTime)) {
                Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (view.getId() == R.id.tv_confirm) {
            finish();
        } else if (view.getId() == R.id.rl_toaddress_activity_driver_mode) {
            //选择目的地
            goToActivity(DestinationActivity.class);
        } else if (view.getId() == R.id.edt_start_activity_driver_mode) {
            //选择起始时间
            DriveTimeSelectUtils timeSelectUtils = new DriveTimeSelectUtils(this, null, null, null, new DriveTimeSelectUtils.GetSubmitTime() {
                @Override
                public void selectTime(String startDate, String entDate) {
                    mEdtStartActivityDriverMode.setText(startDate);
                }
            });
            //显示我们的时间选择器
            timeSelectUtils.dateTimePicKDialog();

        } else if (view.getId() == R.id.edt_end_activity_driver_mode) {
            //选择终点时间
            DriveTimeSelectUtils timeSelectUtils = new DriveTimeSelectUtils(this, null, null, null, new DriveTimeSelectUtils.GetSubmitTime() {
                @Override
                public void selectTime(String startDate, String entDate) {
                    mEdtEndActivityDriverMode.setText(startDate);
                    requestSetOrderType(startTime, endTime, toAddress, 0);
                }
            });
            //显示我们的时间选择器
            timeSelectUtils.dateTimePicKDialog();

        }

    }


    /**
     * 模式类型设置
     *
     * @param begintime 开始时间
     * @param endtime   结束时间
     * @param toaddress 目的地
     * @param type      类型
     */
    private void requestSetOrderType(String begintime, String endtime, String toaddress, int type) {

        Map<String, Object> param = new HashMap<>();
        param.put("begintime", begintime);
        param.put("endtime", endtime);
        param.put("toaddress", toaddress);
        param.put("type", type);
        param.put("pk_user", "752b85a9-6ca0-4467-93e8-5fbf9d1c2f90");
        mDriverCarAPI.setOrderType(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != response && !TextUtils.isEmpty(response.body())) {
                    BaseResultBean res = new Gson().fromJson(response.body(), BaseResultBean.class);
                    if ("0".equals(res.getCode())) {
                        showToast(res.getMsg());
                        Log.v("system-------->", res.getMsg());
                    } else {
                        showToast(res.getMsg());
                        Log.v("system-------->", res.getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("注册失败！！");
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

    @Override
    public void onRouteCalculate(float cost, float distance, int duration) {

        Log.v("system----address---->", RouteTask
                .getInstance(getApplicationContext()).getEndPoint().address);

        Log.v("system---distance---->", distance + "");
        mTvToaddressActivityDriverMode.setText(RouteTask
                .getInstance(getApplicationContext()).getEndPoint().address);

    }





}
