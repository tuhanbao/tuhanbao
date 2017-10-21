package com.tuhanbao.api.crm.constants.enums;

public enum ColType {
    /**
     * 枚举
     */
    ENUM(1),

    /**
     * 字符串
     */
    STRING(2),

    /**
     * 整数
     */
    INT(3),

    /**
     * 浮点
     */
    FLOAT(4),

    /**
     * 日期
     */
    DATE(5);

    public final int value;

    private ColType(int value) {
        this.value = value;
    }

    public static ColType getColType(int value) {
        for (ColType temp : ColType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}