package com.tuhanbao.base.util.objutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.MyException;

public final class TimeUtil
{
    public static final int TIME_SPEED = 1;//ConfigManager.getBaseConfig().getInt(BaseConfigConstants.TIME_SPEED); 

    //秒换成ms
    public static final long SEC2MILL = 1000L;

    //分钟换成s
    public static final int MIN2SEC = 60;

    //小时换成s
    public static final int HOUR2SEC = 3600;

    //分钟换成ms
    public static final long MIN2MILL = 60L * SEC2MILL;

    //小时换成ms
    public static final long HOUR2MILLS = 60L * MIN2MILL;

    //天换成ms
    public static final long DAY2MILLS = 24L * HOUR2MILLS;
    
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
    
    public static long now()
    {
        return System.currentTimeMillis();
    }
    
    public static String nowStr()
    {
    	return getDateFormat("MM-dd HH:mm:ss SSS").format(new Date());
    }
    
    public static String nowStr(String format)
    {
    	return getDateFormat(format).format(new Date());
    }

    public static String getTimeStr(String format, long time)
    {
    	return getDateFormat(format).format(new Date(time));
    }
    
    /**
     * 传入的时分秒，以当前时区为准
     * @param hour
     * @param min
     * @param sec
     * @return
     */
    public static long getTodayTime(int hour, int min, int sec)
    {
        return getTodayTime(now(), hour, min, sec);
    }
    
