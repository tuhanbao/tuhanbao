package com.tuhanbao.autotool.mvc.util;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.Constants;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DBType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.src.TableSrcUtilFactory;
import com.tuhanbao.base.util.io.excel.Excel2007Util;
import com.tuhanbao.base.util.objutil.ArrayUtil;
import com.tuhanbao.base.util.objutil.StringUtil;

public class ExcelTableSrcManager {
	private static Map<String, ImportTable> TABLES = new HashMap<String, ImportTable>();

	private static DBType DB_TYPE = DBType.MYSQL;
	
	/**
	 * 第一页是配置文件
	 * 其他为table
	 * @param url
	 */
	public static void getTables(String url) {
		String[][][] arrays = Excel2007Util.read(url);
		
		initTableConfig(arrays[0]);
		int length = arrays.length;
		for (int i = 1; i < length; i++) {
			String tableName = Excel2007Util.getSheetName(url, i);
			ImportTable table = getTable(tableName, arrays[i], DB_TYPE);
			TABLES.put(tableName, table);
		}
	}
	
	private static void initTableConfig(String[][] strings) {
		//TODO
	}
    
    public static ImportTable getTable(String tableName, String[][] arrays, DBType dbType) {
		ImportTable table = new ImportTable(tableName);
		int length = arrays.length;
		//首行是列标
		for (int i = 1; i < length; i++) {
			String[] array = arrays[i];
			ImportColumn column = getColumn(table, array, dbType);
			if (column.isPK()) table.setPK(column);
			else table.addColumn(column);
		}
    	
    	return table;
    }
    
	private static ImportColumn getColumn(ImportTable table, String[] array, DBType dbType) {
		String colName = array[0];
		String dataType = array[1];
		String dataLengthStr = ArrayUtil.indexOf(array, 2);
		int dataLength = 0;
		
		if (!StringUtil.isEmpty(dataLengthStr)) dataLength = Integer.valueOf(dataLengthStr);
		ImportColumn col = new ImportColumn(table, colName, TableSrcUtilFactory.getDBDataType(dataType, dbType), dataLength);

		String temp = ArrayUtil.indexOf(array, 5);
		if (!StringUtil.isEmpty(temp)) {
			if (temp.startsWith("PK")) {
				col.setPK(true);
			} 
			else if (temp.startsWith("FK")) {
	//			FK(T_CABLES_ROLE.ROLE_ID)
				int start = temp.indexOf(Constants.LEFT_PARENTHESE);
				int end = temp.indexOf(Constants.RIGHT_PARENTHESE);
				String FK = temp.substring(start + 1, end);
				col.setFK(FK);
			}
		}
		return col;
	}
}
