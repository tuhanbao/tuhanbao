package {package};

import {base_project_head}.base.Constants;
import {base_project_head}.base.dataservice.CutTableServiceBean;
import {base_project_head}.base.dataservice.ServiceBean;
import {base_project_head}.base.dataservice.filter.Filter;
import {base_project_head}.base.dataservice.filter.operator.Operator;
import {base_project_head}.base.dataservice.filter.page.Page;
import {base_project_head}.base.util.db.table.Column;
import {base_project_head}.base.util.db.table.CutByHashTable;
import {base_project_head}.base.util.db.table.CutChildTable;
import {base_project_head}.base.util.db.table.CutTable;
import {base_project_head}.base.util.db.table.MathFunc;
import {base_project_head}.base.util.db.table.Table;
import {base_project_head}.base.util.exception.AppException;
import {base_project_head}.base.util.log.LogUtil;
import {base_project_head}.base.util.objutil.ArrayUtil;
import {base_project_head}.base.util.objutil.TimeUtil;
import {base_project_head}.base.util.thread.ScheduledThreadManager;
import {base_project_head}.web.filter.MyBatisFilter;
import {base_project_head}.web.filter.MyBatisSelectorFilter;
import {base_project_head}.web.filter.SelectorFactory;
import {base_project_head}.web.filter.TableSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 分表的update方法暂时不够全面
 * 如果update的对象不包含cut key column，会存在无法找到对应子表的问题
 * <p/>
 * 分表的连表查询会有问题，暂不可用
 */
@Slf4j
public class CutTableByHashServiceImpl<E extends CutTableServiceBean> extends ServiceImpl<E> {

    @Override
    public void init() {
        // hash分表的表个数是确定的，可以提前预置到数据库中，此处不需要提前检查表是否创建
        CutByHashTable table = (CutByHashTable) this.getCacheTable();
        table.init();

        if (table.getMaxRows() > 0 || table.getMaxTime() > 0) {
            ScheduledThreadManager.executeOnTimer(4, 0, 0, () -> {
                deleteSuperfluousData();
            });
        }
    }

    private void deleteSuperfluousData() {
        CutByHashTable cutTable = (CutByHashTable) this.getCacheTable();
        int maxRows = cutTable.getMaxRows();
        long maxTime = cutTable.getMaxTime();
        Column col = cutTable.getColumn(Constants.CREATE_TIME_COL_NAME);
        for (Table table : cutTable.getAllTables()) {
            if (maxTime > 0 && col != null) {
                long l = TimeUtil.now() - TimeUnit.HOURS.toMillis(maxTime);
                log.info("delete data time < {}", l);
                this.delete(table, new Filter().andFilter(Operator.LESS, table.getColumn(Constants.CREATE_TIME_COL_NAME), l));
            }
            if (maxRows > 0) {
                int count = this.count(table, null);
                if (count > maxRows) {
                    log.info("delete ros < {}", count - maxRows);
                    deleteLastRow(table, maxRows, count - maxRows);
                }
            }
        }
    }

    private void deleteLastRow(Table table, int start, int num) {
        Filter filter = new Filter();
        filter.setPage(new Page(start, 1));
        filter.addOrderField(table.getPK(), true);
        List<E> list = this.select(table, filter);
        if (!ArrayUtil.isEmpty(list)) {
            E e = list.get(0);
            this.delete(table, new Filter().andFilter(Operator.LESS, table.getPK(), e.getKeyValue()));
        }
    }

    public void deleteByCutTableKey(Object value) {
        deleteByCutTableKey(value, null);
    }

    public void deleteByCutTableKey(Object value, Filter filter) {
        CutTable cutTable = (CutTable) this.getCacheTable();
        if (filter == null) {
            filter = new Filter();
        }
        filter.andFilter(cutTable.getCutTableColumn(), value);
        delete(cutTable.getTableByCutTableValue(value), filter);
    }

    @Override
    protected int update2DBInner(ServiceBean e, Table table, Filter filter, boolean isSelective) {
        if (table instanceof CutTable) {
            if (isSelective) {
                mapper.updateSelectiveCutTable(e, table, MyBatisFilter.getInstance(filter));
            } else {
                mapper.updateByFilterCutTable(e, table, MyBatisFilter.getInstance(filter));
            }
            // 暂时返回0
            return 0;
        } else {
            return super.update2DBInner(e, table, filter, isSelective);
        }
    }

