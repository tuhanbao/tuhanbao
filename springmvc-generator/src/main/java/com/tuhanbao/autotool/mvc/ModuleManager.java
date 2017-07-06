package com.tuhanbao.autotool.mvc;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigPattern;
import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.objutil.StringUtil;

public class ModuleManager {
	private static final Map<ConfigPattern, Map<String, DBSrc>> MODULES = new HashMap<>();
	
	public static void addModule(ConfigPattern cp, String module, DBSrc src) {
		if (StringUtil.isEmpty(module)) module = Constants.EMPTY;
		if (!MODULES.containsKey(cp)) {
		    MODULES.put(cp, new HashMap<String, DBSrc>());
		}
		MODULES.get(cp).put(module, src);
	}
	
	public static DBSrc getDBSrc(String module) {
	    return getDBSrc(ConfigManager.DEFAULT_CONFIG_PATTERN, module);
	}

	public static DBSrc getDBSrc(ConfigPattern cp, String module) {
	    if (StringUtil.isEmpty(module)) module = Constants.EMPTY;
	    Map<String, DBSrc> map = MODULES.get(cp);
	    if (map == null) return null;
	    return map.get(module);
	}
	
	public static String getModule(DBSrc src) {
	    return getModule(ConfigManager.DEFAULT_CONFIG_PATTERN, src);
	}

	public static String getModule(ConfigPattern cp, DBSrc src) {
		Map<String, DBSrc> allDBSrc = getAllModules(cp);
		if (allDBSrc == null) return null;
		
        for (Map.Entry<String, DBSrc> entry : allDBSrc.entrySet()) {
			if (entry.getValue() == src) return entry.getKey();
		}
		return null;
	}
	
	public static Map<String, DBSrc> getAllModules() {
	    return getAllModules(ConfigManager.DEFAULT_CONFIG_PATTERN);
	}

	public static Map<String, DBSrc> getAllModules(ConfigPattern cp) {
		return MODULES.get(cp);
	}
}

