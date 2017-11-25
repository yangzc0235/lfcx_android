package com.lfcx.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.lfcx.common.net.result.BaseResultBean;
import com.lfcx.common.widget.LoadingDialog;

import retrofit2.Response;

public class CustomerBaseActivity extends FragmentActivity {
    public static final String BUNDLE_KEY = "datas";
    private LoadingDialog mLoadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        mLoadDialog.dismiss();
    }



    //---------------------组件通信
    public void goToActivity(Class aClass, Bundle bundle) {
        Intent intent = new Intent(this, aClass);
        if (bundle != null) {
            intent.putExtra(BUNDLE_KEY, bundle);
        }
        this.startActivity(intent);
    }


    public void goToActivity(Class aClass) {
        goToActivity(aClass, null);
    }

}
