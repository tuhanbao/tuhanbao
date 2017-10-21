package com.tuhanbao.api.crm.constants.enums;

public enum CustomState {
    /**
     * 创建
     */
    CREATE(1),

    /**
     * 意向中
     */
    INTENTION(2),

    /**
     * 已谈妥
     */
    SETTLED(3),

    /**
     * 失败
     */
    FAIL(4);

    public final int value;

    private CustomState(int value) {
        this.value = value;
    }

    public static CustomState getCustomState(int value) {
        for (CustomState temp : CustomState.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}