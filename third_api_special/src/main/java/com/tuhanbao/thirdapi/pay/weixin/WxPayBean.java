package com.tuhanbao.thirdapi.pay.weixin;

import com.tuhanbao.thirdapi.pay.PayBean;

public class WxPayBean extends PayBean {

    private String appId;

    private String mchId;

    private String prepayId;

    private String packageVal;

    private String nonceStr;

    // 单位s (原因不明，copy时返回的是秒给app，这里暂定位秒)
    private long time;

    private String sign;

    public WxPayBean(String flowNum, String payUrl) {
        super(flowNum, payUrl);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageVal() {
        return packageVal;
    }

    public void setPackageVal(String packageVal) {
        this.packageVal = packageVal;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
