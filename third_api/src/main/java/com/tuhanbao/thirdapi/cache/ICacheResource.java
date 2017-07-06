package com.tuhanbao.thirdapi.cache;

import java.util.List;

import com.tuhanbao.base.dataservice.IData;
import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.base.dataservice.IDataService;

public interface ICacheResource extends IDataService<IData> {

    int set(IDataGroup<?> dg, Object key, Object value);

    void release();

    int setex(IDataGroup<?> dg, Object key, int seconds, Object value);

    int del(IDataGroup<?> dg, Object key);

    int del(IData data);

    Object get(IDataGroup<?> dg, Object key);

    boolean exists(IDataGroup<?> dg, Object key);

    int len(IDataGroup<?> dg);
    
    List<Object> get(IDataGroup<?> dg);

    <T> T get(IDataGroup<?> dg, Object key, Class<T> clazz);

    boolean hasCacheDataGroup(IDataGroup<?> dg);
}
