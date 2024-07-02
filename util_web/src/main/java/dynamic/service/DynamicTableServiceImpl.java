/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.service;

import com.td.ca.base.dataservice.filter.Filter;
import com.td.ca.base.util.db.table.Table;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.objutil.ArrayUtil;
import com.td.ca.base.util.rm.DefaultResourceImpl;
import com.td.ca.base.util.rm.ResourceManager;
import com.td.ca.web.db.dynamic.DynamicTableUtil;
import com.td.ca.web.db.dynamic.constant.config.DynamicErrorCode;
import com.td.ca.web.db.dynamic.constant.config.DynamicTableConstants;
import com.td.ca.web.db.dynamic.model.DynamicColumn;
import com.td.ca.web.db.dynamic.model.DynamicTable;
import com.td.ca.web.db.dynamic.model.OldColumn;
import com.td.ca.web.filter.TableSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dynamicTableService")
public class DynamicTableServiceImpl extends DynamicServiceImpl<DynamicTable> {
    public static final TableSelector COLUMN_SELECTOR = new TableSelector(DynamicTableConstants.DynamicTable.TABLE);

    public static final TableSelector FK_TABLE_SELECTOR = new TableSelector(DynamicTableConstants.DynamicTable.TABLE);

    @Autowired
    private DynamicColumnServiceImpl columnService;

    static {
        COLUMN_SELECTOR.joinTable(DynamicTableConstants.DynamicColumn.TABLE);

        FK_TABLE_SELECTOR.addSelectColumn(DynamicTableConstants.DynamicTable.TABLE_NAME);
        FK_TABLE_SELECTOR.addSelectColumn(DynamicTableConstants.DynamicTable.NAME_CN);
        FK_TABLE_SELECTOR.addSelectColumn(DynamicTableConstants.DynamicTable.NAME_EN);
    }

    @Override
    public void init() {
        if (this.commonService == null) {
            return;
        }
        super.init();
        List<DynamicTable> list = super.select(COLUMN_SELECTOR, null);

        // 必须先初始化表，不然会导致外键找不到表
        for (DynamicTable table : list) {
            DynamicTableUtil.registerTable(table);
        }
        for (DynamicTable table : list) {
            initDynamicTableCols(table);
        }
    }

    public List<DynamicTable> selectFKTableList() {
        return this.select(FK_TABLE_SELECTOR, null);
    }

    private void initDynamicTableCols(DynamicTable table) {
        Table tableInfo = DynamicTableUtil.getTable(table.getTableName());
        List<DynamicColumn> cols = table.getDynamicColumns();
        if (cols != null) {
            for (DynamicColumn col : cols) {
                DynamicTableUtil.registerColumn(tableInfo, col);
            }
        }

        checkDB(table, cols);
    }

    private void checkDB(DynamicTable table, List<DynamicColumn> cols) {
        try {
            if (lock != null) {
                lock.tryLock(table.getTableName());
            }
            Map<String, OldColumn> oldCols = getOldColumns(table.getTableName());
            if (oldCols == null) {
                createTableInDB(table);
            }
            if (cols != null) {
                for (DynamicColumn col : cols) {
                    OldColumn oldCol = oldCols == null ? null : oldCols.get(col.getColName());
                    if (oldCol == null) {
                        columnService.createColumnInDB(col);
                    }
                }
            }
        } finally {
            if (lock != null) {
                lock.release(table.getTableName());
            }
        }
    }

    public void addNewTable(DynamicTable table) {
        remediate(table);
        if (this.selectById(table.getTableName()) != null) {
            throw new AppException(DynamicErrorCode.TABLE_EXIST);
        }
        addBizTable(table);
        this.addRelative(table);
    }

