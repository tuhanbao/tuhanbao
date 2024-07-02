package com.td.ca.web.db.dynamic.constant.config;

import com.td.ca.base.util.config.Config;
import com.td.ca.base.util.config.ConfigManager;
import com.td.ca.base.util.config.ConfigRefreshListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 动态表常量配置
 */
@Slf4j
public class DynamicConstantsConfig implements ConfigRefreshListener {
    public static final String KEY = "dynamic_constants";

    public static final String DYNAMIC_COLUMN_MAX_NUM_NAME = "DYNAMIC_COLUMN_MAX_NUM";

    public static int DYNAMIC_COLUMN_MAX_NUM = 100;

    static {
        ConfigManager.addListener(new DynamicConstantsConfig());
        init();
    }

    private DynamicConstantsConfig() {

    }

    @Override
    public void refresh() {
        init();
    }

    @Override
    public String getKey() {
        return KEY;
    }

    public static void init() {
        Config config = ConfigManager.getConfig(KEY);
        if (config == null) {
            log.warn("no config file for : " + KEY);
            return;
        }

        DYNAMIC_COLUMN_MAX_NUM = config.getInt(DYNAMIC_COLUMN_MAX_NUM_NAME);
    }

    public static void save(String key, String value) {
        ConfigRefreshListener.save(KEY, key, value);
    }

    public static void save(Map<String, String> properties) {
        ConfigRefreshListener.save(KEY, properties);
    }

}