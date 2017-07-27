package com.sztx.se.dataaccess.mysql.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import com.sztx.se.dataaccess.mysql.client.DataSourceProxy;
import com.sztx.se.dataaccess.mysql.config.DynamicDb;
import com.sztx.se.dataaccess.mysql.util.DataSourceUtil;

/**
 * 动态创建数据源的工具类
 * 
 * @author zhihongp
 * 
 */
public class DynamicCreateDataSourceManager {

	private DynamicDataSource dynamicDataSource;

	private List<DataSourceProxy> dataSourceProxyList;

	public void setDynamicDataSource(DynamicDataSource dynamicDataSource) {
		this.dynamicDataSource = dynamicDataSource;
	}

	public void setDataSourceProxyList(List<DataSourceProxy> dataSourceProxyList) {
		this.dataSourceProxyList = dataSourceProxyList;
	}

	/**
	 * 初始化数据库数据源
	 * 
	 * @param applicationContext
	 */
	public void initCreateDataSource() {
		// 把数据源bean注册到容器中
		registerDataSource();
	}

	private void registerDataSource() {
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		Map<String, DynamicDb> dbDataSourceMap = new HashMap<String, DynamicDb>();
		Map<String, String> dataSourceDbMap = new HashMap<String, String>();
		DataSource defaultTargetDataSource = null;
		String defaultTargetDataSourceKey = null;
		List<DataSourceProxy> dataSourceProxyList = new ArrayList<DataSourceProxy>();

		if (this.dataSourceProxyList == null || this.dataSourceProxyList.isEmpty()) {
//			Map<String, DataSourceProxy> dataSourceProxyMap = SpringContextHolder.applicationContext.getBeansOfType(DataSourceProxy.class);
			//临时解决下编译错误
		    Map<String, DataSourceProxy> dataSourceProxyMap = null;

			if (dataSourceProxyMap != null && !dataSourceProxyMap.isEmpty()) {
				for (Entry<String, DataSourceProxy> en : dataSourceProxyMap.entrySet()) {
					dataSourceProxyList.add(en.getValue());
				}
			}
		} else {
			dataSourceProxyList = this.dataSourceProxyList;
		}

		// 将默认数据源放入 targetDataSources和单元测试数据源加入到 map中
		for (DataSourceProxy dataSourceProxy : dataSourceProxyList) {
			String dataSourceKey = dataSourceProxy.getDataSourceKey();
			DataSource dataSource = dataSourceProxy.getDataSource();
			String dbName = dataSourceProxy.getDbName();
			boolean isDefault = dataSourceProxy.getIsDefault();
			Map<String, DataSource> slaveDataSourceMap = dataSourceProxy.getSlaveDataSourceMap();

			if (isDefault) {
				defaultTargetDataSource = dataSource;
				defaultTargetDataSourceKey = dataSourceKey;
			}

			if (slaveDataSourceMap != null && !slaveDataSourceMap.isEmpty()) {
				List<String> slaveDataSourceKeyList = new ArrayList<String>();

				for (Entry<String, DataSource> sen : slaveDataSourceMap.entrySet()) {
					String slaveDataSourceKey = sen.getKey();
					DataSource slaveDataSource = sen.getValue();
					slaveDataSourceKeyList.add(slaveDataSourceKey);
					targetDataSources.put(slaveDataSourceKey, slaveDataSource);
					dataSourceDbMap.put(slaveDataSourceKey, dbName);
				}

				dbDataSourceMap.put(dbName, new DynamicDb(dbName, dataSourceKey, slaveDataSourceKeyList));
			} else {
				dbDataSourceMap.put(dbName, new DynamicDb(dbName, dataSourceKey, null));
			}

			dataSourceDbMap.put(dataSourceKey, dbName);
			targetDataSources.put(dataSourceKey, dataSource);
		}

		dynamicDataSource.setDbDataSourceMap(dbDataSourceMap);
		dynamicDataSource.setDataSourceDbMap(dataSourceDbMap);
		dynamicDataSource.setTargetDataSources(targetDataSources);
		dynamicDataSource.setDataSourceMap(targetDataSources);
		dynamicDataSource.setDefaultTargetDataSource(defaultTargetDataSource);
		dynamicDataSource.setDefaultTargetDataSourceKey(defaultTargetDataSourceKey);
		DataSourceUtil.dataSourceDbMap = dataSourceDbMap;
		DataSourceUtil.dbDataSourceMap = dbDataSourceMap;
		dynamicDataSource.initRswConfig();
		dynamicDataSource.initDdlConfig();
		dynamicDataSource.initSqlLog();
		dynamicDataSource.afterPropertiesSet();
	}
}