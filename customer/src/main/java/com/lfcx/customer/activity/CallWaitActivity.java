package com.lfcx.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.event.CallCarCancelEvent;
import com.lfcx.customer.event.CallCarSuccessEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CallWaitActivity extends CustomerBaseActivity {
    @BindView(R2.id.iv_back)
    ImageView ivBack;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_wait);
        unbinder = ButterKnife.bind(this);
        ivBack.setVisibility(View.VISIBLE);
        EventBus.getDefault().register(this);
    }

    @OnClick(R2.id.iv_back)
    public void onClickBack(){
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallCarSuccess(CallCarSuccessEvent event){
        Intent intent = new Intent(this,CallCarSucessActivity.class);
        intent.putExtra("pk_user",event.getPk_user());
        startActivity(intent);
        finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallCarCancel(CallCarCancelEvent event){

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
