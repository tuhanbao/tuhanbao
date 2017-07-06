package com.tuhanbao.base.dataservice;

import java.util.List;

import com.tuhanbao.base.util.db.IField;

public interface IDataGroup<T extends IField> {
    String getName();
    
    List<T> getFields();

    /**
     * 反序列化时需要使用
     * 
     * @return
     */
    Class<?> getModelClassName();
}
