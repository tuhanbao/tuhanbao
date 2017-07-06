package com.hhnz.impl.cfcrm.service.fz;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hhnz.api.cfcrm.constants.TableConstants;
import com.hhnz.impl.cfcrm.mapper.fz.FzMapper;
import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.base.dataservice.filter.Filter;
import com.tuhanbao.base.dataservice.filter.FilterItem;
import com.tuhanbao.base.dataservice.filter.IFilterItem;
import com.tuhanbao.base.dataservice.filter.page.Page;
import com.tuhanbao.base.util.db.table.CacheType;
import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.db.table.Table;
import com.tuhanbao.base.util.db.table.data.DataValue;
import com.tuhanbao.base.util.db.table.data.LongValue;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.ArrayUtil;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.thirdapi.cache.CacheManager;
import com.tuhanbao.web.db.MyBatisSqlSentence;
import com.tuhanbao.web.filter.MyBatisFilter;
import com.tuhanbao.web.filter.MyBatisSelector;
import com.tuhanbao.web.filter.MyBatisSelectorFilter;
import com.tuhanbao.web.filter.SelectTable;
import com.tuhanbao.web.filter.SelectorFactory;
import com.tuhanbao.web.service.IService;

@Transactional
public class ServiceImpl<E extends ServiceBean> implements IService<E> {
    @Autowired
    protected FzMapper<ServiceBean> mapper;

    private Table cachedTable = null;

    @Override
    public void add(E record) {
        Table table = record.getTable();
        String seqName = table.getSeqName();
        if (!StringUtil.isEmpty(seqName) && record.getKeyValue() == null) {
            Long id = mapper.nextVal(seqName).getId();
            record.setValue(record.getTable().getPK(), LongValue.valueOf(id));
        }
        
        mapper.insert(record);
        if (table.getCacheType() == CacheType.CACHE_ALL) {
            CacheManager.save(record);
        }
    }

    @Override
    public int delete(Filter filter) {
        Table table = getTable(filter);
        if (table.getCacheType() == CacheType.CACHE_ALL) {
            throw new MyException("you canot invoke delete(filter) to a cache all table!");
        }
        return mapper.deleteByFilter(table, MyBatisFilter.getInstance(filter));
    }

    @Override
    public int deleteById(Object pkValue) {
        Table table = getCacheTable();
        Filter filter = new Filter(table).andFilter(table.getPK(), pkValue);
        int result = mapper.deleteByFilter(table, filter);
        if (table.getCacheType() == CacheType.CACHE_ALL) {
            CacheManager.delete(table, pkValue);
        }
        return result;
    }

    @Override
    public int update(E record, Filter filter) {
        Table table = record.getTable();
        if (table.getCacheType() == CacheType.CACHE_ALL) {
            throw new MyException("you canot invoke update(record, filter) to a cache all table!");
        }
        int result = mapper.updateByFilter(record, filter);
        return result;
    }

    @Override
    public int update(E record) {
        Table table = record.getTable();
        int result = mapper.updateByFilter(record, new Filter().andFilter(record.getTable().getPK(), record.getKeyValue()));
        if (table.getCacheType() == CacheType.CACHE_ALL) {
            CacheManager.save(record);
        }
        return result;
    }

    @Override
    public int updateSelective(E record, Filter filter) {
        int result = mapper.updateSelective(record, filter);
        if (record.getTable().getCacheType() == CacheType.CACHE_ALL) {
            throw new MyException("you canot invoke updateSelective(record, filter) to a cache all table!");
        }
        return result;
    }

    @Override
    public int updateSelective(E record) {
        DataValue keyValue = record.getKeyValue();
        if (keyValue == null) throw new MyException("which record i will update?? " + record);
        int result = mapper.updateSelective(record, new Filter().andFilter(record.getTable().getPK(), keyValue));
        if (record.getTable().getCacheType() == CacheType.CACHE_ALL) {
            CacheManager.saveSelective(record);
        }
        return result;
    }

