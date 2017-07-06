package com.tuhanbao.base.util.io.codeGenarator.tableUtil;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.db.table.dbtype.DBDataType;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.codeGenarator.classUtil.IEnumType;
import com.tuhanbao.base.util.objutil.StringUtil;

public class ImportColumn implements IColumn, Comparable<IColumn>
{
	protected final String name;
	
	protected String defaultValue;

	//DBDataType是指数据库的类型，因为java类型是无法确定该字段在数据库是什么类型，必须记录
	protected DBDataType dbDataType;
	
	protected DataType dataType;
    
	protected long length = -1L;
    
    //字段是否需要。某些字段虽然在数据库中存在，但是不必生成代码。
	protected boolean isNeed = true;
	
	//自己的table
	private ImportTable table;
	
	private ImportColumn fkColumn;
	
	private Relation fkRT;
	
	//有些时候并不能找到fk的table和column，先记录str信息
	private String FK;
	
	private boolean isPK;
	
	private String comment;
	
	private boolean isNullable;
	
	private IEnumType enumInfo;
	
	protected boolean canFilter = false;
	
    public ImportColumn(ImportTable table, String name, DBDataType dbDataType, long length)
    {
    	this(table, name, dbDataType, length, true, null);
    } 
    
    public ImportColumn(ImportTable table, String name, DataType dataType, DBDataType dbDataType, long length)
    {
    	this(table, name, dataType, dbDataType, length, true, null);
    }
    
    public ImportColumn(ImportTable table, String name, DataType dataType, DBDataType dbDataType, long length, boolean isNullable, String comment)
    {
    	this.name = name.toUpperCase();
    	this.dataType = dataType;
    	this.dbDataType = dbDataType;
    	this.length = length;
    	this.isNullable = isNullable;
    	this.comment = comment;
    }
    
    public ImportColumn(ImportTable table, String name, DBDataType dbDataType, long length, boolean isNullable, String comment)
    {
    	this.table = table;
    	this.name = name.toUpperCase();
    	this.dbDataType = dbDataType;
    	this.dataType = dbDataType.getDataType();
    	this.length = length;
    	this.isNullable = isNullable;
    	this.comment = comment;
    }
    
    public String[] toArray()
    {
        return new String[]{name, dataType.toString(), length + "", name};
    }
    
    public String getName() {
		return name;
	}
    
    public DataType getDataType() {
		return dataType;
	}

	public DBDataType getDBDataType() {
		return dbDataType;
	}

	public long getLength() {
		return length;
	}
	
	public boolean isBlob() {
		return dbDataType.isBlob();
	}
    
    public boolean isNeed() {
		return isNeed;
	}

	public void setNeed(boolean isNeed) {
		this.isNeed = isNeed;
	}

	public ImportTable getTable() {
		return this.table;
	}
	
	public void setTable(ImportTable table) {
		this.table = table;
	}


	public ImportColumn getFkColumn() {
		if (fkColumn != null) return fkColumn;
		else if (!StringUtil.isEmpty(FK)) {
			if (FK.startsWith("FK")) {
				//去掉首尾括号和FK
				FK = FK.substring(3, FK.length() - 1);
			}
			String[] fkAndRt = StringUtil.string2Array(FK, "#");
			String[] tableNameAndCol = StringUtil.string2Array(fkAndRt[0], ".");
			if (tableNameAndCol.length != 2) {
				StringBuilder msg = new StringBuilder();
				msg.append(this.getTable().name).append(".").append(this.getName()).append("'s fk config is wrong:");
				msg.append(fkAndRt[0]);
				throw new MyException(msg.toString());
			}
			ImportTable fkTable = TableManager.getTable(tableNameAndCol[0]);
			if (fkTable == null) {
				StringBuilder msg = new StringBuilder("cannot find fk table \"").append(tableNameAndCol[0]).append("\" on column ");
				msg.append(this.getTable().name).append(".").append(this.getName());
				msg.append(Constants.ENTER).append("please check ").append(this.getTable().name).append("'s config is right?");
				throw new MyException(msg.toString());
			}
			this.fkColumn = fkTable.getColumn(tableNameAndCol[1]);
			if (this.fkColumn == null) {
				StringBuilder msg = new StringBuilder("cannot find fk column \"").append(tableNameAndCol[0]).append(".").append(tableNameAndCol[1]).append("\" on column ");
				msg.append(this.getTable().name).append(".").append(this.getName());
				msg.append(Constants.ENTER).append("please check ").append(this.getTable().name).append("'s config is right?");
				throw new MyException(msg.toString());
			}
			
			if (fkAndRt.length == 2) this.fkRT = Relation.getRelation(fkAndRt[1]);
			else this.fkRT = Relation.N2One;
			
			this.fkColumn.getTable().addFkColumn(this);
			return fkColumn;
		}
		else {
			return null;
		}
	}

	public Relation getFkRT() {
		return fkRT;
	}

	public void setFkRT(Relation fkRT) {
		this.fkRT = fkRT;
	}

	public void setFkColumn(ImportColumn fkColumn) {
		this.fkColumn = fkColumn;
	}
	
	public String getFK() {
		if (StringUtil.isEmpty(FK) && this.fkColumn != null) {
			this.FK = this.fkColumn.getTable().getName() + "." + fkColumn.getName();
		}
		return this.FK;
	}
	
	public void setFK(String FK) {
		this.FK = FK.toUpperCase();
	}

	public boolean isPK() {
		return isPK;
	}

	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

    public boolean isCanFilter() {
        return canFilter;
    }

    public void setCanFilter(boolean canFilter) {
        this.canFilter = canFilter;
    }

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.GAP2).append("public static final Column ").append(getName()).append(" = ColumnFactory.createColumn(TABLE, \"").append(name.toUpperCase())
                .append("\", DataType.").append(getDataType());
        ImportColumn fc = getFkColumn();
        if (fc != null) {
            sb.append(", " + fc.getTable().getName() + ".TABLE, Relation." + fkRT.name());
        }
        else {
            sb.append(", ").append(isPK());
        }
        if (this.isCanFilter()) {
            sb.append(", ").append(this.isCanFilter());
            if (this.getEnumInfo() != null) {
                sb.append(", \""+ this.getEnumInfo().getClassName() + "\"");
            }
        }
        
        sb.append(");");
        return sb.toString();
    }
    
    public void setEnumInfo(IEnumType enumInfo) {
    	this.enumInfo = enumInfo;
    }
    
    public IEnumType getEnumInfo() {
    	return this.enumInfo;
    }
    
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

    @Override
    public int compareTo(IColumn o) {
        if (this.getName() == null) return -1;
        return this.getName().compareTo(o.getName());
    }
}
