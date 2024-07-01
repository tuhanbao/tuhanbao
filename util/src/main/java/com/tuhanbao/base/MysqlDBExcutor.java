package com.td.ca.base.util.io.codegenarator.sql.excutor;

import com.td.ca.base.Constants;
import com.td.ca.base.util.io.codegenarator.sql.IndexInfo;
import com.td.ca.base.util.io.codegenarator.sql.IndexType;
import com.td.ca.base.util.io.codegenarator.table.DataType;
import com.td.ca.base.util.io.codegenarator.table.IColumn;
import com.td.ca.base.util.io.codegenarator.table.ImportColumn;
import com.td.ca.base.util.io.codegenarator.table.ImportTable;
import com.td.ca.base.util.io.codegenarator.table.XlsTable;
import com.td.ca.base.util.io.codegenarator.table.XlsTable.XlsColumn;
import com.td.ca.base.util.objutil.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wangbing
 *
 */
public class MysqlDBExcutor extends AbstractDBExcutor {
    /**
     * 构造sql文件
     *
     * 老系统用的，现在已经用不着
     *
     * @param tables
     * @return
     * @throws IOException
     */
    @Override
    @Deprecated
    public String getSql(List<XlsTable> tables) throws IOException {
        StringBuilder sb = new StringBuilder();

        List<String> idList = new ArrayList<String>();
        for (XlsTable table : tables) {
            sb.append("drop table" + " if exists" + " ")
                    .append(table.getTableName())
                    .append(Constants.SEMICOLON)
                    .append(Constants.ENTER);
            sb.append("create table ").append(table.getTableName()).append(Constants.LEFT_PARENTHESE);
            XlsColumn pk = null;
            for (XlsColumn col : table.getList()) {
                sb.append(getColumnSqlInner(col, false));
                if (col.isPK()
                        && (col.getDataType() == DataType.LONG
                        || DataType.INT.getName().equalsIgnoreCase(col.getDataType().getName()))) {
                    pk = col;
                    sb.append(Constants.BLANK).append("auto_increment");
                }
                sb.append(Constants.COMMA).append(Constants.BLANK);
            }

            if (pk != null) {
                sb.append("primary key (`").append(pk.getName()).append("`)");
            } else {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append(Constants.RIGHT_PARENTHESE).append(Constants.SEMICOLON).append(Constants.ENTER);
            sb.append(Constants.ENTER);

            if (table.getIdIndex() != null && !table.getIdIndex().isEmpty()) {
                idList.add(table.getIdIndex());
            }
        }

        for (String id : idList) {
            sb.append(Constants.ENTER).append("insert into ids values(").append(id).append(",1,1);");
        }

        return sb.toString();
    }

    private static boolean needAutoIncrement(IColumn iColumn) {
        if (iColumn instanceof ImportColumn) {
            ImportColumn col = (ImportColumn) iColumn;
            return col.isPK()
                    && col.getTable().isAutoIncrement()
                    && (col.getDataType() == DataType.LONG
                    || DataType.INT.getName().equalsIgnoreCase(col.getDataType().getName()));
        } else {
            return false;
        }
    }

    protected String getDropTableSql(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop table if exists ").append(tableName).append(Constants.SEMICOLON);
        return sb.toString();
    }

    @Override
    protected String getColumnSql(ImportColumn col, boolean isChangeCol) {
        return getColumnSqlInner(col, isChangeCol);
    }

    private String getColumnSqlInner(IColumn col, boolean isChangeCol) {
        StringBuilder sb = new StringBuilder("`" + col.getName() + "`");
        sb.append(Constants.BLANK);
        DataType dt = col.getDataType();

        long length = col.getLength();
        if (dt == DataType.STRING) {
            sb.append("varchar(").append(length).append(") character set utf8mb4 collate utf8mb4_general_ci");
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
            // 始终保留十位整数，小数位可调
            if (length > 0) {
                sb.append("decimal(15, 5)");
            } else {
                sb.append("decimal(" + (length + 10) + ", " + length + ")");
            }
        } else if (dt == DataType.BIGDEECIMAL) {
            sb.append("decimal");
        } else if (dt == DataType.GIS) {
            sb.append("geometry");
        }

        String defaultValue = col.getDefaultValue();
        // string需要给一个空默认值
        if (dt == DataType.STRING && StringUtil.isEmpty(defaultValue)) {
            sb.append(Constants.BLANK)
                    .append("default")
                    .append(Constants.BLANK)
                    .append("''");
        } else if (!StringUtil.isEmpty(defaultValue)) {
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
        } else if (isChangeCol && !col.isPK()) {
            sb.append(Constants.BLANK).append("default").append(Constants.BLANK).append("null");
        }
        if (!StringUtil.isEmpty(col.getComment())) {
            sb.append(" comment '").append(col.getComment()).append("'");
        }

        if (needAutoIncrement(col)) {
            // 默认自增
            sb.append(Constants.BLANK).append("auto_increment");
        }
        return sb.toString();
    }

    @Override
    protected String getSeqSql(ImportTable table) {
        return Constants.EMPTY;
    }

    @Override
    protected String getAddIndexSql(ImportTable table, String tableName, IndexInfo indexInfo) {
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isEmpty(tableName)) {
            tableName = table.getName();
        }
        sb.append("alter table `").append(tableName);
        if (indexInfo.indexType == IndexType.NORMAL) {
            sb.append("` add index `");
        } else {
            sb.append("` add unique index `");
        }
        sb.append(indexInfo.name).append("` (");

        int length = indexInfo.cols.length;
        String[] cols = new String[length];
        for (int i = 0; i < length; i++) {
            cols[i] = "`" + indexInfo.cols[i] + "`";
        }
        sb.append(StringUtil.array2String(cols, ", "));
        sb.append(");");
        return sb.toString();
    }

