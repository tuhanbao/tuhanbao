package com.tuhanbao.web.filter;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.dataservice.ICTBean;
import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.base.dataservice.filter.JoinType;
import com.tuhanbao.base.util.db.table.CacheType;
import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.db.table.Table;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.StringUtil;

/**
 * 查询器 暂时只支持单表查询，可以扩展为多表查询
 * 
 * @author tuhanbao
 *
 */
public class SelectTable {
	private Table table;
	
	private SelectTable parent;
	
	private MyBatisFilter filter;
	
	private AsColumn fkColumn;
	
	//表示外键是自己的还是别人的
	private boolean fkIsMine;

	private List<SelectTable> children;
	
	private List<AsColumn> columns = new ArrayList<AsColumn>();
	
	private MyBatisSelector selector;
	
	/**
	 * 别名
	 */
	private String asName;
	
	private JoinType joinType;
	
	protected SelectTable(Table table, MyBatisSelector selector) {
		this(table, table.getName());
		this.selector = selector;
	}
	
	protected SelectTable(Table table, String asName, MyBatisSelector selector) {
		this(table, asName);
		this.selector = selector;
	}
	
	private SelectTable(Table table, String asName) {
		this.table = table;
		this.asName = asName.toUpperCase();
		joinType = JoinType.LEFT_JOIN;
		for (Column col : table.getColumns()) {
			this.columns.add(new AsColumn(col, this));
		}
	}
	
	private SelectTable(Table table, String asName, Filter filter, SelectTable parent, JoinType joinType) {
		this(table, asName);
		this.filter = MyBatisFilter.getInstance(filter);
		this.parent = parent;
		this.joinType = joinType;
	}

	private SelectTable(Table table, String asName, Column fkColumn, SelectTable parent, JoinType joinType) {
		this(table, asName);
		this.fkIsMine = isMineFk(table, fkColumn, parent);
		this.parent = parent;
		this.filter = MyBatisFilter.getInstance(new Filter());
		if (fkIsMine) {
			this.fkColumn = getColumn(fkColumn);
			this.filter.andFilter(this.fkColumn, parent.getPK());
		}
		else {
			this.fkColumn = parent.getColumn(fkColumn);
			this.filter.andFilter(this.fkColumn, this.getPK());
		}
		this.joinType = joinType;
	}

    private boolean isMineFk(Table table, Column fkColumn, SelectTable parent) {
        //fkColumn如果是AsColumn，证明是
        if (fkColumn instanceof AsColumn) {
            if (((AsColumn)fkColumn).getSelectTable() == parent) {
                return false;
            }
        }
        return table.getIndex(fkColumn) != -1;
    }
	
    public SelectTable joinTable(Table table) {
        return this.joinTable(table, table.getName());
    }   
    
    /**
     * 默认情况下，优先取this中table的外键
     * 
     * 例如Area中parent_id连自己本身，
     * 那么最后获取的关系是取parent
     * 如果调用
     * 
     * @param table
     * @param asName
     * @return
     */
    public SelectTable joinTable(Table table, String asName) {
        Column column = this.findFKColumn(table);
        if (column == null) throw new MyException("no this column for fk!");
        return joinTable(table, asName, column);
    } 
    
	public SelectTable joinTable(Table table, Column fkColumn) {
		return joinTable(table, table.getName(), fkColumn);
	}	

    public SelectTable joinTable(Table table, JoinType joinType) {
        Column column = this.findFKColumn(table);
        if (column == null) throw new MyException("no this column for fk!");
        return joinTable(table, column, joinType);
    }
	
	public SelectTable joinTable(Table table, Column fkColumn, JoinType joinType) {
		return joinTable(table, table.getName(), fkColumn, joinType);
	}
	
	public SelectTable joinTable(Table table, String asName, Column fkColumn) {
		return joinTable(table, asName, fkColumn, JoinType.LEFT_JOIN);
	}
	
	public SelectTable joinTable(Table table, String asName, Column fkColumn, JoinType joinType) {
		if (fkColumn == null) throw new MyException("fk column is null!");
		
		SelectTable child = new SelectTable(table, asName, fkColumn, this, joinType);
		addChild(child);
		return child;
	}
	
