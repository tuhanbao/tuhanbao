package com.tuhanbao.base.util.io.codeGenarator.tableUtil;

public interface IColumn {

	String getName();

	DataType getDataType();

	long getLength();

	String getDefaultValue();

	String getComment();
	
	boolean isPK();

}
