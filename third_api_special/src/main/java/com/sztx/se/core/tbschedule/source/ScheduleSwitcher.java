package com.sztx.se.core.tbschedule.source;

public class ScheduleSwitcher {
	
	private static final ThreadLocal<String> scheduleContextHolder = new ThreadLocal<String>();

	public static void setScheduleTypeInContext(String scheduleContext) {
		scheduleContextHolder.set(scheduleContext);
	}

	public static String getScheduleType() {
		String scheduleType = (String) scheduleContextHolder.get();
		return scheduleType;
	}

	public static String getScheduleTypeFromContext() {
		String scheduleType = (String) scheduleContextHolder.get();
		return scheduleType;
	}

	public static void clearScheduleContextType() {
		scheduleContextHolder.remove();
	}
}
