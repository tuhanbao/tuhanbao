package {package};

import {base_project_head}.base.Constants;
import {base_project_head}.base.dataservice.CreateAndModifyTimeRecorder;
import {base_project_head}.base.dataservice.IData;
import {base_project_head}.base.dataservice.IDataGroup;
import {base_project_head}.base.dataservice.ServiceBean;
import {base_project_head}.base.dataservice.cache.CacheEntry;
import {base_project_head}.base.dataservice.cache.Event;
import {base_project_head}.base.dataservice.cache.ICacheGroupKey;
import {base_project_head}.base.dataservice.cache.ICacheService;
import {base_project_head}.base.dataservice.filter.Filter;
import {base_project_head}.base.dataservice.filter.FilterItem;
import {base_project_head}.base.dataservice.filter.IFilterItem;
import {base_project_head}.base.dataservice.filter.page.Page;
import {base_project_head}.base.util.cache.CacheManager;
import {base_project_head}.base.util.db.table.CacheType;
import {base_project_head}.base.util.db.table.Column;
import {base_project_head}.base.util.db.table.DataValueFactory;
import {base_project_head}.base.util.db.table.MathFunc;
import {base_project_head}.base.util.db.table.ServiceBeanKeyManager;
import {base_project_head}.base.util.db.table.Table;
import {base_project_head}.base.util.db.table.data.AbstractDataValue;
import {base_project_head}.base.util.db.table.data.LongValue;
import {base_project_head}.base.util.exception.AppException;
import {base_project_head}.base.util.objutil.ArrayUtil;
import {base_project_head}.base.util.objutil.StringUtil;
import {base_project_head}.base.util.objutil.TimeUtil;
import {base_project_head}.base.util.snowflake.SnowFlakeUtil15;
import {mapper};
import {base_project_head}.web.db.CacheSyncThreadManager;
import {base_project_head}.web.db.MyBatisSqlSentence;
import {base_project_head}.web.db.SelectorResultParser;
import {base_project_head}.web.db.TableManager;
import {base_project_head}.web.filter.MyBatisFilter;
import {base_project_head}.web.filter.MyBatisSelectorFilter;
import {base_project_head}.web.filter.SelectTable;
import {base_project_head}.web.filter.SelectorFactory;
import {base_project_head}.web.filter.TableSelector;
import {base_project_head}.web.service.IService;
import {base_project_head}.web.util.validator.ValidatorManager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * 只要有缓存功能，都不能使用filter进行增改删，但是只要不是AYNC_WRITE，可以使用filter直接入库查询
 * warning : 目前缓存管理器不支持事务，请谨慎使用.
 *
 * @author wWX497819
 *
 */
@Transactional
@Slf4j
public class ServiceImpl<E extends ServiceBean> implements IService<E> {
    @Autowired
    protected {mapper.name}<ServiceBean> mapper;

    protected Table cachedTable = null;

    @Override
    public void init() {
        Table table = this.getCacheTable();
        if (table.getCacheType().isAllCache()) {
            MyBatisSelectorFilter selectorFilter = getInitFilter();
            List<E> list = convert(selectorFilter, mapper.select(selectorFilter));
            Map<String, Object> datas = new HashMap<>();
            for (E e : list) {
                AbstractDataValue keyValue = e.getKeyValue();
                datas.put(keyValue.toString(), e);
            }
            if (table.getCacheType() == CacheType.CACHE_ALL_LOCAL) {
                CacheManager.getLocalCacheManagerImpl().init(table, datas);
            } else {
                CacheManager.init(table, datas);
            }
        }
    }

    protected MyBatisSelectorFilter getInitFilter() {
        Table table = this.getCacheTable();
        return new MyBatisSelectorFilter(SelectorFactory.getTablesSelector(table), null);
    }

    @Override
    public void add(E record) {
        this.addServiceBean(record);
    }

    protected void addServiceBean(ServiceBean record) {
        checkAndInitRecord(record);
        addServiceBean(record, record.getMoTable());
        notifyAdd(record);
    }

