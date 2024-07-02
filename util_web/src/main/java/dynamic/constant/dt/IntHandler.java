/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.constant.dt;

import com.td.ca.base.util.db.table.Column;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.web.db.dynamic.constant.DiyDataTypeHandler;
import com.td.ca.web.db.dynamic.constant.enums.DiyDataType;
import com.td.ca.web.db.dynamic.model.DynamicColumn;

public class IntHandler implements DiyDataTypeHandler {
    @Override
    public void handle(DynamicColumn column, Column colInfo) {
        String args = column.getArgs();
        if (StringUtil.isEmpty(args)) {
            return;
        }

        colInfo.addValidatorStr("range(" + args + ")");
    }

    @Override
    public DiyDataType getDiyDataType() {
        return DiyDataType.INT;
    }
}