    @Override
    protected String getDropIndexSql(ImportTable table, String tableName, IndexInfo indexInfo) {
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isEmpty(tableName)) {
            tableName = table.getName();
        }
        sb.append("alter table `").append(tableName).append("` drop index `");
        sb.append(indexInfo.name).append("`;");
        return sb.toString();
    }

    @Override
    protected String changePK(ImportColumn pk, ImportColumn oldPk) {
        if (pk == null && oldPk == null) {
            return Constants.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        if (pk == null && oldPk != null) {
            String tableName = oldPk.getTable().getName();
            sb.append("alter table ").append(tableName).append(" drop primary key;");
        } else if (pk != null && oldPk == null) {
            String tableName = pk.getTable().getName();
            sb.append("alter" + " table" + " ")
                    .append(tableName)
                    .append(" add primary key (")
                    .append(pk.getName())
                    .append(");")
                    .append(Constants.ENTER);
            sb.append("alter table ").append(tableName).append(" modify column ").append(getColumnSql(pk));

            if (needAutoIncrement(pk)) {
                sb.append(Constants.BLANK).append("auto_increment");
            }
            sb.append(Constants.SEMICOLON);
        } else if (!pk.getName().equals(oldPk.getName())) {
            String tableName = pk.getTable().getName();
            sb.append("alter table ")
                    .append(tableName)
                    .append(" drop primary key," + " add primary" + " key (")
                    .append(pk.getName())
                    .append(");");
            sb.append("alter table ").append(tableName).append(" modify column ").append(getColumnSql(pk));
            if (needAutoIncrement(pk)) {
                sb.append(Constants.BLANK).append("auto_increment");
            }
            sb.append(Constants.SEMICOLON);
        }
        return sb.toString();
    }

    @Override
    protected String changeColumn(ImportColumn col, ImportColumn oldCol) {
        String tableName = col.getTable().getName();
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ").append(tableName);
        if (col.getName().equalsIgnoreCase(oldCol.getName())) {
            sb.append(" modify column ").append(getColumnSql(col, true));
            if (needAutoIncrement(col)) {
                sb.append(Constants.BLANK).append("auto_increment");
            }
        } else {
            sb.append(" change column ").append(oldCol.getName()).append(Constants.BLANK).append(getColumnSql(col));
        }
        sb.append(Constants.SEMICOLON);
        return sb.toString();
    }

    @Override
    protected String addColumn(ImportColumn col, ImportColumn preCol) {
        String tableName = col.getTable().getName();
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ").append(tableName).append(" add column ").append(getColumnSql(col));
        if (preCol != null) {
            sb.append(" after ").append("`" + preCol.getName() + "`");
        }
        sb.append(Constants.SEMICOLON);
        return sb.toString();
    }

    @Override
    protected String dropColumn(ImportColumn col) {
        String tableName = col.getTable().getName();
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ")
                .append(tableName)
                .append(" drop column ")
                .append("`" + col.getName() + "`")
                .append(Constants.SEMICOLON);
        return sb.toString();
    }
}
