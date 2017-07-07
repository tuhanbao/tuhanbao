package com.hhnz.api.cfcrm.constants.enums;

public enum FestivalType {
    SOLAR(1),

    LUNAR(2);

    public final int value;

    private FestivalType(int value) {
        this.value = value;
    }

    public static FestivalType getFestivalType(int value) {
        for (FestivalType temp : FestivalType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}