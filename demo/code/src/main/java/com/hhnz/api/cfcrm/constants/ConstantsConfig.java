package com.hhnz.api.cfcrm.constants;

import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;
import com.tuhanbao.base.util.log.LogManager;

public class ConstantsConfig implements ConfigRefreshListener {
    public static final String KEY = "constants";

    private static final String SEND_SMS_MAX_NUM_NAME = "SEND_SMS_MAX_NUM";

    public static int SEND_SMS_MAX_NUM;

    private static final String AFFAIR_EXUTE_TIME_NAME = "AFFAIR_EXUTE_TIME";

    public static int AFFAIR_EXUTE_TIME;

    private static final String REMIND_DROP_THRESHOLD_NAME = "REMIND_DROP_THRESHOLD";

    public static int REMIND_DROP_THRESHOLD;

    private static final String SYSTEM_DELAY_NAME = "SYSTEM_DELAY";

    public static int SYSTEM_DELAY;

    private static final String DEFAULT_OFF_DAY_NAME = "DEFAULT_OFF_DAY";

    public static int DEFAULT_OFF_DAY;

    private static final String AUTO_MSG_MAX_NUM_PERDAY_NAME = "AUTO_MSG_MAX_NUM_PERDAY";

    public static int AUTO_MSG_MAX_NUM_PERDAY;

    private static final String MAX_SHOW_PHONE_NUM_NAME = "MAX_SHOW_PHONE_NUM";

    public static int MAX_SHOW_PHONE_NUM;

    private static final String MAX_SHOW_SEND_CONTENT_NAME = "MAX_SHOW_SEND_CONTENT";

    public static int MAX_SHOW_SEND_CONTENT;

    private static final String MAX_MSG_UPDATE_TIME_NAME = "MAX_MSG_UPDATE_TIME";

    public static int MAX_MSG_UPDATE_TIME;

    private static final String MONTH_NAME = "MONTH";

    public static int MONTH;

    private static final String TEST_INTEGRAL_URL_NAME = "TEST_INTEGRAL_URL";

    public static String TEST_INTEGRAL_URL;

    private static final String TEST_COUPON_URL_NAME = "TEST_COUPON_URL";

    public static String TEST_COUPON_URL;

    private static final String MERCHANT_ID_NAME = "MERCHANT_ID";

    public static String MERCHANT_ID;

    private static final String CLIENT_ID_NAME = "CLIENT_ID";

    public static String CLIENT_ID;

    private static final String COUPON_GROUP_ID_NAME = "COUPON_GROUP_ID";

    public static String COUPON_GROUP_ID;

    private static final String SITE_ID_NAME = "SITE_ID";

    public static String SITE_ID;

    private static final String CLIENT_SECRET_NAME = "CLIENT_SECRET";

    public static String CLIENT_SECRET;

    private static final String SECRET_NAME = "SECRET";

    public static String SECRET;

    private static final String ZERO_DAY_NAME = "ZERO_DAY";

    public static int ZERO_DAY;

    private static final String MIN_DAY_NAME = "MIN_DAY";

    public static int MIN_DAY;

    private static final String MEDIUM_DAY_NAME = "MEDIUM_DAY";

    public static int MEDIUM_DAY;

    private static final String MAX_DAY_NAME = "MAX_DAY";

    public static int MAX_DAY;

    private static final String INVEST_RATE_NAME = "INVEST_RATE";

    public static int INVEST_RATE;

    private static final String INVEST_DAY_NAME = "INVEST_DAY";

    public static int INVEST_DAY;

    private static final String DEFAULT_NAME = "DEFAULT";

    public static String DEFAULT;

    private static final String FILE_NAME_NAME = "FILE_NAME";

    public static String FILE_NAME;

    private static final String IS_DEBUG_NAME = "IS_DEBUG";

    public static int IS_DEBUG;

    private static final String CONNECT_TIME_OUT_NAME = "CONNECT_TIME_OUT";

    public static int CONNECT_TIME_OUT;

    private static final String READ_TIME_OUT_NAME = "READ_TIME_OUT";

    public static int READ_TIME_OUT;

    static {
        ConfigManager.addListener(new ConstantsConfig());
        init();
    }

    private ConstantsConfig() {

    }

    public static void init() {
        Config config = ConfigManager.getConfig(KEY);
        if (config == null) {
            LogManager.warn("no config file for : " + KEY);
            return;
        }

        SEND_SMS_MAX_NUM = config.getInt(SEND_SMS_MAX_NUM_NAME);
        AFFAIR_EXUTE_TIME = config.getInt(AFFAIR_EXUTE_TIME_NAME);
        REMIND_DROP_THRESHOLD = config.getInt(REMIND_DROP_THRESHOLD_NAME);
        SYSTEM_DELAY = config.getInt(SYSTEM_DELAY_NAME);
        DEFAULT_OFF_DAY = config.getInt(DEFAULT_OFF_DAY_NAME);
        AUTO_MSG_MAX_NUM_PERDAY = config.getInt(AUTO_MSG_MAX_NUM_PERDAY_NAME);
        MAX_SHOW_PHONE_NUM = config.getInt(MAX_SHOW_PHONE_NUM_NAME);
        MAX_SHOW_SEND_CONTENT = config.getInt(MAX_SHOW_SEND_CONTENT_NAME);
        MAX_MSG_UPDATE_TIME = config.getInt(MAX_MSG_UPDATE_TIME_NAME);
        MONTH = config.getInt(MONTH_NAME);
        TEST_INTEGRAL_URL = config.getString(TEST_INTEGRAL_URL_NAME);
        TEST_COUPON_URL = config.getString(TEST_COUPON_URL_NAME);
        MERCHANT_ID = config.getString(MERCHANT_ID_NAME);
        CLIENT_ID = config.getString(CLIENT_ID_NAME);
        COUPON_GROUP_ID = config.getString(COUPON_GROUP_ID_NAME);
        SITE_ID = config.getString(SITE_ID_NAME);
        CLIENT_SECRET = config.getString(CLIENT_SECRET_NAME);
        SECRET = config.getString(SECRET_NAME);
        ZERO_DAY = config.getInt(ZERO_DAY_NAME);
        MIN_DAY = config.getInt(MIN_DAY_NAME);
        MEDIUM_DAY = config.getInt(MEDIUM_DAY_NAME);
        MAX_DAY = config.getInt(MAX_DAY_NAME);
        INVEST_RATE = config.getInt(INVEST_RATE_NAME);
        INVEST_DAY = config.getInt(INVEST_DAY_NAME);
        DEFAULT = config.getString(DEFAULT_NAME);
        FILE_NAME = config.getString(FILE_NAME_NAME);
        IS_DEBUG = config.getInt(IS_DEBUG_NAME);
        CONNECT_TIME_OUT = config.getInt(CONNECT_TIME_OUT_NAME);
        READ_TIME_OUT = config.getInt(READ_TIME_OUT_NAME);
    }

    public void refresh() {
        init();
    }

    public String getKey() {
        return KEY;
    }

}