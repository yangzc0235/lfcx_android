package com.lfcx.driver.util;


import com.amap.api.location.AMapLocation;

/**
 * author: drawthink
 * date  : 2017/10/11
 * des   :
 */

public class LocationUtils {
    private static AMapLocation  location;

    public static AMapLocation  getLocation() {
        return location;
    }

    public static void setLocation(AMapLocation location) {
        LocationUtils.location = location;
    }


}
