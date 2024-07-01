package com.td.ca.base.util.rm;

import com.td.ca.base.Constants;
import com.td.ca.base.util.config.BaseConfigUtil;
import com.td.ca.base.util.config.Config;
import com.td.ca.base.util.config.ConfigManager;
import com.td.ca.base.util.config.ConfigRefreshListener;
import com.td.ca.base.util.objutil.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangbing
 */
@Slf4j
public class DefaultResourceImpl implements ResourceImpl, ConfigRefreshListener {
    private static final String KEY = "language";

    public static final String DEFAULT = "default";

    public static final String ZH = "zh";
    public static final String EN = "en";

    private Config config = null;

    private String locale;

    private boolean hasInited = false;

    public DefaultResourceImpl() {
        ConfigManager.addListener(this);
    }

    @Override
    public void addResource(String key, String value) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        Config config = getConfig();
        if (config != null) {
            config.putProperty(key.toUpperCase(), value);
        }
    }

    @Override
    public void removeResource(String key) {
        if (StringUtil.isEmpty(key)) {
            return;
        }
        Config config = getConfig();
        if (config != null) {
            config.removeProperty(key.toUpperCase());
        }
    }

    @Override
    public String getResource(String key, String... args) {
        Config config = getConfig();
        if (config == null && !hasInited) {
            init();
        }
        if (StringUtil.isEmpty(key)) {
            return null;
        }
        key = key.toLowerCase();
        config = getConfig();
        String msg = config == null ? null : config.getValue(key);
        if (msg == null) {
            return key;
        }
        return replaceArgs(msg, args);
    }

    @Override
    public String getLocale() {
        return this.locale;
    }

    @Override
    public void setLocale(String locale) {
        // donothing
    }

    public Config getConfig() {
        return config;
    }

    private static String replaceArgs(String msg, String... args) {
        return StringUtil.formatMessage(msg, args);
    }

    private void init() {
        reloadLocal();
        config = ConfigManager.getConfig(KEY + "_" + locale);

        if (config == null) {
            log.warn("no resource for {}", KEY);
        } else {
            ResourceManager.notifyListeners();
        }
        this.hasInited = true;
    }

    @Override
    public void refresh() {
        init();
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public void refreshLocale() {
        init();
    }

    public void reloadLocal() {
        Config baseConfig = ConfigManager.getBaseConfig();
        String locale = baseConfig == null ? Constants.EMPTY : baseConfig.getValue(BaseConfigUtil.LANGUAGE);
        this.locale = getLocalKey(locale);
    }

    public static String getLocalKey(String locale) {
        if (StringUtil.isEmpty(locale) || DEFAULT.equals(locale)) {
            locale = ZH;
        }
        return locale;
    }
}
