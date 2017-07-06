package com.tuhanbao.base.dataservice;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.db.table.Table;
import com.tuhanbao.base.util.db.table.data.DataValue;

/**
 * 元数据对象
 * 元数据对象虽然支持事务处理，但只能回滚修改操作；删除，新增等操作无法处理。切勿滥用。
 * 另，处于事务状态中的元数据，通过get获取的属性依旧获取的是旧属性值。
 * @author tuhanbao
 *
 */
public final class MetaObject
{
    private Map<Column, DataValue> properties;

    public final Table table;

    public MetaObject(Table table)
    {
        this.table = table;
        properties = new HashMap<Column, DataValue>(table.getColumns().size());
    }

    public MetaObject(Table table, Map<Column, DataValue> properties)
    {
        this.table = table;
        this.properties = properties;
    }

    /**
     * 修改属性
     * @param key
     * @param newValue
     */
    protected void put(Column key, DataValue newValue)
    {
        DataValue oldValue = get(key);
        if (isSame(oldValue, newValue))
        {
            return;
        }
        //修改数据
        properties.put(key, newValue);
    }
    
    private static boolean isSame(DataValue oldValue, DataValue newValue)
    {
        if (oldValue == null && newValue == null)
        {
            return true;
        }

        if (oldValue != null && oldValue.equals(newValue))
        {
            return true;
        }
        return false;
    }

    public Map<Column, DataValue> getProperties()
    {
        return properties;
    }
    
    public void syncMo(MetaObject otherMo) {
        properties.putAll(otherMo.properties);
    }

    public DataValue getKeyValue()
    {
        return get(table.getPK());
    }

    /**
     * 内部代码访问
     * 
     * @param column
     * @return
     */
    public DataValue get(Column column)
    {
        return properties.get(column);
    }

    @Override
    public int hashCode()
    {
        return getKeyValue().hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof MetaObject)
        {
            MetaObject temp = (MetaObject) o;
            return this.table == temp.table && temp.getKeyValue().equals(this.getKeyValue());
        }

        return false;
    }

    @Override
    public String toString()
    {
        return this.table.getName() + "." + this.getKeyValue();
    }
    
    public String getDetail()
    {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Column, DataValue> entry : properties.entrySet())
        {
            sb.append(entry.getKey()).append(Constants.COLON).append(entry.getValue()).append(Constants.COMMA);
        }
        return sb.toString();
    }
}
