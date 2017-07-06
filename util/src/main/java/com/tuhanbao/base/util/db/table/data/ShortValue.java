package com.tuhanbao.base.util.db.table.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortValue extends DataValue
{
    private final short value;
    
    private static final ShortValue cache[];
    
    private static final short HIGH = 256;
    
    private static final short LOW = 0;
    
    static {
        int length = (HIGH - LOW) + 1;
        short low = LOW;
        cache = new ShortValue[length];
        for(int k = 0; k < length; k++)
            cache[k] = new ShortValue(low++);
    }

    private ShortValue(short value)
    {
        this.value = value;
    }
    
    public static ShortValue valueOf(Short value)
    {
        if (value == null) return null;
        
        if (value >= LOW && value <= HIGH)
        {
            return cache[value - LOW];
        }
        else
        {
            return new ShortValue(value);
        }
    }

    public short getValue()
    {
        return this.value;
    }
    
    @Override
    public int hashCode()
    {
        return this.value;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof ShortValue)
        {
            return this.value == ((ShortValue)o).value;
        }
        
        return false;
    }
    
    public int compareTo(DataValue o)
    {
        ShortValue anotherObj = (ShortValue) o;
        return this.value - anotherObj.value;
    }
    
    @Override
    public String toString()
    {
        return String.valueOf(value);
    }

    @Override
    public void write(ResultSet rs, String colName) throws SQLException
    {
        rs.updateShort(colName, value);
    }
    
    public Short getValue4DB() {
    	return value;
    }
}
