package com.lfcx.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.lfcx.common.R;

/**
 * author: drawthink
 * desc  : (该类描述)
 */

public class MapMarkerUtils {

    private static MapMarkerUtils util;
    private MapMarkerUtils(){}

   public static MapMarkerUtils instance(){
       if(null == util){
           util = new MapMarkerUtils();
       }
       return util;
   }

    public MarkerOptions getMarkerOptions(LatLng lng,Bitmap bitmap){
        MarkerOptions markerOptions = new MarkerOptions();
        try{
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(lng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }catch (Exception e){

        }
        return markerOptions;
    }
}
