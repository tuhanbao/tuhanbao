package com.tuhanbao.base.util.db.table.dbtype;

import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;


public enum MysqlDataType implements DBDataType {
	
	TINYINT(DataType.BYTE), BIGDECIMAL(DataType.BIGDEECIMAL), BIGINT(DataType.LONG),
	INT(DataType.INT), INTEGER(DataType.INT), SMALLINT(DataType.SHORT), FLOAT(DataType.FLOAT), DOUBLE(DataType.DOUBLE),
	DECIMAL(DataType.BIGDEECIMAL), NUMERIC(DataType.BIGDEECIMAL),
	DATETIME(DataType.DATE), DATE(DataType.DATE), TIMESTAMP(DataType.DATE), TIME(DataType.DATE), 
	CHAR(DataType.STRING), VARCHAR(DataType.STRING), TEXT(DataType.TEXT), LONGTEXT(DataType.TEXT), LONGBLOB(DataType.BLOB), BLOB(DataType.BLOB);
	
	private DataType dataType;
	
	private MysqlDataType(DataType dt) {
		this.dataType = dt;
	}
	
	@Override
	public DataType getDataType() {
		return this.dataType;
	}
	
	public boolean isBlob() {
		return this == TEXT || this == BLOB;
	}
	
	public static MysqlDataType getDBDataType(String value) {
		if ("String".equalsIgnoreCase(value)) {
			return VARCHAR;
		}
		if ("boolean".equalsIgnoreCase(value)) {
			return TINYINT;
		}
		if ("long".equalsIgnoreCase(value)) {
			return BIGINT;
		}
		if ("date".equalsIgnoreCase(value)) {
			//mysql datetime是日期加时间，time只有时分秒，date只有年月日
			return DATETIME;
		}
		return MysqlDataType.valueOf(value.toUpperCase());
	}
	
	public static MysqlDataType getDBDataType(DataType dataType) {
		if (dataType == DataType.STRING) {
			return VARCHAR;
		}
		if (dataType == DataType.BOOLEAN) {
			return TINYINT;
		}
		if (dataType == DataType.LONG) {
			return BIGINT;
		}
		if (dataType == DataType.DATE) {
			return DATETIME;
		}
		return getDBDataType(dataType.name());
	}
}
