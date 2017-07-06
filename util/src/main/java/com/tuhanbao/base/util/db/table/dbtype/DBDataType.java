package com.tuhanbao.base.util.db.table.dbtype;

import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;

public interface DBDataType {
	DataType getDataType();
	
	String name();

	boolean isBlob();
}
