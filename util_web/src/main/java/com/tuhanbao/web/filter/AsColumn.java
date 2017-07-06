package com.tuhanbao.web.filter;

import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.db.table.Table;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 连表的时候会有别名，需要扩展column
 * @author Administrator
 *
 */
public class AsColumn extends Column
{
	//table的别名
	private SelectTable selectTable;
	
	private Column column;
	
	private String asName;
	
	protected AsColumn(Column column) {
		this(column, null, null);
	}
	
	public AsColumn(Column column, String asName) {
		this(column, asName, null);
	}
	
	protected AsColumn(Column column, SelectTable selectTable) {
		this(column, null, selectTable);
	}	

	protected AsColumn(Column column, String asName, SelectTable selectTable) {
		this.selectTable = selectTable;
		this.column = column;
		this.asName = asName;
	}	
    
	public String getNameWithTable() {
		if (selectTable == null) return column.getNameWithTable();
		else return selectTable.getName() + "." + getName();
	}	
	
	/**
	 * 兼容oracle和mysql，列名查询必须形如
	 * select t1.id as t1__ID
	 * @return
	 */
	public String getNameWithAs() {
		if (StringUtil.isEmpty(asName)) return getNameWithTable();
		else return getNameWithTable() + " as " + getAsName();
	}
	
	public String getAsName() {
		if (StringUtil.isEmpty(asName)) return this.column.getAsName();
		else return asName;
	}
    
    public boolean equals(Object o) {
        if (this == o) return true;
        
        if (o instanceof AsColumn) {
            AsColumn other = (AsColumn)o;
            return this.getColumn() == other.getColumn() && this.selectTable == other.selectTable;
        }
        return false;
    }
	
	public Column getColumn() {
		return this.column;
	}
    
	public String getName() {
		return this.column.getName();
	}
	
	public DataType getDataType() {
		return this.column.getDataType();
	}

	public int getLength() {
		return this.column.getLength();
	}

	public Table getTable() {
		return this.column.getTable();
	}
	
	@Override
	public String getTableName() {
		if (selectTable == null) return column.getTableName();
		else return this.selectTable.getName();
	}
	
	public int hashCode() {
		return this.column.hashCode();
	}
	
	@Override
	public Table getFK() {
		return this.column.getFK();
	}

	public void setAsName(String asName) {
		this.asName = asName;
	}

    public int getIndex() {
        return this.column.getIndex();
    }
    
    public SelectTable getSelectTable() {
        return this.selectTable;
    }
    
    public boolean isCanFilter() {
        return this.column.isCanFilter();
    }
    
    public String getEnumStr() {
        return this.column.getEnumStr();
    }

}
