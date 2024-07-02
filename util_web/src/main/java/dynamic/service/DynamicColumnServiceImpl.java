/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.service;

import com.td.ca.base.Constants;
import com.td.ca.base.dataservice.filter.Filter;
import com.td.ca.base.util.clazz.ClazzUtil;
import com.td.ca.base.util.db.table.Table;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.exception.BaseErrorCode;
import com.td.ca.base.util.io.codegenarator.table.DataType;
import com.td.ca.base.util.log.LogUtil;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.web.db.TableManager;
import com.td.ca.web.db.dynamic.DynamicTableUtil;
import com.td.ca.web.db.dynamic.constant.config.DynamicConstantsConfig;
import com.td.ca.web.db.dynamic.constant.config.DynamicErrorCode;
import com.td.ca.web.db.dynamic.constant.config.DynamicTableConstants;
import com.td.ca.web.db.dynamic.model.DynamicColumn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("dynamicColumnService")
public class DynamicColumnServiceImpl extends DynamicServiceImpl<DynamicColumn> {

    @Autowired
    private DynamicTableServiceImpl tableService;

    public void addBizColumn(DynamicColumn column) {
        remediate(column);
        // 1. 增加db中的字段；2. 入库，3. 从TableManager注册的Table中增加字段
        checkTableExist(column.getTableName());
        checkColumnNum(column);
        checkColumnExist(column);
        createColumnInDB(column);

        this.add(column);
        DynamicTableUtil.registerColumn(DynamicTableUtil.getTable(column.getTableName()), column);
    }

    public void updateBizColumn(DynamicColumn column) {
        remediate(column);
        // 1. 修改db中的字段； 2. 从TableManager注册的Table中修改字段 3. 入库
        DynamicColumn oldCol = this.selectById(column.getId());
        if (oldCol == null) {
            throw new AppException(BaseErrorCode.NO_RECORD_DATA);
        }
        if (!StringUtil.isEqual(column.getColName(), oldCol.getColName()) || column.getDataType() != oldCol.getDataType()) {
            throw new AppException("name and dataType cannot be change.");
        }
        String sql = changeColumn(column, oldCol);
        try {
            this.excuteSql(sql);

            Table table = DynamicTableUtil.getTable(oldCol.getTableName());
            table.removeColumn(oldCol.getColName());
            DynamicTableUtil.registerColumn(table, column);
        } catch (Exception e) {
            log.error(LogUtil.getMessage(e));
            throw new AppException("modify column fail.");
        }

        this.update(column);
    }

    public void dropBizColumn(long id) {
        // 1. 删除db中业务表的字段； 2. 从TableManager注册的Table中删除字段, 3, 入库
        DynamicColumn col = this.selectById(id);
        if (col == null) {
            throw new AppException("column is deleted.");
        }
        String sql = dropColumn(col);
        this.excuteSql(sql);

        DynamicTableUtil.unregisterColumn(DynamicTableUtil.getTable(col.getTableName()), col);
        this.deleteById(id);
    }

    private void checkColumnNum(DynamicColumn column) {
        if (getColumnNum(column.getTableName()) >= DynamicConstantsConfig.DYNAMIC_COLUMN_MAX_NUM) {
            throw new AppException(DynamicErrorCode.COLUMN_TOO_MANY, DynamicConstantsConfig.DYNAMIC_COLUMN_MAX_NUM + "");
        }
    }

    private int getColumnNum(String tableName) {
        Filter filter = new Filter();
        filter.andFilter(DynamicTableConstants.DynamicColumn.TABLE_NAME, tableName);
        List<DynamicColumn> list = this.select(filter);
        return list == null ? 0 : list.size();
    }

    public void createColumnInDB(DynamicColumn column) {
        String sql = addColumn(column);
        this.excuteSql(sql);
    }

    public void checkIsLastColumn(long... ids) {
        Filter filter = new Filter();
        filter.andFilter(DynamicTableConstants.DynamicColumn.ID, ids);
        List<DynamicColumn> list = this.select(filter);
        if (list == null) {
            return;
        }

        // 只能删同一个表的字段
        String tableName = null;
        for (DynamicColumn column : list) {
            if (tableName == null) {
                tableName = column.getTableName();
            } else {
                if (!StringUtil.isEqual(tableName, column.getTableName())) {
                    throw new AppException("cannot delete columns in diffrent table.");
                }
            }
        }

        if (getColumnNum(tableName) <= list.size()) {
            if (commonService.count(TableManager.getTableByClassName(ClazzUtil.convertUnderline2UpperHump(tableName)), null) > 0) {
                throw new AppException(DynamicErrorCode.DELETE_LAST_COLUMN);
            }
        }
    }

