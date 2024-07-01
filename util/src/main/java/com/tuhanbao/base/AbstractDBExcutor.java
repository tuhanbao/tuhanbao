package com.td.ca.base.util.io.codegenarator.sql.excutor;

import com.td.ca.base.Constants;
import com.td.ca.base.util.db.conn.DBSrc;
import com.td.ca.base.util.db.table.CutType;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.io.codegenarator.sql.IndexInfo;
import com.td.ca.base.util.io.codegenarator.sql.IndexType;
import com.td.ca.base.util.io.codegenarator.table.CutTableInfo;
import com.td.ca.base.util.io.codegenarator.table.DataType;
import com.td.ca.base.util.io.codegenarator.table.ImportColumn;
import com.td.ca.base.util.io.codegenarator.table.ImportTable;
import com.td.ca.base.util.io.codegenarator.table.XlsTable;
import com.td.ca.base.util.io.codegenarator.table.src.TableSrcUtilFactory;
import com.td.ca.base.util.log.LogUtil;
import com.td.ca.base.util.objutil.ArrayUtil;
import com.td.ca.base.util.objutil.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author wangbing
 *
 */
@Slf4j
public abstract class AbstractDBExcutor {

    public abstract String getSql(List<XlsTable> tables) throws IOException;

    protected abstract String getDropTableSql(String tableName);

    protected String getColumnSql(ImportColumn col) {
        return getColumnSql(col, false);
    }

    protected abstract String getColumnSql(ImportColumn col, boolean isChangeCol);

    protected abstract String getSeqSql(ImportTable table);

    protected abstract String getAddIndexSql(ImportTable table, String tableName, IndexInfo indexInfo);

    protected abstract String getDropIndexSql(ImportTable table, String tableName, IndexInfo indexInfo);

    protected abstract String dropColumn(ImportColumn col);

    protected abstract String addColumn(ImportColumn ic, ImportColumn preColumn);

    protected abstract String changeColumn(ImportColumn key, ImportColumn value);

    protected abstract String changePK(ImportColumn pk, ImportColumn oldPk);

    public String getSql(ImportTable table, DBSrc dbSrc) throws IOException {
        String tableName = table.getName();
        if (table.getCutTableInfo() != null && table.getCutTableInfo().getCutType() == CutType.HASH) {
            StringBuilder sb = new StringBuilder();
            String size = table.getCutTableInfo().getValue(CutTableInfo.SIZE);
            for (int i = 0; i < Integer.valueOf(size); i++) {
                sb.append(getCompareSql(table, dbSrc, tableName + i));
                sb.append(Constants.ENTER);
            }
            return sb.toString();
        } else {
            StringBuilder sb = getCompareSql(table, dbSrc, tableName);
            return sb.toString();
        }
    }

