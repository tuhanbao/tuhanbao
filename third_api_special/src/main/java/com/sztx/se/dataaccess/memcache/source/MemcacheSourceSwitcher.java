package com.sztx.se.dataaccess.memcache.source;



/**
 * 
 * @author zhihongp
 *
 */
public class MemcacheSourceSwitcher {

	private static final ThreadLocal<String> memcacheContextHolder = new ThreadLocal<String>();

	public static void setMemcacheSourceTypeInContext(String memcacheSourceType) {
		memcacheContextHolder.set(memcacheSourceType);
	}
	
	public static String getMemcacheSourceType() {
		String memcacheSourceType = (String) memcacheContextHolder.get();
		return memcacheSourceType;
	}
	
	public static String getMemcacheSourceFromContext() {
		String memcacheSourceType = (String) memcacheContextHolder.get();
		return memcacheSourceType;
	}
	
	public static void clearMemcacheSourceType() {
		memcacheContextHolder.remove();
	}
}