    /**
     * 需要考虑时区
     * @param hour
     * @param min
     * @param sec
     * @return
     */
    public static long getTodayTime(long time, int hour, int min, int sec)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    
    /**
     * args从年开始，有几个设置几个
     * @param args
     * @return
     */
    public static long getTime(int ... args)
    {
        Calendar calendar = Calendar.getInstance();
        int i= 0;
        int length = args.length;
        if (length > i) {
            calendar.set(Calendar.YEAR, args[i]);
        }
        i++;
        
        if (length > i) {
            calendar.set(Calendar.MONTH, args[i] - 1);
        }
        else {
            calendar.set(Calendar.MONTH, 0);
        }
        i++;
        
        if (length > i) {
            calendar.set(Calendar.DAY_OF_MONTH, args[i]);
        }
        else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        i++;
        
        if (length > i) {
            calendar.set(Calendar.HOUR_OF_DAY, args[i]);
        }
        else {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
        }
        i++;

        if (length > i) {
            calendar.set(Calendar.MINUTE, args[i]);
        }
        else {
            calendar.set(Calendar.MINUTE, 0);
        }
        i++;

        if (length > i) {
            calendar.set(Calendar.SECOND, args[i]);
        }
        else {
            calendar.set(Calendar.SECOND, 0);
        }
        i++;
        
        if (length > i) {
            calendar.set(Calendar.MILLISECOND, args[i]);
        }
        else {
            calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTimeInMillis();
    }
    
    public static long getFirstDay(int year, int month)
    {
        return getTime(year, month, 1, 0, 0, 0);
    }
    
    public static long getFirstDay(int year)
    {
        return getFirstDay(year, 1);
    }
    
    public static long getLastDay(int year)
    {
        return getLastDay(year, 12);
    }
    
    public static long getLastDay(int year, int month)
    {
    	Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }
    
    public static int getLastDayOfMonth(int year, int month)
    {
    	Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * 是否已过期
     * @param time 开始时间
     * @param overdue 过期时间，单位s
     * @return
     */
    public static boolean isOverdue(long time, long overdue)
    {
        return isOverdue(time, overdue, TimeUnit.SECONDS);
    }
    
    /**
     * 是否已过期
     * @param time
     * @param overdue 过期时间
     * @param unit overdue的时间单位
     * @return
     */
    public static boolean isOverdue(long time, long overdue, TimeUnit unit)
    {
        return isOverdue(time, now(), overdue, unit);
    }
    
    /**
     * 获取还剩余的时间
     * @param time 开始时间
     * @param term 期限
     * @return
     */
    public static long getRemainTime(long time, long term)
    {
        return getRemainTime(time, term, TimeUnit.SECONDS);
    }
    
    public static long getRemainTime(long time, long term, TimeUnit unit)
    {
        return time + (unit.toMillis(term) / TIME_SPEED) - now();
    }

    /**
     * 是否过期
     * @param start
     * @param end
     * @param overdue
     * @param unit overdue的时间单位
     * @return
     */
    public static boolean isOverdue(long start, long end, long overdue, TimeUnit unit)
    {
        long gap = end - start;
        return gap >= (unit.toMillis(overdue) / TIME_SPEED);
    }
    
    /**
     * 获取今天是星期几
     * 1代表周日，2代表周一，....7代表周六
     * @return
     */
    public static int getDayOfWeekToday()
    {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    
    /**
     * 获取当前时间是星期几
     * 1代表周日，2代表周一，....7代表周六
     * @return
     */
    public static int getDayOfWeek(long time)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    
    public static long nextTime(long time)
    {
        return nextTime(time, TimeUnit.SECONDS);
    }

    public static long nextTime(long time, TimeUnit unit)
    {
        return now() + (unit.toMillis(time) / TIME_SPEED) ;
    }
    
    public static long getTime(long time)
    {
        return getTime(time, TimeUnit.SECONDS);
    }
    
    public static Date parse(String format, String source) {
        try {
            return getDateFormat(format).parse(source);
        }
        catch (ParseException e) {
            throw MyException.getMyException(e);
        }
    }

    public static long getTimeByDay(String dateStr)
    {
    	return getTime(dateStr, "yyyy-MM-dd", Calendar.HOUR_OF_DAY);
    }
    
    public static long getTime(String dateStr)
    {
    	return getTime(dateStr, "yyyy-MM-dd HH:mm:ss", Calendar.MILLISECOND);
    }
    
    public static long getLastSecondOfDay(long time) {
        int ymd[] = getYearMonthDayHour(time);
        return getTime(ymd[0], ymd[1], ymd[2], 23, 59, 59);
    }
    
    public static long getFirstSecondOfDay(long time) {
        int ymd[] = getYearMonthDayHour(time);
        return getTime(ymd[0], ymd[1], ymd[2], 0, 0, 0);
    }
    
    /**
     * 暂时只支持省略时分秒毫秒
     * @param dateStr
     * @param format
     * @param deleteUnit 省略多少单位之后的数据
     * @return
     */
	public static long getTime(String dateStr, String format, int deleteUnit)
    {
        Date date;
		try {
			date = getDateFormat(format).parse(dateStr);
		} catch (ParseException e) {
			throw MyException.getMyException(e);
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		if (deleteUnit <= Calendar.MILLISECOND) calendar.set(Calendar.MILLISECOND, 0);
		if (deleteUnit <= Calendar.SECOND) calendar.set(Calendar.SECOND, 0);
		if (deleteUnit <= Calendar.MINUTE) calendar.set(Calendar.MINUTE, 0);
		if (deleteUnit <= Calendar.HOUR_OF_DAY) calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTimeInMillis();
    }

    public static long getTime(long time, TimeUnit unit)
    {
        return unit.toMillis(time) / TIME_SPEED ;
    }
    
    /**
     * Calendar中1代表周日，7代表周六
     * 获取day之后的n天是星期几
     * @param day 周几
     * @param n
     * @return
     */
    public static int getWeekDayAfterDay(int day, int n)
    {
        return (day + n - 1) % 7 + 1;
    }
    
    /**
     * 返回当前时间是从2000年开始的第多少天 
     * @return
     */
    public static int getDayFromZeroDay()
    {
        //这里不需要-1,因为此方法一般用于计算相对天数。
        return (int) (now() / DAY2MILLS);
    }
    
    public static int[] getTodayYearMonthDayHour()
    {
        return getYearMonthDayHour(now());
    }
    
    /**
     * 返回年月日
     * @return
     */
    public static int[] getYearMonthDayHour(long time)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        return new int[]{year, month, day, hour, min, sec};
    }
    
    /**
     * 返回年月日
     * @return
     */
    public static Calendar getCalendar(long time)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }
	
    public static String getYmdStr(int[] ymd)
    {
    	return getYmdStr(ymd, Constants.MID_LINE, Constants.BLANK, Constants.COLON);
    }
    
    public static String getYmdStr(int[] ymd, String ymdGap)
    {
    	return getYmdStr(ymd, ymdGap, Constants.BLANK, Constants.COLON);
    }
    
    /**
     * 不支持毫秒
     * @param ymd
     * @param gap
     * @return
     */
	public static String getYmdStr(int[] ymd, String ymdGap, String midGap, String hmsGap)
	{
		if (ymd == null || ymd.length == 0) return Constants.EMPTY;
		StringBuilder sb = new StringBuilder();
		
		int length = ymd.length;
		int ymdLength = length < 3 ? length : 3;
		sb.append(ymd[0]);
		for (int i = 1; i < ymdLength; i++) {
			//年月日
			sb.append(ymdGap);
			sb.append(String.format("%02d", ymd[i]));
		}
		
		if (length > 3) {
			int hmsLength = length < 6 ? length : 6;
			//时分秒
			sb.append(midGap);
			sb.append(String.format("%02d", ymd[3]));
			for (int i = 4; i < hmsLength; i++) {
				sb.append(Constants.COLON);
				sb.append(String.format("%02d", ymd[i]));
			}
		}
		return sb.toString();
	}
    
    /**
     * SimpleDateFormat是线程不安全的，必须每次都new
     * 
     * @param format
     * @return
     */
    public static SimpleDateFormat getDateFormat(String format)
    {
        return new SimpleDateFormat(format);
    }
}
