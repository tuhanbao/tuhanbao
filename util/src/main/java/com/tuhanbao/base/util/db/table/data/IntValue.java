package com.tuhanbao.base.util.db.table.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * int类型数据
 * 
 * @author tuhanbao
 * 
 */
public class IntValue extends DataValue
{
    private final int value;

    private static final IntValue cache[];

    private static final int HIGH = 256;

    private static final int LOW = 0;

    static
    {
        int length = (HIGH - LOW) + 1;
        int low = LOW;
        cache = new IntValue[length];
        for (int k = 0; k < length; k++)
            cache[k] = new IntValue(low++);
    }

    private IntValue(int value)
    {
        this.value = value;
    }

    public static IntValue valueOf(Integer value)
    {
        if (value == null) return null;
        
        if (value >= LOW && value <= HIGH)
        {
            return cache[value - LOW];
        }
        else
        {
            return new IntValue(value);
        }
    }

    public int getValue()
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
        if (o instanceof IntValue)
        {
            return this.value == ((IntValue) o).value;
        }

        return false;
    }

    public int compareTo(DataValue o)
    {
        IntValue anotherObj = (IntValue) o;
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
        rs.updateInt(colName, value);
    }
    
    public Integer getValue4DB() {
    	return value;
    }
}
