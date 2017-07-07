package com.hhnz.api.cfcrm.constants.enums;

public enum InvestType {
    /**
     * 90
     */
    MIN_DAY(1),

    /**
     * 180
     */
    MEDIUM_DAY(2),

    /**
     * 365
     */
    MAX_DAY(3);

    public final int value;

    private InvestType(int value) {
        this.value = value;
    }

    public static InvestType getInvestType(int value) {
        for (InvestType temp : InvestType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}