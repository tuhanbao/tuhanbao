package com.tuhanbao.base.util.config;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;

public class DSConfig {
	
	public static String DB_DRIVER;
	public static String DB_URL;
	
	
	public static String DB_DRIVER_NAME = "db_driver";
	public static String DB_URL_NAME = "db_url";
	
	public static String DB_USER_NAME = "db_user_";
	public static String DB_PASSWORD_NAME = "db_password_";
	
	public static Map<String, DBSrc> DBS = new HashMap<String, DBSrc>();
	private static final String KEY = "ds";
	
	static
	{
		init();
	}
	
	private static void init() {
		Config config = ConfigManager.getConfig(KEY);
        if (config == null) {
            LogManager.warn("no config file for : " + KEY);
            return;
        }
        
		DB_DRIVER = config.getString(DB_DRIVER_NAME);
		DB_URL = config.getString(DB_URL_NAME);
		
		for (Map.Entry<String, String> entry : config.getProperties().entrySet()) {
			String key = entry.getKey();
			if (key.startsWith(DB_USER_NAME)) {
				String user = entry.getValue();
				String dbName = key.substring(DB_USER_NAME.length());
				if (StringUtil.isEmpty(user)) break;
				String password = config.getString(DB_PASSWORD_NAME + dbName);
				
				String dbDriver = config.getString(DB_DRIVER_NAME + "_" + dbName);
				String dbUrl = config.getString(DB_URL_NAME + "_" + dbName);
				if (StringUtil.isEmpty(dbDriver)) {
					dbDriver = DB_DRIVER;
				}
				if (StringUtil.isEmpty(dbUrl)) {
					dbUrl = DB_URL;
				}
				
				DBS.put(dbName, new DBSrc(dbDriver, dbUrl, user, password, 1));
			}
		}
	}
	
	/**
	 * 重新读取配置
	 */
	public static void refresh() {
		ConfigManager.refreshConfig(KEY);
		init();
	}
	
	public static DBSrc getDBSrc(String name) {
		return DBS.get(name);
	}
	
}
