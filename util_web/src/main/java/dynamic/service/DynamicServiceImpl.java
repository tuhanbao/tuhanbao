/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.service;

import com.td.ca.base.dataservice.IData;
import com.td.ca.base.dataservice.IDataGroup;
import com.td.ca.base.dataservice.ServiceBean;
import com.td.ca.base.dataservice.cache.ICacheGroupKey;
import com.td.ca.base.dataservice.filter.Filter;
import com.td.ca.base.dataservice.filter.FilterItem;
import com.td.ca.base.dataservice.filter.IFilterItem;
import com.td.ca.base.util.cache.CacheManager;
import com.td.ca.base.util.db.table.Column;
import com.td.ca.base.util.db.table.Table;
import com.td.ca.base.util.db.table.data.AbstractDataValue;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.io.codegenarator.table.DataType;
import com.td.ca.web.db.TableManager;
import com.td.ca.web.db.dynamic.constant.config.DynamicTableConstants;
import com.td.ca.web.filter.SelectorFactory;
import com.td.ca.web.filter.TableSelector;
import com.td.ca.web.service.IService;
import com.td.ca.web.util.lock.DistributedLock;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 使用动态表服务必须自己提供CommonService实现！
 * @param <E>
 */
@Slf4j
@Transactional
@SuppressWarnings({"rawtypes", "unchecked"})
public class DynamicServiceImpl<E extends ServiceBean> implements IService<E> {
    @Autowired(required = false)
    protected CommonService commonService;

    @Autowired(required = false)
    protected DistributedLock lock;

    protected Table cachedTable = null;

    @PostConstruct
    public void registerDynamicTables() {
        if (commonService == null) {
            return;
        }
        start();
    }

    private static void start() {
        if (TableManager.getTableByClassName(DynamicTableConstants.DynamicTable.TABLE.getAliasName()) == null) {
            TableManager.register(DynamicTableConstants.DynamicTable.TABLE);
            TableManager.register(DynamicTableConstants.DynamicColumn.TABLE);
            TableManager.register(DynamicTableConstants.DynamicEnum.TABLE);
            TableManager.register(DynamicTableConstants.DynamicEnumItem.TABLE);
        }
    }

    @Override
    public void init() {
        if (commonService == null) {
            return;
        }
        Table table = this.getCacheTable();
        if (table.getCacheType().isAllCache()) {
            if (lock != null) {
                lock.lock(table.getName());
            }
            try {
                if (!CacheManager.hasCache(table)) {
                    List<E> list = commonService.select(table, null);
                    AbstractDataValue maxValue = null;
                    if (list != null) {
                        Map<String, Object> datas = new HashMap<>();
                        for (E e : list) {
                            AbstractDataValue keyValue = e.getKeyValue();
                            if (maxValue == null || keyValue.compareTo(maxValue) > 0) {
                                maxValue = keyValue;
                            }
                            datas.put(keyValue.toString(), e);
                        }
                        CacheManager.init(table, datas);
                    }
                }
            } finally {
                if (lock != null) {
                    lock.release(table.getName());
                }
            }
        }
    }

    private static boolean isNumberCol(Column col) {
        return col.getDataType() == DataType.LONG || col.getDataType() == DataType.INT;
    }

    @Override
    public void add(E record) {
        this.commonService.add(record);
    }

    @Override
    public void addRelative(E e) {
        this.commonService.addRelative(e);
    }

    @Override
    public void addRelativeBatch(Collection<? extends ServiceBean> datas) {
        this.commonService.addRelativeBatch(datas);
    }

    @Override
    public void addBatch(Collection<? extends ServiceBean> beans) {
        this.commonService.addBatch(beans);
    }

    /**
     * 删除所有数据
     */
    public void deleteAll() {
        this.delete((Filter)null);
    }

    @Override
    public void delete(Filter filter) {
        delete(getTable(filter), filter);
    }

    @Override
    public void delete(IDataGroup<?> dg, Filter filter) {
        this.commonService.delete(dg, filter);
    }

    @Override
    public void deleteByKey(IDataGroup<?> dg, Object pkValue) {
        this.deleteById((Table)dg, pkValue);
    }

    @Override
    public void deleteById(Object pkValue) {
        Table table = getCacheTable();
        deleteById(table, pkValue);
    }

    public void deleteById(Table table, Object pkValue) {
        this.commonService.deleteById(table, pkValue);
    }

    @Override
    public void delete(Collection<E> list) {
        this.commonService.delete(list);
    }

