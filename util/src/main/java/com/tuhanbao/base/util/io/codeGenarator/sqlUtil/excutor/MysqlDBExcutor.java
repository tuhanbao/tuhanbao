package com.tuhanbao.base.util.io.codeGenarator.sqlUtil.excutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.IColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.XlsTable;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.XlsTable.XlsColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.src.TableSrcUtilFactory;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;

public class MysqlDBExcutor extends DBExcutor
{

    /**
     * 构造sql文件
     * 
     * 老系统用的，现在已经用不着
     * 
     * @param tables
     * @return
     * @throws IOException
     */
	@Deprecated
    public String getSql(List<XlsTable> tables) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        
        List<String> idList = new ArrayList<String>();
        for (XlsTable table : tables)
        {
            sb.append("drop table if exists ").append(table.getTableName().toUpperCase()).append(Constants.SEMICOLON).append(Constants.ENTER);
            sb.append("create table ").append(table.getTableName().toUpperCase()).append(Constants.LEFT_PARENTHESE);
            XlsColumn pk = null;
            for (XlsColumn col : table.getList())
            {
                sb.append(getSqlColumn(col));
                if (col.isPK() && (col.getDataType() == DataType.LONG || DataType.INT.getName().equalsIgnoreCase(col.getDataType().getName())))
                {
                    pk = col;
                    sb.append(Constants.BLANK).append("AUTO_INCREMENT");
                }
                sb.append(Constants.COMMA).append(Constants.BLANK);
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
            
            if (table.getIdIndex() != null && !table.getIdIndex().isEmpty())
            {
                idList.add(table.getIdIndex());
            }
        }
        
        
        for (String id : idList)
        {
            sb.append(Constants.ENTER).append("insert into IDS values(").append(id).append(",1,1);");
        }
       
        return sb.toString();
    }

    private static boolean needAutoIncrement(ImportColumn col) {
        return col.isPK() && col.getTable().isAutoIncrement() && (col.getDataType() == DataType.LONG || DataType.INT.getName().equalsIgnoreCase(col.getDataType().getName()));
    }

