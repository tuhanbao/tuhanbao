package com.tuhanbao.base.util.thread;


/**
 * TimerEvent的run方法不能有阻塞操作
 * 
 * @author tuhanbao
 *
 */
public abstract class TimerEvent
{
    //运行次数，-1代表无限运行
    private int maxRunTime = -1;

    private int runTime = 0;
    
    public TimerEvent()
    {
        this(-1);
    }

    public TimerEvent(int maxRunTime)
    {
        this.maxRunTime = maxRunTime;
    }

    public void onTimer() throws Exception
    {
        run();
        runTime++;
    }
    
    public int getRunTime()
    {
        return runTime;
    }

    public void setRunTime(int runTime)
    {
        this.runTime = runTime;
    }
    
    protected abstract void run() throws Exception;

    public int getMaxRunTime()
    {
        return maxRunTime;
    }
}