    protected void addServiceBean(ServiceBean record, Table table) {
        if (table.getCacheType() == CacheType.AYNC_WRITE) {
            if (record.getKeyValue() == null) {
                record.setKeyValue(this.nextId(table));
            }
            CacheManager.add(record);
            CacheSyncThreadManager.add(record, (ICacheService) this);
        } else {
            add2DBInner(record);
            if (table.getCacheType() == CacheType.CACHE_ALL_LOCAL) {
                CacheManager.getLocalCacheManagerImpl().add(record);
            } else if (table.getCacheType() != CacheType.NOT_CACHE) {
                CacheManager.add(record);
            }
        }
    }

    @Override
    public void addRelative(E e) {
        if (this.getCacheTable().isAutoIncrement()) {
            this.addRelativeServiceBeans(e);
        } else {
            this.addBatchInner(getRelativeServiceBeans(e));
        }
    }

    private void addRelativeServiceBeans(ServiceBean e) {
        Map<String, List<ServiceBean>> fkBeans = e.getFkBeans();
        if (fkBeans != null) {
            // 关联对象有先后顺序，必须先添加谁再添加谁
            Map<String, Column> addFirst = new HashMap<>();
            Map<String, Column> addLast = new HashMap<>();
            sortServiceBeanByFk(e, fkBeans, addFirst, addLast);

            // 添加前置对象
            addFrontBeans(e, addFirst, fkBeans, null);
            this.addServiceBean(e);
            // 添加后置对象
            addBackBeans(e, addLast, fkBeans, null);
        } else {
            this.addServiceBean(e);
        }
    }

    private List<ServiceBean> getRelativeServiceBeans(ServiceBean e) {
        Map<String, List<ServiceBean>> fkBeans = e.getFkBeans();
        List<ServiceBean> batchList = new ArrayList<>();
        if (fkBeans != null) {
            // 关联对象有先后顺序，必须先添加谁再添加谁
            Map<String, Column> frontBeanMap = new HashMap<>();
            Map<String, Column> backBeanMap = new HashMap<>();
            sortServiceBeanByFk(e, fkBeans, frontBeanMap, backBeanMap);

            // 获取前置对象
            addFrontBeans(e, frontBeanMap, fkBeans, batchList);

            this.checkAndInitRecord(e);
            batchList.add(e);

            // 获取后置对象
            addBackBeans(e, backBeanMap, fkBeans, batchList);
        } else {
            this.checkAndInitRecord(e);
            batchList.add(e);
        }
        return batchList;
    }

    private void addFrontBeans(ServiceBean e, Map<String, Column> addFirst, Map<String, List<ServiceBean>> fkBeans, List<ServiceBean> batchList) throws AppException {
        for (Entry<String, Column> item : addFirst.entrySet()) {
            String key = item.getKey();
            List<ServiceBean> value = fkBeans.get(key);
            // 先加的对象不可能是多个，否则会有问题
            if (value.size() != 1) {
                throw new AppException("table ralation error, cannot add first a record is N:1!");
            }

            ServiceBean sb = value.get(0);
            if (batchList != null) {
                batchList.addAll(this.getRelativeServiceBeans(sb));
            } else {
                this.addRelativeServiceBeans(sb);
            }
            // 并不强制真实具有主外键关系
            if (e.getValue(item.getValue()) == null) {
                e.setValue(item.getValue(), sb.getKeyValue());
            }
        }
    }

    private void addBackBeans(ServiceBean e, Map<String, Column> backBeanMap, Map<String, List<ServiceBean>> fkBeans, List<ServiceBean> batchList) {
        for (Entry<String, Column> item : backBeanMap.entrySet()) {
            String key = item.getKey();
            List<ServiceBean> value = fkBeans.get(key);

            for (ServiceBean sb : value) {
                //并不强制真实具有主外键关系
                if (sb.getValue(item.getValue()) == null) {
                    sb.setValue(item.getValue(), e.getKeyValue());
                }
                if (batchList != null) {
                    batchList.addAll(this.getRelativeServiceBeans(sb));
                } else {
                    this.addRelativeServiceBeans(sb);
                }
            }
        }
    }

