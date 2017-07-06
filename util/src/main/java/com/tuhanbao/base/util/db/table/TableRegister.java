package com.tuhanbao.base.util.db.table;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.util.clazz.ClazzUtil;

public final class TableRegister
{
    public static final Map<String, Table> TABLES = new HashMap<String, Table>();
    
    private TableRegister()
    {
        
    }
    
    public static final void register(Table table)
    {
        TABLES.put(getClassName(table.getName()), table);
    }

	public static Table getTableByClassName(String name) {
		return TABLES.get(name);
	}    	
	
	private static String getClassName(String tableName) {
		if (tableName.startsWith("T_") || tableName.startsWith("I_")) {
			tableName = tableName.substring(2);
		}
		return ClazzUtil.getClassName(tableName);
	}
}
