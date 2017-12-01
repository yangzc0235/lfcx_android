package com.lfcx.main.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.main.R;
import com.lfcx.main.activity.CustomerBaseActivity;
import com.lfcx.main.consts.SPConstants;
import com.lfcx.main.event.EventUtil;
import com.lfcx.main.net.api.UserAPI;
import com.lfcx.main.net.result.PayEntity;
import com.lfcx.main.net.result.PayResultEntity;
import com.lfcx.main.pay.alipay.Alipay;
import com.lfcx.main.pay.weixin.WXPay;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by yzc on 2016/12/3.
 * 解绑dialog
 */
public class PayDialog extends Dialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    private LinearLayout mLlOne;
    private RadioGroup mRpActivityPayOrder;
    private RadioButton mLlWxPayActivityPayOrder;
    private RadioButton mLlZfbPayActivityPayOrder;
    private TextView mCBtnConfirm;
    private TextView mTvName;
    private TextView mTvMoney;
    private TextView mTvQuestion;
    private TextView mTvClose;
    private Context mContext;
    private UserAPI userAPI;
    private boolean mChecked=true;
    private PayEntity mPayEntity;
    private String mWaitpay;
    public PayDialog(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * 自定义主题及布局的构造方法
     *
     * @param context
     * @param theme
     */
    public PayDialog(Context context, int theme,PayEntity payEntity) {
        super(context, theme);
        mContext = context;
        mPayEntity=payEntity;
    }

    /**
     * 自定义主题及布局的构造方法
     *
     * @param context
     * @param theme
     */
    public PayDialog(CustomerBaseActivity context, int theme, PayEntity payEntity, String type) {
        super(context, theme);
        mContext = context;
        mPayEntity=payEntity;
        mWaitpay=type;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.customer_order_pay_fragment);
        userAPI = APIFactory.create(UserAPI.class);
        mLlOne = (LinearLayout) findViewById(R.id.ll_one);
        mRpActivityPayOrder = (RadioGroup) findViewById(R.id.rp_activity_pay_order);
        mLlWxPayActivityPayOrder = (RadioButton) findViewById(R.id.ll_wx_pay_activity_pay_order);
        mLlZfbPayActivityPayOrder = (RadioButton) findViewById(R.id.ll_zfb_pay_activity_pay_order);
        mTvClose = (TextView) findViewById(R.id.tv_close);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvMoney = (TextView) findViewById(R.id.tv_money);
        mTvQuestion = (TextView) findViewById(R.id.tv_question);
        mCBtnConfirm = (TextView) findViewById(R.id.c_btn_confirm);
        mLlZfbPayActivityPayOrder.setOnClickListener(this);
        mTvClose.setOnClickListener(this);
        mCBtnConfirm.setOnClickListener(this);
        mRpActivityPayOrder.setOnCheckedChangeListener(this);
        mLlZfbPayActivityPayOrder.setOnCheckedChangeListener(this);
        mTvMoney.setText(String.valueOf(mPayEntity.getCashnum()));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.c_btn_confirm) {//立即支付
            try {
                if (mChecked) {
                    //微信支付
                    requestDataPayParams("wx",String.valueOf(mPayEntity.getCashnum()),mPayEntity.getPk_userorder());
                } else {
                    //支付宝支付
                    requestDataPayParams("ali",String.valueOf(mPayEntity.getCashnum()),mPayEntity.getPk_userorder());
                }
            }catch (Exception e){
                Toast.makeText(mContext, "支付参数错误", Toast.LENGTH_SHORT).show();
            }

        }else if(i==R.id.tv_close){
            dismiss();
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.ll_wx_pay_activity_pay_order) {//选中微信支付
            mChecked = true;

        } else if (checkedId == R.id.ll_zfb_pay_activity_pay_order) {//选中支付宝支付
            mChecked = false;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    /**
     * 支付
     * @param paytype
     */
    private void requestDataPayParams(final String paytype , String cashnum, String pk_userorder) {
        Map<String, String> param = new HashMap<>();
        Log.v("system--userorder-->",pk_userorder);
        Log.v("system-----cashnum-->",cashnum);
        Log.v("system-----paytype-->",paytype);
        param.put("pk_userorder", pk_userorder);
        param.put("paytype", paytype);
        param.put("cashnum", cashnum);
        userAPI.wxPay(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.v("system--微信支付参数--->",response.body()+"  ");
                try {
                    Gson gson=new Gson();
                    PayResultEntity payResultEntity=gson.fromJson(response.body(), PayResultEntity.class);
                    if(payResultEntity.getCode()==0){
                        String s = payResultEntity.getResult().toString();
                        if(paytype.equals("wx")){
                            doWXPay(s);
                        }else {
                            doAlipay(s);
                        }

                        Log.v("system--微信支付参数--->",response.body());
                    }else {
                        Log.v("system----->","获取参数失败");
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "支付异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "获取支付参数失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 支付宝支付
     * @param pay_param 支付服务生成的支付参数
     */
    private void doAlipay(String pay_param) {
        new Alipay(mContext, pay_param, new Alipay.AlipayResultCallBack() {
            @Override
            public void onSuccess() {
//                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                try {
                    SPUtils.setParam(mContext, SPConstants.KEY_SUCCESS_CAR, "");
                    SPUtils.setParam(mContext, SPConstants.KEY_CAR_CODE, "");
                    SPUtils.setParam(mContext, SPConstants.KEY_CAR_TYPE, "");
                    SPUtils.setParam(mContext, SPConstants.KEY_DRIVER_NAME, "");
                    SPUtils.setParam(mContext, SPConstants.KEY_USER_ORDER, "");
                    SPUtils.setParam(mContext, SPConstants.KEY_DRIVER_MOBILE, "");
                    Toast.makeText(mContext, "您已经付款成功,感谢您下次乘坐雷风专车", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new EventUtil("close_carsuccess"));
                    dismiss();

                }catch (Exception e){

                }

            }

            @Override
            public void onDealing() {
                Toast.makeText(mContext, "支付处理中...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case Alipay.ERROR_RESULT:
                        Toast.makeText(mContext, "支付失败:支付结果解析错误", Toast.LENGTH_SHORT).show();
                        break;

                    case Alipay.ERROR_NETWORK:
                        Toast.makeText(mContext, "支付失败:网络连接错误", Toast.LENGTH_SHORT).show();
                        break;

                    case Alipay.ERROR_PAY:
                        Toast.makeText(mContext, "支付错误:支付码支付失败", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(mContext, "支付错误", Toast.LENGTH_SHORT).show();

                        break;
                }

            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
            }
        }).doPay();
    }


    /**
     * 微信支付
     * @param pay_param 支付服务生成的支付参数
     */
    private void doWXPay(String pay_param) {
        String wx_appid = "wxa7329e064ae58113";     //替换为自己的appid
        WXPay.init(mContext, wx_appid);      //要在支付前调用
        WXPay.getInstance().doPay(pay_param, new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
//                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                try {
                    SPUtils.setParam(mContext, SPConstants.KEY_SUCCESS_CAR, "");
                    SPUtils.setParam(mContext, SPConstants.KEY_CAR_CODE, "");
                    SPUtils.setParam(mContext, SPConstants.KEY_CAR_TYPE, "");
                    SPUtils.setParam(mContext, SPConstants.KEY_DRIVER_NAME, "");
                    SPUtils.setParam(mContext, SPConstants.KEY_USER_ORDER, "");
                    SPUtils.setParam(mContext, SPConstants.KEY_DRIVER_MOBILE, "");
                    Toast.makeText(mContext, "您已经付款成功,感谢您下次乘坐雷风专车", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new EventUtil("close_carsuccess"));
                    dismiss();
                }catch (Exception e){

                }
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        Toast.makeText(mContext, "未安装微信或微信版本过低", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY_PARAM:
                        Toast.makeText(mContext, "参数错误", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY:
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();

                        break;
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
