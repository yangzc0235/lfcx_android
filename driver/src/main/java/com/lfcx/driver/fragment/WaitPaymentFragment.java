package com.lfcx.driver.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lfcx.driver.R;

/**
 *
 */

public class WaitPaymentFragment extends Fragment implements View.OnClickListener {
    private ListView mListviewWaitPatmentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_wait_payment, container, false);
        mListviewWaitPatmentFragment = (ListView) view.findViewById(R.id.listview_wait_patment_fragment);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }




    @Override
    public void onClick(View v) {

    }


}
