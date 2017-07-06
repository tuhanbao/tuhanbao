package com.tuhanbao.base.util.thread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tuhanbao.base.util.log.LogManager;

/**
 * period，此线程执行一次的周期 单位s
 * 加入到其中TimerEvent必须能承受period的延迟
 * 
 * TimerThread并不能精确计时，实际间隔时间为period + 所有events中的事件run方法完成时间总和。
 * 
 * @author tuhanbao
 *
 */
public class TimerThread extends Thread
{
    private List<TimerEvent> events = new ArrayList<TimerEvent>();
    
    private final int period;

    private boolean isActive;

    public TimerThread(String name, int period)
    {
        super(name);
        this.period = period;
        isActive = true;
    }
    
    public void close()
    {
        isActive = false;
    }
    
    @Override
    public void run()
    {
        while (isActive)
        {
            ThreadUtil.sleep(period);
            for (Iterator<TimerEvent> it = events.iterator(); it.hasNext();)
            {
                TimerEvent event = it.next();
                try
                {
                    event.onTimer();
                }
                catch(Exception e)
                {
                    LogManager.error(e);
                }
                
                //如果设置了最高运行次数，运行完之后移除
                if (event.getMaxRunTime() != -1 && event.getRunTime() >= event.getMaxRunTime())
                {
                    removeTimerEvent(event);
                }
            }
        }
    }
    
    public long getPeriod()
    {
        return period;
    }
    
    public void addTimerEvent(TimerEvent event)
    {
        synchronized (events)
        {
            events.add(event);
        }
    }
    
    public void addTimerEventAndRunImmediately(final TimerEvent event)
    {
        addTimerEvent(event);
        
        try
        {
            ScheduledThreadManager.execute(new Runnable() {

				@Override
				public void run() {
					try {
						event.onTimer();
					} catch (Exception e) {
						LogManager.error(e);
					}
				}
            });
        }
        catch(Exception e)
        {
            LogManager.error(e);
        }
    }
    
    public void removeTimerEvent(TimerEvent event)
    {
        synchronized (events)
        {
            events.remove(event);
            if (events.size() == 0) TimerThreadFactory.removeTimerEvent(this.period);
        }
    }
    
}
