package org.apache.ibatis.executor.resultset;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuhanbao.base.dataservice.ICTBean;
import com.tuhanbao.base.dataservice.ServiceBean;
import com.tuhanbao.base.util.db.table.CTTable;
import com.tuhanbao.base.util.db.table.Column;
import com.tuhanbao.base.util.db.table.DataValueFactory;
import com.tuhanbao.base.util.db.table.Table;
import com.tuhanbao.base.util.db.table.data.DataValue;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.web.filter.AsColumn;
import com.tuhanbao.web.filter.MyBatisSelectorFilter;
import com.tuhanbao.web.filter.SelectTable;

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
     * @param rsw
     * @return
     * @throws SQLException
     */
	public static List<ServiceBean> parser(MyBatisSelectorFilter selector, ResultSetWrapper rsw) throws SQLException {
		List<ServiceBean> list = new ArrayList<ServiceBean>();
		
		while (rsw.getResultSet().next()) {
			Map<String, ServiceBean> map = new HashMap<String, ServiceBean>();
			for (Column column : selector.getSelectColumns()) {
				initValue(selector, rsw.getResultSet(), column, map);
			}

            // 取主表，主表会附带副表信息进行meger
			ServiceBean bean = map.get(selector.getTable().getName());
			int index = list.indexOf(bean);
			if (index >= 0) {
				list.get(index).mergerFKBean(bean);
			}
			else {
				list.add(bean);
			}
		}
		
		return list;
	}
	
	private static void initValue(MyBatisSelectorFilter selector, ResultSet rs, Column col, Map<String, ServiceBean> map) throws SQLException {
		DataValue dataValue = DataValueFactory.read(col, rs);
		if (dataValue == null) return;
		
		String key = col.getTableName();
		
		createParentBean(map, selector, key, selector.getCTTable(col.getTable()));
		map.get(key).setValue(col.getColumn(), dataValue);
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
		Column fk = selectTable.getFKColumn();
		if (parent != null) {
		    if (fk instanceof AsColumn) {
		        if (((AsColumn)fk).getSelectTable() == selectTable && selectTable.getTable() == parent.getTable()) {
		            createParentBean(map, selector, parent.getName(), parent.getTable()).addFKBean(fk.getColumn(), serviceBean, true);
		        }
		        else {
		            createParentBean(map, selector, parent.getName(), parent.getTable()).addFKBean(fk.getColumn(), serviceBean);
		        }
		    }
		    else {
		        createParentBean(map, selector, parent.getName(), parent.getTable()).addFKBean(fk.getColumn(), serviceBean);
		    }
		}
		return serviceBean;
	}

	private static ServiceBean getServiceBean(Table table) {
		try {
			Class<?> type = Class.forName(table.getModelName());
		
			if (table instanceof CTTable) {
				Constructor<?> constructor = type.getDeclaredConstructor(ICTBean.class);
				if (!constructor.isAccessible()) {
					constructor.setAccessible(true);
				}
			
				return (ServiceBean) constructor.newInstance(((CTTable)table).getCTBean());
			}
			else {
				Constructor<?> constructor = type.getDeclaredConstructor();
				if (!constructor.isAccessible()) {
					constructor.setAccessible(true);
				}
				return (ServiceBean) constructor.newInstance();
			}
		} catch (Exception e) {
			throw MyException.getMyException(e);
		}
	}
}
