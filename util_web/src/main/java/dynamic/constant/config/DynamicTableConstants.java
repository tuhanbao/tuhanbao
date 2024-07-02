/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.constant.config;

import com.td.ca.base.util.db.table.CacheType;
import com.td.ca.base.util.db.table.Column;
import com.td.ca.base.util.db.table.ColumnFactory;
import com.td.ca.base.util.db.table.ServiceBeanKeyManager;
import com.td.ca.base.util.db.table.Table;
import com.td.ca.base.util.io.codegenarator.table.DataType;
import com.td.ca.base.util.io.codegenarator.table.Relation;
import com.td.ca.web.db.TableManager;

/**
 * 本来为自动生成的常量类
 * 请勿擅自修改
 * @tuhanbao
 */
public class DynamicTableConstants {

    private static String TABLE_PREFIX = null;

    static {
        initTablePrefix();
    }

    private static void initTablePrefix() {
        if (TABLE_PREFIX == null) {
            TABLE_PREFIX = "tb_diy_,tb_";
            ServiceBeanKeyManager.addTablePrefix(TABLE_PREFIX);
        }
    }

    public static Table getTable(int colSize, String name, String aliasName, CacheType cacheType, String modelName) {
        return getTable(colSize, name, aliasName, cacheType, modelName, null);
    }

    public static Table getTable(int colSize, String name, String aliasName, CacheType cacheType, String modelName, String seqName) {
        initTablePrefix();
        Table table = new Table(colSize, name, aliasName, cacheType, modelName, seqName);
        return table;
    }

    public static final class DynamicColumn {
        public static final Table TABLE = getTable(16, "tb_dynamic_column", "DynamicColumn", CacheType.NOT_CACHE, com.td.ca.web.db.dynamic.model.DynamicColumn.class.getName());

        public static final Column ID = ColumnFactory.createColumn(TABLE, "id", "id", DataType.LONG, true).setLength(0);
        public static final Column COL_NAME = ColumnFactory.createColumn(TABLE, "col_name", "colName", DataType.STRING, false).setLength(255).setValidatorStr("length(,128)");
        public static final Column NAME_CN = ColumnFactory.createColumn(TABLE, "name_cn", "nameCn", DataType.STRING, false).setLength(511).setValidatorStr("length(,128)");
        public static final Column NAME_EN = ColumnFactory.createColumn(TABLE, "name_en", "nameEn", DataType.STRING, false).setLength(511).setValidatorStr("length(,128)");
        public static final Column TABLE_NAME = ColumnFactory.createColumn(TABLE, "table_name", "tableName", DataType.STRING, false).setLength(255).setRelation(Relation.N2One).setFk(DynamicTable.TABLE);
        public static final Column DATA_TYPE = ColumnFactory.createColumn(TABLE, "data_type", "dataType", DataType.INT, false).setLength(4);
        public static final Column ARGS = ColumnFactory.createColumn(TABLE, "args", "args", DataType.STRING, false).setLength(255);
        public static final Column NEED_SHOW = ColumnFactory.createColumn(TABLE, "need_show", "needShow", DataType.BOOLEAN, false).setLength(0);
        public static final Column NEED_FILTER = ColumnFactory.createColumn(TABLE, "need_filter", "needFilter", DataType.BOOLEAN, false).setLength(0);
        public static final Column NEED_ORDER = ColumnFactory.createColumn(TABLE, "need_order", "needOrder", DataType.BOOLEAN, false).setLength(0);
        public static final Column REQUIRED = ColumnFactory.createColumn(TABLE, "required", "required", DataType.BOOLEAN, false).setLength(0);
        public static final Column DEFAULT_VALUE = ColumnFactory.createColumn(TABLE, "default_value", "defaultValue", DataType.STRING, false).setLength(255);
        public static final Column PRIORITY = ColumnFactory.createColumn(TABLE, "priority", "priority", DataType.INT, false).setLength(0);
        public static final Column REMARK = ColumnFactory.createColumn(TABLE, "remark", "remark", DataType.STRING, false).setLength(511);
        public static final Column GMT_CREATED = ColumnFactory.createColumn(TABLE, "gmt_created", "gmtCreated", DataType.LONG, false).setLength(0);
        public static final Column GMT_MODIFIED = ColumnFactory.createColumn(TABLE, "gmt_modified", "gmtModified", DataType.LONG, false).setLength(0);
    }

    public static final class DynamicEnum {
        public static final Table TABLE = getTable(6, "tb_dynamic_enum", "DynamicEnum", CacheType.NOT_CACHE, com.td.ca.web.db.dynamic.model.DynamicEnum.class.getName());

