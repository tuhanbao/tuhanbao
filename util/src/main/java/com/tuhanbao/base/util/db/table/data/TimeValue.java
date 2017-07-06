package com.tuhanbao.base.util.db.table.data;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * 时间数据，使用long型
 * @author tuhanbao
 *
 */
public class TimeValue extends DataValue
{
    private final Date value;
    
    private TimeValue(Date value)
    {
        this.value = value;
    }
    
    public static TimeValue valueOf(Date value)
    {
    	if (value == null) return null;
        return new TimeValue(value);
    }

    public static TimeValue valueOf(long time)
    {
        return new TimeValue(new Date(time));
    }

    @Override
    public int compareTo(DataValue o)
    {
        TimeValue anotherObj = (TimeValue) o;
        return this.value.compareTo(anotherObj.value);
    }
    
    @Override
    public String toString()
    {
        return String.valueOf(value);
    }
    
    @Override
    public int hashCode()
    {
        return value.hashCode();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof TimeValue)
        {
            return this.value.equals(((TimeValue)o).value);
        }
        
        return false;
    }

    @Override
    public void write(ResultSet rs, String colName) throws SQLException
    {
        rs.updateDate(colName, new java.sql.Date(value.getTime()));
    }
    
    public Date getValue4DB() {
    	return value;
    }
    
    public Date getValue() {
    	return this.value;
    }
    
}
