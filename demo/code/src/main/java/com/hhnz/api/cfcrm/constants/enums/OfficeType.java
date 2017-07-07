package com.hhnz.api.cfcrm.constants.enums;

public enum OfficeType {
    /**
     * 大A
     */
    A(1),

    /**
     * B店
     */
    B(2),

    /**
     * 默认
     */
    DEFAULT(0);

    public final int value;

    private OfficeType(int value) {
        this.value = value;
    }

    public static OfficeType getOfficeType(int value) {
        for (OfficeType temp : OfficeType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}