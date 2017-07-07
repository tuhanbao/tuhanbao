package com.hhnz.api.cfcrm.constants.enums;

public enum VipType {
    /**
     * 长线客户
     */
    LONG_VIP(1),

    /**
     * 短线客户
     */
    SHORT_VIP(2),

    /**
     * 中线客户
     */
    MEDIUM_VIP(3),

    /**
     * 中长线客户
     */
    MEDIUM_LONG_VIP(4);

    public final int value;

    private VipType(int value) {
        this.value = value;
    }

    public static VipType getVipType(int value) {
        for (VipType temp : VipType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}