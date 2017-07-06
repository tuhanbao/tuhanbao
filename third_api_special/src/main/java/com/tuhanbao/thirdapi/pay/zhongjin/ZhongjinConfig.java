package com.tuhanbao.thirdapi.pay.zhongjin;

import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;
import com.tuhanbao.base.util.log.LogManager;

public class ZhongjinConfig implements ConfigRefreshListener {
    public static final String KEY = "zhongjin";

    private static final String SYSTEM_CONFIG_PATH_NAME = "system_config_path";

    public static String SYSTEM_CONFIG_PATH;

    private static final String PAYMENT_CONFIG_PATH_NAME = "payment_config_path";

    public static String PAYMENT_CONFIG_PATH;

    private static final String INSTITUTION_ID_NAME = "institution_id";

    public static String INSTITUTION_ID;

    static {
        ConfigManager.addListener(new ZhongjinConfig());
        init();
    }

    private ZhongjinConfig() {

    }

    public static void init() {
        Config config = ConfigManager.getConfig(KEY);
        if (config == null) {
            LogManager.warn("no config file for : " + KEY);
            return;
        }
        SYSTEM_CONFIG_PATH = config.getString(SYSTEM_CONFIG_PATH_NAME);
        PAYMENT_CONFIG_PATH = config.getString(PAYMENT_CONFIG_PATH_NAME);
        INSTITUTION_ID = config.getString(INSTITUTION_ID_NAME);
    }

    public void refresh() {
        init();
    }

    public String getKey() {
        return KEY;
    }

}