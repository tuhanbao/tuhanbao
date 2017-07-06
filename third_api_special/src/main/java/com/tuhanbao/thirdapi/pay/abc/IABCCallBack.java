package com.tuhanbao.thirdapi.pay.abc;

import java.util.Map;

import com.abc.trustpay.client.ebus.PaymentResult;

public interface IABCCallBack {
    //回调
    Map<String, Object> callBackBeforeGetPayResult(Map<String, Object> args, PaymentResult tResult);
}
