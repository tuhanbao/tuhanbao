package com.tuhanbao.base.util.io.codeGenarator.tableUtil.src;

import java.util.List;

import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.db.table.dbtype.DBDataType;
import com.tuhanbao.base.util.db.table.dbtype.MysqlDataType;
import com.tuhanbao.base.util.db.table.dbtype.OracleDataType;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DBType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.log.LogManager;

public class TableSrcUtilFactory {
	
	public static ITableSrcUtil getTableSrcUtil(DBType type) {
		if (type == DBType.MYSQL) {
			return MysqlTableSrcUtil.getInstance();
		}
		else if (type == DBType.ORACLE) {
			return OracleTableSrcUtil.getInstance();
		}
		else {
			return null;
		}
	}
	
	public static ImportTable getTable(DBSrc src, String tableName) throws Exception {
		ITableSrcUtil tableSrcUtil = getTableSrcUtil(src.getDBType());
		if (tableSrcUtil == null) throw new MyException("cannot support this dbtype!");
		try {
			return tableSrcUtil.getTable(src, tableName);
		} catch (Exception e) {
			LogManager.error(e);
		}
		return null;
	}

	public static List<ImportTable> getTables(DBSrc src) {
		ITableSrcUtil tableSrcUtil = getTableSrcUtil(src.getDBType());
		if (tableSrcUtil == null) throw new MyException("cannot support this dbtype!");
		try {
			return tableSrcUtil.getTables(src);
		} catch (Exception e) {
			LogManager.error(e);
		}
		return null;
	}
	
	public static DBDataType getDBDataType(String typeName, DBType dbType) {
		if (dbType == DBType.ORACLE) return OracleDataType.getDBDataType(typeName);
		if (dbType == DBType.MYSQL) return MysqlDataType.getDBDataType(typeName);
		return null;
	}	
	
	public static DBDataType getDBDataType(DataType dataType, DBType dbType) {
		if (dbType == DBType.ORACLE) return OracleDataType.getDBDataType(dataType);
		if (dbType == DBType.MYSQL) return MysqlDataType.getDBDataType(dataType);
		return null;
	}
}
