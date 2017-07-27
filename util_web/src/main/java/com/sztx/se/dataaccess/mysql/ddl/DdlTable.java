package com.sztx.se.dataaccess.mysql.ddl;

/**
 * 
 * @author zhihongp
 *
 */
public class DdlTable {
	
	private String ddlTableName;

	private Integer ddlDbNum;

	public DdlTable(String ddlTableName, Integer ddlDbNum) {
		this.ddlTableName = ddlTableName;
		this.ddlDbNum = ddlDbNum;
	}

	public String getDdlTableName() {
		return ddlTableName;
	}

	public void setDdlTableName(String ddlTableName) {
		this.ddlTableName = ddlTableName;
	}

	public Integer getDdlDbNum() {
		return ddlDbNum;
	}

	public void setDdlDbNum(Integer ddlDbNum) {
		this.ddlDbNum = ddlDbNum;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ddlDbNum == null) ? 0 : ddlDbNum.hashCode());
		result = prime * result + ((ddlTableName == null) ? 0 : ddlTableName.hashCode());
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
		DdlTable other = (DdlTable) obj;
		if (ddlDbNum == null) {
			if (other.ddlDbNum != null)
				return false;
		} else if (!ddlDbNum.equals(other.ddlDbNum))
			return false;
		if (ddlTableName == null) {
			if (other.ddlTableName != null)
				return false;
		} else if (!ddlTableName.equals(other.ddlTableName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DdlTable [ddlTableName=" + ddlTableName + ", ddlDbNum=" + ddlDbNum + "]";
	}
	
}
