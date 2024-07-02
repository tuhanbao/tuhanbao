/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.constant.config;

import com.td.ca.base.util.exception.BaseErrorCode;

public class DynamicErrorCode extends BaseErrorCode {
    /**
     * 资源类型名称已存在。
     */
    public static final int TABLE_EXIST = 1001;

    /**
     * 同一个资源类型中已存在相同名称的字段。
     */
    public static final int COLUMN_EXIST = 1002;

    /**
     * 一个资源类型最多只能创建{0}个属性。
     */
    public static final int COLUMN_TOO_MANY = 1003;

    /**
     * 请先删除资源类型中的所有数据，再删除所有字段。
     */
    public static final int DELETE_LAST_COLUMN = 1004;

    /**
     * 请先删除资源类型中的所有数据，再删除所有字段。
     */
    public static final int TABLE_HAS_NO_CLOUMNS = 1005;

    /**
     * 请先删除资源类型中的所有数据，再删除所有字段。
     */
    public static final int ENUM_IS_USE = 1006;
}
