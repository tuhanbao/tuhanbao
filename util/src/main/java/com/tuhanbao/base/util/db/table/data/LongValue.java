package com.tuhanbao.base.util.db.table.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LongValue extends DataValue
{
    private final long value;

    private static final LongValue cache[];

    private static final int HIGH = 256;

    private static final int LOW = 0;
    
    static
    {
        int length = (HIGH - LOW) + 1;
        int low = LOW;
        cache = new LongValue[length];
        for (int k = 0; k < length; k++)
            cache[k] = new LongValue(low++);
    }

    private LongValue(long value)
    {
        this.value = value;
    }

    public static LongValue valueOf(Long value)
    {
        if (value == null) return null;
        
        if (value >= LOW && value <= HIGH)
        {
            return cache[(int) (value - LOW)];
        }
        else
        {
            return new LongValue(value);
        }
    }

    public long getValue()
    {
        return this.value;
    }

    @Override
    public int hashCode()
    {
        return Long.valueOf(value).hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof LongValue)
        {
            return this.value == ((LongValue) o).value;
        }

        return false;
    }

    public int compareTo(DataValue o)
    {
        LongValue anotherObj = (LongValue) o;
        return this.value > anotherObj.value ? 1 : (this.value == anotherObj.value ? 0 : -1);
    }

    @Override
    public String toString()
    {
        return String.valueOf(value);
    }

    @Override
    public void write(ResultSet rs, String colName) throws SQLException
    {
        rs.updateLong(colName, value);
    }
    
    public Long getValue4DB() {
    	return value;
    }
}
