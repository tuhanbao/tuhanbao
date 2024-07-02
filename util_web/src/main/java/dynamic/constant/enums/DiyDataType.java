/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.constant.enums;

public enum DiyDataType {
    /**
     * 整数
     */
    INT(1),

    /**
     * 小数
     */
    FLOAT(2),

    /**
     * 字符串
     */
    STRING(3),

    /**
     * 日期
     */
    TIME(4),

    /**
     * 布尔值
     */
    BOOLEAN(5),

    /**
     * 外键
     */
    FK(6),

    /**
     * 长文本
     */
    TEXT(7),

    /**
     * 坐标
     */
    GIS(8),

    /**
     * 下拉框
     */
    SELECT(16),

    /**
     * 单选
     */
    RADIO(17),

    /**
     * 多选
     */
    MULTIPLE_SELECT(18),

    /**
     * 级联下拉框
     */
    CASCAD_SELECT(19);

    public final int value;

    private DiyDataType(int value) {
        this.value = value;
    }

    public static DiyDataType getDiyDataType(int value) {
        for (DiyDataType temp : DiyDataType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}