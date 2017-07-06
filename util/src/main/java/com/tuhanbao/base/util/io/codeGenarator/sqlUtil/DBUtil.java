package com.tuhanbao.base.util.io.codeGenarator.sqlUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.db.conn.MyConnection;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.sqlUtil.excutor.DBExcutor;
import com.tuhanbao.base.util.io.codeGenarator.sqlUtil.excutor.MysqlDBExcutor;
import com.tuhanbao.base.util.io.codeGenarator.sqlUtil.excutor.OracleDBExcutor;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DBType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.XlsTable;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.src.TableSrcUtilFactory;
import com.tuhanbao.base.util.io.excel.ExcelUtil;
import com.tuhanbao.base.util.log.LogManager;

public class DBUtil
{
	private static Map<DBType, DBExcutor> excutors = new HashMap<DBType, DBExcutor>();
    
	private static final String AUTOCREATE = "autocreate";
	
    private DBUtil(){}
    
    public static DBExcutor getDBExcutor(DBType dbType)
    {
        if (excutors.containsKey(dbType)) return excutors.get(dbType);
        
        //暂时不支持其他数据库
        if (dbType == DBType.MYSQL)
        {
            excutors.put(dbType, new MysqlDBExcutor());
            return excutors.get(dbType);
        }
        else if (dbType == DBType.ORACLE)
        {
            excutors.put(dbType, new OracleDBExcutor());
            return excutors.get(dbType);
        }
        
        throw new MyException(BaseErrorCode.ERROR, "Donot support this type db!");
    }
    
    public static String getSql(List<XlsTable> tables, DBType dbType) throws IOException
    {
        return getDBExcutor(dbType).getSql(tables);
    }
    
    public static String getSql(ImportTable table, DBSrc dbSrc) throws IOException
    {
        return getDBExcutor(dbSrc.getDbType()).getSql(table, dbSrc);
    }
    
    public static String getDropTableSql(ImportTable table, DBType dbType) throws IOException
    {
        return "drop table " + table.getTableName() + ";" + Constants.ENTER;
    }
    
    public static void close(Statement st)
    {
        if (st != null)
        {
            try
            {
                st.close();
            }
            catch (SQLException e)
            {
                LogManager.error(e);
            }
        }
    }
    
    public static void close(ResultSet set)
    {
        if (set != null)
        {
            try
            {
                set.close();
            }
            catch (SQLException e)
            {
                LogManager.error(e);
            }
        }
    }
    
    public static void release(MyConnection conn)
    {
        if (conn != null)
        {
            conn.release();
        }
    }
    
    /**
     * 导出一个用户下面的表
     * 
     * 仅用于oracle数据库
     * @param src
     * @throws Exception 
     */
    public static void exportUserTable(DBSrc src, String url) throws Exception {
		List<ImportTable> tables = TableSrcUtilFactory.getTables(src);
		List<String[]> baseInfo = new ArrayList<String[]>();

		baseInfo.add(new String[]{"#表名", "中文描述", "模块（可为空）", "缓存(NOT/ALL/AUTO)默认为NO", "序列号（oracle数据库可配置，非必填，填autocreate，会根据表名自动生成序列）", "默认排序字段（可不填）", "备注"});
		for (ImportTable table : tables) {
			List<String[]> datas = new ArrayList<String[]>();
			datas.add(new String[]{"字段名称", "字段类型", "长度", "是否可以为空", "主外键信息", "默认值", "备注"});
			for (ImportColumn col : table.getColumns()) {
				datas.add(getRowValue(col));
			}
			
			String[][] result = new String[datas.size()][];
			int i = 0;
			for (String[] data : datas) {
				result[i++] = data;
			}
			baseInfo.add(new String[]{table.getName(), "", "", "NOT", AUTOCREATE, "", "", ""});
			ExcelUtil.createSheet(url, table.getName(), result);
		}
		
		String[][] result = new String[baseInfo.size()][];
		int i = 0;
		for (String[] data : baseInfo) {
			result[i++] = data;
		}
		ExcelUtil.createSheet(url, "base", result);
    }
    
    /**
     * 导出一个用户下面的表
     * 
     * 仅用于oracle数据库
     * @param src
     * @throws Exception 
     */
    public static List<ImportTable> getTables(DBSrc src) throws Exception {
    	return TableSrcUtilFactory.getTables(src);
    }

	private static String[] getRowValue(ImportColumn col) throws SQLException {
		String colName = col.getName();
		String dataType = col.getDataType().getName();
		String dataLength = col.getLength() + "";
		String nullAble = col.isNullable() + "";
		String keyValue = col.isPK() ? "PK" : col.getFK();
		String comment = col.getComment();
		return new String[]{colName, dataType, dataLength, nullAble, keyValue, "", comment};
	}
}
