package com.tuhanbao.base.util.db.table;

import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.Relation;


public class ColumnFactory
{
    public static Column createColumn(Table table, String name, DataType dataType)
    {
        return createColumn(table, name, dataType, false);
    }
    
    public static Column createColumn(Table table, String name, DataType dataType, boolean isPK)
    {
        return createColumn(table, name, dataType, isPK, false);
    }
    
    public static Column createColumn(Table table, String name, DataType dataType, boolean isPK, boolean canFilter)
    {
        return createColumn(table, name, dataType, isPK, canFilter, null);
    }

    public static Column createColumn(Table table, String name, DataType dataType, boolean isPK, boolean canFilter, String enumName)
    {
        Column col = getColumn(table, name, dataType);
        if (table != null)
        {
            if (isPK) {
                table.setPK(col);
            }
            table.addColumn(col);
        }
        col.setCanFilter(canFilter);
        col.setEnumStr(enumName);
        return col;
    }
    public static Column createColumn(Table table, String name, DataType dataType, Table fkTable, Relation relation) {
        return createColumn(table, name, dataType, fkTable, relation, false);
    }

    public static Column createColumn(Table table, String name, DataType dataType, Table fkTable, Relation relation, boolean canFilter) {
        return createColumn(table, name, dataType, fkTable, relation, canFilter, null);
    }
    
    public static Column createColumn(Table table, String name, DataType dataType, Table fkTable, Relation relation, boolean canFilter, String enumName)
    {
    	Column col = createColumn(table, name, dataType, false, canFilter);
    	col.setFk(fkTable);
    	col.setRelation(relation);
    	col.setEnumStr(enumName);
    	
    	//一个表只需要执行一次
    	if (fkTable != table) {
    	    ServiceBeanKeyManager.init(fkTable, col);
    	}
    	ServiceBeanKeyManager.init(table, col);
    	return col;
    }
    
    
    
    
    private static Column getColumn(Table table, String name, DataType dataType)
    {
        return new Column(table, name, dataType, 0);
    }
}
