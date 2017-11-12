package com.lfcx.customer.util;

import com.lfcx.customer.maphelper.PositionEntity;

/**
 * author: drawthink
 * date  : 2017/10/11
 * des   :
 */

public class LocationUtils {
    private static PositionEntity location;

    public static PositionEntity getLocation() {
        return location;
    }

    public static void setLocation(PositionEntity location) {
        LocationUtils.location = location;
    }


}
