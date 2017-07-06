package com.tuhanbao.autotool.mvc;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.db.conn.DBSrc;
import com.tuhanbao.base.util.objutil.StringUtil;

public class ModuleManager {
	private static final Map<String, DBSrc> MODULE = new HashMap<>();
	
	public static void addModule(String module, DBSrc src) {
		if (StringUtil.isEmpty(module)) module = Constants.EMPTY;
		MODULE.put(module, src);
	}
	
	public static DBSrc getDBSrc(String module) {
	    if (StringUtil.isEmpty(module)) module = Constants.EMPTY;
	    return MODULE.get(module);
	}
	
	public static String getModule(DBSrc src) {
        for (Map.Entry<String, DBSrc> entry : MODULE.entrySet()) {
			if (entry.getValue() == src) return entry.getKey();
		}
		return null;
	}
	
	public static Map<String, DBSrc> getAllModules() {
		return MODULE;
	}
}

