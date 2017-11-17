package com.lfcx.customer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.common.utils.ToastUtils;
import com.lfcx.customer.R;
import com.lfcx.customer.R2;
import com.lfcx.customer.activity.CustomerMainActivity;
import com.lfcx.customer.activity.CustomerOrderActivity;
import com.lfcx.customer.activity.DestinationActivity;
import com.lfcx.customer.adapter.ShareCarAdapter;
import com.lfcx.customer.consts.SPConstants;
import com.lfcx.customer.maphelper.PositionEntity;
import com.lfcx.customer.maphelper.RouteTask;
import com.lfcx.customer.net.api.CarAPI;
import com.lfcx.customer.net.result.CallCarResult;
import com.lfcx.customer.util.EdtUtil;
import com.lfcx.customer.util.LocationUtils;
import com.lfcx.customer.util.TimeSelectUtils;
import com.lfcx.customer.widget.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author: drawthink
 * date  : 2017/7/28
 * des   :  顺风车
 */
public class SharingCarFragment extends Fragment implements CustomerMainActivity.IBackListener, RouteTask.OnRouteCalculateListener {
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
    CarAPI carAPI;
    /**
     * 1 为点击开始位置  2为点击结束位置
     */
    private int clickType = -1;
    /**
     * 如果已经重新选择了开始位置，则onResume里则不再修改
     */
    private boolean isSelectStartAdress = false;

    private int mSftype = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cv = inflater.inflate(R.layout.fragment_share_car, container, false);
        carAPI = APIFactory.create(CarAPI.class);
        unbinder = ButterKnife.bind(this, cv);
        RouteTask.getInstance(getActivity().getApplicationContext())
                .addRouteCalculateListener(this);
        return cv;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    private void init() {
        ((CustomerMainActivity) getActivity()).setiBackListener(this);
        initViews();
        mAdapter = new ShareCarAdapter(mViews);
        vpShare.setOffscreenPageLimit(3);
        vpShare.setAdapter(mAdapter);
    }

