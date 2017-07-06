package com.tuhanbao.base.util.db.table;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.base.util.log.LogManager;

/**
 * 业务项目用到的Table
 * 与ImportTable不同
 * 
 * @author Administrator
 *
 */
public class Table implements IDataGroup<Column>
{
    private String name;
    
    private List<Column> list = new ArrayList<Column>();
    
    private CacheType cacheType;
    
    private Column PK;
    
    private String modelName;
    
    private String seqName;
    
    protected Table() {
    	
    }
    
    public Table(int colSize, String name, CacheType cacheType, String modelName)
    {
    	list = new ArrayList<Column>(colSize);
    	this.name = name;
    	this.cacheType = cacheType;
    	this.modelName = modelName;
    }
    
    public Table(int colSize, String name, CacheType cacheType, String modelName, String seqName)
    {
    	this(colSize, name, cacheType, modelName);
    	this.seqName = seqName;
    }
    
    public void addColumn(Column col)
    {
        list.add(col);
        col.index = list.size() - 1;
    }

    public void setPK(Column pk) {
    	this.PK = pk;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getModelName() {
    	return this.modelName;
    }

	public CacheType getCacheType() {
		return cacheType;
	}
	
	public String getSeqName() {
		return seqName;
	}

	public Column getPK() {
		return PK;
	}

	public List<Column> getColumns() {
		return list;
	}

    @Override
    public List<Column> getFields() {
        return getColumns();
    }
	
	public String toString() {
		return this.getName();
	}
	
	public Column getColumn(int index) {
		return list.get(index);
	}
	
	public int getIndex(Column col) {
		if (col.getTable() == this) return col.getIndex();
		return -1;
	}

	public Column findFKColumn(Table table) {
		for (Column col : this.list) {
			if (col.getFK() == table) {
				return col;
			}
		}
		
		for (Column col : table.list) {
			if (col.getFK() == this) {
				return col;
			}
		}
		
		return null;
	}

	public Column getColumn(String name) {
		for (Column col : this.list) {
			if (col.getName().equalsIgnoreCase(name)) {
				return col;
			}
		}
		return null;
	}

    @Override
    public Class<?> getModelClassName() {
        try {
            return Class.forName(this.getModelName());
        }
        catch (ClassNotFoundException e) {
            LogManager.error(e);
            return null;
        }
    }
}
