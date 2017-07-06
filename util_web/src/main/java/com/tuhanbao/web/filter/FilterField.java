package com.tuhanbao.web.filter;

import com.tuhanbao.base.util.db.table.Column;

/**
 * 不需要特定转换的column
 * 用于自定义过滤条件
 * 
 * @author Administrator
 *
 */
public class FilterField implements IFilterField {
    private Column column;
    
    private String sqlName;
    
    public FilterField(Column col, String sqlName) {
        this.column = col;
        this.sqlName = sqlName;
    }
    
    public String getSqlName() {
        return this.sqlName;
    }
    
    public Column getColumn() {
        return this.column;
    }

    public String toString() {
        return this.sqlName;
    }
}
