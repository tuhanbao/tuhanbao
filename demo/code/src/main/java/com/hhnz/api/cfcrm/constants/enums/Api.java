package com.hhnz.api.cfcrm.constants.enums;

public enum Api {
    /**
     * 发积分调用接口地址
     */
    INTEGRAL_SEND("api/VipCoupon/exchangeIntegral.do"),

    /**
     * 发券调用接口地址
     */
    COUPON_SEND("api/VipCoupon/sendCoupon.json"),

    /**
     * 获取券组调用接口地址
     */
    GET_COUPON("api/VipCoupon/queryCouponGroup.json");

    public final String value;

    private Api(String value) {
        this.value = value;
    }

    public static Api getApi(String value) {
        for (Api temp : Api.values()) {
            if (temp.value.equals(value)) {
                return temp;
            }
        }

        return null;
    }

}