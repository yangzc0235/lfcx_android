package com.lfcx.customer.fragment.order;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lfcx.customer.R;
import com.lfcx.customer.pay.alipay.Alipay;
import com.lfcx.customer.pay.weixin.WXPay;

/**
 *
 */

public class OrderPayFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private LinearLayout mLlOne;
    private RadioGroup mRpActivityPayOrder;
    private RadioButton mLlWxPayActivityPayOrder;
    private RadioButton mLlZfbPayActivityPayOrder;
    private TextView mCBtnConfirm;
    private boolean mChecked=true;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_order_pay_fragment, container, false);
        mLlOne = (LinearLayout) view.findViewById(R.id.ll_one);
        mRpActivityPayOrder = (RadioGroup) view.findViewById(R.id.rp_activity_pay_order);
        mLlWxPayActivityPayOrder = (RadioButton) view.findViewById(R.id.ll_wx_pay_activity_pay_order);
        mLlZfbPayActivityPayOrder = (RadioButton) view.findViewById(R.id.ll_zfb_pay_activity_pay_order);
        mCBtnConfirm = (TextView) view.findViewById(R.id.c_btn_confirm);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRpActivityPayOrder.setOnCheckedChangeListener(this);
        mLlWxPayActivityPayOrder.setOnClickListener(this);
        mLlZfbPayActivityPayOrder.setOnClickListener(this);

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
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.c_btn_confirm) {//立即支付
            if (mChecked) {
                //微信支付
                requestDataPayParams("", "2");
            } else {
                //支付宝支付
                requestDataPayParams("", "1");
            }

        }
    }


    /**
     * 获取支付参数
     *
     */
    private void requestDataPayParams(String orderSn, final String optionsRadios) {


    }

    /**
     * 支付宝支付
     * @param pay_param 支付服务生成的支付参数
     */
    private void doAlipay(String pay_param) {
        new Alipay(getActivity().getApplicationContext(), pay_param, new Alipay.AlipayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity().getApplication(), "支付成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDealing() {
                Toast.makeText(getActivity().getApplication(), "支付处理中...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case Alipay.ERROR_RESULT:
                        Toast.makeText(getActivity().getApplication(), "支付失败:支付结果解析错误", Toast.LENGTH_SHORT).show();
                        break;

                    case Alipay.ERROR_NETWORK:
                        Toast.makeText(getActivity().getApplication(), "支付失败:网络连接错误", Toast.LENGTH_SHORT).show();
                        break;

                    case Alipay.ERROR_PAY:
                        Toast.makeText(getActivity().getApplication(), "支付错误:支付码支付失败", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(getActivity().getApplication(), "支付错误", Toast.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplication(), "支付取消", Toast.LENGTH_SHORT).show();
            }
        }).doPay();
    }


    /**
     * 微信支付
     * @param pay_param 支付服务生成的支付参数
     */
    private void doWXPay(String pay_param) {
        String wx_appid = "wx113de15918bdeb42";     //替换为自己的appid
        WXPay.init(getActivity().getApplicationContext(), wx_appid);      //要在支付前调用
        WXPay.getInstance().doPay(pay_param, new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity().getApplication(), "支付成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        Toast.makeText(getActivity().getApplication(), "未安装微信或微信版本过低", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY_PARAM:
                        Toast.makeText(getActivity().getApplication(), "参数错误", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY:
                        Toast.makeText(getActivity().getApplication(), "支付失败", Toast.LENGTH_SHORT).show();

                        break;
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplication(), "支付取消", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
