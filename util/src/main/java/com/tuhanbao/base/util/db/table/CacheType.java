package com.tuhanbao.base.util.db.table;

public enum CacheType
{
    /**
     * 自动缓存
     */
    AUTO, 
    
    /**
     * 完全不进行缓存
     */
    NOT_CACHE, 
    
    /**
     * 强行缓存所有 
     */
    CACHE_ALL;
    

    public static CacheType getCacheType(String str) {
        if ("auto".equalsIgnoreCase(str)) return CacheType.AUTO;
        if ("all".equalsIgnoreCase(str)) return CacheType.CACHE_ALL;
        return CacheType.NOT_CACHE;
    }
}