    private static void sortServiceBeanByFk(ServiceBean e, Map<String, List<ServiceBean>> fkBeans, Map<String, Column> addFirst, Map<String, Column> addLast) {
        for (Entry<String, List<ServiceBean>> entry : fkBeans.entrySet()) {
            List<ServiceBean> value = entry.getValue();
            String key = entry.getKey();
            Table table = e.getMoTable();
            if (ArrayUtil.isEmpty(value)) {
                continue;
            }

            Column col = ServiceBeanKeyManager.getColumn(table, key);
            Table fkTable = value.get(0).getMoTable();
            if (col == null) {
                //这种情况就属于乱写外键key了
                log.warn("hava you write a bad fk key name for " + e + " : " + key);
                col = table.findFKColumn(fkTable);

                if (col == null) {
                    continue;
                }
            }

            if (col.getTable() == table) {
                //一种特殊情况，自联表，此时仍然不知道是bean依赖fkbean还是fkbean依赖bean
                if (col.getTable() == fkTable && !ServiceBeanKeyManager.isSingleKey(table, key)) {
                    // 这里的column实际上是一个clone的，需要换成真正的
                    addLast.put(key, table.getColumn(col.getIndex()));
                } else {
                    addFirst.put(key, col);
                }
            } else {
                addLast.put(key, col);
            }
        }
    }

    /**
     * addRelativeBatch其实无法实现真正的批量，因为子表需要副表的外键id，add存在顺序依赖
     */
    @Override
    public void addRelativeBatch(Collection<? extends ServiceBean> datas) {
        if (datas == null) {
            return;
        }
        List<ServiceBean> batchList = new ArrayList<>();
        datas.forEach(item -> batchList.addAll(getRelativeServiceBeans(item)));
        this.addBatchInner(batchList);
    }

    /**
     * add方法有mybatis的keyProperty支持，可以回填id，但是批量执行均无法回填id
     * @param beans
     */
    @Override
    public void addBatch(Collection<? extends ServiceBean> beans) {
        for (ServiceBean record : beans) {
            checkAndInitRecord(record);
        }
        addBatchInner(beans);
    }

    private void addBatchInner(Collection<? extends ServiceBean> beans) {
        // 这一步不能报错，因为有些表设置了缓存，无法回滚
        List<ServiceBean> batchAddList = new ArrayList<>();
        for (ServiceBean record : beans) {
            Table table = record.getMoTable();
            if (table.getCacheType() == CacheType.AYNC_WRITE) {
                CacheManager.add(record);
                CacheSyncThreadManager.add(record, (ICacheService) this);
            } else {
                batchAddList.add(record);
            }
        }

        if (!batchAddList.isEmpty()) {
            // 批量新增，效率更高
            this.mapper.excuteBatch(batchAddList.stream().map(item -> new CacheEntry<>(item, Event.ADD)).collect(Collectors.toList()));
            for (ServiceBean record : batchAddList) {
                Table table = record.getMoTable();
                if (table.getCacheType() == CacheType.CACHE_ALL_LOCAL) {
                    CacheManager.getLocalCacheManagerImpl().add(record);
                } else if (table.getCacheType() != CacheType.NOT_CACHE) {
                    CacheManager.add(record);
                }
            }
        }

        // 通知
        notifyAdd(beans);
    }

    /**
     * 异步写的service必须实现此方法
     */
    protected long nextId(Table table) {
        if (table != null && !StringUtil.isEmpty(table.getSeqName())) {
            return mapper.nextVal(table.getSeqName()).getId();
        } else {
            return SnowFlakeUtil15.nextId();
        }
    }

    private void checkAndInitRecord(ServiceBean record) {
        Table table = record.getMoTable();
        if (!table.isAutoIncrement() && record.getKeyValue() == null) {
            long id = nextId(table);
            record.setValue(table.getPK(), LongValue.valueOf(id));
        }

        for (Column col : table.getColumns()) {
            ValidatorManager.check(record, col, this);
        }

        if (record instanceof CreateAndModifyTimeRecorder) {
            long now = TimeUtil.now();
            ((CreateAndModifyTimeRecorder) record).setGmtCreated(now);
            ((CreateAndModifyTimeRecorder) record).setGmtModified(now);
        } else if (table.isDynamic()) {
            record.setValue(table.getColumn(Constants.CREATE_TIME_COL_NAME), LongValue.valueOf(TimeUtil.now()));
        }
    }

    /**
     * 删除所有数据
     */
    public void deleteAll() {
        this.delete((Filter) null);
        Table table = this.getCacheTable();
        if (this.cachedTable.getCacheType() == CacheType.CACHE_ALL_LOCAL) {
            CacheManager.getLocalCacheManagerImpl().deleteAll(table);
        } else if (table.getCacheType() != CacheType.NOT_CACHE) {
            CacheManager.deleteAll(table);
            if (table.getCacheType() == CacheType.AYNC_WRITE) {
                CacheSyncThreadManager.deleteAll(table, (ICacheService) this);
            }
        }
    }

