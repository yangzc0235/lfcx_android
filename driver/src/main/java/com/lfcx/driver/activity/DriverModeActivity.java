package com.lfcx.driver.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lfcx.driver.R;
import com.lfcx.driver.R2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DriverModeActivity extends DriverBaseActivity implements View.OnClickListener{

    private Unbinder unbinder;

    TextView tvConfrim;
    LinearLayout real_container;
    LinearLayout book_container;

    Button btn_current;
    Button btn_book;
    Button btn_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_mode);
        unbinder = ButterKnife.bind(this);
        init();
    }

    private void init() {
        real_container = (LinearLayout) findViewById(R.id.real_container);
        book_container = (LinearLayout) findViewById(R.id.book_container);

        tvConfrim = (TextView) findViewById(R.id.tv_confirm);

        btn_current = (Button) findViewById(R.id.btn_current);
        btn_current.setOnClickListener(this);
        btn_book = (Button) findViewById(R.id.btn_book);
        btn_book.setOnClickListener(this);
        btn_all = (Button) findViewById(R.id.btn_all);
        btn_all.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_current) {
            real_container.setVisibility(View.VISIBLE);
            book_container.setVisibility(View.GONE);
            btn_current.setBackgroundResource(R.drawable.btn_yellow_shape_bg);
            btn_book.setBackgroundResource(android.R.color.transparent);
            btn_all.setBackgroundResource(android.R.color.transparent);
        }
        else if(view.getId() == R.id.btn_book) {
            real_container.setVisibility(View.GONE);
            book_container.setVisibility(View.VISIBLE);
            btn_book.setBackgroundResource(R.drawable.btn_yellow_shape_bg);
            btn_current.setBackgroundResource(android.R.color.transparent);
            btn_all.setBackgroundResource(android.R.color.transparent);
        }
        else if(view.getId() == R.id.btn_all) {
            real_container.setVisibility(View.VISIBLE);
            book_container.setVisibility(View.VISIBLE);
            btn_all.setBackgroundResource(R.drawable.btn_yellow_shape_bg);
            btn_book.setBackgroundResource(android.R.color.transparent);
            btn_current.setBackgroundResource(android.R.color.transparent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
