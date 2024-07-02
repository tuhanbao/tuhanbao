/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.constant.enums;

public enum EnumType {
    /**
     * 普通的下拉框
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
     * 级联下拉
     */
    CASCAD_ENUM(19);

    public final int value;

    private EnumType(int value) {
        this.value = value;
    }

    public static EnumType getEnumType(int value) {
        for (EnumType temp : EnumType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

}