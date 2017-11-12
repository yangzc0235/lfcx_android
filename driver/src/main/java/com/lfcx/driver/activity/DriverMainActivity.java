package com.lfcx.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lfcx.driver.R;
import com.lfcx.driver.R2;
import com.lfcx.driver.service.LocationService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DriverMainActivity extends DriverBaseActivity {

    @BindView(R2.id.d_iv_message)
    ImageView ivMessage;

    ImageView iv_user;
    TextView titleBar;
    ImageView iv_message;

    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_main);
        unbinder = ButterKnife.bind(this);
        LocationService.startService(this);
        init();
    }

    private void init() {
        iv_user = (ImageView) findViewById(R.id.iv_user);
        iv_user.setVisibility(View.VISIBLE);
        iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverMainActivity.this,DriverUserActivity.class);
                startActivity(intent);
            }
        });
        titleBar = (TextView) findViewById(R.id.title_bar);
        titleBar.setText("雷风司机");
        iv_message = (ImageView) findViewById(R.id.d_iv_message);
        iv_message.setVisibility(View.VISIBLE);

    }


    @OnClick(R2.id.d_iv_message)
    public void onClickMessage(View view){
        Intent intent = new Intent(this,DriverNewsActivity.class);
        startActivity(intent);
    }
    @OnClick(R2.id.d_mode_select)
    public void onClickMode(View view){
        Intent intent = new Intent(this,DriverModeActivity.class);
        startActivity(intent);
    }
    @OnClick(R2.id.btn_start_work)
    public void onClickStart(View view){
        showToast("开始接单");
        Intent intent = new Intent(DriverMainActivity.this, DriverOrderActivity.class);
        DriverMainActivity.this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