    /**
     * 初始化自己叫车页面相关
     *
     * @return
     */
    private View initMyView() {
        View myView = LayoutInflater.from(getActivity()).inflate(R.layout.c_layout_share_three, null);
        myView.findViewById(R.id.btn_help_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调到帮人叫车界面
                vpShare.setCurrentItem(1);
                mSftype=2;
            }
        });
        myView.findViewById(R.id.btn_with_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调到带小件界面
                vpShare.setCurrentItem(2);
                mSftype=3;
            }
        });

        etStartAdressMy = (EditText) myView.findViewById(R.id.et_start_address);
        etEndAdressMy = (EditText) myView.findViewById(R.id.et_end_address);
        etPeopleCountMy = (EditText) myView.findViewById(R.id.et_people_count);
        etTimeMy = (EditText) myView.findViewById(R.id.et_start_time);
        btnConfrimMy = (Button) myView.findViewById(R.id.btn_confirm);
        etStartAdressMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickType = 1;
                Intent destinationIntent = new Intent(getActivity(), DestinationActivity.class);
                startActivity(destinationIntent);
            }
        });
        etEndAdressMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickType = 2;
                Intent destinationIntent = new Intent(getActivity(), DestinationActivity.class);
                startActivity(destinationIntent);
            }
        });
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
     *
     * @return
     */
    private View initPeoView() {
        View peoView = LayoutInflater.from(getActivity()).inflate(R.layout.c_layout_share_one, null);
        etStartAdressOther = (EditText) peoView.findViewById(R.id.et_start_address);
        etEndAdressOther = (EditText) peoView.findViewById(R.id.et_end_address);
        etPeopleCountOther = (EditText) peoView.findViewById(R.id.et_peo_count);
        etTimeOther = (EditText) peoView.findViewById(R.id.et_start_time);
        etPhoneOther = (EditText) peoView.findViewById(R.id.et_contact_user);
        btnConfrimOther = (Button) peoView.findViewById(R.id.btn_confirm);
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

        etStartAdressOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickType = 1;
                Intent destinationIntent = new Intent(getActivity(), DestinationActivity.class);
                startActivity(destinationIntent);
            }
        });

        etEndAdressOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickType = 2;
                Intent destinationIntent = new Intent(getActivity(), DestinationActivity.class);
                startActivity(destinationIntent);
            }
        });
        return peoView;
    }

    private View initGoodsView() {
        View goodsView = LayoutInflater.from(getActivity()).inflate(R.layout.c_layout_share_two, null);
        etStartAdressGoods = (EditText) goodsView.findViewById(R.id.et_start_address);
        etEndAdressGoods = (EditText) goodsView.findViewById(R.id.et_end_address);
        etTimeGoods = (EditText) goodsView.findViewById(R.id.et_start_time);
        etPhoneGoods = (EditText) goodsView.findViewById(R.id.et_contact_user);
        etUserNameGoods = (EditText) goodsView.findViewById(R.id.et_uname);
        etWeightGoods = (EditText) goodsView.findViewById(R.id.et_goods_weight);
        btnConfrimGoods = (Button) goodsView.findViewById(R.id.btn_confirm);
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

        etStartAdressGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickType = 1;
                Intent destinationIntent = new Intent(getActivity(), DestinationActivity.class);
                startActivity(destinationIntent);
            }
        });

        etEndAdressGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickType = 2;
                Intent destinationIntent = new Intent(getActivity(), DestinationActivity.class);
                startActivity(destinationIntent);
            }
        });
        return goodsView;
    }

    private void initViews() {
        mViews.add(0, initMyView());
        mViews.add(1, initPeoView());
        mViews.add(2, initGoodsView());
    }

    /**
     * 自己叫车下单
     */
    private void generateOrderMy() {
        if (EdtUtil.isEdtEmpty(etPeopleCountMy)) {
            Toast.makeText(getContext().getApplicationContext(), "请输入乘车人数", Toast.LENGTH_SHORT).show();
            return;
        }
        String personCount = EdtUtil.getEdtText(etPeopleCountMy);
        bookCar(personCount, "1", "", "", "1");
    }

    /**
     * 帮人叫车下单
     */
    private void generateOrderOther() {
        if (EdtUtil.isEdtEmpty(etPeopleCountOther)) {
            Toast.makeText(getContext().getApplicationContext(), "请输入乘车人数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (EdtUtil.isEdtEmpty(etPhoneOther)) {
            Toast.makeText(getContext().getApplicationContext(), "请输入乘车人电话", Toast.LENGTH_SHORT).show();
            return;
        }
        String personCount = EdtUtil.getEdtText(etPeopleCountOther);
        String personPhone = EdtUtil.getEdtText(etPhoneGoods);
        String personName = EdtUtil.getEdtText(etUserNameGoods);
        bookCar(personCount, "1", personName, personPhone, "0");
    }

    /**
     * 带小件下单
     */
    private void generateOrderGoods() {

        if (EdtUtil.isEdtEmpty(etWeightGoods)) {
            Toast.makeText(getContext().getApplicationContext(), "请输入货物重量", Toast.LENGTH_SHORT).show();
            return;
        }
        if (EdtUtil.isEdtEmpty(etTimeGoods)) {
            Toast.makeText(getContext().getApplicationContext(), "请选择乘车时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (EdtUtil.isEdtEmpty(etUserNameGoods)) {
            Toast.makeText(getContext().getApplicationContext(), "请输入收货人姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (EdtUtil.isEdtEmpty(etPhoneGoods)) {
            Toast.makeText(getContext().getApplicationContext(), "请输入收货人电话", Toast.LENGTH_SHORT).show();
            return;
        }
        String personPhone = EdtUtil.getEdtText(etPhoneGoods);
        String personName = EdtUtil.getEdtText(etUserNameGoods);
        bookCar("0", "0", personName, personPhone, "0");
    }

    public void bookCar(String personcount, String ispacket, String consignee, String consigneetel, String ishelpother) {

//        pk_user:(用户主键 必填);fromlatitude:(纬度 必填); fromlongitude:(经度 必填);title:订单标题 (必填);
//        content:订单内容(必填) ;fromaddress: 开始位置(必填) ; toaddress: 目的地 (必填);tolatitude:(纬度 必填);
//        tolongitude:(经度 必填);ordertype : 订单类型 (必填)(0 顺风车 1 专车 ，2 专车-〉预约 3 专车-〉包车 4 专车-〉接机 5 专车-〉送机);
//        status:订单状态(*)（0：待付款；1订单完成2 ：订单取消）, cancelreason(取消原因),personcount 乘车人数，
//        begintime（包车下单开始时间）, privatetype(0:4小时套餐 ；1 ：8小时套餐);aircode（航班号）;
//        ispacket :( 是否带小件 0:是;1:否);packetmoney;小件金额；consignee 收货人; consigneetel 收货人电话;
//        ishelpother(是否替人叫车 0:是;1:否);Name :(乘车人姓名);ridertel(乘车人电话);
//        carstyletype:车辆类型(0:舒适型，1:豪华型，2:7座商务) ;isprivatecar:是否专车(0:是专车;1:顺风车)
        String from_address = LocationUtils.getLocation().address;
        String to_address = RouteTask
                .getInstance(getActivity()).getEndPoint().address;
        Map<String, Object> param = new HashMap<>();
        param.put("pk_user", SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_PK_USER, ""));
        param.put("fromaddress", LocationUtils.getLocation().address);
        param.put("toaddress", RouteTask
                .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
        param.put("fromlongitude", LocationUtils.getLocation().longitude);
        param.put("fromlatitude", LocationUtils.getLocation().latitue);
        param.put("tolongitude", RouteTask
                .getInstance(getActivity().getApplicationContext()).getEndPoint().longitude);
        param.put("tolatitude", RouteTask
                .getInstance(getActivity().getApplicationContext()).getEndPoint().latitue);
        param.put("title", "用户123预约您");
        param.put("content", "用户123预约您");
        param.put("reservatedate", "2017-10-22 20:00:00");
        param.put("ridertel", SPUtils.getParam(getActivity(), SPConstants.KEY_CUSTOMER_MOBILE, ""));
        param.put("ordertype", 0);
        param.put("status", 0);// 0 待付款 1 订单完成 2 订单取消
        param.put("isprivatecar", 1);
        param.put("personcount", personcount);//乘车人数
        param.put("ispacket", ispacket);//是否带小件
        param.put("consignee", consignee);//收货人姓名
        param.put("consigneetel", consigneetel);//收货人电话
        param.put("ishelpother", ishelpother);//是否替人叫车
        Log.v("fromlatitude----------",LocationUtils.getLocation().latitue+"");
        Log.v("fromlongitude----------",LocationUtils.getLocation().longitude+"");
        Log.v("tolatitude----------",RouteTask
                .getInstance(getActivity()).getEndPoint().latitue+"");
        Log.v("tolongitude----------",RouteTask
                .getInstance(getActivity()).getEndPoint().longitude+"");
        carAPI.generateOrder(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    CallCarResult result = new Gson().fromJson(response.body(), CallCarResult.class);
                    Toast.makeText(getActivity().getApplicationContext(), response.body(), Toast.LENGTH_SHORT).show();

                    Log.v("system---下单信息-->",response.body());
                    //下单成功
                    if ("0".equals(result.getCode())) {
                        ((CustomerOrderActivity)getActivity()).switchFragment(CustomerOrderActivity.WAIT);
                        Toast.makeText(getContext(), "等待接单啦", Toast.LENGTH_SHORT).show();
                    } else {
                        ToastUtils.shortToast(getActivity(), result.getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onBack() {
        //当此时展示个人叫车页面，返回键交由上一层处理
        if (vpShare.getCurrentItem() != 0) {
            vpShare.setCurrentItem(0);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        PositionEntity location = LocationUtils.getLocation();
        if (null != location && !isSelectStartAdress) {
            if (mSftype == 1) {
                etStartAdressMy.setText(location.getAddress());
            } else if (mSftype == 2) {
                etStartAdressOther.setText(location.getAddress());
            } else if (mSftype == 3) {
                etStartAdressGoods.setText(location.getAddress());
            }

        }
    }

    @Override
    public void onRouteCalculate(float cost, float distance, int duration) {

        Log.v("system----address---->", RouteTask
                .getInstance(getActivity().getApplicationContext()).getEndPoint().address);

        Log.v("system---distance---->", distance + "");


        if(mSftype == 1){
            if (clickType == 2) {
                etEndAdressMy.setText(RouteTask
                        .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
            } else if (clickType == 1) {
                etStartAdressMy.setText(RouteTask
                        .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
                isSelectStartAdress = true;
            }
        }else if(mSftype==2){
            if (clickType == 2) {
                etEndAdressOther.setText(RouteTask
                        .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
            } else if (clickType == 1) {
                etStartAdressOther.setText(RouteTask
                        .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
                isSelectStartAdress = true;
            }
        }else if(mSftype==3){
            if (clickType == 2) {
                etEndAdressGoods.setText(RouteTask
                        .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
            } else if (clickType == 1) {
                etStartAdressGoods.setText(RouteTask
                        .getInstance(getActivity().getApplicationContext()).getEndPoint().address);
                isSelectStartAdress = true;
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
