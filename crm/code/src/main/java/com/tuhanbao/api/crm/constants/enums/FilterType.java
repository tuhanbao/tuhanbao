package com.tuhanbao.api.crm.constants.enums;

public enum FilterType {
    /**
     * 客户管理
     */
    CUSTOM(1);

    public final int value;

    private FilterType(int value) {
        this.value = value;
    }

    public static FilterType getFilterType(int value) {
        for (FilterType temp : FilterType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}