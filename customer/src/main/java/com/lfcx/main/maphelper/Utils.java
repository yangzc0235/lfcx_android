/**  
 * Project Name:Android_Car_Example  
 * File Name:Utils.java  
 * Package Name:com.amap.api.car.example  
 * Date:2015年4月7日下午3:43:05  
 *  
*/  
  
package com.lfcx.main.maphelper;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.google.gson.Gson;
import com.lfcx.common.net.APIFactory;
import com.lfcx.common.utils.SPUtils;
import com.lfcx.main.R;
import com.lfcx.main.application.CustomerApplication;
import com.lfcx.main.consts.SPConstants;
import com.lfcx.main.net.api.CarAPI;
import com.lfcx.main.net.result.CarInfoEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**  
 * ClassName:Utils <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2015年4月7日 下午3:43:05 <br/>  
 * @author   yiyi.qi  
 * @version    
 * @since    JDK 1.6  
 * @see        
 */
public class Utils {

	private static ArrayList<Marker>markers=new ArrayList<Marker>();
	private static double[] coords;
	private static List<LatLng> carsLatLng;
	private static List<LatLng> goLatLng;
	private static List<Marker> showMarks;
	private static List<SmoothMoveMarker> smoothMarkers;
	private static double lng = 0.0;
	private static double lat = 0.0;
	private static CarAPI carAPI;
	private static LatLng mCenter;
	private static AMap mAmap;
	private static Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				refreshCarLocation();
			}
		}
	};
	/**  
	 * 添加模拟测试的车的点
	 */
	public static void addEmulateData(final AMap amap, final LatLng center){
		carAPI= APIFactory.create(CarAPI.class);
		mCenter=center;
		mAmap=amap;
		Map<String, Object> param = new HashMap<>();
		param.put("latitude", center.latitude);
		param.put("longitude",center.longitude);
		param.put("pk_user", SPUtils.getParam(CustomerApplication.getInstance(), SPConstants.KEY_CUSTOMER_PK_USER,""));
		Log.v("system---",center.latitude+"");
		Log.v("system---",center.longitude+"");
		Log.v("system---",SPUtils.getParam(CustomerApplication.getInstance(), SPConstants.KEY_CUSTOMER_PK_USER,"")+"");
		carAPI.getCarInfo(param).enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				try {
					String carInfo = response.body();
					Log.v("system------carInfo-->",carInfo+"");
					CarInfoEntity carInfoEntity=new Gson().fromJson(carInfo,CarInfoEntity.class);
					if(carInfoEntity.getCode().equals("0")){
						carsLatLng = new ArrayList<>();
						LatLng car;
						for (int i = 0; i < carInfoEntity.getList().size(); i++) {
							car= new LatLng(carInfoEntity.getList().get(i).getLatitude(), carInfoEntity.getList().get(i).getLongitude());
							carsLatLng.add(car);

						}
						addCar(amap);
						mHandler.sendEmptyMessageDelayed(1,100000);
					}else {
						Toast.makeText(CustomerApplication.getInstance(), carInfoEntity.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Log.v("system------carInfo-->","异常");
				}

			}
			@Override
			public void onFailure(Call<String> call, Throwable t) {
				Log.v("system--->","请求失败");
				Toast.makeText(CustomerApplication.getInstance(), "失败了", Toast.LENGTH_SHORT).show();
			}
		});
