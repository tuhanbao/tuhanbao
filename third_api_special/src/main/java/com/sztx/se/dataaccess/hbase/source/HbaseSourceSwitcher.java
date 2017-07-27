package com.sztx.se.dataaccess.hbase.source;

/**
 * hbase动态数据源选择器
 * 
 * @author zhihongp
 * 
 */
public class HbaseSourceSwitcher {

	private static final ThreadLocal<String> hbaseContextHolder = new ThreadLocal<String>();

	public static void setHbaseSourceTypeInContext(String hbaseSourceType) {
		hbaseContextHolder.set(hbaseSourceType);
	}

	public static String getHbaseSourceType() {
		String hbaseSourceType = (String) hbaseContextHolder.get();
		return hbaseSourceType;
	}

	public static String getHbaseSourceTypeFromContext() {
		String hbaseSourceType = (String) hbaseContextHolder.get();
		return hbaseSourceType;
	}

	public static void clearHbaseSourceType() {
		hbaseContextHolder.remove();
	}
}
