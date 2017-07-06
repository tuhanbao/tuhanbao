package com.tuhanbao.thirdapi.cache;

public class NoExpireCacheKey extends AbstractCacheKey {
    
    private String name;
    
    public NoExpireCacheKey(String name) {
        this.name = name;
    }

    @Override
    public boolean isExpire() {
        return false;
    }

    @Override
    public int getExpireTime() {
        return -1;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
