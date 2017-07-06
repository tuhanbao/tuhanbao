package com.tuhanbao.thirdapi.pay.weixin;

import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;
import com.tuhanbao.base.util.log.LogManager;

public class WeixinConfig implements ConfigRefreshListener {
    public static final String KEY = "weixin";

    public static String MCH_ID;

    public static String PROXY;

    private static Config config;

    static {
        ConfigManager.addListener(new WeixinConfig());
        init();
    }

    private WeixinConfig() {

    }

    public static String getValue(String key) {
        return config.getString(key);
    }

    public static void init() {
        config = ConfigManager.getConfig(KEY);
        if (config == null) {
            LogManager.warn("no config file for : " + KEY);
            return;
        }
        MCH_ID = config.getString(WeixinConstants.MCH_ID);
        PROXY = config.getString(WeixinConstants.PROXY);
    }

    public void refresh() {
        init();
    }

    public String getKey() {
        return KEY;
    }

}