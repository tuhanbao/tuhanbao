package com.tuhanbao.base.util.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.tuhanbao.base.util.db.table.data.BooleanValue;
import com.tuhanbao.base.util.db.table.data.ByteArrayValue;
import com.tuhanbao.base.util.db.table.data.ByteValue;
import com.tuhanbao.base.util.db.table.data.DataValue;
import com.tuhanbao.base.util.db.table.data.DoubleValue;
import com.tuhanbao.base.util.db.table.data.FloatValue;
import com.tuhanbao.base.util.db.table.data.IntValue;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.db.table.data.ShortValue;
import com.tuhanbao.base.util.db.table.data.StringValue;
import com.tuhanbao.base.util.db.table.data.TimeValue;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.objutil.ByteBuffer;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.objutil.TimeUtil;

public final class DataValueFactory
{

    /**
     * @param col
     * @param rs
     * @return
     * @throws SQLException
     */
    public static DataValue read(Column col, ResultSet rs) throws SQLException
    {
        DataType dataType = col.getDataType();
        String colName = col.getAsName();
        //STRING, BYTE, SHORT, INT, LONG, BYTE_ARRAY, BOOLEAN, TIME, FLOAT, DOUBLE
        if (dataType == DataType.STRING)
        {
        	return StringValue.valueOf(rs.getString(colName));
        }
        else if (dataType == DataType.BYTEARRAY)
        {
            byte[] bytes = rs.getBytes(colName);
            if (bytes == null || bytes.length == 0) return null;
            return ByteArrayValue.valueOf(new ByteBuffer(bytes));
        }       
        else if (dataType == DataType.DATE)
        {
            return TimeValue.valueOf(rs.getTimestamp(colName));
        }
        
        //以下的为基本类型，由于基本类型即使为空，地城也会返回默认值，所以这里都需要先判断取值是否为空
        if (StringUtil.isEmpty(rs.getString(colName))) return null;
        
        if (dataType == DataType.BOOLEAN)
        {
            return BooleanValue.valueOf(rs.getByte(colName));
        }
        else if (dataType == DataType.BYTE)
        {
            //只要是存在的字段，rs读取出来都会有值，数值型的默认为0
            return ByteValue.valueOf(rs.getByte(colName));
        }
        else if (dataType == DataType.SHORT)
        {
            return ShortValue.valueOf(rs.getShort(colName));
        }
        else if (dataType == DataType.INT)
        {
            return IntValue.valueOf(rs.getInt(colName));
        }
        else if (dataType == DataType.LONG)
        {
            return LongValue.valueOf(rs.getLong(colName));
        }
        else if (dataType == DataType.FLOAT)
        {
            return FloatValue.valueOf(rs.getFloat(colName));
        }
        else if (dataType == DataType.DOUBLE)
        {
            return DoubleValue.valueOf(rs.getDouble(colName));
        }
        return null;
    }
    
    public static DataValue toDataValue(DataType dataType, Object value)
    {
        if (value instanceof DataValue) return (DataValue) value;
        
        if (value == null) return null;
        
        if (dataType == DataType.BOOLEAN)
        {
            if (value instanceof Boolean)
                return BooleanValue.valueOf((Boolean)value);
            if (value instanceof Number)
                return BooleanValue.valueOf(((Number)value).byteValue());
        }
        else if (dataType == DataType.STRING)
        {
            return StringValue.valueOf(value.toString());
        }
        else if (dataType == DataType.BYTE)
        {
            return ByteValue.valueOf(((Number)value).byteValue());
        }
        else if (dataType == DataType.SHORT)
        {
            return ShortValue.valueOf(((Number)value).shortValue());
        }
        else if (dataType == DataType.INT)
        {
            return IntValue.valueOf(((Number)value).intValue());
        }
        else if (dataType == DataType.LONG)
        {
            return LongValue.valueOf(((Number)value).longValue());
        }
        else if (dataType == DataType.BYTEARRAY)
        {
            if (value instanceof String)
                return ByteArrayValue.valueOf((String)value);
            if (value instanceof ByteBuffer)
                return ByteArrayValue.valueOf((ByteBuffer)value);
        }
        else if (dataType == DataType.DATE)
        {
            if (value instanceof Date)  return TimeValue.valueOf((Date)value);
            if (value instanceof String) return TimeValue.valueOf(TimeUtil.getTime((String)value));
            if (value instanceof Number)  return TimeValue.valueOf(((Number)value).longValue());
        }
        else if (dataType == DataType.FLOAT)
        {
            return FloatValue.valueOf(((Number)value).floatValue());
        }
        else if (dataType == DataType.DOUBLE)
        {
            return DoubleValue.valueOf(((Number)value).doubleValue());
        }
        
        throw new MyException(BaseErrorCode.ILLEGAL_ARGUMENT, "DataValue arg is wrong");
    }

}