    public int updateByCutTableKey(Object value, E e, Filter filter) {
        CutTable cutTable = (CutTable) this.getCacheTable();
        Table childTable = cutTable.getTableByCutTableValue(value);
        if (filter == null) {
            filter = new Filter();
        }
        return this.updateServiceBean(e, childTable, false, filter);
    }

    public int updateSelectiveByCutTableKey(Object value, E e, Filter filter) {
        CutTable cutTable = (CutTable) this.getCacheTable();
        Table childTable = cutTable.getTableByCutTableValue(value);
        if (filter == null) {
            filter = new Filter();
        }
        return this.updateServiceBean(e, childTable, true, filter);
    }

    public List<E> selectByCutTableKey(Object value) {
        CutTable cutTable = (CutTable) this.getCacheTable();
        Filter filter = new Filter();
        filter.andFilter(cutTable.getCutTableColumn(), value);
        return selectByCutTableKey(value, filter);
    }

    public List<E> selectByCutTableKey(Object value, Filter filter) {
        CutTable cutTable = (CutTable) this.getCacheTable();
        return this.select(cutTable.getTableByCutTableValue(value), filter);
    }

    @Override
    public void delete(Filter filter) {
        CutTable table = (CutTable) this.getCacheTable();
        List<E> deleteList = null;
        if (needNotify(table)) {
            deleteList = this.select(filter);
        }
        mapper.deleteByFilterCutTable(table, MyBatisFilter.getInstance(filter));
        if (deleteList != null) {
            notifyDelete(deleteList);
        }
    }

    @Override
    public void deleteById(Object pkValue) {
        CutTable table = (CutTable) this.getCacheTable();
        Filter filter = new Filter(table).andFilter(table.getPK(), pkValue);
        mapper.deleteByFilterCutTable(table, MyBatisFilter.getInstance(filter));
        notifyDelete(table, pkValue);
    }


    @Override
    public int count(Filter filter) {
        CutTable table = (CutTable) this.getCacheTable();
        MyBatisSelectorFilter selectorFilter = new MyBatisSelectorFilter(SelectorFactory.getTablesSelector(table), filter);
        List<Object> result = mapper.countByFilterCutTable(selectorFilter);
        int sum = 0;
        for (Object o : result) {
            if (o != null) {
                long value = Integer.valueOf(o.toString());
                sum += value;
            }
        }
        return sum;
    }

    @Override
    public List<E> select(TableSelector selector, Filter filter) {
        Page page = filter != null ? filter.getPage() : null;
        MyBatisSelectorFilter selectorFilter = new MyBatisSelectorFilter(selector, filter);
        if (page != null && page.isNeedCountTotal()) {
            page.setTotalCount(count(selector, filter));
        }
        // 如果是CutChildTable代表已经指定表了
        if (selector.getTable().getTable() instanceof CutChildTable) {
            return convert(selectorFilter, mapper.select(selectorFilter));
        } else {
            return convert(selectorFilter, mapper.selectCutTable(selectorFilter));
        }
    }

    @Override
    public long max(Column column, Filter filter) {
        CutTable table = (CutTable) this.getCacheTable();
        List<Object> result = mapper.executeMathFuncByFilterCutTable(MathFunc.MAX, column, SelectorFactory.getTablesSelector(table), MyBatisFilter.getInstance(filter));

        long max = 0;
        for (Object o : result) {
            if (o != null) {
                long value = Long.valueOf(o.toString());
                if (value > max) {
                    max = value;
                }
            }
        }
        return max;
    }

    protected <T> List<T> executeAllTablesAsync(Function<Table, T> func) {
        CutTable cutTable = (CutTable) this.getCacheTable();
        Collection<Table> allTables = cutTable.getAllTables();

        List<Future<T>> futureList = new ArrayList<>();

        for (Table table : allTables) {
            futureList.add(ScheduledThreadManager.submitWithVt(() -> {
                try {
                    return func.apply(table);
                } catch (Exception e) {
                    log.info("execute jdbc error", e);
                    throw AppException.getAppException(e);
                }
            }));
        }

        List<T> list = new ArrayList<>();
        try {
            for (Future<T> future : futureList) {
                list.add(future.get());
            }
        } catch (Exception e) {
            throw new AppException("scheduledThreadManager execute task error: " + LogUtil.getMessage(e));
        }
        return list;
    }

    protected boolean needAsync() {
        return true;
    }
}
