package com.tuhanbao.thirdapi.pay.zhongjin;

import com.tuhanbao.thirdapi.pay.PayBean;

public class ZjPayBean extends PayBean {

    private String message;

    private String signature;

    public ZjPayBean(String flowNum, String message, String signature, String payUrl) {
        super(flowNum, payUrl);
        this.message = message;
        this.signature = signature;
    }

    public String getMessage() {
        return message;
    }

    public String getSignature() {
        return signature;
    }

}
