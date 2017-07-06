package com.tuhanbao.base.util.db.table.dbtype;

import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;


public enum OracleDataType implements DBDataType {
	VARCHAR(DataType.STRING), VARCHAR2(DataType.STRING), NVARCHAR2(DataType.STRING), CHAR(DataType.STRING), 
    LONG(DataType.LONG), INT(DataType.INT), INTEGER(DataType.INT), SMALLINT(DataType.SHORT), FLOAT(DataType.FLOAT),  
    NUMBER(DataType.BIGDEECIMAL), NUMERIC(DataType.BIGDEECIMAL), 
    DATE(DataType.DATE), TIMESTAMP(DataType.DATE), 
    CLOB(DataType.BLOB), BLOB(DataType.BLOB), NCLOB(DataType.BLOB), UNDEFINED(DataType.OBJECT);
	
	private DataType dataType;
	
	private OracleDataType(DataType dt) {
		this.dataType = dt;
	}
	
	@Override
	public DataType getDataType() {
		return this.dataType;
	}
	
	public boolean isBlob() {
		return this == CLOB || this == BLOB;
	}
	
	public static OracleDataType getDBDataType(String value) {
		if ("String".equalsIgnoreCase(value)) {
			return VARCHAR2;
		}
		if ("boolean".equalsIgnoreCase(value)) {
			return SMALLINT;
		}
		if ("Text".equalsIgnoreCase(value)) {
			return CLOB;
		}
		return OracleDataType.valueOf(value.toUpperCase());
	}
	
	public static OracleDataType getDBDataType(DataType dataType) {
		if (dataType == DataType.STRING) {
			return VARCHAR2;
		}
		if (dataType == DataType.BOOLEAN) {
			return SMALLINT;
		}
		if (dataType == DataType.TEXT) {
			return CLOB;
		}
		if (dataType == DataType.DATE) {
			return DATE;
		}
		return getDBDataType(dataType.name());
	}
}
