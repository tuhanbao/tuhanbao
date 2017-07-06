package com.tuhanbao.base.util.rm;

import java.util.concurrent.TimeUnit;

import com.tuhanbao.base.util.objutil.TimeUtil;

public abstract class IResource {
    
    protected IResourceManager manager;

    private long lastConnectTime;
    
    private boolean isUsed = false;
    
    public IResource(IResourceManager manager) {
        this.manager = manager;
    }

    public void use() throws Exception {
        checkResource();
        this.isUsed = true;
    }

    public void release() {
        this.isUsed = false;
        manager.notifyNext();
    }

    public boolean isUsed() {
        return this.isUsed;
    }

    protected boolean isActive() {
        return true;
    }


    public boolean isOverdue() {
        //小于0表示不检查
        if (manager.getOverdueTime() < 0) return false;
        return !this.isUsed && TimeUtil.isOverdue(lastConnectTime, manager.getOverdueTime(), TimeUnit.MINUTES);
    }

    protected void checkResource() throws Exception {
        if (!this.isActive() || isOverdue()) {
            recreateResource();
        }
    }

    public abstract void destroy();

    public abstract void recreateResource() throws Exception;
}