    private StringBuilder getCompareSql(ImportTable table, DBSrc dbSrc, String tableName) {
        ImportTable oldTable = null;
        try {
            oldTable = TableSrcUtilFactory.getTable(dbSrc, tableName);
        } catch (Exception e) {
            log.error(LogUtil.getMessage(e));
        }

        StringBuilder sb = new StringBuilder();
        if (oldTable == null) {
            sb.append(getDropTableSql(tableName)).append(Constants.ENTER);
            sb.append("create table ").append(tableName).append(Constants.LEFT_PARENTHESE).append(Constants.ENTER);
            ImportColumn pk = table.getPK();
            for (ImportColumn col : table.getColumns()) {
                sb.append("    ").append(getColumnSql(col));
                sb.append(Constants.COMMA).append(Constants.ENTER);
                if (col.isPK()) {
                    pk = col;
                }
            }

            if (pk != null) {
                sb.append("    ").append("PRIMARY KEY (").append(pk.getName()).append(")");
            } else {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append(Constants.ENTER);
            sb.append(Constants.RIGHT_PARENTHESE).append(Constants.SEMICOLON).append(Constants.ENTER);
            sb.append(getIndexSql(table, tableName));
            sb.append(getSeqSql(table));
            sb.append(Constants.ENTER);
        } else {
            List<ImportColumn> newCols = new ArrayList<ImportColumn>(table.getColumns());
            List<ImportColumn> oldCols = new ArrayList<ImportColumn>(oldTable.getColumns());

            Map<ImportColumn, ImportColumn> modifyCols = new HashMap<ImportColumn, ImportColumn>();
            Iterator<ImportColumn> it = newCols.iterator();
            for (; it.hasNext(); ) {
                ImportColumn newCol = it.next();

                Iterator<ImportColumn> oldColsIt = oldCols.iterator();
                for (; oldColsIt.hasNext(); ) {
                    ImportColumn oldCol = oldColsIt.next();
                    if (oldCol.getName().equals(newCol.getName())) {
                        if (!isEquals(newCol, oldCol)) {
                            modifyCols.put(newCol, oldCol);
                        }
                        it.remove();
                        oldColsIt.remove();
                        break;
                    }

                }
            }

            it = newCols.iterator();
            Iterator<ImportColumn> oldColsIt = oldCols.iterator();
            if (it.hasNext() && oldColsIt.hasNext()) {
                ImportColumn newCol = it.next();
                ImportColumn oldCol = oldColsIt.next();
                int newIndex = table.getIndex(newCol);
                int oldIndex = oldTable.getIndex(oldCol);

                while (true) {
                    if (newIndex == oldIndex) {
                        if (isSameCol(newCol, oldCol)) {
                            modifyCols.put(newCol, oldCol);
                            it.remove();
                            oldColsIt.remove();
                        }
                        if (it.hasNext() && oldColsIt.hasNext()) {
                            newCol = it.next();
                            oldCol = oldColsIt.next();
                            newIndex = table.getIndex(newCol);
                            oldIndex = oldTable.getIndex(oldCol);
                        } else {
                            break;
                        }
                    } else if (newIndex < oldIndex) {
                        if (it.hasNext()) {
                            newCol = it.next();
                            newIndex = table.getIndex(newCol);
                        } else {
                            break;
                        }
                    } else {
                        if (oldColsIt.hasNext()) {
                            oldCol = oldColsIt.next();
                            oldIndex = oldTable.getIndex(oldCol);
                        } else {
                            break;
                        }
                    }
                }
            }

            for (Entry<ImportColumn, ImportColumn> entry : modifyCols.entrySet()) {
                sb.append(changeColumn(entry.getKey(), entry.getValue())).append(Constants.ENTER);
            }

            // 只有当index相同，dataType和length相同，认为是修改
            for (ImportColumn ic : newCols) {
                sb.append(addColumn(ic, table.getPreColumn(ic))).append(Constants.ENTER);
            }

            for (ImportColumn ic : oldCols) {
                sb.append(dropColumn(ic)).append(Constants.ENTER);
            }

            ImportColumn pk = table.getPK();
            ImportColumn oldPk = oldTable.getPK();
            // 如果主键名称有变化
            String changePK = changePK(pk, oldPk);
            if (!StringUtil.isEmpty(changePK)) {
                sb.append(changePK).append(Constants.ENTER);
            }

            try {
                sb.append(getIndexUpdateInfo(dbSrc, table, oldTable));
            } catch (Exception e) {
                throw AppException.getAppException(e);
            }
        }
        return sb;
    }

    private String getIndexUpdateInfo(DBSrc dbSrc, ImportTable newTable, ImportTable oldTable) throws Exception {
        List<IndexInfo> newList = getIndexInfo(newTable);
        List<IndexInfo> oldList = TableSrcUtilFactory.getAllIndexInfo(dbSrc, oldTable.getName());
        Collection<IndexInfo>[] diffCols = ArrayUtil.getDifference(newList, oldList);

        StringBuilder sb = new StringBuilder();
        Collection<IndexInfo> addList = diffCols[1];
        Collection<IndexInfo> deleteList = diffCols[2];
        if (deleteList != null) {
            for (IndexInfo indexInfo : deleteList) {
                sb.append(getDropIndexSql(oldTable, null, indexInfo)).append(Constants.ENTER);
            }
        }
        if (addList != null) {
            for (IndexInfo indexInfo : addList) {
                sb.append(getAddIndexSql(newTable, null, indexInfo)).append(Constants.ENTER);
            }
        }
        return sb.toString();
    }

    private String getIndexSql(ImportTable table, String tableName) {
        List<IndexInfo> list = getIndexInfo(table);
        if (list == null || list.isEmpty()) {
            return Constants.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        for (IndexInfo indexInfo : list) {
            sb.append(getAddIndexSql(table, tableName, indexInfo)).append(Constants.ENTER);
        }
        return sb.toString();
    }

    private static List<IndexInfo> getIndexInfo(ImportTable table) {
        String indexInfo = table.getIndexInfo();
        if (StringUtil.isEmptyTrim(indexInfo)) {
            return null;
        }

        indexInfo = indexInfo.trim();
        List<IndexInfo> list = new ArrayList<>();
        // 索引格式a,b:1;c:0;d
        for (String[] array : StringUtil.string2Arrays(indexInfo, ";", ":")) {
            String[] cols = StringUtil.string2Array(array[0], ",");
            check(table, cols);

            String indexTypeValue = ArrayUtil.getValue(array, 1);
            IndexType indexType = IndexType.NORMAL;
            if (!StringUtil.isEmpty(indexTypeValue)) {
                indexType = IndexType.getIndexType(Integer.valueOf(indexTypeValue));
            }

            String indexName = ArrayUtil.getValue(array, 2);
            if (StringUtil.isEmpty(indexName)) {
                indexName = createIndexName(indexType, table, cols);
            }
            list.add(new IndexInfo(indexName, cols, indexType));
        }
        return list;
    }

    private static void check(ImportTable table, String[] cols) {
        for (String col : cols) {
            if (table.getColumn(col) == null) {
                throw new AppException("cannot find index col name for table : " + table.getName() + ", " + col);
            }
        }
    }

    private static String createIndexName(IndexType indexType, ImportTable table, String[] cols) {
        if (indexType == IndexType.UNIQUE) {
            return "uq_" + StringUtil.array2String(cols, "_");
        } else {
            return "idx_" + StringUtil.array2String(cols, "_");
        }
    }

    private static boolean isSameCol(ImportColumn newCol, ImportColumn oldCol) {
        return newCol.getDBDataType() == oldCol.getDBDataType() && newCol.getLength() >= oldCol.getLength();
    }

    public static boolean isEquals(ImportColumn newCol, ImportColumn oldCol) {
        if (oldCol == newCol) {
            return true;
        }
        if (newCol == null || oldCol == null) {
            return false;
        }

        boolean result = newCol.getTable().getName().equals(oldCol.getTable().getName()) && newCol.getName().equals(oldCol.getName())
                && newCol.getDBDataType() == oldCol.getDBDataType();

        if (!result) {
            return false;
        }

        if (newCol.getDataType() == DataType.STRING) {
            result = result && (newCol.getLength() == oldCol.getLength());
        }
        result = result && StringUtil.isEqual(newCol.getDefaultValue(), oldCol.getDefaultValue())
                && StringUtil.isEqual(newCol.getComment(), oldCol.getComment());
        return result;
    }
}
