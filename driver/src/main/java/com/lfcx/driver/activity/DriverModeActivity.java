package com.lfcx.driver.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
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
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.driver.R;
import com.lfcx.driver.maphelper.RouteTask;
import com.lfcx.driver.net.api.DriverCarAPI;
import com.lfcx.driver.util.DriveTimeSelectUtils;
import com.lfcx.driver.util.StringEmptyUtil;
import com.lfcx.driver.util.TtsSettings;

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

    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private SharedPreferences mSharedPreferences;

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
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, MODE_PRIVATE);
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


    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
                FlowerCollector.onEvent(DriverModeActivity.this, "tts_play");
                // 设置参数
                setParam();
                mTts.startSpeaking("设置模式", mTtsListener);

            }
        }
    };


    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {

        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {

        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void showTip(final String str) {
        Log.v("system-------->", str);
    }


}
