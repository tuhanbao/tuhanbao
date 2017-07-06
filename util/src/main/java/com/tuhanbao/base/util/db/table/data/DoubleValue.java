package com.tuhanbao.base.util.db.table.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * double类型数据
 * @author tuhanbao
 *
 */
public class DoubleValue extends DataValue
{
    private final double value;
    
    private DoubleValue(double value)
    {
        this.value = value;
    }
    
    public static DoubleValue valueOf(Double value)
    {
        if (value == null) return null;
        return new DoubleValue(value);
    }
    
    public double getValue()
    {
        return this.value;
    }
    
    @Override
    public int hashCode()
    {
        return Double.valueOf(this.value).hashCode();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof DoubleValue)
        {
            return this.value == ((DoubleValue)o).value;
        }
        
        return false;
    }
    
    
    @Override
    public int compareTo(DataValue o)
    {
        DoubleValue anotherObj = (DoubleValue) o;
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
        rs.updateDouble(colName, value);
    }
    
    public Double getValue4DB() {
    	return value;
    }
}
