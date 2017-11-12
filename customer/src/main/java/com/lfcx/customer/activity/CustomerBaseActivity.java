package com.lfcx.customer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.common.widget.LoadingDialog;
import com.lfcx.customer.net.result.LoginResult;

import retrofit2.Response;

public class CustomerBaseActivity extends FragmentActivity {

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

    public boolean checkResponse(Response<BaseResultBean> response){
        if(null == response || null == response.body()){
            return false;
        }
        return true;
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
