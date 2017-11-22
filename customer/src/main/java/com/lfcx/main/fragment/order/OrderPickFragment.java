package com.lfcx.main.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfcx.main.R;
import com.lfcx.main.activity.CustomerOrderActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */

public class OrderPickFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_order_pick_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //模拟支付
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((CustomerOrderActivity)OrderPickFragment.this.getActivity()).switchFragment(CustomerOrderActivity.PAY);
                    }
                });
            }
        }, 8000);
    }
}
