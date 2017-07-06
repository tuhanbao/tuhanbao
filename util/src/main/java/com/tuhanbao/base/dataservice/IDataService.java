package com.tuhanbao.base.dataservice;

import java.util.Collection;
import java.util.List;

import com.tuhanbao.base.dataservice.filter.Filter;

public interface IDataService<T extends IData>
{
	void add(T data);

    int delete(Filter filter);
    
    int deleteByKey(IDataGroup<?> dg, Object pkValue);

    void delete(Collection<T> list);
    
    int update(T data, Filter filter);

    int update(T data);
    
	int updateSelective(T data, Filter filter);

	int updateSelective(T data);
    
	T selectByKey(IDataGroup<?> dg, Object pkValue);

    List<T> select(Filter filter);
}