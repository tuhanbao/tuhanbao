package com.tuhanbao.base.util.objutil;

import java.util.HashMap;
import java.util.Map;


/**
 * 时间区间
 * @author tuhanbao
 *
 */
public class Duration {
	private long startTime, endTime;
	
	public static long MIN_VALUE = 0;
	public static long MAX_VALUE = Long.MAX_VALUE / 2 - 1;
	
	private static Map<Integer, Duration> DAY_CACHE = new HashMap<Integer, Duration>();
	private static Map<Integer, Duration> MONTH_CACHE = new HashMap<Integer, Duration>();
	private static Map<Integer, Duration> YEAR_CACHE = new HashMap<Integer, Duration>();
	
	public Duration(long time) {
		this(time, time);
	}

	/**
	 * 
	 * @param startTime 0表示到目前为止
	 * @param endTime 最大为MAX_VALUE，否则可能会出错
	 */
	public Duration(long startTime, long endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * 融合两个时间，返回null表示无法融合
	 * @param d1
	 * @param d2
	 * @return
	 */
	public boolean meger(Duration d) {
		//只有当两个时间交叉时才可以融合
		long min = this.startTime < d.startTime ? this.startTime : d.startTime;
		long max = this.endTime > d.endTime ? this.endTime : d.endTime;
		//间隔1s就算可以时间相连
		if ((max - min) > (this.endTime - this.startTime + d.endTime - d.startTime + TimeUtil.SEC2MILL)) {
			return false;
		}
		else {
			this.startTime = min;
			this.endTime = max;
			return true;
		}
	}
	
	/**
	 * 按天的区间，直接缓存
	 * @param time
	 * @return
	 */
	public static Duration getDay(long time)
	{
		int[] ymd = TimeUtil.getYearMonthDayHour(time);
		return getDay(ymd[0], ymd[1], ymd[2]);
	}
	
	public static Duration getDay(int year, int month, int day)
	{
		int key = year * 10000 + month * 100 + day;
		if (!DAY_CACHE.containsKey(key))
		{
			DAY_CACHE.put(key, new Duration(TimeUtil.getTime(year, month, day, 0, 0, 0), TimeUtil.getTime(year, month, day, 23, 59, 59) + 999));
		}
		return DAY_CACHE.get(key);
	}
	
	/**
	 * getMonth
	 * @param time
	 * @return
	 */
	public static Duration getMonth(int year, int month)
	{
		int key = year * 100 + month;
		if (!MONTH_CACHE.containsKey(key))
		{
			long monthFirstDay = TimeUtil.getFirstDay(year, month);
			long monthLastDay = TimeUtil.getLastDay(year, month);
			MONTH_CACHE.put(key, new Duration(monthFirstDay, monthLastDay));
		}
		return MONTH_CACHE.get(key);
	}
	
	/**
	 * getMonth
	 * @param time
	 * @return
	 */
	public static Duration getYear(int year)
	{
		int key = year;
		if (!YEAR_CACHE.containsKey(key))
		{
			long yearFirstDay = TimeUtil.getFirstDay(year);
			long yearLastDay = TimeUtil.getLastDay(year);
			YEAR_CACHE.put(key, new Duration(yearFirstDay, yearLastDay));
		}
		return YEAR_CACHE.get(key);
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}
	
	/**
	 * ********特别注意
	 * 
	 * 缓存中的Duration，绝对不允许修改
	 * @param startTime
	 */
//	public void setStartTime(long startTime) {
//		this.startTime = startTime;
//	}
//
//	public void setEndTime(long endTime) {
//		this.endTime = endTime;
//	}

	/**
	 * 传入的区间是否在本区间内
	 * @param duration
	 * @return
	 */
	public boolean isIn(Duration duration) {
		return duration.startTime >= this.startTime && duration.endTime <= this.endTime;
	}
	
	public boolean isIn(long time) {
		return time >= this.startTime && time <= this.endTime;
	}
	
	public String toString()
	{
		if (this.startTime == this.endTime)
		{
			int[] ymd = TimeUtil.getYearMonthDayHour(startTime);
			return TimeUtil.getYmdStr(ymd);
		}
		else
		{
			int[] ymd1 = TimeUtil.getYearMonthDayHour(startTime);
			int[] ymd2 = TimeUtil.getYearMonthDayHour(endTime);
			return TimeUtil.getYmdStr(ymd1) + "~" + TimeUtil.getYmdStr(ymd2);
		}
	}
	
	public int hashCode() {
		return (int)(this.startTime + this.endTime);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null) return false;
		if (this == o) return true;
		
		Duration duration = (Duration) o;
		return duration.startTime == this.startTime && duration.endTime == this.endTime;
	}

	public long getTime() {
		return this.getEndTime() - this.getStartTime();
	}
}
