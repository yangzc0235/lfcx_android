package com.lfcx.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lfcx.main.R;
import com.lfcx.main.net.result.OrderListEntity;

import java.util.ArrayList;
import java.util.List;

public class LvItemActivityTravelFinishAdapter extends BaseAdapter {

    private List<OrderListEntity.PaymentlistBean> mDatas = new ArrayList<OrderListEntity.PaymentlistBean>();

    private Context context;
    private LayoutInflater layoutInflater;

    public LvItemActivityTravelFinishAdapter(Context context, List<OrderListEntity.PaymentlistBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public OrderListEntity.PaymentlistBean getItem(int position) {
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
        initializeViews((OrderListEntity.PaymentlistBean) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(OrderListEntity.PaymentlistBean mDatas, ViewHolder holder) {
        //TODO implement
        holder.mTvCarType.setText(mDatas.getOrderType());
        holder.mTvOrderType.setText(mDatas.getOrderStatus());
        holder.mTvTime.setText(mDatas.getGenerateDate());
        holder.mTvFromAddress.setText(mDatas.getFromAddress());
        holder.mTvToAddress.setText(mDatas.getToAddress());
    }

    protected class ViewHolder {
        private LinearLayout mLlOne;
        private TextView mTvCarType;
        private TextView mTvOrderType;
        private TextView mTvTime;
        private TextView mTvFromAddress;
        private TextView mTvToAddress;


        public ViewHolder(View itemView) {
            mLlOne = (LinearLayout) itemView.findViewById(R.id.ll_one);
            mTvCarType = (TextView) itemView.findViewById(R.id.tv_car_type);
            mTvOrderType = (TextView) itemView.findViewById(R.id.tv_order_type);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvFromAddress = (TextView) itemView.findViewById(R.id.tv_from_address);
            mTvToAddress = (TextView) itemView.findViewById(R.id.tv_to_address);
        }
    }
}
