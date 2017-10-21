package com.tuhanbao.api.crm.constants.enums;

public enum Sex {
    /**
     * 女性
     */
    FAMALE(0),

    /**
     * 男性
     */
    MALE(1);

    public final int value;

    private Sex(int value) {
        this.value = value;
    }

    public static Sex getSex(int value) {
        for (Sex temp : Sex.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}