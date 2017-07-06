package com.tuhanbao.base.util.db.table.data;

import java.sql.ResultSet;
import java.sql.SQLException;


public class StringValue extends DataValue
{
    private final String value;
    
    private StringValue(String value)
    {
        this.value = value;
    }
    
    public static StringValue valueOf(String value)
    {
        if (value == null) return null;
        return new StringValue(value);
    }
    
    @Override
    public int compareTo(DataValue o)
    {
        StringValue anotherObj = (StringValue) o;
        return value.compareTo(anotherObj.value);
    }
    
    public String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        // 不知道为什么会加单引号
//        return Constants.SINGLE_QUOTA + value + Constants.SINGLE_QUOTA;
        return value;
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof StringValue)
        {
            return this.value.equals(((StringValue)o).value);
        }
        return false;
    }

    @Override
    public void write(ResultSet rs, String colName) throws SQLException
    {
        rs.updateString(colName, value);
    }
    
    public String getValue4DB() {
    	return value;
    }
}