    @Override
    public void delete(Filter filter) {
        delete(getTable(filter), filter);
    }

    @Override
    public void delete(IDataGroup<?> dg, Filter filter) {
        // 缓存表不能使用filter删除，不然不知道影响到多少缓存
        Table table = (Table) dg;
        deleteByFilterHandler(table);

        List<E> deleteList = null;
        if (needNotify(table)) {
            deleteList = this.select(dg, filter);
        }
        mapper.deleteByFilter(table, MyBatisFilter.getInstance(filter));

        if (deleteList != null) {
            notifyDelete(deleteList);
        }
    }

    protected void deleteByFilterHandler(Table table) {
        if (table.getCacheType() != CacheType.NOT_CACHE || needNotify(table)) {
            throw new AppException("cache table not support this method.");
        }
    }

    /**
     * 级联删除不支持通知，不支持异步或缓存类型的表
     */
    public int deleteRelative(TableSelector selector, Filter filter) {
        // 缓存表不能使用filter删除，不然不知道影响到多少缓存
        for (SelectTable table : selector.getTableList()) {
            deleteByFilterHandler(table.getTable());
        }

        MyBatisSelectorFilter selectorFilter = new MyBatisSelectorFilter(selector, filter);
        return mapper.deleteRelative(selectorFilter);
    }

    @Override
    public void deleteByKey(IDataGroup<?> dg, Object pkValue) {
        this.deleteById((Table) dg, pkValue);
    }

    @Override
    public void deleteById(Object pkValue) {
        Table table = getCacheTable();
        deleteById(table, pkValue);
    }

    public void deleteById(Table table, Object pkValue) {
        if (table.getCacheType() == CacheType.AYNC_WRITE) {
            CacheManager.delete(table, pkValue.toString());
            deleteBySyncThread(table, pkValue);
        } else {
            Filter filter = new Filter(table).andFilter(table.getPK(), pkValue);
            mapper.deleteByFilter(table, MyBatisFilter.getInstance(filter));
            if (table.getCacheType() == CacheType.CACHE_ALL_LOCAL) {
                CacheManager.getLocalCacheManagerImpl().delete(table, pkValue.toString());
            } else if (table.getCacheType() != CacheType.NOT_CACHE) {
                CacheManager.delete(table, pkValue.toString());
            }
        }

        notifyDelete(table, pkValue);
    }

    @Override
    public void delete(Collection<E> list) {
        if (list != null && !list.isEmpty()) {
            this.deleteBatch(list);
        }
    }

    @Override
    public void deleteRelative(E e) {
        List<ServiceBean> list = new ArrayList<>();
        getAllRelativeBean(list, e);
        if (list.size() >= 2) {
            this.deleteBatch(list);
        } else {
            this.deleteById(e.getTable(), e.getKeyValue());
        }
    }

    @Override
    public void deleteAllRelative(Collection<E> list) {
        if (list != null && !list.isEmpty()) {
            List<ServiceBean> deleteList = new ArrayList<>();
            for (ServiceBean e : list) {
                getAllRelativeBean(deleteList, e);
            }
            this.deleteBatch(deleteList);
        }
    }

    private void deleteBatch(Collection<? extends ServiceBean> list) {
        List<ServiceBean> batchDeleteList = new ArrayList<>();
        for (ServiceBean serviceBean : list) {
            Table table = serviceBean.getMoTable();
            if (table.getCacheType() == CacheType.AYNC_WRITE) {
                deleteBySyncThread(table, serviceBean.getKeyValue());
            } else {
                batchDeleteList.add(serviceBean);
            }
        }

        if (!batchDeleteList.isEmpty()) {
            // 批量处理，效率更高
            this.mapper.excuteBatch(batchDeleteList.stream().map(item ->
                    new CacheEntry<>(item, Event.DELETE)).collect(Collectors.toList()));
            for (ServiceBean record : batchDeleteList) {
                Table table = record.getMoTable();
                if (table.getCacheType() == CacheType.CACHE_ALL_LOCAL) {
                    CacheManager.getLocalCacheManagerImpl().delete(table, record.getKeyValue().toString());
                } else if (table.getCacheType() != CacheType.NOT_CACHE) {
                    CacheManager.delete(table, record.getKeyValue().toString());
                }
            }
        }

        // 通知
        notifyDelete(list);
    }

