package com.lfcx.driver.util;


import com.lfcx.driver.maphelper.PositionEntity;

/**
 * author: drawthink
 * date  : 2017/10/11
 * des   :
 */

public class DriverLocationUtils {
    private static PositionEntity location;

    public static PositionEntity getLocation() {
        return location;
    }

    public static void setLocation(PositionEntity location) {
        DriverLocationUtils.location = location;
    }


}
