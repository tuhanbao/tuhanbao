package com.tuhanbao.base.util.db.table;

import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.base.util.db.IField;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.Relation;

public class Column implements Cloneable, IField
{
	protected String name;
    
	protected DataType dataType;

	protected int length;
	
	protected Table table;
	
	protected Table fk;
	
	protected Relation relation;
	
	protected boolean canFilter;

    //为了提升性能，这里存一个序号
	protected int index = -1;
	
	protected String enumStr;

    protected Column() {
		
	}
	
	public Column(Table table, String name, DataType dataType, int length) {
		this.name = name;
		this.dataType = dataType;
		this.length = length;
		this.table = table;
	}	

	public Column(Table table, String name, DataType dataType, int length, Table fk, Relation relation) {
		this(table, name, dataType, length);
		this.fk = fk;
		this.relation = relation;
	}
    
	public Column clone() {
	    Column clone = new Column(this.table, this.name, this.dataType, this.length, this.fk, this.relation);
	    clone.index = index;
	    return clone;
    }

    public String getName() {
		return name;
	}
	
	public String getNameWithTable() {
		return getDataGroup().getName() + "." + name;
	}	
	
	/**
	 * 兼容oracle和mysql，列名查询必须形如
	 * select t1.id as t1__ID
	 * @return
	 */
	public String getNameWithAs() {
		return getNameWithTable();
	}
	
	public String getAsName() {
		return name;
	}
	
	public String getTableName() {
		return this.table.getName();
	}

	public DataType getDataType() {
		return dataType;
	}


	public int getLength() {
		return length;
	}
	
	public Column getColumn() {
		return this;
	}

	public Table getTable() {
		return table;
	}
	
	public Table getFK() {
		return fk;
	}
	
	public Relation getRelation() {
		return this.relation;
	}
	
	public int hashCode() {
		return super.hashCode();
	}
	
	public String toString() {
		return this.getNameWithTable();
	}
	
	public void setFk(Table fk) {
		this.fk = fk;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

    public int getIndex() {
        return this.index;
    }

    public boolean isCanFilter() {
        return canFilter;
    }

    public void setCanFilter(boolean canFilter) {
        this.canFilter = canFilter;
    }
    
    public String getEnumStr() {
        return enumStr;
    }

    public void setEnumStr(String enumStr) {
        this.enumStr = enumStr;
    }

    @Override
    public IDataGroup<Column> getDataGroup() {
        return this.getTable();
    }

    @Override
    public String getNameWithDataGroup() {
        return this.getNameWithTable();
    }
}