//		refreshCarLocation();

	}


	public static void refreshCarLocation(){
		Map<String, Object> param = new HashMap<>();
		param.put("latitude", mCenter.latitude);
		param.put("longitude",mCenter.longitude);
		param.put("pk_user", SPUtils.getParam(CustomerApplication.getInstance(), SPConstants.KEY_CUSTOMER_PK_USER,""));
		Log.v("system---",mCenter.latitude+"");
		Log.v("system---",mCenter.longitude+"");
		Log.v("system---",SPUtils.getParam(CustomerApplication.getInstance(), SPConstants.KEY_CUSTOMER_PK_USER,"")+"");
		carAPI.getCarInfo(param).enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				try {
					String carInfo = response.body();
					Log.v("system------carInfo-->",carInfo+"");
					CarInfoEntity carInfoEntity=new Gson().fromJson(carInfo,CarInfoEntity.class);
					if(carInfoEntity.getCode().equals("0")){
						goLatLng = new ArrayList<>();
						LatLng car;
						for (int i = 0; i < carInfoEntity.getList().size(); i++) {
							car= new LatLng(carInfoEntity.getList().get(i).getLatitude(), carInfoEntity.getList().get(i).getLongitude());
							goLatLng.add(car);
						}
						startMove(mAmap);
						mHandler.sendEmptyMessageDelayed(1,100000);
					}else {
						Toast.makeText(CustomerApplication.getInstance(), carInfoEntity.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Log.v("system------carInfo-->","异常");
				}

			}
			@Override
			public void onFailure(Call<String> call, Throwable t) {
				Log.v("system--->","请求失败");
				Toast.makeText(CustomerApplication.getInstance(), "失败了", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private static void initData(LatLng latLng) {
		double latitudeDelt=(Math.random()-0.5)*0.1;
		double latitudeDelt1=(Math.random()-0.5)*0.1;
		double latitudeDelt2=(Math.random()-0.5)*0.1;
		double latitudeDelt3=(Math.random()-0.5)*0.1;
		double latitudeDelt4=(Math.random()-0.5)*0.1;
		double latitudeDelt5=(Math.random()-0.5)*0.1;
		double longtitudeDelt=(Math.random()-0.5)*0.1;
		double longtitudeDelt1=(Math.random()-0.5)*0.1;
		double longtitudeDelt2=(Math.random()-0.5)*0.1;
		double longtitudeDelt3=(Math.random()-0.5)*0.1;
		double longtitudeDelt4=(Math.random()-0.5)*0.1;
		double longtitudeDelt5=(Math.random()-0.5)*0.1;
		LatLng car1 = new LatLng(latLng.latitude, latLng.longitude);
		LatLng car2 = new LatLng(latLng.latitude+latitudeDelt1, latLng.longitude+longtitudeDelt);
		LatLng car3 = new LatLng(latLng.latitude+latitudeDelt2, latLng.longitude+longtitudeDelt);
		LatLng car4 = new LatLng(latLng.latitude+latitudeDelt3, latLng.longitude+longtitudeDelt);
		LatLng car5 = new LatLng(latLng.latitude+latitudeDelt4, latLng.longitude+longtitudeDelt);
		LatLng car6 = new LatLng(latLng.latitude+latitudeDelt5, latLng.longitude+longtitudeDelt);
		carsLatLng = new ArrayList<>();
		carsLatLng.add(car1);
		carsLatLng.add(car2);
		carsLatLng.add(car3);
		carsLatLng.add(car4);
		carsLatLng.add(car5);
		carsLatLng.add(car6);
		LatLng go1 = new LatLng(latLng.latitude+latitudeDelt, latLng.longitude+longtitudeDelt);
		LatLng go2 = new LatLng(latLng.latitude+latitudeDelt, latLng.longitude+longtitudeDelt1);
		LatLng go3 = new LatLng(latLng.latitude+latitudeDelt, latLng.longitude+longtitudeDelt2);
		LatLng go4 = new LatLng(latLng.latitude+latitudeDelt, latLng.longitude+longtitudeDelt3);
		LatLng go5 = new LatLng(latLng.latitude+latitudeDelt, latLng.longitude+longtitudeDelt4);
		LatLng go6 = new LatLng(latLng.latitude+latitudeDelt, latLng.longitude+longtitudeDelt5);
		goLatLng = new ArrayList<>();
		goLatLng.add(go1);
		goLatLng.add(go2);
		goLatLng.add(go3);
		goLatLng.add(go4);
		goLatLng.add(go5);
		goLatLng.add(go6);

	}

	/**
	 * 添加车辆
	 * @param aMap
	 */
	private static void addCar(AMap aMap) {
		if (smoothMarkers != null) {
			for (int i = 0; i < smoothMarkers.size(); i++) {
				smoothMarkers.get(i).destroy();
			}
		}

		if (showMarks == null) {
			showMarks = new ArrayList<Marker>();
		}
		for (int j = 0; j < showMarks.size(); j++) {
			showMarks.get(j).remove();
		}
		for (int i = 0; i < carsLatLng.size(); i++) {
			BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.move_car2);
			lng = Double.valueOf(carsLatLng.get(i).longitude);
			lat = Double.valueOf(carsLatLng.get(i).latitude);
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(new LatLng(lat, lng))
					.icon(icon);
			showMarks.add(aMap.addMarker(markerOptions));
			Animation startAnimation = new AlphaAnimation(0, 1);
			startAnimation.setDuration(6000);

//                            showMarks.get(i).setRotateAngle(Float.valueOf(listBaseBean.datas.get(i).angle));
			showMarks.get(i).setAnimation(startAnimation);
			showMarks.get(i).setRotateAngle(new Random().nextInt(359));
			showMarks.get(i).startAnimation();
		}


	}

	/**
	 * 开始移动
	 * @param aMap
	 */
	private static void startMove(AMap aMap) {
		if (smoothMarkers != null) {
			for (int i = 0; i < smoothMarkers.size(); i++) {
				smoothMarkers.get(i).destroy();
			}
		}
		if (showMarks == null) {
			showMarks = new ArrayList<Marker>();
		}
		for (int j = 0; j < showMarks.size(); j++) {
			showMarks.get(j).remove();
		}
		smoothMarkers = null;
		smoothMarkers = new ArrayList<SmoothMoveMarker>();
		BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.move_car2);
		for (int i = 0; i < carsLatLng.size(); i++) {
			double[] newoords = {Double.valueOf(carsLatLng.get(i).longitude), Double.valueOf(carsLatLng.get(i).latitude),
					Double.valueOf(goLatLng.get(i).longitude), Double.valueOf(goLatLng.get(i).latitude)};
			coords = newoords;
			movePoint(icon,aMap);
		}
	}

	/**
	 * 移除位置
	 * @param bitmap
	 * @param aMap
	 */
	public static void movePoint(BitmapDescriptor bitmap,AMap aMap) {
		List<LatLng> points = readLatLngs();
		SmoothMoveMarker smoothMarker = new SmoothMoveMarker(aMap);
		smoothMarkers.add(smoothMarker);
		int num = smoothMarkers.size() - 1;
		smoothMarkers.get(num).setDescriptor(bitmap);
		LatLng drivePoint = points.get(0);
		Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
		points.set(pair.first, drivePoint);
		List<LatLng> subList = points.subList(pair.first, points.size());

		smoothMarkers.get(num).setPoints(subList);
		smoothMarkers.get(num).setTotalDuration(100);
		smoothMarkers.get(num).startSmoothMove();
	}

	private static List<LatLng> readLatLngs() {
		List<LatLng> points = new ArrayList<LatLng>();
		for (int i = 0; i < coords.length; i += 2) {
			points.add(new LatLng(coords[i + 1], coords[i]));
		}
		return points;
	}

	/**
	 * 移除marker
	 */
	public static void removeMarkers() {
		for(Marker marker:markers){
			marker.remove();
			marker.destroy();
		}
		markers.clear();
	}
	
}
  
