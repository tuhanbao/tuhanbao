package com.sztx.se.dataaccess.mysql.ddl;

/**
 * 
 * @author zhihongp
 *
 */
public class DdlConfig {

	private String table;

	private String column;

	private Integer tableNum;

	private String db;

	private Integer dbNum;

	private String isTransaction;

	public DdlConfig() {

	}

	public DdlConfig(String table, String column, Integer tableNum, String db, Integer dbNum) {
		this.table = table;
		this.column = column;
		this.tableNum = tableNum;
		this.db = db;
		this.dbNum = dbNum;
	}

	public DdlConfig(String table, String column, Integer tableNum, String db, Integer dbNum, String isTransaction) {
		this.table = table;
		this.column = column;
		this.tableNum = tableNum;
		this.db = db;
		this.dbNum = dbNum;
		this.isTransaction = isTransaction;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Integer getTableNum() {
		return tableNum;
	}

	public void setTableNum(Integer tableNum) {
		this.tableNum = tableNum;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public Integer getDbNum() {
		return dbNum;
	}

	public void setDbNum(Integer dbNum) {
		this.dbNum = dbNum;
	}

	public String getIsTransaction() {
		return isTransaction;
	}

	public void setIsTransaction(String isTransaction) {
		this.isTransaction = isTransaction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((db == null) ? 0 : db.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DdlConfig other = (DdlConfig) obj;
		if (db == null) {
			if (other.db != null)
				return false;
		} else if (!db.equals(other.db))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DdlConfig [table=" + table + ", column=" + column + ", tableNum=" + tableNum + ", db=" + db + ", dbNum=" + dbNum + ", isTransaction="
				+ isTransaction + "]";
	}

}
