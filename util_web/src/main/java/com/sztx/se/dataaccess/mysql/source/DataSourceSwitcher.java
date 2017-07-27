package com.sztx.se.dataaccess.mysql.source;

/**
 * 动态数据源选择器，必须使用在事务打开之前，事务打开后无法切换非当前事务正在使用之外的数据源
 * 
 * @author zhihongp
 * 
 */
public class DataSourceSwitcher {

	private static final ThreadLocal<String> dbContextHolder = new ThreadLocal<String>();
	private static final ThreadLocal<Boolean> dbContextReadWriteSeparate = new ThreadLocal<Boolean>();

	public static void setDataSourceTypeInContext(String dataSourceType) {
		dbContextHolder.set(dataSourceType);
		dbContextReadWriteSeparate.set(true);
	}

	/**
	 * 强制设置当前数据源到主库数据源上下文中，即本次线程生命周期内读请求都走主库(
	 * 即使读写分离开关readWriteSeparateFlag为true读请求也会强制走主库)
	 * 
	 */
	public static void setDataSourceTypeForceMaster() {
		setDataSourceTypeForceMaster(null);
	}

	/**
	 * 强制设置数据源到主库数据源上下文中，即本次线程生命周期内读请求都走主库(
	 * 即使读写分离开关readWriteSeparateFlag为true读请求也会强制走主库)
	 * 
	 * @param dataSourceType
	 */
	public static void setDataSourceTypeForceMaster(String dataSourceType) {
		if (dataSourceType != null) {
			dbContextHolder.set(dataSourceType);
		}

		dbContextReadWriteSeparate.set(false);
	}

	public static String getDataSourceType() {
		// 目前支持从线程上下文中获取，以后提供扩展接口，允许用户实现具体的获取方式
		return getDataSourceTypeFromContext();
	}

	public static String getDataSourceTypeFromContext() {
		String dataSourceType = (String) dbContextHolder.get();
		return dataSourceType;
	}

	public static Boolean getReadWriteSeparateFromContext() {
		Boolean readWriteSeparateFlag = dbContextReadWriteSeparate.get();
		return readWriteSeparateFlag;
	}

	public static void clearDataSourceType() {
		dbContextHolder.remove();
		dbContextReadWriteSeparate.remove();
	}

}
