package com.hhnz.api.cfcrm.constants.enums;

public enum CustomType {
    /**
     * 复合肥用户
     */
    IS_FH(1),

    /**
     * 非复合肥用户
     */
    NOT_FH(2);

    public final int value;

    private CustomType(int value) {
        this.value = value;
    }

    public static CustomType getCustomType(int value) {
        for (CustomType temp : CustomType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}