	@Override
	public String getSql(ImportTable table, DBSrc dbSrc) throws IOException {
		ImportTable oldTable = null;
		try {
			oldTable = TableSrcUtilFactory.getTable(dbSrc, table.getTableName());
		} catch (Exception e) {
			LogManager.error(e);
		}
		
		StringBuilder sb = new StringBuilder();
		if (oldTable == null) {
			sb.append("drop table if exists ").append(table.getTableName().toUpperCase()).append(Constants.SEMICOLON).append(Constants.ENTER);
	        sb.append("create table ").append(table.getTableName().toUpperCase()).append(Constants.LEFT_PARENTHESE);
	        ImportColumn pk = table.getPK();
	        for (ImportColumn col : table.getColumns())
	        {
	            sb.append(getSqlColumn(col));
	            if (needAutoIncrement(col))
	            {
	                pk = col;
	                //默认自增
	                sb.append(Constants.BLANK).append("AUTO_INCREMENT");
	            }
	            sb.append(Constants.COMMA).append(Constants.BLANK);
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
		}
		else {
			List<ImportColumn> newCols = new ArrayList<ImportColumn>(table.getColumns());
			List<ImportColumn> oldCols = new ArrayList<ImportColumn>(oldTable.getColumns());
			
			Map<ImportColumn, ImportColumn> modifyCols = new HashMap<ImportColumn, ImportColumn>();
			Iterator<ImportColumn> it = newCols.iterator();
			for (; it.hasNext();) {
				ImportColumn newCol = it.next();
				
				Iterator<ImportColumn> oldColsIt = oldCols.iterator();
				for (; oldColsIt.hasNext();) {
					ImportColumn oldCol = oldColsIt.next();
					if (oldCol.getName().equals(newCol.getName())) {
						if (!isEquals(newCol, oldCol)) {
							modifyCols.put(newCol, oldCol);
						}
						it.remove();
						oldColsIt.remove();
						break;
					}
					
				}
			}
			
			it = newCols.iterator();
			Iterator<ImportColumn> oldColsIt = oldCols.iterator();
			if (it.hasNext() && oldColsIt.hasNext()) {
				ImportColumn newCol = it.next();
				ImportColumn oldCol = oldColsIt.next();
				int newIndex = table.getIndex(newCol);
				int oldIndex = oldTable.getIndex(oldCol);
				
				while (true) {
					if (newIndex == oldIndex) {
						if (isSameCol(newCol, oldCol)) {
							modifyCols.put(newCol, oldCol);
							it.remove();
							oldColsIt.remove();
						}
						if (it.hasNext() && oldColsIt.hasNext()) {
							newCol = it.next();
							oldCol = oldColsIt.next();
							newIndex = table.getIndex(newCol);
							oldIndex = oldTable.getIndex(oldCol);
						}
						else break;
					}
					else if (newIndex < oldIndex) {
						if (it.hasNext()) {
							newCol = it.next();
							newIndex = table.getIndex(newCol);
						}
						else break;
					}
					else {
						if (oldColsIt.hasNext()) {
							oldCol = oldColsIt.next();
							oldIndex = oldTable.getIndex(oldCol);
						}
						else break;
					}
				}
			}
			
			for (Entry<ImportColumn, ImportColumn> entry : modifyCols.entrySet()) {
			    sb.append(changeColumn(entry.getKey(), entry.getValue())).append(Constants.ENTER);
			}
			
			//只有当index相同，dataType和length相同，认为是修改
			for (ImportColumn ic : newCols) {
				sb.append(addColumn(ic, table.getPreColumn(ic))).append(Constants.ENTER);
			}
			
			for (ImportColumn ic : oldCols) {
				sb.append(dropColumn(ic)).append(Constants.ENTER);
			}
			
			ImportColumn pk = table.getPK();
			ImportColumn oldPk = oldTable.getPK();
			//如果主键名称有变化
			String changePK = changePK(pk, oldPk);
			if (!StringUtil.isEmpty(changePK)) {
				sb.append(changePK).append(Constants.ENTER);
			}
		}
		return sb.toString();
	}
	
	private static boolean isSameCol(ImportColumn newCol, ImportColumn oldCol) {
		return newCol.getDBDataType() == oldCol.getDBDataType() && newCol.getLength() >= oldCol.getLength();
	}
	
	public static boolean isEquals(ImportColumn newCol, ImportColumn oldCol) {
		if (oldCol == newCol) return true;
		if (newCol == null || oldCol == null) return false;
		
		boolean result = newCol.getTable().getName().equals(oldCol.getTable().getName()) && newCol.getName().equals(oldCol.getName()) && newCol.getDBDataType() == oldCol.getDBDataType();
		
		if (!result) {
			return false;
		}
		
		if (newCol.getDataType() == DataType.STRING) {
			result = result && (newCol.getLength() == oldCol.getLength());
		}
		result = result && StringUtil.isEqual(newCol.getDefaultValue(), oldCol.getDefaultValue())
			&& StringUtil.isEqual(newCol.getComment(), oldCol.getComment());
		return result;
	}

    /**
     * 构造sql文件
     * 
     * @param c
     * @return
     */
    private static String getSqlColumn(IColumn c) {
        return getSqlColumn(c, false);
    }

    private static String getSqlColumn(IColumn c, boolean isChangeCol)
    {
        StringBuilder sb = new StringBuilder(c.getName());
        sb.append(Constants.BLANK);
        DataType dt = c.getDataType();
        
        if (dt == DataType.STRING)
        {
            sb.append("VARCHAR(").append(c.getLength()).append(") COLLATE utf8_unicode_ci");
        }
        else if (dt == DataType.TEXT)
        {
            sb.append("TEXT COLLATE utf8_unicode_ci");
        }
        else if (dt == DataType.BYTE || dt == DataType.BOOLEAN)
        {
            sb.append("TINYINT");
        }
        else if (dt == DataType.SHORT)
        {
            sb.append("SMALLINT");
        }
        else if (dt == DataType.INT)
        {
            sb.append("INT");
        }
        else if (dt == DataType.LONG)
        {
            sb.append("BIGINT");
        }
        else if (dt == DataType.DATE)
        {
            sb.append("DATETIME");
        }
        else if (dt == DataType.BYTEARRAY || dt == DataType.BLOB)
        {
            sb.append("BLOB");
        }
        else if (dt == DataType.FLOAT)
        {
            sb.append("FLOAT");
        }
        else if (dt == DataType.DOUBLE)
        {
            sb.append("DOUBLE");
        }
        else if (dt == DataType.BIGDEECIMAL)
        {
            sb.append("DECIMAL");
        }
        
        String defaultValue = c.getDefaultValue();
        if (defaultValue != null && !defaultValue.isEmpty())
        {
            if (dt == DataType.STRING)
                sb.append(Constants.BLANK).append("DEFAULT").append(Constants.BLANK).append("'").append(defaultValue).append("'");
            else
                sb.append(Constants.BLANK).append("DEFAULT").append(Constants.BLANK).append(defaultValue);
        }
        else if (isChangeCol && !c.isPK()){
            sb.append(Constants.BLANK).append("DEFAULT").append(Constants.BLANK).append("NULL");
        }
        if (!StringUtil.isEmpty(c.getComment())) {
        	sb.append(" COMMENT \"").append(c.getComment()).append("\"");
        }
        return sb.toString();
    }
    

//    ALTER TABLE `TEST` DROP PRIMARY KEY, ADD PRIMARY KEY (`NAME`);
//    ALTER TABLE `TEST` MODIFY COLUMN `ID`  varchar(11);
//    ALTER TABLE `TEST` CHANGE COLUMN `age` `age1` varchar(11);
//    ALTER TABLE `TEST` ADD COLUMN `age2`  varchar(11) after age1;
//    ALTER TABLE `TEST` DROP COLUMN `SEX`;
    private static String changePK(ImportColumn pk, ImportColumn oldPk) {
        if (pk == null && oldPk == null) return Constants.EMPTY;
        
        StringBuilder sb = new StringBuilder();
        if (pk == null && oldPk != null) {
            String tableName = oldPk.getTable().getTableName();
            sb.append("ALTER TABLE ").append(tableName).append(" DROP PRIMARY KEY;");
        }
        else if (pk != null && oldPk == null) {
            String tableName = pk.getTable().getTableName();
            sb.append("ALTER TABLE ").append(tableName).append(" ADD PRIMARY KEY (").append(pk.getName())
                .append(");").append(Constants.ENTER);
            sb.append("ALTER TABLE ").append(tableName).append(" MODIFY COLUMN ").append(getSqlColumn(pk));
            
            if (needAutoIncrement(pk)) {
                sb.append(Constants.BLANK).append("AUTO_INCREMENT");
            }
        }
        else if (!pk.getName().equals(oldPk.getName())) {
            String tableName = pk.getTable().getTableName();
            sb.append("ALTER TABLE ").append(tableName).append(" DROP PRIMARY KEY, ADD PRIMARY KEY (").append(pk.getName())
                .append(");");
            sb.append("ALTER TABLE ").append(tableName).append(" MODIFY COLUMN ").append(getSqlColumn(pk));
            if (needAutoIncrement(pk)) {
                sb.append(Constants.BLANK).append("AUTO_INCREMENT");
            }
        }
        return sb.toString();
    }
    
    private static String changeColumn(ImportColumn col, ImportColumn oldCol) {
    	String tableName = col.getTable().getTableName();
    	StringBuilder sb = new StringBuilder();
    	sb.append("ALTER TABLE ").append(tableName);
    	if (col.getName().equalsIgnoreCase(oldCol.getName())) {
    		sb.append(" MODIFY COLUMN ").append(getSqlColumn(col, true));
    		if (needAutoIncrement(col)) {
                sb.append(Constants.BLANK).append("AUTO_INCREMENT");
            }
    	}
    	else {
    		sb.append(" CHANGE COLUMN ").append(oldCol.getName()).append(Constants.BLANK).append(getSqlColumn(col));
    		//额外增加自增
    		if (col.getName().equalsIgnoreCase(oldCol.getName())) {
    		    sb.append(Constants.SEMICOLON);
        		sb.append(Constants.ENTER).append("ALTER TABLE ").append(tableName);
                sb.append(" MODIFY COLUMN ").append(getSqlColumn(col, true));
                if (needAutoIncrement(col)) {
                    sb.append(Constants.BLANK).append("AUTO_INCREMENT");
                }
            }
    		
    	}
    	sb.append(Constants.SEMICOLON);
    	return sb.toString();
    }
    
    private static String addColumn(ImportColumn col, ImportColumn preCol) {
    	String tableName = col.getTable().getTableName();
    	StringBuilder sb = new StringBuilder();
    	sb.append("ALTER TABLE ").append(tableName).append(" ADD COLUMN ").append(getSqlColumn(col));
    	if (preCol != null) {
    	    sb.append(" after ").append(preCol.getName());
    	}
		sb.append(Constants.SEMICOLON);
    	return sb.toString();
    }
    
    private static String dropColumn(ImportColumn col) {
    	String tableName = col.getTable().getTableName();
    	StringBuilder sb = new StringBuilder();
    	sb.append("ALTER TABLE ").append(tableName).append(" DROP COLUMN ").append(col.getName())
    			.append(Constants.SEMICOLON);
    	return sb.toString();
    }
}
