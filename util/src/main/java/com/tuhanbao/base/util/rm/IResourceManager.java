package com.tuhanbao.base.util.rm;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.tuhanbao.base.util.db.conn.ConnectionManager;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.thread.TimerEvent;
import com.tuhanbao.base.util.thread.TimerThreadFactory;

public abstract class IResourceManager
{
    private int minSize;

    private int maxSize;

    //单位分钟
    private int overdueTime;
    
    private boolean needAutoClean = false;
    
    private final Set<IResource> resources = new HashSet<IResource>();

    public IResourceManager(int minSize, int maxSize, int overdueTime, boolean needAutoClean) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        
        this.overdueTime = overdueTime;
        this.needAutoClean = needAutoClean;
        
        init(minSize);
        
        if (this.needAutoClean) {
            TimerThreadFactory.addTimerEvent(new TimerEvent() {
                @Override
                protected void run() throws Exception {
                    clear();
                }
            }, this.overdueTime, TimeUnit.MINUTES);
        }
    }
    
    protected abstract IResource createResource() throws Exception;

    private void init(int num)
    {
        int failNum = 0;
        for (int i = 0; i < num; i++)
        {
            try
            {
                IResource resource = createResource();
                resources.add(resource);
            }
            catch (Exception e)
            {
                LogManager.error(e);
                failNum++;
            }
        }
        
        //一个都没创建成功
        if (num > 0 && failNum == num) throw new MyException(this.getClass().getName() + " create resource fail!");
        if (failNum > 0) LogManager.warn(this.getClass().getName() + " create resource fail : " + failNum);
    }
    


    /**
     * 请谨慎使用
     * 
     * 适用本方法，你必须很清楚什么时候该释放连接，否则很可能导致资源枯竭
     * @return
     * @throws SQLException
     */
    public synchronized IResource getResource() throws Exception
    {
        for (IResource iResource : this.resources)
        {
            if (!iResource.isUsed())
            {
                iResource.use();
                return iResource;
            }
        }
        
        if (this.size() >= ConnectionManager.MAX_SIZE)
        {
            LogManager.warn("resource is full: " + this.getClass().getName());
            waitForResource();
            return getResource();
        }
        else
        {
            IResource resource = createResource();
            resources.add(resource);
            resource.use();
            return resource;
        }
        
    }
    
    public int size()
    {
        return resources.size();
    }
    
    /**
     * 清理闲置连接
     */
    protected void clear() {
        for (IResource iResource : this.resources) {
            if (iResource.isOverdue()) {
                iResource.destroy();
            }
        }
    }

    private void waitForResource()
    {
        try
        {
            this.wait();
        }
        catch (InterruptedException e)
        {
        }
    }

    public synchronized void notifyNext()
    {
        this.notify();
    }

    public int getMinSize() {
        return minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getOverdueTime() {
        return overdueTime;
    }

    public boolean isNeedAutoClean() {
        return needAutoClean;
    }
    
    public void close() {
        for (IResource resource : resources) {
            resource.destroy();
        }
    }
}
