package com.lfcx.driver.net.result;

import java.io.Serializable;

/**
 * Created by yzc on 2017/11/24.
 */

public class NameEntity implements Serializable{
    private String carmodels;
    private String type;

    public String getCarmodels() {
        return carmodels;
    }

    public void setCarmodels(String carmodels) {
        this.carmodels = carmodels;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
