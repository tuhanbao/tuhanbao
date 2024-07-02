/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.constant;

import com.td.ca.base.util.db.table.Column;
import com.td.ca.web.db.dynamic.constant.enums.DiyDataType;
import com.td.ca.web.db.dynamic.model.DynamicColumn;

public interface DiyDataTypeHandler {
    void handle(DynamicColumn column, Column colInfo);

    DiyDataType getDiyDataType();
}