    private void deleteBySyncThread(Table table, Object pk) {
        ServiceBean item = new ServiceBean(table);
        item.setValue(table.getPK(), DataValueFactory.toDataValue(table.getPK().getDataType(), pk));
        CacheSyncThreadManager.delete(item, (ICacheService) this);
    }

    @Override
    public void update(E record) {
        updateServiceBean(record, false, null);
    }

    @Override
    public int update(E record, Filter filter) {
        if (filter == null) {
            filter = new Filter();
        }
        return this.updateServiceBean(record, false, filter);
    }

    @Override
    public void updateSelective(E record) {
        updateServiceBean(record, true, null);
    }

    /**
     * 返回更新的记录条数，如果使用了异步更新或者全缓存，返回结果无参考意义
     *
     * 使用filter更新，有些字段校验会失效： 比如唯一性校验， 如果更新的字段包含唯一性字段，方法会报错。
     */
    @Override
    public int updateSelective(E record, Filter filter) {
        if (filter == null) {
            filter = new Filter();
        }
        return updateServiceBean(record, true, filter);
    }

    /**
     * 需要抽出来给updateRelative用
     *
     * 如果是使用filter进行更新，暂不会通知
     * @return 返回更新的记录条数，如果使用了异步更新或者全缓存，返回结果无参考意义
     */
    protected int updateServiceBean(ServiceBean record, boolean isSelective, Filter filter) {
        return this.updateServiceBean(record, record.getTable(), isSelective, filter);
    }

    protected int updateServiceBean(ServiceBean record, Table table, boolean isSelective, Filter filter) {
        if (isSelective && record.isEmpty()) {
            return 0;
        }

        int result = 0;
        if (filter == null) {
            AbstractDataValue keyValue = record.getKeyValue();
            if (keyValue == null) {
                throw new AppException("which record i will update?? " + record);
            }
        } else {
            updateByFilterHandler(table);
        }

        checkAndSetModifyData4Update(record, isSelective);
        if (filter == null) {
            if (table.getCacheType() == CacheType.AYNC_WRITE) {
                CacheManager.update(record, isSelective);
                CacheSyncThreadManager.update(record, (ICacheService) this, isSelective);
            } else {
                filter = new Filter().andFilter(table.getPK(), record.getKeyValue());
                result = update2DBInner(record, table, filter, isSelective);
                if (table.getCacheType() == CacheType.CACHE_ALL_LOCAL) {
                    CacheManager.getLocalCacheManagerImpl().update(record, isSelective);
                } else if (table.getCacheType() != CacheType.NOT_CACHE) {
                    CacheManager.update(record, isSelective);
                }
            }
            notifyUpdate(record, isSelective);
        } else {
            result = update2DBInner(record, table, filter, isSelective);
        }

        return result;
    }

    protected void updateByFilterHandler(Table table) {
        if (table.getCacheType() != CacheType.NOT_CACHE) {
            throw new AppException("use update(E record) instead.");
        }
    }

    protected int update2DBInner(ServiceBean e, Table table, Filter filter, boolean isSelective) {
        if (table == null) {
            table = e.getTable();
        }
        String tableAsName = table.getAsName();
        if (isSelective) {
            return mapper.updateSelective(e, tableAsName, MyBatisFilter.getInstance(filter));
        } else {
            return mapper.updateByFilter(e, tableAsName, MyBatisFilter.getInstance(filter));
        }
    }

    @Override
    public void updateRelative(E e) {
        updateRelative(e, false);
    }

    @Override
    public void updateRelative(E e, boolean isSelective) {
        updateRelativeInner(e, isSelective);
    }

    /**
     * 通过filter连表更新
     *
     * 级联更新不支持通知，不支持异步或缓存类型的表。
     * 如果需要上述功能，先使用级联查询，再进行数据级的更新。
     */
    public void updateRelative(TableSelector selector, Filter filter, ServiceBean... models) {
        if (models == null) {
            return;
        }

        if (filter == null) {
            throw new AppException("this method is for update by filter, filter can not be null.");
        }

        for (SelectTable table : selector.getTableList()) {
            updateByFilterHandler(table.getTable());
        }

        for (ServiceBean item : models) {
            checkAndSetModifyData4Update(item, true);
        }

        this.mapper.updateRelative(selector, MyBatisFilter.getInstance(filter), models);
    }

