/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic;

import com.td.ca.base.Constants;
import com.td.ca.base.util.clazz.ClazzUtil;
import com.td.ca.base.util.db.table.CacheType;
import com.td.ca.base.util.db.table.Column;
import com.td.ca.base.util.db.table.ColumnFactory;
import com.td.ca.base.util.db.table.Table;
import com.td.ca.base.util.io.codegenarator.table.DataType;
import com.td.ca.base.util.log.LogUtil;
import com.td.ca.base.util.rm.DefaultResourceImpl;
import com.td.ca.base.util.rm.ResourceManager;
import com.td.ca.web.db.TableManager;
import com.td.ca.web.db.dynamic.constant.DiyDataTypeHandler;
import com.td.ca.web.db.dynamic.constant.config.DynamicTableConstants;
import com.td.ca.web.db.dynamic.constant.enums.DiyDataType;
import com.td.ca.web.db.dynamic.model.DynamicColumn;
import com.td.ca.web.db.dynamic.model.DynamicTable;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DynamicTableUtil {

    private static final String TABLE_PREFIX = "tb_diy_";

    private static Map<DiyDataType, DiyDataTypeHandler> dataTypeHandlers = new HashMap<>();

    static {
        try {
            List<DiyDataTypeHandler> list = ClazzUtil.getAllInstanceOfInterface(null, DiyDataTypeHandler.class);
            for (DiyDataTypeHandler handler : list) {
                DiyDataType diyDataType = handler.getDiyDataType();
                if (diyDataType != null) {
                    dataTypeHandlers.put(diyDataType, handler);
                }
            }
        } catch (IOException e) {
            log.error(LogUtil.getMessage(e));
            log.warn("cannot init DynamicTableUtil.");
        }
    }

    public static void registerColumn(Table table, DynamicColumn column) {
        DiyDataType dataType = column.getDataType();
        Column colInfo = ColumnFactory.createColumn(table, column.getColName().toLowerCase(), ClazzUtil.convertUnderline2LowerHump(column.getColName()),
            getRealDataType(dataType), false);
        handleDataType(column, colInfo);

        ResourceManager.addResource(DefaultResourceImpl.ZH, colInfo.getNameWithTable(), column.getNameCn());
        ResourceManager.addResource(DefaultResourceImpl.EN, colInfo.getNameWithTable(), column.getNameEn());
    }

    private static void handleDataType(DynamicColumn column, Column colInfo) {
        // 必选
        if (column.getRequired()) {
            colInfo.setValidatorStr("notNull");
        }

        DiyDataType diyDataType = column.getDataType();
        DiyDataTypeHandler dataTypeHandler = dataTypeHandlers.get(diyDataType);
        if (dataTypeHandler != null) {
            dataTypeHandler.handle(column, colInfo);
        }
    }

    public static void unregisterColumn(Table table, DynamicColumn column) {
        table.removeColumn(column.getColName());

        ResourceManager.removeResource(table.getName() + "." + column.getColName());
    }

    public static Table registerTable(DynamicTable table) {
        String tableName = DynamicTableUtil.getTableName(table.getTableName());
        Table tableInfo =  DynamicTableConstants.getTable(1, tableName, ClazzUtil.convertUnderline2UpperHump(table.getTableName()),
            getCacheType(table.getCacheType()), null);
        tableInfo.setDynamic(true);
        ColumnFactory.createColumn(tableInfo, "id", "id", DataType.LONG, true).setLength(0);
        ColumnFactory.createColumn(tableInfo, Constants.CREATE_TIME_COL_NAME, ClazzUtil.convertUnderline2LowerHump(Constants.CREATE_TIME_COL_NAME), DataType.LONG, false).setLength(0);
        ColumnFactory.createColumn(tableInfo, Constants.MODIFY_TIME_COL_NAME, ClazzUtil.convertUnderline2LowerHump(Constants.MODIFY_TIME_COL_NAME), DataType.LONG, false).setLength(0);
        TableManager.register(tableInfo);

        ResourceManager.addResource(DefaultResourceImpl.ZH, tableName, table.getNameCn());
        ResourceManager.addResource(DefaultResourceImpl.EN, tableName, table.getNameEn());
        return tableInfo;
    }

    public static void unregisterTable(DynamicTable table) {
        TableManager.unregister(ClazzUtil.convertUnderline2UpperHump(table.getTableName()));
        String tableName = getTableName(table.getTableName());
        ResourceManager.removeResource(tableName);
        for (DynamicColumn column : table.getDynamicColumns()) {
            ResourceManager.removeResource(tableName + "." + column.getColName());
        }
    }

    public static int getLength(DiyDataType dataType) {
        // 根据客户填的类型，预估他想要的长度
        // 目前该字段没有使用到，可以默认返回511
        return 511;
    }

    public static DataType getRealDataType(DiyDataType dataType) {
        if (dataType == DiyDataType.STRING) {
            return DataType.STRING;
        } else if (dataType == DiyDataType.FLOAT) {
            return DataType.DOUBLE;
        } else if (dataType == DiyDataType.TIME) {
            return DataType.DATE;
        } else if (dataType == DiyDataType.BOOLEAN) {
            return DataType.BOOLEAN;
        } else if (dataType == DiyDataType.FK) {
            return DataType.LONG;
        } else if (dataType == DiyDataType.TEXT) {
            return DataType.TEXT;
        } else if (dataType == DiyDataType.GIS) {
            return DataType.GIS;
        } else if (dataType == DiyDataType.MULTIPLE_SELECT) {
            // 多选使用字符串，逗号分隔
            return DataType.STRING;
        } else {
            // 所有的下拉框都先使用INT
            return DataType.INT;
        }
    }


    public static String getTableName(DynamicColumn col) {
        return getTableName(col.getTableName());
    }

    /**
     * @param tableName 下划线式的table名称
     * @return
     */
    public static Table getTable(String tableName) {
        return TableManager.getTableByClassName(ClazzUtil.convertUnderline2UpperHump(tableName));
    }

    public static String getTableName(String tableName) {
        return TABLE_PREFIX + tableName.toLowerCase();
    }

    private static CacheType getCacheType(int cacheType) {
        return CacheType.values()[cacheType];
    }
}
