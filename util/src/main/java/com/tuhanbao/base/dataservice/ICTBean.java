package com.tuhanbao.base.dataservice;

import com.tuhanbao.base.util.db.table.CTTable;
import com.tuhanbao.base.util.db.table.Table;

/**
 * 可以决定分表名称的java对象
 * 一般用于表的横向切割
 * 比如按时间分表，按用户分表等
 * @author Administrator
 *
 */
public interface ICTBean {
	/**
	 * 返回分表的标签
	 * 
	 * 比如custom表
	 * 
	 * 正常表名为T_CUSTOM，如果按年进行分表，可能会有T_CUSTOM_2016, T_CUSTOM_2017
	 * 那么tag分别范围2016,2017
	 * @return
	 */
	String getTag();

	/**
	 * 获取分表
	 * @param table
	 * @return
	 */
	Table getTable(Table table);
}
