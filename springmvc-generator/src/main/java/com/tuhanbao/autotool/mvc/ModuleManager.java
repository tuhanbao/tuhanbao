package com.tuhanbao.autotool.mvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tuhanbao.base.Constants;
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
	
	/**
	 * 生成代码用
	 * 
	 * @param module
	 * @return
	 */
	public static DBSrc getDBSrc(String module) {
	    return getDBSrc(ConfigPattern.PRODUCE, module);
	}

	public static DBSrc getDBSrc(ConfigPattern cp, String module) {
	    if (StringUtil.isEmpty(module)) module = Constants.EMPTY;
	    Map<String, DBSrc> map = MODULES.get(cp);
	    if (map == null) return null;
	    return map.get(module);
	}

	public static String getModule(ConfigPattern cp, DBSrc src) {
		Map<String, DBSrc> allDBSrc = getAllModules(cp);
		if (allDBSrc == null) return null;
		
        for (Map.Entry<String, DBSrc> entry : allDBSrc.entrySet()) {
			if (entry.getValue() == src) return entry.getKey();
		}
		return null;
	}
	
	public static Map<ConfigPattern, Map<String, DBSrc>> getAllModules() {
	    return MODULES;
	}

	/**
	 * 生成代码用，所以取生产环境的即可。
	 * 
	 * @return
	 */
	public static Map<String, DBSrc> getModules() {
	    return MODULES.get(ConfigPattern.PRODUCE);
	}

	/**
	 * 生成代码用，所以取生产环境的即可。
	 * 
	 * @return
	 */
	public static Set<ConfigPattern> getAllConfigPatterns() {
	    return MODULES.keySet();
	}

	public static Map<String, DBSrc> getAllModules(ConfigPattern cp) {
		return MODULES.get(cp);
	}
}

