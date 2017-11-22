package com.lfcx.main.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lfcx.main.R;
import com.lfcx.main.activity.CustomerOrderActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 */

public class OrderWaitFragment extends Fragment {

    TextView cancelBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_order_wait_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        cancelBtn = (TextView)getActivity().findViewById(R.id.cancel_book);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CustomerOrderActivity)getActivity()).switchFragment(CustomerOrderActivity.CANCEL);
            }
        });

        //模拟司机接单
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((CustomerOrderActivity)OrderWaitFragment.this.getActivity()).switchFragment(CustomerOrderActivity.PICK);
                    }
                });
            }
        }, 8000);
    }
}
