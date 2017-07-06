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
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.IColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.XlsTable;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.src.TableSrcUtilFactory;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;

public class OracleDBExcutor extends DBExcutor
{
	private static final String DROP_SEQ_STRING = "DECLARE\n  V_NUM number;\nBEGIN\n  V_NUM := 0;\n  select count(0) into V_NUM from user_sequences where sequence_name = '{0}';\n" 
	         + "  if V_NUM > 0 then\n    execute immediate 'DROP SEQUENCE {0}';\n  end if;\nEND;\n/";

	private static final String DROP_TABLE_STRING = "DECLARE\n  V_NUM number;\nBEGIN\n  V_NUM := 0;\n  select count(0) into V_NUM from user_tables where table_name = '{0}';\n" 
	        + "  if V_NUM > 0 then\n    execute immediate 'DROP TABLE {0}';\n  end if;\nEND;\n/";
	           
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
        throw new MyException("no use now!");
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
		StringBuilder comment = new StringBuilder();
		if (oldTable == null) {
		    sb.append(DROP_TABLE_STRING.replace("{0}", table.getTableName().toUpperCase())).append(Constants.ENTER);
	        sb.append("create table ").append(table.getTableName().toUpperCase()).append(Constants.LEFT_PARENTHESE);
	        ImportColumn pk = table.getPK();
	        for (ImportColumn col : table.getColumns())
	        {
	            sb.append(getSqlColumn(col));

	            if (!StringUtil.isEmpty(col.getComment())) {
	                comment.append("comment on column ").append(table.getTableName().toUpperCase())
	                    .append("." + col.getName().toUpperCase() + " is '").append(col.getComment()).append("';").append(Constants.ENTER);
	            }
	            
	            if (col.isPK())
	            {
	                pk = col;
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
	        sb.append(comment.toString());
	        sb.append(Constants.ENTER);
	        
	        String seqName = table.getSeqName();
            if (!StringUtil.isEmpty(seqName)) {
    	        //如果需要创建序列，还需要生成序列
    	        sb.append(DROP_SEQ_STRING.replace("{0}", seqName)).append(Constants.ENTER);
    	        sb.append("CREATE SEQUENCE " + seqName + " INCREMENT BY 1 START WITH 1;");
    	        sb.append(Constants.ENTER);
	        }
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
			
			//只有当index相同，dataType和length相同，认为是修改
			for (ImportColumn ic : newCols) {
				sb.append(addColumn(ic, table.getPreColumn(ic))).append(Constants.ENTER);
			}
			
			for (ImportColumn ic : oldCols) {
				sb.append(dropColumn(ic)).append(Constants.ENTER);
			}
			
			for (Entry<ImportColumn, ImportColumn> entry : modifyCols.entrySet()) {
				sb.append(changeColumn(entry.getKey(), entry.getValue())).append(Constants.ENTER);
			}
			
			ImportColumn pk = table.getPK();
			ImportColumn oldPk = oldTable.getPK();
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
        long length = c.getLength();
        
        if (dt == DataType.STRING)
        {
            sb.append("VARCHAR2(").append(length).append(")");
        }
        else if (dt == DataType.TEXT)
        {
            sb.append("TEXT");
        }
        else if (dt == DataType.BYTE || dt == DataType.BOOLEAN)
        {
            sb.append("NUMBER(4)");
        }
        else if (dt == DataType.SHORT)
        {
            sb.append("NUMBER(6)");
        }
        else if (dt == DataType.INT)
        {
            sb.append("NUMBER(10)");
        }
        else if (dt == DataType.LONG)
        {
            if (c.getLength() != -1) {
                sb.append("NUMBER(11)");
            }
            else {
                sb.append("NUMBER(" + c.getLength() + ")");
            }
        }
        else if (dt == DataType.DATE)
        {
            sb.append("TIMESTAMP");
        }
        else if (dt == DataType.BYTEARRAY || dt == DataType.BLOB)
        {
            sb.append("BLOB");
        }
        else if (dt == DataType.FLOAT)
        {
            sb.append("NUMBER(11, 10)");
        }
        else if (dt == DataType.DOUBLE)
        {
            sb.append("NUMBER(11, 10)");
        }
        else if (dt == DataType.BIGDEECIMAL)
        {
            sb.append("NUMBER(11, 10)");
        }
        
        String defaultValue = c.getDefaultValue();
        if (defaultValue != null && !defaultValue.isEmpty())
        {
            if (dt == DataType.STRING)
                sb.append(Constants.BLANK).append("DEFAULT").append(Constants.BLANK).append("'").append(defaultValue).append("'");
            else
                sb.append(Constants.BLANK).append("DEFAULT").append(Constants.BLANK).append(defaultValue);
        }
        else if (isChangeCol && !c.isPK()) {
            sb.append(Constants.BLANK).append("default").append(Constants.BLANK).append("NULL");
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
    			.append(");");
    	}
    	else if (!pk.getName().equals(oldPk.getName())) {
    	    String tableName = pk.getTable().getTableName();
    		sb.append("ALTER TABLE ").append(tableName).append(" DROP PRIMARY KEY, ADD PRIMARY KEY (").append(pk.getName())
				.append(");");
    	}
    	return sb.toString();
    }
    
    private static String changeColumn(ImportColumn col, ImportColumn oldCol) {
    	String tableName = col.getTable().getTableName();
    	StringBuilder sb = new StringBuilder();
    	if (!col.getName().equalsIgnoreCase(oldCol.getName())) {
    	    sb.append("ALTER TABLE ").append(tableName).append(" RENAME COLUMN ").append(oldCol.getName()).append(" to ").append(col.getName()).append(Constants.SEMICOLON);
    	}
    	sb.append("ALTER TABLE ").append(tableName).append(" MODIFY ").append(getSqlColumn(col, true)).append(Constants.SEMICOLON);
    	return sb.toString();
    }
    
    private static String addColumn(ImportColumn col, ImportColumn preCol) {
    	String tableName = col.getTable().getTableName();
    	StringBuilder sb = new StringBuilder();
    	sb.append("ALTER TABLE ").append(tableName).append(" ADD ").append(getSqlColumn(col)).append(Constants.SEMICOLON);
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
