package com.tuhanbao.thirdapi.ftp;

import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;
import com.tuhanbao.base.util.log.LogManager;

public class FtpConfig implements ConfigRefreshListener {
    public static final String KEY = "ftp";

    public static final String HOSTNAME_NAME = "hostname";

    public static String HOSTNAME;

    public static final String FTPPORT_NAME = "ftpport";

    public static int FTPPORT;

    public static final String USERNAME_NAME = "username";

    public static String USERNAME;

    public static final String PASSWORD_NAME = "password";

    public static String PASSWORD;

    public static final String ROOT_PATH_NAME = "root_path";

    public static String ROOT_PATH;

    public static final String SHOW_PATH_NAME = "show_path";

    public static String SHOW_PATH;

    public static final String MIN_FTP_SIZE_NAME = "min_ftp_size";

    public static int MIN_FTP_SIZE;

    public static final String MAX_FTP_SIZE_NAME = "max_ftp_size";

    public static int MAX_FTP_SIZE;

    static {
        ConfigManager.addListener(new FtpConfig());
        init();
    }

    private FtpConfig() {

    }

    public static void init() {
        Config config = ConfigManager.getConfig(KEY);
        if (config == null) {
            LogManager.warn("no config file for : " + KEY);
            return;
        }
        HOSTNAME = config.getString(HOSTNAME_NAME);
        FTPPORT = config.getInt(FTPPORT_NAME);
        USERNAME = config.getString(USERNAME_NAME);
        PASSWORD = config.getString(PASSWORD_NAME);
        ROOT_PATH = config.getString(ROOT_PATH_NAME);
        SHOW_PATH = config.getString(SHOW_PATH_NAME);
        MIN_FTP_SIZE = config.getInt(MIN_FTP_SIZE_NAME);
        MAX_FTP_SIZE = config.getInt(MAX_FTP_SIZE_NAME);
    }

    public void refresh() {
        init();
    }

    public String getKey() {
        return KEY;
    }
    
    public static String getStringValue(String key) {
        return ConfigManager.getConfig(KEY).getString(key);
    }

    public static int getIntValue(String key) {
        return ConfigManager.getConfig(KEY).getInt(key);
    }

}