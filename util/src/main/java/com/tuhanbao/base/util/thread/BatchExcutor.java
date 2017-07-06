package com.tuhanbao.base.util.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class BatchExcutor
{
    private int maxNum;
    
    private int curentNum;
    
    private long timeout;
    
    protected BatchExcutor(int maxNum)
    {
        this.maxNum = maxNum;
        this.curentNum = 0;
    }
    
    protected BatchExcutor(int maxNum, long timeout)
    {
        this.maxNum = maxNum;
        this.curentNum = 0;
        this.timeout = timeout;
    }
    
    public synchronized Map<Object, Object> excute(List<BatchExcutorCallable> list)
    {
        Map<Object, Object> result = new HashMap<Object, Object>();
        for (BatchExcutorCallable command : list)
        {
            excuteCommand(command, result);
        }
        
        while (curentNum > 0)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
            }
        }
        
        return result;
    }
    
    public synchronized void finish()
    {
        this.notify();
        this.curentNum--;
    }
    
    public synchronized void excuteCommand(BatchExcutorCallable command, Map<Object, Object> result)
    {
        while (curentNum >= maxNum)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
            }
        }
        curentNum++;
        ScheduledThreadManager.execute(new BatchExcutorRunnable(command, result));
    }
    
    private class BatchExcutorRunnable implements Runnable
    {
        private BatchExcutorCallable command;
        
        private Map<Object, Object> result;
        
        public BatchExcutorRunnable(BatchExcutorCallable command, Map<Object, Object> result)
        {
            this.command = command;
            this.result = result;
        }
        
        @Override
        public void run()
        {
            Future<Object> future = ScheduledThreadManager.submit(command);
            try
            {
                if (timeout <= 0)
                {
                    result.put(command.getKey(), future.get());
                }
                else
                {
                    result.put(command.getKey(), future.get(timeout, TimeUnit.SECONDS));
                }
            }
            catch (Exception e)
            {
                result.put(command.getKey(), null);
            }
            finish();
        }
        
    }
}
