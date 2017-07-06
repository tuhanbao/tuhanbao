package com.tuhanbao.web.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.base.dataservice.filter.JoinType;
import com.tuhanbao.base.dataservice.filter.Selector;
import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.db.table.Table;
import com.tuhanbao.base.util.exception.MyException;

/**
 * 大部分情况下，一个表的查询关联关系是固定的， 此类用于固化这些信息，避免创建Selector时重复创建
 * 
 * @author Administrator
 *
 */
public class MyBatisSelector extends Selector<Table, Column> {

    private SelectTable table;

    private SelectColumns selectColumns;

    // 记录一个selector里面的所有table，用于做一些验证和全局缓存用
    private Map<String, SelectTable> allTables = new HashMap<String, SelectTable>();

    public MyBatisSelector(Table table) {
        super(table);
        this.table = new SelectTable(table, this);
        this.addTable(this.table);
    }

    public MyBatisSelector(Table table, String asName) {
        super(table);
        this.table = new SelectTable(table, asName, this);
        this.addTable(this.table);
    }

    /**
     * 返回的子table构造的selectTable本身
     * 
     * @param table
     * @param column
     * @return
     */
    public SelectTable joinTable(Table table) {
        return this.table.joinTable(table);
    }

    /**
     * 返回的子table构造的selectTable本身
     * 
     * @param table
     * @param column
     * @return
     */
    public SelectTable joinTable(Table table, String asName) {
        return this.table.joinTable(table, asName);
    }

    public SelectTable joinTable(Table table, Column column) {
        if (column == null) throw new MyException("no this column for fk!");
        return this.table.joinTable(table, column);
    }

    public SelectTable joinTable(Table table, JoinType joinType) {
        return this.table.joinTable(table, joinType);
    }

    public SelectTable joinTable(Table table, Column column, JoinType joinType) {
        if (column == null) throw new MyException("no this column for fk!");
        return this.table.joinTable(table, column, joinType);
    }

    /**
     * 1.普通情况下，column为两表之间外键关系非主键的那一个 如A.B_ID 与 B.ID 关联，则column必须为A.B_ID 如A.ID 与
     * B.A_ID 关联，则column必须为B.A_ID
     * 
     * 2.树形关联 如A.PARENT_ID 与 A.ID 关联，column必须为A.PARENT_ID
     * 如何区分t1和t2的关系，上面这种情况，有两种理解 1） select * from A as A left join A as A1 on
     * A.PARENT_ID = A1.ID 2） select * from A as A left join A as A1 on
     * A1.PARENT_ID = A.ID 默认情况下为第二种用法， 如果是第一种用法，示例代码如下 Selector selector = new
     * Selector(TableConstants.AREA.TABLE);
     * selector.joinTable(TableConstants.AREA.TABLE, "AREA1",
     * selector.getTable().getColumn(TableConstants.AREA.PARENT_ID));
     * 
     * @param table
     * @param asName
     * @param column
     * @return
     */
    public SelectTable joinTable(Table table, String asName, Column column) {
        return this.table.joinTable(table, asName, column);
    }

    public SelectTable joinTable(Table table, String asName, Column column, JoinType joinType) {
        return this.table.joinTable(table, asName, column, joinType);
    }

    @Deprecated
    public SelectTable joinTable(Table table, Filter filter) {
        return this.table.joinTable(table, filter);
    }

    @Deprecated
    public SelectTable joinTable(Table table, String asName, Filter filter) {
        return this.table.joinTable(table, asName, filter);
    }

    @Deprecated
    public SelectTable joinTable(Table table, String asName, Filter filter, JoinType joinType) {
        return this.table.joinTable(table, asName, filter, joinType);
    }

    public void addSelectColumn(Column column) {
        if (this.selectColumns == null) this.selectColumns = new SelectColumns();
        this.selectColumns.addColumn(column);
    }

    public void addSelectColumn(Table table) {
        if (this.selectColumns == null) this.selectColumns = new SelectColumns();
        for (Column column : table.getColumns()) {
            this.selectColumns.addColumn(column);
        }
    }

    public Collection<Column> getSelectColumns() {
        if (this.selectColumns == null) {
            this.selectColumns = new SelectColumns();
            this.table.addColumns2List(selectColumns);
        }
        return this.selectColumns.getColumns();
    }

    /**
     * getter and setter
     */
    public SelectTable getTable() {
        return this.table;
    }

    public void setSelectColumns(SelectColumns selectColumns) {
        this.selectColumns = selectColumns;
    }

    public void addTable(SelectTable selectTable) {
        if (selectTable == null) return;

        String key = selectTable.getName();
        if (this.allTables.containsKey(key)) {
            throw new MyException("two table has the same name!");
        }
        this.allTables.put(key, selectTable);
    }

    public Map<String, SelectTable> getAllTables() {
        return this.allTables;
    }
}
