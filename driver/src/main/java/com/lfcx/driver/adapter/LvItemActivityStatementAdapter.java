package com.lfcx.driver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lfcx.driver.R;
import com.lfcx.driver.event.ReceiptEvent;
import com.lfcx.driver.popwindow.CarNumPopWindow;
import com.lfcx.driver.popwindow.StateEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class LvItemActivityStatementAdapter extends BaseAdapter {

    private List<StateEntity> mDatas = new ArrayList<StateEntity>();
    private Context context;
    private LayoutInflater layoutInflater;
    TextView mTextView;
    CarNumPopWindow mMorePopWindow;
    public LvItemActivityStatementAdapter(Context context, List<StateEntity> mDatas, TextView textView, CarNumPopWindow morePopWindow) {
        this.context = context;
        this.mDatas = mDatas;
        mTextView=textView;
        mMorePopWindow=morePopWindow;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public StateEntity getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.lv_item_activity_statement, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((StateEntity) getItem(position), (ViewHolder) convertView.getTag(), position);
        return convertView;
    }

    private void initializeViews(final StateEntity mDatas, final ViewHolder holder, int pos) {
        //TODO implement
        holder.tvCard.setText(mDatas.getCode());
        holder.tvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ReceiptEvent("card_id",mDatas.getCode()));
                mMorePopWindow.dismiss();
            }
        });

    }

    protected class ViewHolder {
        private TextView tvCard;

        public ViewHolder(View view) {
            tvCard = (TextView) view.findViewById(R.id.tv_card);

        }
    }
}
