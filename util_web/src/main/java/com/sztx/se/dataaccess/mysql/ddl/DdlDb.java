//package com.sztx.se.dataaccess.mysql.ddl;
//
//import java.util.List;
//
///**
// * 
// * @author zhihongp
// * 
// */
//public class DdlDb {
//	
//	private String dbName;
//
//	private String masterDataSourceKey;
//
//	private List<String> slaveDataSourceKeyList;
//	
//	public DdlDb(String dbName, String masterDataSourceKey, List<String> slaveDataSourceKeyList) {
//		this.dbName = dbName;
//		this.masterDataSourceKey = masterDataSourceKey;
//		this.slaveDataSourceKeyList = slaveDataSourceKeyList;
//	}
//
//	public String getDbName() {
//		return dbName;
//	}
//
//	public void setDbName(String dbName) {
//		this.dbName = dbName;
//	}
//
//	public String getMasterDataSourceKey() {
//		return masterDataSourceKey;
//	}
//
//	public void setMasterDataSourceKey(String masterDataSourceKey) {
//		this.masterDataSourceKey = masterDataSourceKey;
//	}
//
//	public List<String> getSlaveDataSourceKeyList() {
//		return slaveDataSourceKeyList;
//	}
//
//	public void setSlaveDataSourceKeyList(List<String> slaveDataSourceKeyList) {
//		this.slaveDataSourceKeyList = slaveDataSourceKeyList;
//	}
//
//	@Override
//	public String toString() {
//		return "DdlDb [dbName=" + dbName + ", masterDataSourceKey=" + masterDataSourceKey + ", slaveDataSourceKeyList=" + slaveDataSourceKeyList + "]";
//	}
//
//}
