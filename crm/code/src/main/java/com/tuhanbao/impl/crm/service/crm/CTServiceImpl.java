package com.tuhanbao.impl.crm.service.crm;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.tuhanbao.base.dataservice.CTServiceBean;
import com.tuhanbao.base.dataservice.ICTBean;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.base.dataservice.filter.page.Page;
import com.tuhanbao.base.util.db.table.CacheType;
import com.tuhanbao.base.util.db.table.Table;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.thirdapi.cache.CacheManager;
import com.tuhanbao.web.filter.MyBatisFilter;
import com.tuhanbao.web.filter.MyBatisSelector;
import com.tuhanbao.web.filter.MyBatisSelectorFilter;
import com.tuhanbao.web.filter.SelectTable;
import com.tuhanbao.web.filter.SelectorFactory;

@Transactional
public class CTServiceImpl<E extends CTServiceBean> extends ServiceImpl<E> {

	@Override
    public int delete(Filter filter) {
		throw new MyException("cannot invoke delete(filter) for a CTTable!");
    }
    
    public int delete(ICTBean ctBean, Filter filter) {
        Table table = ctBean.getTable(getTable(filter));
        if (table.getCacheType() == CacheType.CACHE_ALL) {
            throw new MyException("you canot invoke delete(filter) to a cache all table!");
        }
        return mapper.deleteByFilter(table, MyBatisFilter.getInstance(filter));
    }

    @Override
    public int deleteById(Object pkValue) {
    	throw new MyException("cannot invoke deleteById(pkValue) for a CTTable!");
    }

    public int deleteById(ICTBean ctBean, Object pkValue) {
    	Table table = ctBean.getTable(getCacheTable());
        Filter filter = new Filter(table).andFilter(table.getPK(), pkValue);
        int result = mapper.deleteByFilter(table, MyBatisFilter.getInstance(filter));
        if (table.getCacheType() == CacheType.CACHE_ALL) {
            CacheManager.delete(table, pkValue);
        }
        return result;
    }

    @Override
    public int count(MyBatisSelectorFilter selectorFilter) {
    	throw new MyException("cannot invoke count(selectorFilter) for a CTTable!");
    }
    
    public int count(ICTBean ctBean, MyBatisSelectorFilter selectorFilter) {
    	selectorFilter.refreshCTTable(ctBean);
        Table table = selectorFilter.getTable().getTable();
        Filter filter = selectorFilter.getFilter();
        List<SelectTable> children = selectorFilter.getTable().getChildren();
        //只有全缓存,而且是单表才可以使用
        if (table.getCacheType() == CacheType.CACHE_ALL &&
                (children == null || children.isEmpty()) && (filter == null || filter.isEmpty())) {
            return CacheManager.count(table);
        }
        else {
            selectorFilter.setIs4Count(true);
            int result = mapper.countByFilter(selectorFilter);
            selectorFilter.setIs4Count(false);
            return result;
        }
    }

    @Override
    public E selectById(Object pkValue) {
    	throw new MyException("cannot invoke selectById(pkValue) for a CTTable!");
    }

    @SuppressWarnings("unchecked")
	public E selectById(ICTBean ctBean, Object pkValue) {
    	Table table = ctBean.getTable(getCacheTable());
    	if (table.getCacheType() == CacheType.CACHE_ALL) {
    		return (E)CacheManager.get(table, pkValue); 
    	}
    	
    	Filter filter = new Filter().andFilter(table.getPK(), pkValue);
    	List<E> result = this.select(ctBean, filter);
    	if (result == null || result.isEmpty()) return null;
    	else return result.get(0);
    }

    protected List<E> select(MyBatisSelector selector, Filter filter) {
    	throw new MyException("cannot invoke select(selector, filter) for a CTTable!");
    }

    @SuppressWarnings("unchecked")
    protected List<E> select(ICTBean ctBean, MyBatisSelector selector, Filter filter) {
    	selector.refreshCTTable(ctBean);
    	if (selector.getTable().getCacheType() == CacheType.CACHE_ALL) {
    		if (filter == null || filter.getItems().isEmpty()) {
    			return (List<E>)((List<?>)CacheManager.get(selector.getTable().getTable()));
    		}
    		throw new MyException("you canot invoke select(selector, filter) to a cache all table!");
    	}
    	Page page = filter != null ? filter.getPage() : null;
    	MyBatisSelectorFilter selectorFilter = new MyBatisSelectorFilter(selector, filter);
    	if (page != null) {
    		page.setTotalCount(count(ctBean, selectorFilter));
    	}
    	return (List<E>)((List<? extends ServiceBean>) mapper.select(selectorFilter));
    }

    @Override
    public List<E> select(Filter filter) {
    	throw new MyException("cannot invoke select(filter) for a CTTable!");
    }

    public List<E> select(ICTBean ctBean, Filter filter) {
    	return this.select(ctBean, SelectorFactory.getTablesSelector(getTable(filter)), filter);
    }
}
