/**  
 * Project Name:Android_Car_Example  
 * File Name:RouteTask.java  
 * Package Name:com.amap.api.car.example  
 * Date:2015年4月3日下午2:38:10  
 *  
 */

package com.lfcx.customer.maphelper;

import android.content.Context;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:RouteTask <br/>
 * Function: 封装的驾车路径规划 <br/>
 * Date: 2015年4月3日 下午2:38:10 <br/>
 * 
 * @author yiyi.qi
 * @version
 * @since JDK 1.6
 * @see
 */
public class RouteTask implements OnRouteSearchListener {

	private static RouteTask mRouteTask;

	private RouteSearch mRouteSearch;

	private PositionEntity mFromPoint;

	private PositionEntity mToPoint;

	private List<OnRouteCalculateListener> mListeners = new ArrayList<OnRouteCalculateListener>();
	private List<OnRoutePolyLineCalculateListener> mPolyLineListeners = new ArrayList<OnRoutePolyLineCalculateListener>();

	public interface OnRouteCalculateListener {
		public void onRouteCalculate(float cost, float distance, int duration);

	}

	//路线中的所有点连成的线段绘制成功
	public interface OnRoutePolyLineCalculateListener {
		public void onRoutePolyLineCalculate(List<DriveStep> stepList);
	}

	public static RouteTask getInstance(Context context) {
		if (mRouteTask == null) {
			mRouteTask = new RouteTask(context);
		}
		return mRouteTask;
	}

	public PositionEntity getStartPoint() {
		return mFromPoint;
	}

	public void setStartPoint(PositionEntity fromPoint) {
		mFromPoint = fromPoint;
	}

	public PositionEntity getEndPoint() {
		return mToPoint;
	}

	public void setEndPoint(PositionEntity toPoint) {
		mToPoint = toPoint;
	}

	private RouteTask(Context context) {
		mRouteSearch = new RouteSearch(context);
		mRouteSearch.setRouteSearchListener(this);
	}

	public void search() {
		if (mFromPoint == null || mToPoint == null) {
			return;
		}

		FromAndTo fromAndTo = new FromAndTo(new LatLonPoint(mFromPoint.latitue,
				mFromPoint.longitude), new LatLonPoint(mToPoint.latitue,
				mToPoint.longitude));
		DriveRouteQuery driveRouteQuery = new DriveRouteQuery(fromAndTo,
				RouteSearch.DrivingDefault, null, null, "");

		mRouteSearch.calculateDriveRouteAsyn(driveRouteQuery);
	}

	public void search(PositionEntity fromPoint, PositionEntity toPoint) {

		mFromPoint = fromPoint;
		mToPoint = toPoint;
		search();

	}

	public void addRouteCalculateListener(OnRouteCalculateListener listener) {
		synchronized (this) {
			if (mListeners.contains(listener))
				return;
			mListeners.add(listener);
		}
	}

	public void removeRouteCalculateListener(OnRouteCalculateListener listener) {
		synchronized (this) {
			mListeners.remove(listener);
		}
	}

	public void addRoutePolyLineCalculateListener(OnRoutePolyLineCalculateListener listener) {
		synchronized (this) {
			if (mPolyLineListeners.contains(listener))
				return;
			mPolyLineListeners.add(listener);
		}
	}

	public void removeRoutePolyLineCalculateListener(OnRoutePolyLineCalculateListener listener) {
		synchronized (this) {
			mPolyLineListeners.remove(listener);
		}
	}


	@Override
	public void onDriveRouteSearched(DriveRouteResult driveRouteResult,
			int resultCode) {
		if (resultCode == AMapException.CODE_AMAP_SUCCESS && driveRouteResult != null) {
			synchronized (this) {
				for (OnRouteCalculateListener listener : mListeners) {
					List<DrivePath> drivepaths = driveRouteResult.getPaths();
					float distance = 0;
					int duration = 0;
					if (drivepaths.size() > 0) {
						DrivePath drivepath = drivepaths.get(0);
						distance = drivepath.getDistance() / 1000;
						duration = (int) (drivepath.getDuration() / 60);
					}
					float cost = driveRouteResult.getTaxiCost();
					listener.onRouteCalculate(cost, distance, duration);
				}

				for(OnRoutePolyLineCalculateListener listener :mPolyLineListeners) {
					List<DrivePath> drivepaths = driveRouteResult.getPaths();
					List<DriveStep> lists = null;
					if (drivepaths.size() > 0) {
						DrivePath drivepath = drivepaths.get(0);
						lists = drivepath.getSteps();
					}
					listener.onRoutePolyLineCalculate(lists);
				}
			}
		}
		// TODO 可以根据app自身需求对查询错误情况进行相应的提示或者逻辑处理
	}


	@Override
	public void onWalkRouteSearched(WalkRouteResult arg0, int arg1) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onRideRouteSearched(RideRouteResult arg0, int arg1){

		// TODO Auto-generated method stub

	}

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
	}
}
