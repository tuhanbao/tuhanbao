package com.tuhanbao.web.filter;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.util.db.table.Table;

/**
 * 此类主要为了避免业务人员私自创建selector
 * @author tuhanbao
 *
 */
public class SelectorFactory {
	private static Map<Table, MyBatisSelector> selectors = new HashMap<Table, MyBatisSelector>();
	protected static MyBatisSelector createSelector(Table table) {
		if (!selectors.containsKey(table)) {
			MyBatisSelector selector = new MyBatisSelector(table);
			selectors.put(table, selector);
			return selector;
		}
		return selectors.get(table);
	}

	public static MyBatisSelector getTablesSelector(Table table) {
		if (!selectors.containsKey(table)) {
			return createSelector(table);
		}
		return selectors.get(table);
	}
}
