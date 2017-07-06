package com.tuhanbao.thirdapi.sms;

import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;
import com.tuhanbao.base.util.log.LogManager;

public class SmsConfig implements ConfigRefreshListener {
    private static final String KEY = "sms";

    private static final String URL_NAME = "url";

    public static String URL;

    private static final String USERNAME_NAME = "username";

    public static String USERNAME;

    private static final String PASSWORD_NAME = "password";

    public static String PASSWORD;

    private static final String AUTHCODE_DEADLINE_NAME = "authcode_deadline";

    public static int AUTHCODE_DEADLINE;

    private static final String AUTHCODE_GETAGAIN_NAME = "authcode_getagain";

    public static int AUTHCODE_GETAGAIN;

    static {
        ConfigManager.addListener(new SmsConfig());
        init();
    }

    private SmsConfig() {

    }

    public static void init() {
        Config config = ConfigManager.getConfig(KEY);
        if (config == null) {
            LogManager.warn("no config file for : " + KEY);
            return;
        }
        
        URL = config.getString(URL_NAME);
        USERNAME = config.getString(USERNAME_NAME);
        PASSWORD = config.getString(PASSWORD_NAME);
        AUTHCODE_DEADLINE = config.getInt(AUTHCODE_DEADLINE_NAME);
        AUTHCODE_GETAGAIN = config.getInt(AUTHCODE_GETAGAIN_NAME);
    }

    public void refresh() {
        init();
    }

    public String getKey() {
        return KEY;
    }

}