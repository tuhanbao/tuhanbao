package com.tuhanbao.thirdapi.cache.mem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.thirdapi.cache.CacheResource;

public final class MemResource extends CacheResource {
    
    private Map<IDataGroup<?>, Map<Object, Object>> CACHE;

    protected MemResource(MemCacheManager manager) {
        super(manager);
        CACHE = new HashMap<>();
    }

    @Override
    public int set(IDataGroup<?> dg, Object key, Object value) {
        Map<Object, Object> cache = getCacheByDataGroupNewIfNotExist(dg);
        if (isExpire(dg)) {
            cache.put(key, new ExpireObject(getExpireTime(dg), value));
        }
        else {
            cache.put(key, value);
        }
        return 1;
    }
    
    private Map<Object, Object> getCacheByDataGroup(IDataGroup<?> dg) {
        return CACHE.get(dg);
    }
    
    private Map<Object, Object> getCacheByDataGroupNewIfNotExist(IDataGroup<?> dg) {
        if (!CACHE.containsKey(dg)) {
            CACHE.put(dg, new HashMap<Object, Object>());
        }
        return CACHE.get(dg);
    }

    @Override
    public void release() {
    }

    @Override
    public int setex(IDataGroup<?> dg, Object key, int seconds, Object value) {
        Map<Object, Object> cache = getCacheByDataGroupNewIfNotExist(dg);
        cache.put(key, new ExpireObject(seconds, value));
        return 1;
    }

    @Override
    public int del(IDataGroup<?> dg, Object key) {
        Map<Object, Object> cache = getCacheByDataGroup(dg);
        return (cache == null || cache.remove(key) == null) ? 0 : 1;
    }

    @Override
    public Object get(IDataGroup<?> dg, Object key) {
        Map<Object, Object> cache = getCacheByDataGroup(dg);
        if (cache == null) return null;
        
        if (isExpire(dg)) {
            ExpireObject eo = (ExpireObject)cache.get(key);
            if (eo != null && eo.isExpire()) {
                cache.remove(key);
                return null;
            }
            else {
                if (eo == null) return null;
                else return eo.getObject();
            }
        }
        else {
            return cache.get(key);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(IDataGroup<?> dg, Object key, Class<T> clazz) {
        Object result = get(dg, key);
        if (result == null) return null;
        return (T)result;
    }
    
    @Override
    public List<Object> get(IDataGroup<?> dg) {
        Map<Object, Object> cache = getCacheByDataGroup(dg);
        if (cache == null) return null;
        
        Collection<Object> values = cache.values();
        List<Object> list = new ArrayList<>(values.size());
        list.addAll(values);
        return list;
    }

    @Override
    public void destroy() {
        this.release();
    }
    
    protected void checkResource() throws Exception {
    }

    @Override
    public void recreateResource() {
    }

}
