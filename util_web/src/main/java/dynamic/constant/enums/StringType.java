/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.constant.enums;

public enum StringType {
    IP(1),

    TEL_PHONE(3),

    FIX_PHONE(4),

    ID_CARD(5),

    EMAIL(6),

    DEFAULT(0);

    public final int value;

    public final String pattern;

    private StringType(int value) {
        this(value, null);
    }

    private StringType(int value, String pattern) {
        this.value = value;
        this.pattern = pattern;
    }

    public static StringType getStringType(int value) {
        for (StringType temp : StringType.values()) {
            if (temp.value == value) {
                return temp;
            }
        }

        return null;
    }

    public String getPattern() {
        return pattern;
    }
}
