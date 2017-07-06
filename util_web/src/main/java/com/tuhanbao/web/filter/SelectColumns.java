package com.tuhanbao.web.filter;

import java.util.HashSet;
import java.util.Set;

import com.tuhanbao.base.util.db.table.Column;

public class SelectColumns {
	private Set<Column> columns = new HashSet<Column>();
	
	//用于区分同名的
	private Set<String> colNames = new HashSet<String>();;
	
	public Set<Column> getColumns() {
		return columns;
	}
	
	public void addColumn(Column column) {
		addColumn(column, null);
	}
	
	public void addColumn(Column column, SelectTable selectTable) {
		String asName = column.getAsName();
		if (colNames.contains(asName)) {
			if (column instanceof AsColumn) {
				AsColumn asColumn = (AsColumn) column;
				int i = 1;
				String newName = asName + i++;
				while (colNames.contains(newName)) {
					newName = asName + i++;
				}
				asColumn.setAsName(newName);
				columns.add(asColumn);
				colNames.add(newName);
			}
			else {
				int i = 1;
				String newName = asName + i++;
				while (colNames.contains(newName)) {
					newName = asName + i++;
				}
				columns.add(new AsColumn(column, newName, selectTable));
				colNames.add(newName);
			}
		}
		else {
			//有重复的先删除
			columns.remove(column);
			columns.add(column);
			colNames.add(asName);
		}
	}
}
