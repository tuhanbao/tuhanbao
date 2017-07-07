package com.hhnz.api.cfcrm.constants.enums;

public enum MoneyBackStatus {
    /**
     * 已回款
     */
    BACK(4),

    /**
     * 未回款
     */
    UNBACK(5);

    public final int value;

    private MoneyBackStatus(int value) {
        this.value = value;
    }

    public static MoneyBackStatus getMoneyBackStatus(int value) {
        for (MoneyBackStatus temp : MoneyBackStatus.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}