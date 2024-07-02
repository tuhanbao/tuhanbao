/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.constant.dt;

import com.td.ca.base.util.db.table.Column;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.web.db.dynamic.DynamicTableUtil;
import com.td.ca.web.db.dynamic.constant.DiyDataTypeHandler;
import com.td.ca.web.db.dynamic.constant.enums.DiyDataType;
import com.td.ca.web.db.dynamic.constant.enums.StringType;
import com.td.ca.web.db.dynamic.model.DynamicColumn;

public class StringHandler implements DiyDataTypeHandler {
    @Override
    public void handle(DynamicColumn column, Column colInfo) {
        String args = column.getArgs();
        if (StringUtil.isEmpty(args)) {
            return;
        }

        if (args.startsWith("0,")) {
            args = args.substring(2);
            colInfo.addValidatorStr("pattern(" + args + ")");
        } else {
            StringType st = StringType.getStringType(Integer.valueOf(args));
            if (st != null && !StringUtil.isEmpty(st.getPattern())) {
                colInfo.addValidatorStr("pattern(" + st.getPattern() + ")");
            }
        }
        colInfo.setLength(DynamicTableUtil.getLength(column.getDataType()));
    }

    @Override
    public DiyDataType getDiyDataType() {
        return DiyDataType.STRING;
    }
}
