package com.tuhanbao.base.util.thread;

import java.util.concurrent.Callable;

public abstract class BatchExcutorCallable implements Callable<Object>
{
    private Object key;
    
    public BatchExcutorCallable(Object key)
    {
        this.key = key;
    }
    
    public Object getKey()
    {
        return key;
    }

}
