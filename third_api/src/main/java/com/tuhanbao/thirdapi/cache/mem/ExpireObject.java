package com.tuhanbao.thirdapi.cache.mem;

import java.util.concurrent.TimeUnit;

import com.tuhanbao.base.util.objutil.TimeUtil;

public class ExpireObject {

    private long time;
    
    private Object object;
    
    private long expireTime;

    public ExpireObject(int expireTime, Object object) {
        this.expireTime = expireTime;
        this.object = object;
        this.time = TimeUtil.now();
    }

    public long getTime() {
        return time;
    }

    public Object getObject() {
        return object;
    }
    
    public long getExpireTime() {
        return expireTime;
    }

    public boolean isExpire() {
        return TimeUtil.isOverdue(time, expireTime, TimeUnit.SECONDS);
    }
}
