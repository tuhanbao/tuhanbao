
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
public class CacheManager {
    private static CacheManagerImpl defaultCacheManagerImpl;

    private static CacheManagerImpl localCacheManagerImpl;

    private static volatile boolean hasInit = false;

    public static CacheManagerImpl getLocalCacheManagerImpl() {
        if (localCacheManagerImpl == null) {
            synchronized (CacheManager.class) {
                if (localCacheManagerImpl == null) {
                    localCacheManagerImpl = new CacheManagerImpl(JavaCacheManager.getInstance());
                }
            }
        }
        return localCacheManagerImpl;
    }

    private static void init() throws Exception {
        if (defaultCacheManagerImpl == null) {
            Config baseConfig = ConfigManager.getBaseConfig();
            String cacheKey = null;
            if (baseConfig != null) {
                cacheKey = baseConfig.getValue(BaseConfigUtil.CACHE);
            }

            CacheResourceFactory factory = null;
            if (!StringUtil.isEmpty(cacheKey)) {
                Map<CacheAnnotation, Class<?>> classesWithAnnotation = ClazzUtil.getAllClassesWithAnnotation(null, CacheAnnotation.class);
                for (Entry<CacheAnnotation, Class<?>> item : classesWithAnnotation.entrySet()) {
                    if (StringUtil.isEqual(cacheKey, item.getKey().value())) {
                        factory = (CacheResourceFactory) item.getValue().newInstance();
                        break;
                    }
                }
            } else {
                factory = JavaCacheManager.getInstance();
            }
            defaultCacheManagerImpl = new CacheManagerImpl(factory);
        }
    }

    public static void setCacheResourceFactory(CacheResourceFactory cacheResourceFactory) {
        if (cacheResourceFactory == null) {
            return;
        }
        getDefaultCacheManagerImpl().setCacheResourceFactory(cacheResourceFactory);
    }

    private static CacheManagerImpl getDefaultCacheManagerImpl() {
        if (!hasInit) {
            synchronized (CacheManager.class) {
                if (!hasInit) {
                    try {
                        init();
                    } catch (Throwable e) {
                        throw AppException.getAppException(e);
                    }
                    hasInit = true;
                }
            }
        }
        return defaultCacheManagerImpl;
    }

    public static void init(ICacheKey ck, Map<String, Object> datas) {
        getDefaultCacheManagerImpl().init(ck, datas);
    }

    /**
     * 向缓存中设置对象
     *
     * @param key
     * @param value
     * @return
     */
    public static void set(ICacheKey ck, String key, Object value) {
        getDefaultCacheManagerImpl().set(ck, key, value);
    }

    /**
     * 删除缓存中得对象，根据key
     *
     * @return
     */
    public static int delete(ICacheKey ck, String key) {
        return getDefaultCacheManagerImpl().delete(ck, key);
    }

    /**
     * 删除缓存中所有对象，根据key
     */
    public static void deleteAll(ICacheKey ck) {
        getDefaultCacheManagerImpl().deleteAll(ck);
    }

    /**
     * 删除缓存中所有对象，根据key
     */
    public static void deleteByGroup(ICacheGroupKey groupKey, String groupValue) {
        getDefaultCacheManagerImpl().deleteByGroup(groupKey, groupValue);
    }

    /**
     * 根据key 获取内容
     *
     * @param key
     * @return
     */
    public static Object get(ICacheKey ck, String key) {
        return getDefaultCacheManagerImpl().get(ck, key);
    }

    /**
     * 2016年10月24日
     *
     * @author liuhanhui
     * @param key
     * @return
     */
    public static boolean isExist(ICacheKey ck, String key) {
        return getDefaultCacheManagerImpl().isExist(ck, key);
    }

    /**
     * 根据key 获取内容并转换成对象
     *
     * @param key
     * @return
     */
    public static <T> T get(ICacheKey ck, String key, Class<T> clazz) {
        return getDefaultCacheManagerImpl().get(ck, key, clazz);
    }

    /**
     * servicebean的操作
     */
    public static void save(List<? extends IData> beans) {
        getDefaultCacheManagerImpl().save(beans);
    }

    public static boolean hasCache(ICacheKey ck) {
        return getDefaultCacheManagerImpl().hasCache(ck);
    }

    public static void add(IData data) {
        getDefaultCacheManagerImpl().add(data);
    }

    public static void update(IData data) {
        getDefaultCacheManagerImpl().update(data);
    }

    public static void update(ISyncData data) {
        getDefaultCacheManagerImpl().update(data);
    }

    /**
     * 只支持按主键进行保存，如果不满足条件，redis不会进行处理。返回false
     */
    public static void update(ISyncData data, boolean isSelective) {
        getDefaultCacheManagerImpl().update(data, isSelective);
    }

    public static List<Object> getAll(ICacheKey ck) {
        return getDefaultCacheManagerImpl().getAll(ck);
    }

    public static int count(ICacheKey ck) {
        return getDefaultCacheManagerImpl().count(ck);
    }

    public static Set<String> getAllKeys(ICacheKey ck) {
        return getDefaultCacheManagerImpl().getAllKeys(ck);
    }

    public static List<Object> getAllByGroup(ICacheGroupKey groupKey, String groupValue) {
        return getDefaultCacheManagerImpl().getAllByGroup(groupKey, groupValue);
    }

    /**
     * 支持简单key-value格式
     */
    public static Object get(ICacheKey ck) {
        return getDefaultCacheManagerImpl().get(ck);
    }

    /**
     * 支持简单key-value格式
     */
    public static void set(ICacheKey ck, Object value) {
        getDefaultCacheManagerImpl().set(ck, value);
    }

    public static void delete(ICacheKey ck) {
        getDefaultCacheManagerImpl().delete(ck);
    }
}
