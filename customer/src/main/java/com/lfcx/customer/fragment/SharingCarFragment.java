package com.lfcx.customer.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.activity.CustomerMainActivity;
import com.lfcx.customer.adapter.ShareCarAdapter;
import com.lfcx.customer.maphelper.PositionEntity;
import com.lfcx.customer.util.LocationUtils;
import com.lfcx.customer.util.TimeSelectUtils;
import com.lfcx.customer.widget.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   :  顺风车
 */
public class SharingCarFragment extends Fragment implements CustomerMainActivity.IBackListener{
    private Unbinder unbinder;

    //个人叫车--
    private EditText etStartAdressMy;
    private EditText etEndAdressMy;
    private EditText etTimeMy;
    private EditText etPeopleCountMy;
    private Button btnOther;
    private Button btnGoods;
    private Button btnConfrimMy;
    //

    //帮人叫车--
    private EditText etStartAdressOther;
    private EditText etEndAdressOther;
    private EditText etTimeOther;
    private EditText etPeopleCountOther;
    private EditText etPhoneOther;
    private Button btnConfrimOther;
    //

    //带小件--
    private EditText etStartAdressGoods;
    private EditText etEndAdressGoods;
    private EditText etTimeGoods;
    private EditText etWeightGoods;
    private EditText etPhoneGoods;
    private EditText etUserNameGoods;
    private Button btnConfrimGoods;
    //
    @BindView(R2.id.c_share_vp)
    NoScrollViewPager vpShare;

    private List<View> mViews = new ArrayList<>();
    private ShareCarAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cv = inflater.inflate(R.layout.fragment_share_car, container, false);
        unbinder = ButterKnife.bind(this,cv);
        return cv;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        PositionEntity location = LocationUtils.getLocation();
//        if(null != location && !isSelectStartAdress){
//            etStartAddress.setText(location.getAddress());
//        }
    }

    private void init(){
        ((CustomerMainActivity)getActivity()).setiBackListener(this);
        initViews();
        mAdapter = new ShareCarAdapter(mViews);
        vpShare.setOffscreenPageLimit(3);
        vpShare.setAdapter(mAdapter);
    }

    /**
     * 初始化自己叫车页面相关
     * @return
     */
    private View initMyView(){
        View myView = LayoutInflater.from(getActivity()).inflate(R.layout.c_layout_share_three,null);
        myView.findViewById(R.id.btn_help_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调到帮人叫车界面
                vpShare.setCurrentItem(1);
            }
        });
        myView.findViewById(R.id.btn_with_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调到带小件界面
                vpShare.setCurrentItem(2);
            }
        });

        etStartAdressMy = (EditText) myView.findViewById(R.id.et_start_address);
        etEndAdressMy = (EditText)myView.findViewById(R.id.et_end_address);
        etPeopleCountMy = (EditText)myView.findViewById(R.id.et_people_count);
        etTimeMy = (EditText)myView.findViewById(R.id.et_start_time);
        btnConfrimMy = (Button)myView.findViewById(R.id.btn_confirm);
        btnConfrimMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateOrderMy();
            }
        });
        etTimeMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeSelectUtils timeSelectUtils = new TimeSelectUtils(getActivity(), null, null, null, new TimeSelectUtils.GetSubmitTime() {
                    @Override
                    public void selectTime(String startDate, String entDate) {
                        etTimeMy.setText(startDate);
                    }
                });
                //显示我们的时间选择器
                timeSelectUtils.dateTimePicKDialog();
            }
        });
        return myView;
    }

    /**
     * 初始化帮人叫车
     * @return
     */
    private View initPeoView(){
        View peoView = LayoutInflater.from(getActivity()).inflate(R.layout.c_layout_share_one,null);
        etStartAdressOther = (EditText) peoView.findViewById(R.id.et_start_address);
        etEndAdressOther = (EditText) peoView.findViewById(R.id.et_end_address);
        etPeopleCountOther = (EditText)peoView.findViewById(R.id.et_peo_count);
        etTimeOther = (EditText)peoView.findViewById(R.id.et_start_time);
        etPhoneOther = (EditText)peoView.findViewById(R.id.et_contact_user);
        btnConfrimOther = (Button)peoView.findViewById(R.id.btn_confirm);
        btnConfrimOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateOrderOther();
            }
        });
        etTimeOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeSelectUtils timeSelectUtils = new TimeSelectUtils(getActivity(), null, null, null, new TimeSelectUtils.GetSubmitTime() {
                    @Override
                    public void selectTime(String startDate, String entDate) {
                        etTimeOther.setText(startDate);
                    }
                });
                //显示我们的时间选择器
                timeSelectUtils.dateTimePicKDialog();
            }
        });
        return peoView;
    }

    private View initGoodsView(){
        View goodsView =   LayoutInflater.from(getActivity()).inflate(R.layout.c_layout_share_two,null);
        etStartAdressGoods = (EditText)goodsView.findViewById(R.id.et_start_address);
        etEndAdressGoods = (EditText)goodsView.findViewById(R.id.et_end_address);
        etTimeGoods = (EditText)goodsView.findViewById(R.id.et_start_time);
        etPhoneGoods = (EditText)goodsView.findViewById(R.id.et_contact_user);
        etUserNameGoods = (EditText)goodsView.findViewById(R.id.et_uname);
        etWeightGoods = (EditText)goodsView.findViewById(R.id.et_goods_weight);
        btnConfrimGoods = (Button)goodsView.findViewById(R.id.btn_confirm);
        btnConfrimGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateOrderGoods();
            }
        });
        etTimeGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeSelectUtils timeSelectUtils = new TimeSelectUtils(getActivity(), null, null, null, new TimeSelectUtils.GetSubmitTime() {
                    @Override
                    public void selectTime(String startDate, String entDate) {
                        etTimeGoods.setText(startDate);
                    }
                });
                //显示我们的时间选择器
                timeSelectUtils.dateTimePicKDialog();
            }
        });
        return goodsView;
    }
    private void initViews(){
        mViews.add(0,initMyView());
        mViews.add(1,initPeoView());
        mViews.add(2,initGoodsView());
    }

    /**
     * 自己叫车下单
     */
   private void generateOrderMy(){

   }
    /**
     * 帮人叫车下单
     */
    private void generateOrderOther(){

    }
    /**
     * 带小件下单
     */
    private void generateOrderGoods(){

    }



    @Override
    public boolean onBack() {
        //当此时展示个人叫车页面，返回键交由上一层处理
        if(vpShare.getCurrentItem() != 0){
            vpShare.setCurrentItem(0);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
