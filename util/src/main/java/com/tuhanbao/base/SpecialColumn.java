package com.td.ca.base.util.db.table;

import com.td.ca.base.Constants;
import com.td.ca.base.dataservice.IDataGroup;
import com.td.ca.base.util.db.IField;
import com.td.ca.base.util.io.codegenarator.code.AbstractXls2CodeUtil;
import com.td.ca.base.util.io.codegenarator.table.DataType;
import com.td.ca.base.util.io.codegenarator.table.Relation;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.base.util.objutil.parse.NameAndArgs;

/**
 * 计算列
 * @author wangbing
 */
public class SpecialColumn extends Column {

    protected SpecialColumn() {
        super();
    }

    public SpecialColumn(Table table, String name, String aliasName, DataType dataType) {
        super(table, name, aliasName, dataType);
    }

    @Override
    public SpecialColumn clone() {
        SpecialColumn clone = new SpecialColumn(this.table, this.name, this.aliasName, this.dataType);
        return clone;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getNameWithTable() {
        return name;
    }

    @Override
    public String getSqlName(boolean isFilterCol) {
        return name;
    }

    @Override
    public String getSqlNameWithDataGroup(boolean isFilterCol) {
        return name;
    }

    /**
     * select使用的查询字段
     */
    public String getNameWithAs() {
        return getSqlNameWithDataGroup(false);
    }

    public String getAsName() {
        return name;
    }

    public String getTableName() {
        return Constants.EMPTY;
    }

}
