
package com.td.ca.base.util.cache;

import com.td.ca.base.dataservice.IData;
import com.td.ca.base.dataservice.IDataGroup;
import com.td.ca.base.dataservice.IGroupKeyValueCacheBean;
import com.td.ca.base.dataservice.ISyncData;
import com.td.ca.base.dataservice.cache.CacheResourceFactory;
import com.td.ca.base.dataservice.cache.ICacheGroupKey;
import com.td.ca.base.dataservice.cache.ICacheKey;
import com.td.ca.base.dataservice.cache.ICacheResource;
import com.td.ca.base.util.cache.mem.JavaCacheManager;
import com.td.ca.base.util.clazz.ClazzUtil;
import com.td.ca.base.util.config.BaseConfigUtil;
import com.td.ca.base.util.config.Config;
import com.td.ca.base.util.config.ConfigManager;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.objutil.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author wangbing
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class CacheManagerImpl {
    private CacheResourceFactory factory;

    public CacheManagerImpl(CacheResourceFactory factory) {
        this.factory = factory;
        if (factory != null) {
            factory.init();
        }
    }

    public void setCacheResourceFactory(CacheResourceFactory factory) {
        if (factory != null) {
            factory.init();
        }
        this.factory = factory;
    }

    private CacheResourceFactory getCacheResourceFactory() {
        return factory;
    }

    public void init(ICacheKey ck, Map<String, Object> datas) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            cacheResource.init(ck, datas);

            ICacheGroupKey[] groupKeys = ck.getCacheGroupKeys();
            if (groupKeys != null) {
                for (ICacheGroupKey groupKey : groupKeys) {
                    Map<String, Object> groupDatas = new HashMap<>();
                    for (Entry<String, Object> entry : datas.entrySet()) {
                        Object value = entry.getValue();
                        String groupKeyValue = groupKey.getGroupKeyValue(value);
                        Set<Object> set;
                        if (!groupDatas.containsKey(groupKeyValue)) {
                            set = new HashSet<>();
                            groupDatas.put(groupKeyValue, set);
                        } else {
                            set = (Set<Object>) groupDatas.get(groupKeyValue);
                        }
                        set.add(entry.getKey());
                        if (value instanceof IGroupKeyValueCacheBean) {
                            ((IGroupKeyValueCacheBean) value).setGroupKeyValue(groupKey, groupKeyValue);
                        }
                    }
                    cacheResource.init(groupKey, groupDatas);
                }
            }

        } finally {
            cacheResource.release();
        }
    }

    /**
     * 向缓存中设置对象
     *
     * @param key
     * @param value
     * @return
     */
    public void set(ICacheKey ck, String key, Object value) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            // 更新分组信息
            setGroupValues(cacheResource, ck, key, value);
            cacheResource.set(ck, key, value);
        } finally {
            cacheResource.release();
        }
    }

    private void setGroupValues(ICacheResource cacheResource, ICacheKey ck, String key, Object value) {
        ICacheGroupKey[] groupKeys = ck.getCacheGroupKeys();
        // 一个数据的分组变了，需要移动group
        if (groupKeys != null && groupKeys.length > 0) {
            Object oldValue = get(ck, key);
            for (ICacheGroupKey groupKey : groupKeys) {
                String oldGroupKeyValue;
                if (oldValue == null) {
                    oldGroupKeyValue = null;
                } else {
                    if (oldValue instanceof IGroupKeyValueCacheBean) {
                        oldGroupKeyValue = ((IGroupKeyValueCacheBean) oldValue).getGroupKeyValue(groupKey);
                    } else {
                        oldGroupKeyValue = groupKey.getGroupKeyValue(oldValue);
                    }
                }
                String newGroupKeyValue = value == null ? null : groupKey.getGroupKeyValue(value);
                if (value instanceof IGroupKeyValueCacheBean) {
                    ((IGroupKeyValueCacheBean) value).setGroupKeyValue(groupKey, newGroupKeyValue);
                }

                if (!StringUtil.isEqual(oldGroupKeyValue, newGroupKeyValue)) {
                    removeGroupKeyValue(cacheResource, groupKey, oldGroupKeyValue, key);
                    addGroupKeyValue(cacheResource, groupKey, newGroupKeyValue, key);
                }
            }
        }
    }

    private void removeGroupKeyValue(ICacheResource cacheResource, ICacheGroupKey groupKey, String keyValue, Object value) {
        if (StringUtil.isEmpty(keyValue)) {
            return;
        }
        Set set = getGroupValue(groupKey, keyValue);
        if (set != null) {
            set.remove(value);
            // 本地缓存其实不需要这一步，兼容外部缓存，需要重设
            cacheResource.set(groupKey, keyValue, set);
        }
    }

    private void addGroupKeyValue(ICacheResource cacheResource, ICacheGroupKey groupKey, String key, Object value) {
        if (StringUtil.isEmpty(key) || value == null) {
            return;
        }
        Set set = getGroupValue(groupKey, key);
        if (set == null) {
            set = new HashSet<>();
        }
        set.add(value);
        cacheResource.set(groupKey, key, set);
    }

    private Set getGroupValue(ICacheGroupKey groupKey, String key) {
        return get(groupKey, key, Set.class);
    }

    /**
     * 删除缓存中得对象，根据key
     *
     * @return
     */
    public int delete(ICacheKey ck, String key) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            setGroupValues(cacheResource, ck, key, null);
            return cacheResource.del(ck, key);
        } finally {
            cacheResource.release();
        }
    }

    /**
     * 删除缓存中所有对象，根据key
     */
    public void deleteAll(ICacheKey ck) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            deleteGroupValues(cacheResource, ck);
            cacheResource.deleteAll(ck);
        } finally {
            cacheResource.release();
        }
    }

    /**
     * 删除缓存中所有对象，根据key
     */
    public void deleteByGroup(ICacheGroupKey groupKey, String groupValue) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            Set set = getGroupValue(groupKey, groupValue);
            if (set != null) {
                ICacheKey ck = groupKey.getParentKey();
                for (Object item : set) {
                    cacheResource.del(ck, item.toString());
                }
            }
            cacheResource.del(groupKey, groupValue);
        } finally {
            cacheResource.release();
        }
    }

    private void deleteGroupValues(ICacheResource cacheResource, ICacheKey ck) {
        ICacheGroupKey[] groupKeys = ck.getCacheGroupKeys();
        if (groupKeys != null) {
            for (ICacheGroupKey groupKey : groupKeys) {
                cacheResource.deleteAll(groupKey);
            }
        }
    }

    /**
     * 根据key 获取内容
     *
     * @param key
     * @return
     */
    public Object get(ICacheKey ck, String key) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            return cacheResource.get(ck, key);
        } finally {
            cacheResource.release();
        }
    }

    /**
     * 2016年10月24日
     *
     * @author liuhanhui
     * @param key
     * @return
     */
    public boolean isExist(ICacheKey ck, String key) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            return cacheResource.exists(ck, key);
        } finally {
            cacheResource.release();
        }
    }

    /**
     * 根据key 获取内容并转换成对象
     *
     * @param key
     * @return
     */
    public <T> T get(ICacheKey ck, String key, Class<T> clazz) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            return cacheResource.get(ck, key, clazz);
        } finally {
            cacheResource.release();
        }
    }

    /**
     * servicebean的操作
     */
    public void save(List<? extends IData> beans) {
        if (beans == null || beans.isEmpty()) {
            return;
        }

        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            for (IData bean : beans) {
                cacheResource.set(bean.getDataGroup(), bean.getKeyValue().toString(), bean);
            }
        } finally {
            cacheResource.release();
        }
    }

    public boolean hasCache(ICacheKey ck) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            return cacheResource.hasCache(ck);
        } finally {
            cacheResource.release();
        }
    }

    public void add(IData data) {
        save(data);
    }

    public void update(IData data) {
        save(data);
    }

    private void save(IData data) {
        if (data == null) {
            return;
        }

        set(data.getDataGroup(), data.getKeyValue().toString(), data);
    }

    public void update(ISyncData data) {
        update(data, false);
    }

    /**
     * 只支持按主键进行保存，如果不满足条件，redis不会进行处理。返回false
     */
    public void update(ISyncData data, boolean isSelective) {
        if (data == null) {
            return;
        }
        updateData(data, isSelective);
    }

    private void updateData(ISyncData data, boolean isSelective) {
        IDataGroup<?> dg = data.getDataGroup();
        ISyncData oldValue = (ISyncData) get(dg, data.getKeyValue().toString());
        if (isSelective) {
            oldValue.sync(data);
        } else {
            oldValue = data;
        }
        save(oldValue);
    }

    public List<Object> getAll(ICacheKey ck) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            return cacheResource.getAll(ck);
        } finally {
            cacheResource.release();
        }
    }

    public int count(ICacheKey ck) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            return cacheResource.len(ck);
        } finally {
            cacheResource.release();
        }
    }

    public Set<String> getAllKeys(ICacheKey ck) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            return cacheResource.getAllKeys(ck);
        } finally {
            cacheResource.release();
        }
    }

    public List<Object> getAllByGroup(ICacheGroupKey groupKey, String groupValue) {
        Set set = getGroupValue(groupKey, groupValue);
        List<Object> list = new ArrayList<>();
        if (set != null) {
            ICacheKey ck = groupKey.getParentKey();
            for (Object item : set) {
                Object e = get(ck, item.toString());
                if (e != null) {
                    list.add(e);
                }
            }
        }
        return list;
    }

    /**
     * 支持简单key-value格式
     */
    public Object get(ICacheKey ck) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            return cacheResource.get(ck);
        } finally {
            cacheResource.release();
        }
    }

    /**
     * 支持简单key-value格式
     */
    public void set(ICacheKey ck, Object value) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            cacheResource.set(ck, value);
        } finally {
            cacheResource.release();
        }
    }

    public void delete(ICacheKey ck) {
        ICacheResource cacheResource = getCacheResourceFactory().getCacheResource();
        try {
            cacheResource.del(ck);
        } finally {
            cacheResource.release();
        }
    }
}