    private void checkTableExist(String tableName) {
        if (this.tableService.selectById(tableName) == null) {
            throw new AppException("table not exist");
        }
    }

    private void checkColumnExist(DynamicColumn column) {
        Filter filter = new Filter();
        filter.andFilter(DynamicTableConstants.DynamicColumn.COL_NAME, column.getColName());
        filter.andFilter(DynamicTableConstants.DynamicColumn.TABLE_NAME, column.getTableName());
        List<DynamicColumn> list = this.select(filter);
        if (list != null && !list.isEmpty()) {
            throw new AppException(DynamicErrorCode.COLUMN_EXIST);
        }
    }

    private String changeColumn(DynamicColumn newCol, DynamicColumn oldCol) {
        String tableName = DynamicTableUtil.getTableName(oldCol);
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ").append(tableName);
        if (newCol.getColName().equalsIgnoreCase(oldCol.getColName())) {
            sb.append(" modify column ").append(getColumnSql(newCol, true));
        } else {
            sb.append(" change column ").append("`" + oldCol.getColName().toLowerCase() + "`").append(Constants.BLANK).append(getColumnSql(newCol, false));
        }
        sb.append(Constants.SEMICOLON);
        return sb.toString();
    }

    private String addColumn(DynamicColumn col) {
        String tableName = DynamicTableUtil.getTableName(col);
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ").append(tableName).append(" add column ").append(getColumnSql(col, false));
        sb.append(Constants.SEMICOLON);
        return sb.toString();
    }

    private String dropColumn(DynamicColumn col) {
        String tableName = DynamicTableUtil.getTableName(col);
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ")
            .append(tableName)
            .append(" drop column `")
            .append(col.getColName())
            .append("`" + Constants.SEMICOLON);
        return sb.toString();
    }

    private String getColumnSql(DynamicColumn col, boolean isChangeCol) {
        StringBuilder sb = new StringBuilder("`" + col.getColName().toLowerCase() + "`");
        sb.append(Constants.BLANK);
        DataType dt = DynamicTableUtil.getRealDataType(col.getDataType());

        if (dt == DataType.STRING) {
            sb.append("varchar(").append(DynamicTableUtil.getLength(col.getDataType())).append(") collate utf8_unicode_ci");
        } else if (dt == DataType.TEXT) {
            sb.append("text collate utf8_unicode_ci");
        } else if (dt == DataType.BYTE || dt == DataType.BOOLEAN) {
            sb.append("tinyint");
        } else if (dt == DataType.SHORT) {
            sb.append("smallint");
        } else if (dt == DataType.INT) {
            sb.append("int");
        } else if (dt == DataType.LONG || dt == DataType.DATE) {
            sb.append("bigint");
        } else if (dt == DataType.BYTEARRAY || dt == DataType.BLOB) {
            sb.append("blob");
        } else if (dt == DataType.BIGBLOB) {
            sb.append("mediumblob");
        } else if (dt == DataType.FLOAT || dt == DataType.DOUBLE) {
            sb.append("decimal(9,5)");
        } else if (dt == DataType.BIGDEECIMAL) {
            sb.append("decimal");
        } else if (dt == DataType.GIS) {
            sb.append("geometry");
        }

        String defaultValue = col.getDefaultValue();
        if (defaultValue != null && !defaultValue.isEmpty()) {
            if (dt == DataType.STRING) {
                sb.append(Constants.BLANK)
                    .append("default")
                    .append(Constants.BLANK)
                    .append("'")
                    .append(defaultValue)
                    .append("'");
            } else {
                sb.append(Constants.BLANK).append("default").append(Constants.BLANK).append(defaultValue);
            }
        } else if (isChangeCol) {
            sb.append(Constants.BLANK).append("default").append(Constants.BLANK).append("null");
        }
        if (!StringUtil.isEmpty(col.getRemark())) {
            sb.append(" comment \"").append(col.getRemark()).append("\"");
        }
        return sb.toString();
    }

    private static void remediate(DynamicColumn column) {
        column.setColName(column.getColName().toUpperCase());
        column.setTableName(column.getTableName().toUpperCase());
    }
}