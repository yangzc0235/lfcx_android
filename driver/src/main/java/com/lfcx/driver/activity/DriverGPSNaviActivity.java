package com.lfcx.driver.activity;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.lfcx.driver.R;

import static com.lfcx.driver.activity.DriverOrderActivity.userOrderEntity;

public class DriverGPSNaviActivity extends DriverBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_gpsnavi);
        showLoading();
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        mStartLatlng.setLongitude(userOrderEntity.getFromlongitude());
        mStartLatlng.setLatitude(userOrderEntity.getFromlatitude());
        mEndLatlng.setLongitude(userOrderEntity.getTolongitude());
        mEndLatlng.setLatitude(userOrderEntity.getTolatitude());
        sList.add(mStartLatlng);
        eList.add(mEndLatlng);
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.setCarNumber("京", "DFZ588");
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
        //userOrderEntity.getFromlongitude(), userOrderEntity.getFromlatitude(), userOrderEntity.getTolongitude(), userOrderEntity.getTolatitude()
        Log.v("system---lo-->",userOrderEntity.getFromlongitude()+"");
        Log.v("system---La-->",userOrderEntity.getFromlatitude()+"");
        Log.v("system---Lo222-->",userOrderEntity.getTolongitude()+"");
        Log.v("system---La222-->",userOrderEntity.getTolatitude()+"");

    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);

    }

    @Override
    public void onNaviViewLoaded() {
        super.onNaviViewLoaded();
        hideLoading();
    }
}
