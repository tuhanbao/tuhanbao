package com.tuhanbao.base.util.db.table.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatValue extends DataValue
{
    private final float value;
    
    private FloatValue(float value)
    {
        this.value = value;
    }
    
    public static FloatValue valueOf(Float value)
    {
        if (value == null) return null;
        return new FloatValue(value);
    }

    public float getValue()
    {
        return this.value;
    }    
    
    @Override
    public int hashCode()
    {
        return Float.valueOf(this.value).hashCode();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof FloatValue)
        {
            return this.value == ((FloatValue)o).value;
        }
        
        return false;
    }
    
    
    @Override
    public int compareTo(DataValue o)
    {
        FloatValue anotherObj = (FloatValue) o;
        return (this.value < anotherObj.value) ? -1 : ((this.value == anotherObj.value) ? 0 : 1);
    }
    
    @Override
    public String toString()
    {
        return String.valueOf(value);
    }

    @Override
    public void write(ResultSet rs, String colName) throws SQLException
    {
        rs.updateFloat(colName, value);
    }
    
    public Float getValue4DB() {
    	return value;
    }
}
