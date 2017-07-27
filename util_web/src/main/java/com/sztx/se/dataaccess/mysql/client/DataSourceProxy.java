package com.sztx.se.dataaccess.mysql.client;

import java.util.Map;

import javax.sql.DataSource;

public class DataSourceProxy {

	private DataSource dataSource;

	private String dbName;

	private String dataSourceKey;

	private boolean isDefault;

	private Map<String, DataSource> slaveDataSourceMap;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDataSourceKey() {
		return dataSourceKey;
	}

	public void setDataSourceKey(String dataSourceKey) {
		this.dataSourceKey = dataSourceKey;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Map<String, DataSource> getSlaveDataSourceMap() {
		return slaveDataSourceMap;
	}

	public void setSlaveDataSourceMap(Map<String, DataSource> slaveDataSourceMap) {
		this.slaveDataSourceMap = slaveDataSourceMap;
	}

	@Override
	public String toString() {
		return "DataSourceProxy [dataSource=" + dataSource + ", dbName=" + dbName + ", dataSourceKey=" + dataSourceKey + ", isDefault=" + isDefault
				+ ", slaveDataSourceMap=" + slaveDataSourceMap + "]";
	}

}
