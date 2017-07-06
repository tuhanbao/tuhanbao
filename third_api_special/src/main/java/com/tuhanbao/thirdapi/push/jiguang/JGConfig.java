package com.tuhanbao.thirdapi.push.jiguang;

import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;
import com.tuhanbao.base.util.log.LogManager;

public final class JGConfig implements ConfigRefreshListener {
    public static final String KEY = "jiguang";

    public static int MAX_TRY_TIME = 3;
    public static String APP_KEY;
    public static String SECRET;

    private static String APP_KEY_STR = "appKey";
    private static String SECRET_STR = "secret";
    private static String MAX_TRY_TIME_STR = "maxTryTime";

    static {
        ConfigManager.addListener(new JGConfig());
        init();
    }

    private JGConfig() {

    }

    public static void init() {
        Config config = ConfigManager.getConfig(KEY);
        if (config == null) {
            LogManager.warn("no config file for : " + KEY);
            return;
        }
        
        APP_KEY = config.getString(APP_KEY_STR);
        SECRET = config.getString(SECRET_STR);
        MAX_TRY_TIME = config.getInt(MAX_TRY_TIME_STR);
    }

    public void refresh() {
        init();
    }

    public String getKey() {
        return KEY;
    }

}