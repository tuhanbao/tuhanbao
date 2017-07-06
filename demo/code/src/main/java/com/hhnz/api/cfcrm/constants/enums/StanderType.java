package com.hhnz.api.cfcrm.constants.enums;

public enum StanderType {
    /**
     * 小于三个月
     */
    LESS_THREE(1),

    /**
     * 大于三个月小于六个月
     */
    LARGR_THREE_LESS_SIX(2),

    /**
     * 大于六个月小于十二个月
     */
    LARGR_SIX_LESS_TWELVE(3),

    /**
     * 大于十二个月
     */
    LARGE_TWELVE(4);

    public final int value;

    private StanderType(int value) {
        this.value = value;
    }

    public static StanderType getStanderType(int value) {
        for (StanderType temp : StanderType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}