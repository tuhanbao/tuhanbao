package com.tuhanbao.base.util.db.table.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tuhanbao.base.util.objutil.StringUtil;


public class BooleanValue extends DataValue
{
    private final boolean value;
    
    public static final byte TRUE_VALUE = 1;
    
    public static final byte FALSE_VALUE = 0;
    
    public static final BooleanValue TRUE = new BooleanValue(true);
    
    public static final BooleanValue FALSE = new BooleanValue(false);

    private BooleanValue(boolean value)
    {
        this.value = value;
    }
    
    public boolean getValue()
    {
        return this.value;
    }
    
    public byte getByteValue()
    {
        if (value)
        {
            return TRUE_VALUE;
        }
        else
        {
            return FALSE_VALUE;
        }
    }
    
    public static int getIntValue(boolean value)
    {
        if (value)
        {
            return TRUE_VALUE;
        }
        else
        {
            return FALSE_VALUE;
        }
    }
    
    public static BooleanValue valueOf(Boolean value)
    {
        if (value == null) return null;
        
        if (value)
        {
            return TRUE;
        }
        else
        {
            return FALSE;
        }
    }
    
    public static BooleanValue valueOf(int value)
    {
        if (value == TRUE_VALUE)
        {
            return TRUE;
        }
        else
        {
            return FALSE;
        }
    }
    
    public static BooleanValue valueOf(String value)
    {
        if (StringUtil.isEmpty(value))
        {
            return FALSE;
        }
        else
        {
            if ("true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)) return TRUE;
            else return FALSE;
        }
    }

    @Override
    public int compareTo(DataValue o)
    {
        BooleanValue anotherObj = (BooleanValue) o;
        return this.getByteValue() - anotherObj.getByteValue();
    }
    
    @Override
    public String toString()
    {
        return String.valueOf(value);
    }
    
    public void write(ResultSet rs, String colName) throws SQLException
    {
        if (value)
        {
            rs.updateByte(colName, TRUE_VALUE);
        }
        else
        {
            rs.updateByte(colName, FALSE_VALUE);
        }
    }
    
    public Byte getValue4DB() {
    	 if (value)
         {
             return TRUE_VALUE;
         }
         else
         {
             return FALSE_VALUE;
         }
    }
}
