package com.lfcx.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lfcx.main.R;
import com.lfcx.main.net.result.OrderListEntity;
import com.lfcx.main.net.result.PayEntity;
import com.lfcx.main.widget.dialog.PayDialog;

import java.util.ArrayList;
import java.util.List;

public class LvItemActivityTravelAdapter extends BaseAdapter {

    private List<OrderListEntity.NonPaymentlistBean> mDatas = new ArrayList<OrderListEntity.NonPaymentlistBean>();

    private Context context;
    private LayoutInflater layoutInflater;

    public LvItemActivityTravelAdapter(Context context, List<OrderListEntity.NonPaymentlistBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public OrderListEntity.NonPaymentlistBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.lv_item_activity_bill, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((OrderListEntity.NonPaymentlistBean) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(final OrderListEntity.NonPaymentlistBean mDatas, ViewHolder holder) {
        //TODO implement
        holder.mTvCarType.setText(mDatas.getOrderType());
        holder.mTvOrderType.setText(mDatas.getOrderStatus());
        holder.mTvTime.setText(mDatas.getGenerateDate());
        holder.mTvFromAddress.setText(mDatas.getFromAddress());
        holder.mTvToAddress.setText(mDatas.getToAddress());
        holder.mRlWaitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayEntity payEntity=new PayEntity();
                payEntity.setPk_userorder(mDatas.getPK_UserOrder());
                payEntity.setCashnum(mDatas.getMoney());
                PayDialog payDialog=new PayDialog(context, R.style.customDialog, payEntity);
                payDialog.show();
            }
        });
    }

    protected class ViewHolder {
        private LinearLayout mLlOne;
        private TextView mTvCarType;
        private TextView mTvOrderType;
        private TextView mTvTime;
        private TextView mTvFromAddress;
        private TextView mTvToAddress;
        private RelativeLayout mRlWaitPayment;



        public ViewHolder(View itemView) {
            mRlWaitPayment = (RelativeLayout) itemView.findViewById(R.id.rl_wait_payment);
            mLlOne = (LinearLayout) itemView.findViewById(R.id.ll_one);
            mTvCarType = (TextView) itemView.findViewById(R.id.tv_car_type);
            mTvOrderType = (TextView) itemView.findViewById(R.id.tv_order_type);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvFromAddress = (TextView) itemView.findViewById(R.id.tv_from_address);
            mTvToAddress = (TextView) itemView.findViewById(R.id.tv_to_address);
        }
    }
}
