package com.tuhanbao.base.util.db.table.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DataValue implements Comparable<DataValue>
{
    public abstract void write(ResultSet rs, String colName) throws SQLException;
    
    public abstract Object getValue4DB();
    
    public String toString() {
    	return getValue4DB().toString();
    }
}
