package com.tuhanbao.autotool.mvc;

import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;

public class ProjectConfig implements ConfigRefreshListener {
	public static String CONFIG_URL;
	public static String PROJECT_URL;
	public static String PROJECT_NAME;
	public static String PROJECT_HEAD;
	public static boolean NEED_CREATE_CODE;
	public static boolean NEED_CREATE_WEB;
	public static boolean NEED_SYNC_DB;
	public static boolean NEED_AUTOCREATE_AM;
	public static String FILTERS;

	private static final String CONFIG_URL_NAME = "config_url";
	private static final String PROJECT_URL_NAME = "project_url";
	private static final String PROJECT_NAME_NAME = "project_name";
	private static final String PROJECT_HEAD_NAME = "project_head";
	private static final String NEED_CREATE_CODE_NAME = "need_create_code";
	private static final String NEED_CREATE_WEB_NAME = "need_create_web";
	private static final String NEED_SYNC_DB_NAME = "need_sync_db";
	private static final String NEED_AUTOCREATE_AM_NAME = "need_autocreate_am";
	private static final String FILTERS_NAME = "filters";
	
	private static final String KEY = "project";
	
	static
	{
		init();
	}
	
	private static void init() {
		Config config = ConfigManager.getConfig(KEY);
		PROJECT_NAME = config.getString(PROJECT_NAME_NAME);
		PROJECT_HEAD = config.getString(PROJECT_HEAD_NAME);
		PROJECT_URL = config.getString(PROJECT_URL_NAME);
		CONFIG_URL = config.getString(CONFIG_URL_NAME);
		NEED_CREATE_CODE = config.getInt(NEED_CREATE_CODE_NAME) == 1;
		NEED_CREATE_WEB = config.getInt(NEED_CREATE_WEB_NAME) == 1;
		NEED_SYNC_DB = config.getInt(NEED_SYNC_DB_NAME) == 1;
		NEED_AUTOCREATE_AM = config.getInt(NEED_AUTOCREATE_AM_NAME) == 1;
		FILTERS = config.getString(FILTERS_NAME);
	}
	
	/**
	 * 重新读取配置
	 */
	public void refresh() {
		init();
	}

	@Override
	public String getKey() {
		return KEY;
	}
	
}
