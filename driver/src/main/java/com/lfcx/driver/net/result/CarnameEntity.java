package com.lfcx.driver.net.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yzc on 2017/11/23.
 */

public class CarnameEntity implements Serializable {


    /**
     * firstLetter : A
     * carmodels : 奥迪
     * Pingyin : aodi
     * cartypes : ["A1","A3","A4","A4L","A5","A6","A6L","A7","A8L","Q3","Q5","Q7 ","S4","S5","S6","S8","TT ","R8","Allroad quattro"]
     */

    private String firstLetter;
    private String carmodels;
    private String Pingyin;
    private List<String> cartypes;

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getCarmodels() {
        return carmodels;
    }

    public void setCarmodels(String carmodels) {
        this.carmodels = carmodels;
    }

    public String getPingyin() {
        return Pingyin;
    }

    public void setPingyin(String Pingyin) {
        this.Pingyin = Pingyin;
    }

    public List<String> getCartypes() {
        return cartypes;
    }

    public void setCartypes(List<String> cartypes) {
        this.cartypes = cartypes;
    }
}
