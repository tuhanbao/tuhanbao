package com.tuhanbao.base.util.db.table.data;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigDecimalValue extends DataValue
{
    private final BigDecimal value;

    private BigDecimalValue(BigDecimal value)
    {
        this.value = value;
    }

    public static BigDecimalValue valueOf(BigDecimal value)
    {
        return new BigDecimalValue(value);
    }

    public BigDecimal getValue()
    {
        return this.value;
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof BigDecimalValue)
        {
            return this.value.equals(((BigDecimalValue) o).value);
        }

        return false;
    }

    public int compareTo(DataValue o)
    {
        BigDecimalValue anotherObj = (BigDecimalValue) o;
        return this.value.compareTo(anotherObj.value);
    }

    @Override
    public String toString()
    {
        return value.toString();
    }

    @Override
    public void write(ResultSet rs, String colName) throws SQLException
    {
        rs.updateBigDecimal(colName, value);
    }
    
    public BigDecimal getValue4DB() {
    	return value;
    }
}
