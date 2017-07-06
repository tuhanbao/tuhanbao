package com.tuhanbao.base.util.db;

import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;

public interface IField {
    
    String getName();

    IDataGroup<?> getDataGroup();
    
    String getNameWithDataGroup();

    DataType getDataType();
}