    private void updateRelativeInner(ServiceBean e, boolean isSelective) {
        List<ServiceBean> list = new ArrayList<>();
        getAllRelativeBean(list, e);

        if (list.size() >= 2) {
            this.updateBatch(list, isSelective);
        } else {
            this.updateServiceBean(e, isSelective, null);
        }
    }

    @Override
    public void updateRelativeBatch(Collection<? extends ServiceBean> beans, boolean isSelective) {
        if (beans == null || beans.isEmpty()) {
            return;
        }

        List<ServiceBean> list = new ArrayList<>();
        for (ServiceBean bean : beans) {
            getAllRelativeBean(list, bean);
        }

        if (list.size() >= 2) {
            this.updateBatch(list, isSelective);
        } else {
            this.updateServiceBean(list.get(0), isSelective, null);
        }
    }

    @Override
    public void updateBatch(Collection<? extends ServiceBean> list, boolean isSelective) {
        List<ServiceBean> batchUpdateList = new ArrayList<>();
        for (ServiceBean record : list) {
            if (isSelective && record.isEmpty()) {
                continue;
            }
            checkAndSetModifyData4Update(record, isSelective);

            Table table = record.getTable();
            if (table.getCacheType() == CacheType.AYNC_WRITE) {
                CacheManager.update(record, isSelective);
                CacheSyncThreadManager.update(record, (ICacheService) this, isSelective);
            } else {
                batchUpdateList.add(record);
            }
        }

        // 批量更新更高效
        if (!batchUpdateList.isEmpty()) {
            this.mapper.excuteBatch(batchUpdateList.stream().map(item -> new CacheEntry<>(item, isSelective ? Event.UPDATE_SELECTIVE : Event.UPDATE)).collect(Collectors.toList()));
            for (ServiceBean record : batchUpdateList) {
                Table table = record.getTable();
                if (table.getCacheType() == CacheType.CACHE_ALL_LOCAL) {
                    CacheManager.getLocalCacheManagerImpl().update(record, isSelective);
                } else if (table.getCacheType() != CacheType.NOT_CACHE) {
                    CacheManager.update(record, isSelective);
                }
            }
        }

        notifyUpdate(list, isSelective);
    }

    private void checkAndSetModifyData4Update(ServiceBean record, boolean isSelective) {
        Map<Column, AbstractDataValue> properties = record.getProperties();
        Table table = record.getMoTable();
        for (Column col : table.getColumns()) {
            if (!isSelective || properties.containsKey(col)) {
                ValidatorManager.check(record, col, this);
            }
        }

        if (record instanceof CreateAndModifyTimeRecorder) {
            ((CreateAndModifyTimeRecorder) record).setGmtModified(TimeUtil.now());
        } else if (table.isDynamic()) {
            record.setValue(table.getColumn(Constants.MODIFY_TIME_COL_NAME), LongValue.valueOf(TimeUtil.now()));
        }
    }

    @Override
    public int count(Filter filter) {
        return count(SelectorFactory.getTablesSelector(getTable(filter)), filter);
    }

    @Override
    public int count(IDataGroup<?> dg, Filter filter) {
        return count(SelectorFactory.getTablesSelector((Table) dg), filter);
    }

    @Override
    public int count(TableSelector selector, Filter filter) {
        Table table = selector.getTable().getTable();
        List<SelectTable> children = selector.getTable().getChildren();
        // 只有异步写缓存的count是读缓存的,而且是单表才可以使用
        if (table.getCacheType().isAllCache() && (children == null || children.isEmpty())) {
            if (filter == null || filter.isEmpty()) {
                return CacheManager.count(table);
            }
        }
        if (table.getCacheType() == CacheType.AYNC_WRITE) {
            throw new AppException("async_write table not support count by filter or join table.");
        }

        MyBatisSelectorFilter selectorFilter = new MyBatisSelectorFilter(selector, filter);
        int result = mapper.countByFilter(selectorFilter);
        return result;
    }