    @Override
    public void deleteRelative(E e) {
        this.commonService.deleteRelative(e);
    }

    @Override
    public void deleteAllRelative(Collection<E> list) {
        this.commonService.deleteAllRelative(list);
    }

    @Override
    public void update(E record) {
        this.commonService.update(record);
    }

    @Override
    public int update(E record, Filter filter) {
        return this.commonService.update(record, filter);
    }

    @Override
    public void updateSelective(E record) {
        this.commonService.updateSelective(record);
    }

    @Override
    public int updateSelective(E record, Filter filter) {
        return this.commonService.updateSelective(record, filter);
    }

    @Override
    public void updateRelative(E e) {
        updateRelative(e, false);
    }

    @Override
    public void updateRelative(E e, boolean isSelective) {
        this.commonService.updateRelative(e, isSelective);
    }

    @Override
    public void updateRelativeBatch(Collection<? extends ServiceBean> list, boolean isSelective) {
        this.commonService.updateRelativeBatch(list, isSelective);
    }

    @Override
    public void updateBatch(Collection<? extends ServiceBean> list, boolean isSelective) {
        this.commonService.updateBatch(list, isSelective);
    }

    @Override
    public IData selectByKey(IDataGroup<?> dg, Object pkValue) {
        return this.commonService.selectByKey(dg, pkValue);
    }

    @Override
    public int count(TableSelector selector, Filter filter) {
        return this.commonService.count(selector, filter);
    }

    @Override
    public int count(Filter filter) {
        return count(SelectorFactory.getTablesSelector(getTable(filter)), filter);
    }

    @Override
    public int count(IDataGroup<?> dg, Filter filter) {
        return count(SelectorFactory.getTablesSelector((Table)dg), filter);
    }

    @Override
    public E selectById(Object pkValue) {
        Table table = getCacheTable();
        return (E) this.selectByKey(table, pkValue);
    }

    @Override
    public List<E> select(TableSelector selector, Filter filter) {
        return this.commonService.select(selector, filter);
    }

    @Override
    public List<E> select(Filter filter) {
        return this.select(getTable(filter), filter);
    }

    @Override
    public List<E> select(IDataGroup<?> dg, Filter filter) {
        if (((Table)dg).getCacheType().isAllCache()) {
            if (filter == null) {
                return (List<E>)((List<?>) CacheManager.getAll(dg));
            } else {
                log.warn("A cache all table has select by filter : " + dg.getName());
            }
        }
        return this.select(SelectorFactory.getTablesSelector((Table)dg), filter);
    }

    @Override
    public List<E> selectByGroup(ICacheGroupKey groupKey, String keyValue) {
        return (List<E>)((List<?>)CacheManager.getAllByGroup(groupKey, keyValue));
    }

    /**
     * 获取所有数据
     * @return
     */
    public List<E> getAll() {
        return this.select(null);
    }

    @Override
    public Table getCacheTable() {
        if (cachedTable == null) {
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if (params != null && params.length > 0) {
                String modelName = ((Class<?>)params[0]).getSimpleName();
                if (!"ServiceBean".equals(modelName)) {
                    cachedTable = TableManager.getTableByClassName(modelName);
                }
            }
        }
        return cachedTable;
    }

    /**
     * 慎用，自己执行sql，需要自行处理缓存，通知，加锁
     * @param sql
     * @param args
     * @return
     */
    @Override
    public List<Map<String, Object>> excuteSql(String sql, AbstractDataValue... args) {
        return this.commonService.excuteSql(sql, args);
    }

    private Table getTable(Filter filter) {
        Table table = filter != null ? (Table) filter.getDataGroup() : null;
        if (table != null) {
            return table;
        }

        Table cacheTable = getCacheTable();
        if (cacheTable != null) {
            return cachedTable;
        }

        // 只能通过filter获取table名称了，如果还获取不到，证明业务逻辑出现问题
        if (filter != null) {
            List<IFilterItem> items = filter.getItems();
            if (items != null) {
                for (IFilterItem item : items) {
                    if (item instanceof FilterItem) {
                        FilterItem temp = (FilterItem) item;
                        if (temp.getArg1() instanceof Column) {
                            return ((Column) temp.getArg1()).getTable();
                        }
                        if (temp.getArg2() instanceof Column) {
                            return ((Column) temp.getArg2()).getTable();
                        }
                    }
                }
            }
        }

        throw new AppException("Which table, sir?");
    }

}
