package com.lfcx.driver.net.result;

import java.io.Serializable;

/**
 * author: drawthink
 * date  : 2017/10/21
 * des   :
 */

public class SendMessageResult implements Serializable{
//    /****成功示例**/
//    {
//        "reason": "短信发送成功",
//            "result": {
//        "count": 1, /*发送数量*/
//                "fee": 1, /*扣除条数*/
//                "sid": "23d6bc4913614919a823271d820662af" /*短信ID*/
//    },
//        "error_code": 0 /*发送成功*/
//    }
    private String reason;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