        public static final Column ID = ColumnFactory.createColumn(TABLE, "id", "id", DataType.LONG, true).setLength(0);
        public static final Column NAME_CN = ColumnFactory.createColumn(TABLE, "name_cn", "nameCn", DataType.STRING, false).setLength(511).setValidatorStr("length(,128)");
        public static final Column NAME_EN = ColumnFactory.createColumn(TABLE, "name_en", "nameEn", DataType.STRING, false).setLength(511).setValidatorStr("length(,128)");
        public static final Column ENUM_TYPE = ColumnFactory.createColumn(TABLE, "enum_type", "enumType", DataType.INT, false).setLength(4);
        public static final Column GMT_CREATED = ColumnFactory.createColumn(TABLE, "gmt_created", "gmtCreated", DataType.LONG, false).setLength(0);
        public static final Column GMT_MODIFIED = ColumnFactory.createColumn(TABLE, "gmt_modified", "gmtModified", DataType.LONG, false).setLength(0);
    }

    public static final class DynamicEnumItem {
        public static final Table TABLE = getTable(9, "tb_dynamic_enum_item", "DynamicEnumItem", CacheType.NOT_CACHE, com.td.ca.web.db.dynamic.model.DynamicEnumItem.class.getName());

        public static final Column ID = ColumnFactory.createColumn(TABLE, "id", "id", DataType.LONG, true).setLength(0);
        public static final Column ENUM_ID = ColumnFactory.createColumn(TABLE, "enum_id", "enumId", DataType.LONG, false).setLength(0).setRelation(Relation.N2One).setFk(DynamicEnum.TABLE);
        public static final Column NAME_CN = ColumnFactory.createColumn(TABLE, "name_cn", "nameCn", DataType.STRING, false).setLength(511).setValidatorStr("length(,128)");
        public static final Column NAME_EN = ColumnFactory.createColumn(TABLE, "name_en", "nameEn", DataType.STRING, false).setLength(511).setValidatorStr("length(,128)");
        public static final Column VALUE = ColumnFactory.createColumn(TABLE, "value", "value", DataType.INT, false).setLength(0);
        public static final Column PARENT_ID = ColumnFactory.createColumn(TABLE, "parent_id", "parentId", DataType.LONG, false).setLength(0).setRelation(Relation.N2One).setFk(DynamicEnumItem.TABLE);
        public static final Column PRIORITY = ColumnFactory.createColumn(TABLE, "priority", "priority", DataType.INT, false).setLength(0);
        public static final Column GMT_CREATED = ColumnFactory.createColumn(TABLE, "gmt_created", "gmtCreated", DataType.LONG, false).setLength(0);
        public static final Column GMT_MODIFIED = ColumnFactory.createColumn(TABLE, "gmt_modified", "gmtModified", DataType.LONG, false).setLength(0);
    }

    public static final class DynamicTable {
        public static final Table TABLE = getTable(9, "tb_dynamic_table", "DynamicTable", CacheType.NOT_CACHE, com.td.ca.web.db.dynamic.model.DynamicTable.class.getName());

        public static final Column TABLE_NAME = ColumnFactory.createColumn(TABLE, "table_name", "tableName", DataType.STRING, true).setLength(255).setValidatorStr("length(,128)");
        public static final Column NAME_CN = ColumnFactory.createColumn(TABLE, "name_cn", "nameCn", DataType.STRING, false).setLength(511).setValidatorStr("length(,128)");
        public static final Column NAME_EN = ColumnFactory.createColumn(TABLE, "name_en", "nameEn", DataType.STRING, false).setLength(511).setValidatorStr("length(,128)");
        public static final Column CACHE_TYPE = ColumnFactory.createColumn(TABLE, "cache_type", "cacheType", DataType.INT, false).setLength(0);
        public static final Column TAG = ColumnFactory.createColumn(TABLE, "tag", "tag", DataType.STRING, false).setLength(127);
        public static final Column INDEXES = ColumnFactory.createColumn(TABLE, "indexes", "indexes", DataType.STRING, false).setLength(255);
        public static final Column REMARK = ColumnFactory.createColumn(TABLE, "remark", "remark", DataType.STRING, false).setLength(511);
        public static final Column GMT_CREATED = ColumnFactory.createColumn(TABLE, "gmt_created", "gmtCreated", DataType.LONG, false).setLength(0);
        public static final Column GMT_MODIFIED = ColumnFactory.createColumn(TABLE, "gmt_modified", "gmtModified", DataType.LONG, false).setLength(0);
    }

}