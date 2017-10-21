package com.tuhanbao.base.util.db.table;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.util.clazz.ClazzUtil;

/**
 * 提供给外部使用
 * 
 * 比如传入一个表的属性userId，会返回对应表的column
 * @author Administrator
 *
 */
public class ColumnManager {
	private static final Map<Table, Map<String, Column>> CACHE = new HashMap<>();
	
	public static final Map<String, Column> getTableColumns(Table table) {
		Map<String, Column> cols = CACHE.get(table);
		if (cols == null) {
			cols = new HashMap<String, Column>();
			for (Column col : table.getColumns()) {
				cols.put(getPropertyName(col), col);
			}
			CACHE.put(table, cols);
		}
		
		return cols;
	}

	private static String getPropertyName(Column col) {
		//大写下划线式改成驼峰式
		String name = ClazzUtil.getClassName(col.getName());
		return ClazzUtil.firstCharLowerCase(name);
	}
}
