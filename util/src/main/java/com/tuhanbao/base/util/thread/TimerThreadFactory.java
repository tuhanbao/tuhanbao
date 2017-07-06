package com.tuhanbao.base.util.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 定时执行器生成工厂
 *
 */
public final class TimerThreadFactory
{
    private static final Map<Integer, TimerThread> timers = new HashMap<Integer, TimerThread>();
    
    public static void stop()
    {
        //timer关闭后，在下次执行时会自动关闭线程
        for (TimerThread timer : timers.values())
        {
            timer.close();
        }
    }
    
    /**
     * 每隔多少时间执行一次
     * @param event
     * @param seconds
     */
    public static void addTimerEvent(TimerEvent event, int time, TimeUnit unit)
    {
        addTimerEvent(event, time, unit, false);
    }
    
    public static void removeTimerEvent(int seconds)
    {
        if (timers.containsKey(seconds))
        {
            timers.remove(seconds).close();
        }
    }
    
    /**
     * 每隔多少时间执行一次
     * @param event
     * @param seconds
     */
    public synchronized static void addTimerEvent(TimerEvent event, int time, TimeUnit unit, boolean excuteImmediately)
    {
        int seconds = (int) unit.toSeconds(time);
        if (!timers.containsKey(seconds))
        {
            TimerThread value = new TimerThread(time + unit.toString(), seconds);
            timers.put(seconds, value);
            value.start();
        }
        if (excuteImmediately) timers.get(seconds).addTimerEventAndRunImmediately(event);
        else timers.get(seconds).addTimerEvent(event);
    }
    
    public static void removeTimerEvent(TimerEvent event, int time, TimeUnit unit) {
		int seconds = (int) unit.toSeconds(time);
		if (timers.containsKey(seconds))
        {
            timers.get(seconds).removeTimerEvent(event);
        }
	}
}
