package com.td.ca.web.db;

import com.td.ca.base.dataservice.ServiceBean;
import com.td.ca.base.util.db.table.Column;
import com.td.ca.base.util.db.table.DataValueFactory;
import com.td.ca.base.util.db.table.Table;
import com.td.ca.base.util.db.table.data.AbstractDataValue;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.web.filter.AsColumn;
import com.td.ca.web.filter.MyBatisSelectorFilter;
import com.td.ca.web.filter.SelectTable;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SelectorResultParser {

    /**
     * 如果是一对多的情况，这里返回的数据还是一对多
     *
     * 但如果是多对一，那么是返回多条一对一，而且为一的table对象并没有公用
     *
     * 特殊场景 如：A连B连C表 正常情况下，A,B,C没有问题
     * 如果selectFilter中的column不包含B的id，C无法准确定位到parent对象，会导致关系混乱
     *
     * @param selector
     * @param result
     * @return
     * @throws SQLException
     */
    public static List<ServiceBean> parser(MyBatisSelectorFilter selector, List<Map<?, ?>> result) {
        // 使用map，当数据量很大的时候可以节省大量时间，但是有些查询是有顺序的需要保证排序
        Map<Object, ServiceBean> resultMap = new LinkedHashMap<>();
        boolean isMutiTable = selector.getAllTables().size() > 1;
        for (Map<?, ?> row : result) {
            Map<String, ServiceBean> map = new HashMap<String, ServiceBean>();
            for (Column column : selector.getSelectColumns()) {
                initValue(selector, row.get(column.getAsName()), column, map);
            }

            // 取主表，主表会附带副表信息进行meger
            ServiceBean bean = map.get(selector.getTable().getName());
            bean.initRecursion();
            // 多表查询需要合并
            AbstractDataValue keyValue = bean.getKeyValue();
            Object key = keyValue == null ? bean : keyValue;
            if (isMutiTable) {
                ServiceBean sameBean = resultMap.get(key);
                if (sameBean != null) {
                    sameBean.mergerFKBean(bean);
                } else {
                    resultMap.put(key, bean);
                }
            } else {
                resultMap.put(key, bean);
            }
        }

        return new ArrayList<>(resultMap.values());
    }

    private static void initValue(MyBatisSelectorFilter selector, Object value, Column col, Map<String, ServiceBean> map) {
        AbstractDataValue dataValue = DataValueFactory.read(col, value);
        if (dataValue == null) {
            return;
        }

        String key = col.getTableName();
        ServiceBean sb = createParentBean(map, selector, key, col.getTable());
        sb.setValue(col.getColumn(), dataValue);
    }

    /**
     * 如果需要，创建父类bean
     *
     * @param map
     * @param selector
     * @param key table名称
     * @param table
     * @return
     */
    private static ServiceBean createParentBean(Map<String, ServiceBean> map, MyBatisSelectorFilter selector, String key, Table table) {
        if (map.containsKey(key)) {
            return map.get(key);
        }

        // 新建对象
        ServiceBean serviceBean = getServiceBean(table);
        map.put(key, serviceBean);

        SelectTable selectTable = selector.getAllTables().get(key);
        SelectTable parent = selectTable.getParent();
        //得到join table外键
        if (parent != null) {
            Column fk = selectTable.getFKColumn();
            if (fk instanceof AsColumn) {
                //自关联的情况处理
                if (((AsColumn) fk).getSelectTable() == selectTable && selectTable.getTable() == parent.getTable()) {
                    createParentBean(map, selector, parent.getName(), parent.getTable()).addFKBean(fk.getColumn(), serviceBean, true);
                } else {
                    createParentBean(map, selector, parent.getName(), parent.getTable()).addFKBean(fk.getColumn(), serviceBean);
                }
            } else {
                createParentBean(map, selector, parent.getName(), parent.getTable()).addFKBean(fk.getColumn(), serviceBean);
            }
        }
        return serviceBean;
    }

    public static ServiceBean getServiceBean(Table table) {
        String modelName = table.getModelName();
        if (StringUtil.isEmpty(modelName)) {
            return new ServiceBean(table);
        } else {
            try {
                Class<?> type = Class.forName(modelName);

                Constructor<?> constructor = type.getDeclaredConstructor();
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return (ServiceBean) constructor.newInstance();
            } catch (Exception e) {
                throw AppException.getAppException(e);
            }
        }
    }
}
