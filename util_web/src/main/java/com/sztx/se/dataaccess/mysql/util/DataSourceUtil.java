package com.sztx.se.dataaccess.mysql.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.sztx.se.dataaccess.mysql.config.DynamicDb;
import com.sztx.se.dataaccess.mysql.ddl.DdlConfig;
import com.sztx.se.dataaccess.mysql.ddl.DdlTable;
import com.sztx.se.dataaccess.mysql.source.DataSourceSwitcher;
import com.sztx.se.dataaccess.mysql.source.DynamicDataSource;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.other.ConsistenHashUtil;

public class DataSourceUtil {

	public static final String TABLE_REGULAR_EXPRESSION_PREFIX = "(?:\\.|`|,|\\s)?";

	public static final String TABLE_REGULAR_EXPRESSION_SUFFIX = "(?:\\.|`|,|\\s|$)";

	public static Map<String, DdlConfig> ddlConfigMap;

	public static String tablePattern;

	public static Map<String, ConsistenHashUtil<DdlTable>> consistenHashUtilMap;

	public static Map<String, String> dataSourceDbMap;

	public static Map<String, DynamicDb> dbDataSourceMap;

	public static String getMasterDataSource(String dataSource) {
		String db = DataSourceUtil.getDbByDataSource(dataSource);
		String masterDataSource = DataSourceUtil.getDataSourceByDb(db, false);
		return masterDataSource;
	}

	public static String getSlaveDataSourceByMasterDataSource(String masterDataSource) {
		if (StringUtil.isEmpty(masterDataSource)) {
			return null;
		}

		String db = DataSourceUtil.getDbByDataSource(masterDataSource);
		String slaveDataSource = DataSourceUtil.getDataSourceByDb(db, true);
		return slaveDataSource;
	}

	public static String getCurrentDataSource(DynamicDataSource dynamicDataSource) {
		String currentDataSource = dynamicDataSource.getCurrentDataSource();
		return currentDataSource;
	}

	public static String getCurrentDb(DynamicDataSource dynamicDataSource) {
		String currentDataSource = getCurrentDataSource(dynamicDataSource);
		return getCurrentDb(currentDataSource);
	}

	public static String getCurrentDb(String currentDataSource) {
		return getDbByDataSource(currentDataSource);
	}
	
	public static boolean getCurrentReadWriteSeparate() {
		Boolean currentReadWriteSeparate = DataSourceSwitcher.getReadWriteSeparateFromContext();

		if (currentReadWriteSeparate == null) {
			return true;
		} else {
			return currentReadWriteSeparate;
		}
	}

	public static boolean getReadWriteSeparate() {
		Boolean readWriteSeparate = DataSourceSwitcher.getReadWriteSeparateFromContext();

		if (readWriteSeparate == null) {
			return true;
		} else {
			return readWriteSeparate;
		}
	}

	public static String getDbByDataSource(String dataSource) {
		if (dataSourceDbMap == null) {
			throw new MyException("Can not find a db, because dataSourceDbMap is null.");
		} else {
			String db = dataSourceDbMap.get(dataSource);

			if (db == null) {
				throw new MyException("Can not find a db by dataSource=" + dataSource + " from dataSourceDbMap.");
			}

			return db;
		}
	}

	/**
	 * 根据db获取数据源，如果isSlave为true则获取备库数据源，否则则获取主库数据源(注：如果备库数据源获取不到则使用主库数据源代替)
	 * 
	 * @param db
	 * @param isSlave
	 * @return
	 */
	public static String getDataSourceByDb(String db, boolean isSlave) {
		if (dbDataSourceMap == null) {
			throw new MyException("Can not find a db, because dbDataSourceMap is null.");
		} else {
			DynamicDb dynamicDb = dbDataSourceMap.get(db);

			if (dynamicDb == null) {
				throw new MyException("Can not find a dataSource by db " + db + " from dbDataSourceMap.");
			}

			if (isSlave) {
				List<String> slaveDataSourceKeyList = dynamicDb.getSlaveDataSourceKeyList();

				if (slaveDataSourceKeyList != null && !slaveDataSourceKeyList.isEmpty()) {
					Random random = new Random();
					int size = slaveDataSourceKeyList.size();
					String dataSource = slaveDataSourceKeyList.get(random.nextInt(size));

					if (dataSource != null) {
						return dataSource;
					}
				}
			}

			String dataSource = dynamicDb.getMasterDataSourceKey();
			return dataSource;
		}
	}

	public static String getTablePattern(Set<String> keySet) {
		if (keySet == null || keySet.isEmpty()) {
			return "";
		}

		Iterator<String> iterator = keySet.iterator();
		String first = iterator.next();

		if (!iterator.hasNext()) {
			return TABLE_REGULAR_EXPRESSION_PREFIX + "(" + first + ")" + TABLE_REGULAR_EXPRESSION_SUFFIX;
		}

		StringBuffer buf = new StringBuffer();
		buf.append(TABLE_REGULAR_EXPRESSION_PREFIX);
		buf.append("(");

		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			String table = iterator.next();
			buf.append("|");

			if (table != null) {
				buf.append(table);
			}
		}

		buf.append(")");
		buf.append(TABLE_REGULAR_EXPRESSION_SUFFIX);
		return buf.toString();
	}

	public static boolean isSlaveDataSource(String dataSource) {
		if (dbDataSourceMap == null) {
			throw new MyException("Can not find a db, because dbDataSourceMap is null.");
		} else {
			String db = DataSourceUtil.getDbByDataSource(dataSource);
			DynamicDb dynamicDb = dbDataSourceMap.get(db);

			if (dynamicDb == null) {
				throw new MyException("Can not find a dataSource by db " + db + " from dbDataSourceMap.");
			}

			List<String> slaveDataSourceKeyList = dynamicDb.getSlaveDataSourceKeyList();

			if (slaveDataSourceKeyList == null) {
				return false;
			}

			if (slaveDataSourceKeyList.contains(dataSource)) {
				return true;
			} else {
				return false;
			}
		}
	}
}
