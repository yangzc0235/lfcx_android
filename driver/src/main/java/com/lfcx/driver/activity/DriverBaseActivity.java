package com.lfcx.driver.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.lfcx.common.widget.LoadingDialog;

public class DriverBaseActivity extends FragmentActivity {

    private LoadingDialog mLoadDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToast(String msg){
        if(TextUtils.isEmpty(msg)){
            return;
        }
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    public void showLoading(){
        if(null == mLoadDialog){
            mLoadDialog = new LoadingDialog(this);
        }
        mLoadDialog.show();
    }

    public void hideLoading(){
        if(null == mLoadDialog){
            return;
        }
        mLoadDialog.hide();
    }
}