    @Override
    public int count(MyBatisSelectorFilter selectorFilter) {
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

    @SuppressWarnings("unchecked")
    @Override
    public E selectById(Object pkValue) {
        Table table = getCacheTable();
        if (table.getCacheType() == CacheType.CACHE_ALL) {
            return (E)CacheManager.get(table, pkValue); 
        }
        
        Filter filter = new Filter().andFilter(getCacheTable().getPK(), pkValue);
        List<E> result = this.select(filter);
        if (result == null || result.isEmpty()) return null;
        else return result.get(0);
    }

    @SuppressWarnings("unchecked")
    protected List<E> select(MyBatisSelector selector, Filter filter) {
        if (selector.getTable().getCacheType() == CacheType.CACHE_ALL) {
            if (filter == null || filter.getItems().isEmpty()) {
                return (List<E>)((List<?>)CacheManager.get(selector.getTable().getTable()));
            }
            throw new MyException("you canot invoke select(selector, filter) to a cache all table!");
        }
        Page page = filter != null ? filter.getPage() : null;
        MyBatisSelectorFilter selectorFilter = new MyBatisSelectorFilter(selector, filter);
        if (page != null) {
            page.setTotalCount(count(selectorFilter));
        }
        return (List<E>) mapper.select(selectorFilter);
    }

    @Override
    public List<E> select(Filter filter) {
        return this.select(SelectorFactory.getTablesSelector(getTable(filter)), filter);
    }

    private Table getTable(Filter filter) {
        Table table = filter != null ? (Table)filter.getDataGroup() : null;
        if (table != null) return table;
        
        Table cacheTable = getCacheTable();
        if (cacheTable != null) return cachedTable;
        
        //只能通过filter获取table名称了，如果还获取不到，证明业务逻辑出现问题
        List<IFilterItem> items = filter.getItems();
        if (items != null) {
            for (IFilterItem item : items) {
                if (item instanceof FilterItem) {
                    FilterItem temp = (FilterItem) item;
                    if (temp.getArg1() instanceof Column) {
                        return ((Column)temp.getArg1()).getTable();
                    }
                    if (temp.getArg2() instanceof Column) {
                        return ((Column)temp.getArg1()).getTable();
                    }
                }
            }
        }

        throw new MyException("Which table, sir?");
    }

    protected Table getCacheTable() {
        if (cachedTable == null) {
            Type genType = getClass().getGenericSuperclass();   
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
            if (params != null && params.length > 0) {
                String modelName = ((Class<?>)params[0]).getSimpleName();
                if (!"ServiceBean".equals(modelName)) {
                    cachedTable = TableConstants.getTableByClassName(modelName);
                }
            }
        }
        return cachedTable;
    }

    public void delete(Collection<E> list) {
        if(list != null && !list.isEmpty()) {
            Map<Table, List<Object>> ids = new HashMap<Table, List<Object>>();
            for(E e : list) {
                add(ids, e);
            }
            
            for(Map.Entry<Table, List<Object>> entry : ids.entrySet()) {
                Table table = entry.getKey();
                this.delete(new Filter(table).andFilter(table.getPK(), entry.getValue()));
            }
        }
    }

    public void deleteAllRelative(Collection<E> list) {
        if(list != null && !list.isEmpty()){
            Map<Table, List<Object>> ids = new HashMap<Table, List<Object>>();
            for(ServiceBean e : list){
                addRelative(ids, e);
            }
            
            for(Map.Entry<Table, List<Object>> entry : ids.entrySet()) {
                Table table = entry.getKey();
                this.delete(new Filter(table).andFilter(table.getPK(), entry.getValue()));
            }
        }
        
    }

    private void add(Map<Table, List<Object>> ids, ServiceBean e) {
        Table table = e.getTable();
        if (!ids.containsKey(table)) {
            ids.put(table, new ArrayList<Object>());
        }
        ids.get(table).add(e.getKeyValue().getValue4DB());
    }

    private void addRelative(Map<Table, List<Object>> ids, ServiceBean e) {
        Table table = e.getTable();
        if (!ids.containsKey(table)) {
            ids.put(table, new ArrayList<Object>());
        }
        ids.get(table).add(e.getKeyValue().getValue4DB());
        
        Map<String, List<ServiceBean>> fkBeans = e.getFkBeans();
        if (fkBeans != null) {
            for (List<ServiceBean> entry : fkBeans.values())  {
                if (entry != null) {
                    for (ServiceBean sb : entry) {
                        addRelative(ids, sb);
                    }
                }
            }
        }
    }

    public List<Map<String, Object>> excuteSql(String sql, DataValue... args) {
        List<Map<String, Object>> result = this.mapper.excuteSql(new MyBatisSqlSentence(sql, args));
        ArrayUtil.removeNullItem(result);
        return result;
    }

    @Override
    public int deleteByKey(IDataGroup<?> dg, Object pkValue) {
        return this.deleteById(pkValue);
    }

    @Override
    public E selectByKey(IDataGroup<?> dg, Object pkValue) {
        return this.selectById(pkValue);
    }
}
