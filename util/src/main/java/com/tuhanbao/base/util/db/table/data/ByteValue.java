package com.tuhanbao.base.util.db.table.data;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ByteValue extends DataValue
{
    private final byte value;
    
    private static final ByteValue cache[];
    
    private static final byte HIGH = 127;
    
    private static final byte LOW = -127;
    
    static 
    {
        int length = (HIGH - LOW) + 1;
        byte low = LOW;
        cache = new ByteValue[length];
        for(int k = 0; k < length; k++)
            cache[k] = new ByteValue(low++);
    }

    private ByteValue(byte value)
    {
        this.value = value;
    }


    public byte getValue()
    {
        return this.value;
    }
    
    public static ByteValue valueOf(Byte value)
    {
        if (value == null) return null;
        
        if (value >= LOW && value <= HIGH)
        {
            return cache[value - LOW];
        }
        else
        {
            return null;
        }
    }  
    
    @Override
    public int hashCode()
    {
        return this.value;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof ByteValue)
        {
            return this.value == ((ByteValue)o).value;
        }
        
        return false;
    }
    

    public int compareTo(DataValue o)
    {
        ByteValue anotherObj = (ByteValue) o;
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
        rs.updateByte(colName, value);
    }
    
    public Byte getValue4DB() {
    	return value;
    }
}
