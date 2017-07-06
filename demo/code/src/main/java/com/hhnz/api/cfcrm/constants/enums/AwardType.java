package com.hhnz.api.cfcrm.constants.enums;

public enum AwardType {
    /**
     * 农庄积分
     */
    NONGZHUANG_INTEGRAL(1),

    /**
     * 投资券
     */
    INVESTMENT_CERTIFICATE(2),

    /**
     * 加息券
     */
    COUPON(3),

    /**
     * 商城券
     */
    MALL_COUPON(4),

    /**
     * 粮票
     */
    FOOD_STAMPS(5);

    public final int value;

    private AwardType(int value) {
        this.value = value;
    }

    public static AwardType getAwardType(int value) {
        for (AwardType temp : AwardType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}