    public long max(Column column, Filter filter) {
        return executeMathFunc(MathFunc.MAX, column, filter);
    }

    public long executeMathFunc(MathFunc func, Column column, Filter filter) {
        Table table = getTable(filter);
        TableSelector selector = SelectorFactory.getTablesSelector(table);
        return executeMathFunc(func, column, selector, filter);
    }

    public long executeMathFunc(MathFunc func, Column column, TableSelector selector, Filter filter) {
        if (selector.getTable().getCacheType() == CacheType.AYNC_WRITE) {
            throw new AppException("not support this method for cache table.");
        }
        Object result = mapper.executeMathFuncByFilter(func, column, selector, MyBatisFilter.getInstance(filter));
        return result == null ? 0 : Long.valueOf(result.toString());
    }

    @Override
    public E selectById(Object pkValue) {
        Table table = getCacheTable();
        return (E) this.selectByKey(table, pkValue);
    }

    @Override
    public IData selectByKey(IDataGroup<?> dg, Object pkValue) {
        Table table = (Table) dg;
        if (table.getCacheType() == CacheType.CACHE_ALL_LOCAL) {
            E e = (E) CacheManager.getLocalCacheManagerImpl().get(table, pkValue.toString());
            return e;
        } else if (table.getCacheType() != CacheType.NOT_CACHE) {
            E e = (E) CacheManager.get(table, pkValue.toString());
            // 非全缓存的如果没查到，到数据库去查
            if (e != null || table.getCacheType().isAllCache()) {
                return e;
            }
        }

        Filter filter = new Filter().andFilter(table.getPK(), pkValue);
        List<E> result = this.select(dg, filter);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<E> select(TableSelector selector, Filter filter) {
        if (selector.getTable().getCacheType() == CacheType.AYNC_WRITE) {
            throw new AppException("aync_write table not support this method.");
        }
        Page page = filter != null ? filter.getPage() : null;
        MyBatisSelectorFilter selectorFilter = new MyBatisSelectorFilter(selector, filter);
        if (page != null && page.isNeedCountTotal()) {
            page.setTotalCount(count(selector, filter));
        }
        return convert(selectorFilter, mapper.select(selectorFilter));
    }

    @Override
    public List<E> select(Filter filter) {
        return this.select(getTable(filter), filter);
    }

    @Override
    public List<E> select(IDataGroup<?> dg, Filter filter) {
        CacheType cacheType = ((Table) dg).getCacheType();
        if (cacheType.isAllCache()) {
            if (filter == null) {
                if (cacheType == CacheType.CACHE_ALL_LOCAL) {
                    return (List<E>) ((List<?>) CacheManager.getLocalCacheManagerImpl().getAll(dg));
                } else {
                    return (List<E>) ((List<?>) CacheManager.getAll(dg));
                }
            } else {
                log.warn("A cache all table has select by filter : " + dg.getName());
            }
        }
        return this.select(SelectorFactory.getTablesSelector((Table) dg), filter);
    }

    @Override
    public List<E> selectByGroup(ICacheGroupKey groupKey, String keyValue) {
        if (this.getCacheTable().getCacheType() == CacheType.CACHE_ALL_LOCAL) {
            return (List<E>) ((List<?>) CacheManager.getLocalCacheManagerImpl().getAllByGroup(groupKey, keyValue));
        } else {
            return (List<E>) ((List<?>) CacheManager.getAllByGroup(groupKey, keyValue));
        }
    }

    /**
     * 获取所有数据
     */
    public List<E> getAll() {
        return this.select(null);
    }

    protected List<E> convert(MyBatisSelectorFilter selector, List<Map<?, ?>> result) {
        return (List<E>) SelectorResultParser.parser(selector, result);
    }

    private Table getTable(Filter filter) {
        Table table = filter != null ? (Table) filter.getDataGroup() : null;
        if (table != null) {
            return table;
        }

        Table cacheTable = getCacheTable();
        if (cacheTable != null) {
            return cacheTable;
        }

        // 只能通过filter获取table名称了，如果还获取不到，证明业务逻辑出现问题
        if (filter != null) {
            table = getFilterTable(filter);
            if (table != null) {
                return table;
            }
        }

        throw new AppException("Which table, sir?");
    }

    private Table getFilterTable(Filter filter) {
        List<IFilterItem> items = filter.getItems();
        if (ArrayUtil.isEmpty(items)) {
            return null;
        }

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
        return null;
    }

    @Override
    public Table getCacheTable() {
        if (cachedTable == null) {
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (params != null && params.length > 0) {
                String modelName = ((Class<?>) params[0]).getSimpleName();
                if (!"ServiceBean".equals(modelName)) {
                    cachedTable = TableManager.getTableByClassName(modelName);
                }
            }
        }
        return cachedTable;
    }

    private static void getAllRelativeBean(List<ServiceBean> list, ServiceBean bean) {
        list.add(bean);
        Map<String, List<ServiceBean>> fkBeans = bean.getFkBeans();
        if (fkBeans == null) {
            return;
        }
        fkBeans.values().forEach(item -> {
            if (item != null) {
                for (ServiceBean sb : item) {
                    getAllRelativeBean(list, sb);
                }
            }
        });
    }

    /**
     * 慎用，自己执行sql，需要自行处理缓存，通知，加锁
     */
    @Override
    public List<Map<String, Object>> excuteSql(String sql, AbstractDataValue... args) {
        List<Map<String, Object>> result = this.mapper.excuteSql(new MyBatisSqlSentence(sql, args));
        ArrayUtil.removeNullItem(result);
        return result;
    }

    protected boolean needNotify(Table table) {
        return TableManager.needNotify(table);
    }

    protected void notifyDelete(Collection<? extends ServiceBean> list) {
        for (ServiceBean bean : list) {
            if (needNotify(bean.getMoTable())) {
                TableManager.notify(Event.DELETE, bean);
            }
        }
    }

    protected void notifyDelete(Table table, Object pkValue) {
        if (needNotify(table)) {
            ServiceBean serviceBean = SelectorResultParser.getServiceBean(table);
            serviceBean.setKeyValue(pkValue);
            TableManager.notify(Event.DELETE, serviceBean);
        }
    }

    protected void notifyAdd(Collection<? extends ServiceBean> serviceBeans) {
        if (serviceBeans == null) {
            return;
        }
        for (ServiceBean serviceBean : serviceBeans) {
            this.notifyAdd(serviceBean);
        }
    }

    protected void notifyAdd(ServiceBean serviceBean) {
        if (needNotify(serviceBean.getMoTable())) {
            TableManager.notify(Event.ADD, serviceBean);
        }
    }

    protected void notifyUpdate(Collection<? extends ServiceBean> serviceBeans, boolean isSelective) {
        if (serviceBeans == null) {
            return;
        }
        for (ServiceBean serviceBean : serviceBeans) {
            this.notifyUpdate(serviceBean, isSelective);
        }
    }

    protected void notifyUpdate(ServiceBean serviceBean, boolean isSelective) {
        if (needNotify(serviceBean.getMoTable())) {
            // 更新后传输全量数据
            TableManager.notify(isSelective ? Event.UPDATE_SELECTIVE : Event.UPDATE, serviceBean);
        }
    }

    /**
     * 以下方法提供给ICacheService使用，正常的service类如非必要，忽略以下方法的存在
     */
    public void excuteBatch(List<CacheEntry<E>> list) {
        List<CacheEntry<ServiceBean>> tempList = (List<CacheEntry<ServiceBean>>) ((List<?>) list);
        this.mapper.excuteBatch(tempList);
    }

    public void add2DB(E e) {
        this.add2DBInner(e);
    }

    protected void add2DBInner(ServiceBean e) {
        mapper.insert(e);
    }

    public int update2DB(E e) {
        Filter filter = new Filter().andFilter(e.getMoTable().getPK(), e.getKeyValue());
        return mapper.updateByFilter(e, e.getTable().getAsName(), MyBatisFilter.getInstance(filter));
    }

    public int updateSelective2DB(E e) {
        Filter filter = new Filter().andFilter(e.getMoTable().getPK(), e.getKeyValue());
        return mapper.updateSelective(e, e.getTable().getAsName(), MyBatisFilter.getInstance(filter));
    }

    public int delete2DB(E e) {
        Filter filter = new Filter().andFilter(e.getMoTable().getPK(), e.getKeyValue());
        return mapper.deleteByFilter(e.getTable(), MyBatisFilter.getInstance(filter));
    }

}
