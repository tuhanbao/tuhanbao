package com.td.ca.base.util.rm;

import com.td.ca.base.util.config.Config;
import com.td.ca.base.util.config.ConfigManager;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.objutil.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangbing
 */
@Slf4j
public class MutiLocaleResourceImpl implements ResourceImpl {
    private static final String KEY = "language";

    protected Map<String, Map<String, String>> localResources = new HashMap<>();

    private ReentrantLock lock = new ReentrantLock();

    private static final ThreadLocal<String> LOCALE_CACHE = new ThreadLocal<>();

    @Override
    public void addResource(String key, String value) {
        addResource(getLocale(), key, value);
    }

    @Override
    public void addResource(String locale, String key, String value) {
        log.info("addResource, " + key + ": " + value);

        lock.lock();
        try {
            getLocalResources(locale).put(key, value);
        } finally {
            lock.unlock();
        }
    }

    private Map<String, String> getLocalResources() {
        return getLocalResources(getLocale());
    }

    private Map<String, String> getLocalResources(String locale) {
        checkLocale(locale);
        return localResources.get(getLocale());
    }

    private void checkLocale(String locale) {
        if (!localResources.containsKey(locale)) {
            lock.lock();
            try {
                if (!localResources.containsKey(locale)) {
                    localResources.put(locale, initLocalResource(locale));
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private Map<String, String> initLocalResource(String locale) {
        log.info("init language properties  url :" + locale);
        Config config = ConfigManager.getConfig(KEY + "_" + locale);
        if (config == null) {
            throw new AppException("have not this locale : " + locale);
        }
        // config获取getProperties是不可更改的map，这里需要自己重新new一份
        return new HashMap<>(config.getProperties());
    }

    @Override
    public void removeResource(String key) {
        removeResource(getLocale(), key);
    }

    public void removeResource(String locale, String key) {
        lock.lock();
        try {
            Map<String, String> map = localResources.get(locale);
            if (map != null) {
                map.remove(key);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String getResource(String key, String... args) {
        if (StringUtil.isEmpty(key)) {
            return null;
        }
        key = key.toLowerCase();
        String msg = getLocalResources().get(key);
        if (msg == null) {
            return key;
        }
        return replaceArgs(msg, args);
    }

    private static String replaceArgs(String msg, String... args) {
        return StringUtil.formatMessage(msg, args);
    }

    @Override
    public String getLocale() {
        String locale = LOCALE_CACHE.get();
        if (StringUtil.isEmpty(locale)) {
            return DefaultResourceImpl.ZH;
        } else {
            return locale;
        }
    }

    @Override
    public void setLocale(String locale) {
        LOCALE_CACHE.set(locale);
    }

    @Override
    public void refreshLocale() {
        // do nothing
    }

    public Set<String> getAllLocales() {
        return this.localResources.keySet();
    }
}
