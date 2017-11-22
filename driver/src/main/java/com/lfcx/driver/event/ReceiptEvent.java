package com.lfcx.driver.event;

/**
 * Created by yzc on 2017/11/19.
 */

public class ReceiptEvent {

    private String key;
    private String value;

    public ReceiptEvent(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
