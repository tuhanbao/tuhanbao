package com.tuhanbao.base.util.thread;

import java.util.concurrent.TimeUnit;

import com.tuhanbao.base.util.log.LogManager;

public class ThreadUtil
{
    /**
     * 单位s
     * @param l
     */
    public static final void sleep(double d)
    {
        sleep((long)(d * 1000), TimeUnit.MILLISECONDS);
    }
    
    /**
     * 单位s
     * @param l
     */
    public static final void sleep(long time)
    {
        sleep(time, TimeUnit.SECONDS);
    }
    
    public static final void sleep(long time, TimeUnit unit)
    {
        try
        {
            Thread.sleep(unit.toMillis(time));
        }
        catch (InterruptedException e)
        {
            LogManager.error(e);
        }
    }
}
