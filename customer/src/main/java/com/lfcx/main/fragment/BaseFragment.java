package com.lfcx.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.lfcx.common.widget.LoadingDialog;

/**
 * Created by yzc on 2017/11/24.
 */

public class BaseFragment extends Fragment {
    private LoadingDialog mLoadDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadDialog = new LoadingDialog(getContext());
    }

    public void showLoading(){
        mLoadDialog.show();
    }

    public void hideLoading(){
        if(null == mLoadDialog){
            return;
        }
        mLoadDialog.hide();
        mLoadDialog.dismiss();
    }

}