    public void updateTable(DynamicTable table) {
        remediate(table);

        Filter filter = new Filter();
        filter.andFilter(DynamicTableConstants.DynamicTable.TABLE_NAME, table.getTableName());
        List<DynamicTable> list = this.select(filter);

        DynamicTable oldTable = ArrayUtil.isEmpty(list) ? null : list.get(0);
        if (oldTable == null) {
            throw new AppException(DynamicErrorCode.NO_RECORD_DATA);
        }

        this.updateRelative(table);

        // 修改TableManager中注册的Table，只能修改语言资源
        ResourceManager.addResource(DefaultResourceImpl.ZH, DynamicTableUtil.getTableName(table.getTableName()), table.getNameCn());
        ResourceManager.addResource(DefaultResourceImpl.EN, DynamicTableUtil.getTableName(table.getTableName()), table.getNameEn());
    }

    public void deleteTable(String tableName) {
        Filter filter = new Filter();
        filter.andFilter(DynamicTableConstants.DynamicTable.TABLE_NAME, tableName);
        List<DynamicTable> list = this.select(COLUMN_SELECTOR, filter);
        if (!ArrayUtil.isEmpty(list)) {
            DynamicTable table = list.get(0);
            dropBizTable(table);

            Filter colFilter = new Filter();
            colFilter.andFilter(DynamicTableConstants.DynamicColumn.TABLE_NAME, tableName);
            columnService.delete(colFilter);
            this.deleteById(tableName);
        }
    }

    private void dropBizTable(DynamicTable table) {
        // 1. 删除db中的表； 2. 从TableManager中去注册
        String sql = "drop table `" + DynamicTableUtil.getTableName(table.getTableName()) + "`;";
        this.excuteSql(sql);
        DynamicTableUtil.unregisterTable(table);
    }

    private void addBizTable(DynamicTable table) {
        remediate(table);
        // 1. 增加db中的表； 2. 向TableManager中注册Table
        createTableInDB(table);
        DynamicTableUtil.registerTable(table);
    }

    private void createTableInDB(DynamicTable table) {
        StringBuilder sb = new StringBuilder();
        String tableName = DynamicTableUtil.getTableName(table.getTableName());
        sb.append("create table ").append("`" + tableName + "`");
        sb.append("(id bigint auto_increment, `gmt_created` bigint, `gmt_modified` bigint, primary key (id));");
        this.excuteSql(sb.toString());
    }

    public static void remediate(DynamicTable dynamicTable) {
        dynamicTable.setTableName(dynamicTable.getTableName().toUpperCase());
    }

    public Map<String, OldColumn> getOldColumns(String tableName) {
        String sql = getAllColumnsSql(tableName);
        List<Map<String, Object>> result = this.excuteSql(sql);
        if (result == null || result.isEmpty()) {
            return null;
        }

        Map<String, OldColumn> list = new HashMap<>();
        for (Map<String, Object> map : result) {
            OldColumn column = getColumn(map);
            list.put(column.getColName(), column);
        }
        return list;
    }

    private String getAllColumnsSql(String tableName) {
        return "select distinct column_name,data_type,column_default from information_schema.columns where table_name = '"
            + DynamicTableUtil.getTableName(tableName) + "' and table_schema = '" + getDBInstance() + "'";
    }

    private String getDBInstance() {
        return this.commonService.getDBInstance();
    }

    private OldColumn getColumn(Map<String, Object> map) {
        map = key2lowcase(map);
        String colName = map.get("column_name").toString();
        colName = colName.toUpperCase();
        String dataType = getDataType(map.get("data_type").toString());
        String columnDefault = null;
        Object colDefaultValue = map.get("column_default");
        if (colDefaultValue != null) {
            columnDefault = colDefaultValue.toString();
        }

       OldColumn col = new OldColumn();
       col.setColName(colName);
       col.setDefaultValue(columnDefault);
       col.setDataType(dataType);
       return col;
    }

    private Map<String, Object> key2lowcase(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<>();
        map.forEach((key, value) -> newMap.put(key.toLowerCase(), value));
        return newMap;
    }

    private String getDataType(String dataTypeStr) {
        int index = dataTypeStr.indexOf("(");
        if (index != -1) {
            dataTypeStr = dataTypeStr.substring(0, index);
        }
        return dataTypeStr.toUpperCase();
    }
}