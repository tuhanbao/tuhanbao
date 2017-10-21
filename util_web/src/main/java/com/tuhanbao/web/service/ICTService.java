package com.tuhanbao.web.service;

import java.util.List;

import com.tuhanbao.base.dataservice.ICTBean;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.web.filter.MyBatisSelectorFilter;

public interface ICTService<T extends ServiceBean> extends IService<T> {
	int delete(ICTBean ctBean, Filter filter);
	
	int deleteById(ICTBean ctBean, Object pkValue);
	
	int count(ICTBean ctBean, MyBatisSelectorFilter selectorFilter);
	
	T selectById(ICTBean ctBean, Object pkValue);
	
	List<T> select(ICTBean ctBean, Filter filter);

}