	@Deprecated
	public SelectTable joinTable(Table table, Filter filter) {
		return joinTable(table, null, filter);
	}	
	
	@Deprecated
	public SelectTable joinTable(Table table, String asName, Filter filter) {
		return joinTable(table, asName, filter, JoinType.LEFT_JOIN);
	}
	
	@Deprecated
	public SelectTable joinTable(Table table, String asName, Filter filter, JoinType joinType) {
		if (filter == null) throw new MyException("fk filter is null!");
		SelectTable child = new SelectTable(table, asName, filter, this, joinType);
		addChild(child);
		return child;
	}
	
	public Table getTable() {
		return this.table;
	}
	
	private void addChild(SelectTable child) {
		if (this.children == null) children = new ArrayList<SelectTable>();
		this.children.add(child);
		this.addTable(child);
	}
	
    
    private void addTable(SelectTable selectTable) {
		if (parent != null) {
			parent.addTable(selectTable);
		}
		else {
			this.selector.addTable(selectTable);
		}
	}

	public void addColumn(Column col)
    {
        throw new MyException("cannot invoke this method!");
    }

    public void setPK(Column pk) {
    	throw new MyException("cannot invoke this method!");
    }
    
    public String getName()
    {
    	if (StringUtil.isEmpty(asName)) return this.table.getName();
        return asName;
    }

	public CacheType getCacheType() {
		return table.getCacheType();
	}
	
	public AsColumn getFKColumn() {
		return this.fkColumn;
	}

	public AsColumn getPK() {
		return getColumn(table.getPK());
	}

	protected void addColumns2List(SelectColumns selectColumns) {
		for (Column col : this.columns) {
			selectColumns.addColumn(col, this);
		}
		if (this.children != null) {
			for (SelectTable child : this.children) {
				child.addColumns2List(selectColumns);
			}
		}
	}
	
	public AsColumn getColumn(int index) {
		return columns.get(index);
	}
	
	public int getIndex(Column col) {
		return table.getIndex(col.getColumn());
	}

	public Column findFKColumn(Table table) {
		Column fk = this.table.findFKColumn(table);
		//modify by wangbing
		//增加条件 && table != this.table，当表自己练自己时，系统不要强加干预此字段属于谁
		if (fk != null && fk.getTable() == this.table && table != this.table) return getColumn(fk);
		else return fk;
	}

	public AsColumn getColumn(String name) {
		return getColumn(this.table.getColumn(name));
	}
	
	public AsColumn getColumn(Column column) {
		int index = getIndex(column);
		if (index == -1) return null;
		return this.columns.get(index);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (parent != null) {
			sb.append(joinType.toString()).append(Constants.BLANK);
		}
		sb.append(table.getName());
		//oracle不需要加as，mysql加不加都可
		if (!StringUtil.isEmpty(asName)) sb.append(" ").append(asName);
		if (parent != null) {
			sb.append(" on ").append(filter.getSelectSql());
		}
		sb.append(Constants.ENTER);
		
		if (this.children != null) {
			for (SelectTable child : this.children) {
				sb.append(child.toString());
			}
		}
		return sb.toString();
	}
	
	public SelectTable getParent() {
		return parent;
	}

	public Filter getFilter() {
		return filter;
	}

	public List<SelectTable> getChildren() {
		return children;
	}
	
	public List<AsColumn> getColumns() {
	    return this.columns;
	}
	
	@Override
	public int hashCode() {
		if (StringUtil.isEmpty(asName)) return this.table.hashCode();
		else {
			return this.table.hashCode() * this.asName.hashCode();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof SelectTable) {
			return StringUtil.isEqual(this.asName, ((SelectTable) o).asName) && this.table == ((SelectTable) o).table;
		}
		else if (o instanceof Table) {
			return StringUtil.isEmpty(this.asName);
		}
		return false;
	}
	
	public void refreshCTTable(ICTBean ctBean) {
		this.table = ctBean.getTable(table);
	}
}
