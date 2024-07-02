/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.constant.dt;

import com.td.ca.base.util.db.table.Column;
import com.td.ca.base.util.db.table.Table;
import com.td.ca.base.util.db.table.data.BooleanValue;
import com.td.ca.base.util.io.codegenarator.table.Relation;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.web.db.dynamic.DynamicTableUtil;
import com.td.ca.web.db.dynamic.constant.DiyDataTypeHandler;
import com.td.ca.web.db.dynamic.constant.enums.DiyDataType;
import com.td.ca.web.db.dynamic.model.DynamicColumn;

public class FKHandler implements DiyDataTypeHandler {
    @Override
    public void handle(DynamicColumn column, Column colInfo) {
        String args = column.getArgs();
        if (StringUtil.isEmpty(args)) {
            return;
        }

        String[] array = StringUtil.string2Array(args);
        boolean isN2One = true;
        if (array.length >= 2) {
            isN2One = !(BooleanValue.valueOf(array[1]).getValue());
        }
        colInfo.setRelation(isN2One ? Relation.N2One : Relation.One2One);

        Table table = DynamicTableUtil.getTable(array[0]);
        colInfo.setFk(table);

        colInfo.addValidatorStr("fk");
    }

    @Override
    public DiyDataType getDiyDataType() {
        return DiyDataType.FK;
    }
}
