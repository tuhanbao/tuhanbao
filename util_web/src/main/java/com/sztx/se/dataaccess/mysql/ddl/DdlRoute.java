package com.sztx.se.dataaccess.mysql.ddl;

public class DdlRoute {

    private String db;

    private String table;

    private String column;

    private String columnValue;

    public DdlRoute(String db, String table, String column, String columnValue) {
        this.db = db;
        this.table = table;
        this.column = column;
        this.columnValue = columnValue;
    }
    
    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

}
