package com.tuhanbao.base.util.db.table.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tuhanbao.base.util.objutil.ByteBuffer;

public class ByteArrayValue extends DataValue
{
    private final ByteBuffer value;
    
    private ByteArrayValue(ByteBuffer value)
    {
        this.value = value;
    }
    
    public static ByteArrayValue valueOf(ByteBuffer value)
    {
        return new ByteArrayValue(value);
    }
    
    public static ByteArrayValue valueOf(String value)
    {
        return new ByteArrayValue(new ByteBuffer(value));
    }
    
    /**
     * byteArray不存在比较
     */
    @Override
    public int compareTo(DataValue o)
    {
        return 0;
    }

    @Override
    public void write(ResultSet rs, String colName) throws SQLException
    {
        rs.updateBytes(colName, value.getData());
    }

    public ByteBuffer getValue()
    {
        return value;
    }
    
    public byte[] getValue4DB() {
    	return value.getData();
    }
}
