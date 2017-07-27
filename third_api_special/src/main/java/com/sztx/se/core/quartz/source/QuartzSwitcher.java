package com.sztx.se.core.quartz.source;

public class QuartzSwitcher {
	
	private static final ThreadLocal<String> quartzContextHolder = new ThreadLocal<String>();

	public static void setQuartzTypeInContext(String quartzContext) {
		quartzContextHolder.set(quartzContext);
	}

	public static String getQuartzType() {
		String quartzType = (String) quartzContextHolder.get();
		return quartzType;
	}

	public static String getQuartzTypeFromContext() {
		String quartzType = (String) quartzContextHolder.get();
		return quartzType;
	}

	public static void clearQuartzContextType() {
		quartzContextHolder.remove();
	}
}
