package com.lfcx.driver.event;

/**
 * Created by yzc on 2017/11/20.
 */

public class MoneyEvent {
    private String key;
    private String arg1;
    private String arg2;
    private String arg3;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public MoneyEvent(String key, String arg1, String arg2, String arg3) {
        this.key = key;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    public MoneyEvent(String arg1, String arg2, String arg3) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getArg3() {
        return arg3;
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }
}
