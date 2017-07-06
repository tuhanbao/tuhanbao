package com.tuhanbao.base.dataservice;

public interface IData {
    Object getKeyValue();
    
    IDataGroup<?> getDataGroup();
}
