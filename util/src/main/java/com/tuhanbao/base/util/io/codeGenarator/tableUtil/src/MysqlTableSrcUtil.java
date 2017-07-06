package com.tuhanbao.base.util.io.codeGenarator.tableUtil.src;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.db.conn.MyConnection;
import com.tuhanbao.base.util.db.table.dbtype.DBDataType;
import com.tuhanbao.base.util.db.table.dbtype.MysqlDataType;
import com.tuhanbao.base.util.io.codeGenarator.sqlUtil.DBUtil;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DBType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.objutil.StringUtil;

public class MysqlTableSrcUtil implements ITableSrcUtil{
	
	private static final MysqlTableSrcUtil instance = new MysqlTableSrcUtil();
	
	private MysqlTableSrcUtil() {
		
	}
	
	public static MysqlTableSrcUtil getInstance() {
		return instance;
	}
	
	/**
     * 导出一个用户下面的表
     * 
     * 仅用于mysql数据库
     * @param src
     * @throws Exception 
     */
    public List<ImportTable> getTables(DBSrc src) throws Exception {
    	String getAllTableSql = "select table_name from information_schema.tables where table_schema = '" + src.getDb_instance() + "'";
    	MyConnection connection = src.getConnection();
    	
		ResultSet rs = connection.executeQuery(getAllTableSql);
		List<String> tableNames = new ArrayList<String>();
		List<ImportTable> tables = new ArrayList<ImportTable>();
		while (rs.next()) {
			tableNames.add(rs.getString(1));
		}
		DBUtil.close(rs);
		connection.release();
		
		for (String tableName : tableNames) {
			tables.add(getTable(src, tableName));
		}
    	
    	return tables;
    }
    
    public ImportTable getTable(DBSrc src, String tableName) throws Exception {
		ImportTable table = new ImportTable(tableName);
		MyConnection connection = src.getConnection();
		
		String sql = "SELECT distinct c.COLUMN_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS t, INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS c WHERE t.TABLE_NAME = c.TABLE_NAME AND t.TABLE_SCHEMA = c.TABLE_SCHEMA AND t.TABLE_NAME='" + tableName + "' AND t.CONSTRAINT_TYPE = 'PRIMARY KEY' AND c.CONSTRAINT_NAME = 'PRIMARY' and t.table_schema = '" + src.getDb_instance() + "'";
		ResultSet rs = connection.executeQuery(sql);
		String PK= null;
		//只支持单主键
		int num = 0;
		while (rs.next()) {
			PK = rs.getString(1);
			num++;
			
			if (num >= 2) {
				PK = null;
				break;
			}
		}
		DBUtil.close(rs);
		
		sql = "select distinct column_name,data_type,character_maximum_length,is_nullable,column_comment,column_default from information_schema.COLUMNS where table_name = '" + tableName + "' and table_schema = '" + src.getDb_instance() + "'";
		
		rs = connection.executeQuery(sql);
		while (rs.next()) {
			ImportColumn column = getColumn(table, rs, src.getDBType());
			table.addColumn(column);
			if (column.getName().equalsIgnoreCase(PK)) {
				table.setPK(column);
			}
		}
		
		DBUtil.close(rs);
		connection.release();
    	
		if (table.getColumns() == null || table.getColumns().isEmpty()) return null;
    	return table;
    }

	private static ImportColumn getColumn(ImportTable table, ResultSet rs, DBType dbType) throws SQLException {
		String colName = rs.getString(1);
		String dataType = rs.getString(2);
		String dataLengthStr = rs.getString(3);
		boolean isNullable = !"YES".equals(rs.getString(4));
		String comment = rs.getString(5);
		String columnDefault = rs.getString(6);
		long dataLength = 0;
		
		int index = dataType.indexOf("(");
		if (index != -1) {
			dataType = dataType.substring(0, index);
		}
		if (!StringUtil.isEmpty(dataLengthStr)) {
			dataLength = Long.valueOf(dataLengthStr);
		}
		
		dataType = dataType.toUpperCase();
		ImportColumn ic = new ImportColumn(table, colName, TableSrcUtilFactory.getDBDataType(dataType, dbType), dataLength, isNullable, comment);
		if (!StringUtil.isEmpty(columnDefault)) ic.setDefaultValue(columnDefault);
		return ic;
	}
	
	public String getSql(ImportTable table) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("drop table if exists ").append(table.getTableName().toUpperCase()).append(Constants.SEMICOLON).append(Constants.ENTER);
        sb.append("create table ").append(table.getTableName().toUpperCase()).append(Constants.LEFT_PARENTHESE);
        ImportColumn pk = null;
        for (ImportColumn col : table.getColumns())
        {
            sb.append(getSqlColumn(col)).append(Constants.COMMA).append(Constants.BLANK);
            if (col.isPK())
            {
                pk = col;
            }
        }
        
        if (pk != null)
        {
            sb.append("PRIMARY KEY (").append(pk.getName()).append(")");
        }
        else
        {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(Constants.RIGHT_PARENTHESE).append(Constants.SEMICOLON).append(Constants.ENTER);
        sb.append(Constants.ENTER);
        
        return sb.toString();
    }
	
	private static String getSqlColumn(ImportColumn c)
    {
        StringBuilder sb = new StringBuilder(c.getName());
        sb.append(Constants.BLANK);
        DBDataType dt = c.getDBDataType();
        if (dt == MysqlDataType.VARCHAR)
        {
            sb.append("VARCHAR(").append(c.getLength()).append(") COLLATE utf8_unicode_ci");
        }
        else if (dt == MysqlDataType.TEXT)
        {
            sb.append("TEXT COLLATE utf8_unicode_ci");
        }
        else {
        	sb.append(dt.name().toUpperCase());
        }
        
        return sb.toString();
    }
}
