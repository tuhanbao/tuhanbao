package com.hhnz.api.cfcrm.constants;

import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;
import com.tuhanbao.base.util.log.LogManager;

public class AwardConfig implements ConfigRefreshListener {
    public static final String KEY = "award";

    private static final String NONGZHUANG_INTEGRAL_NAME = "NONGZHUANG_INTEGRAL";

    public static String NONGZHUANG_INTEGRAL;

    private static final String INVESTMENT_CERTIFICATE_NAME = "INVESTMENT_CERTIFICATE";

    public static String INVESTMENT_CERTIFICATE;

    private static final String COUPON_NAME = "COUPON";

    public static String COUPON;

    private static final String MALL_COUPON_NAME = "MALL_COUPON";

    public static String MALL_COUPON;

    private static final String FOOD_STAMPS_NAME = "FOOD_STAMPS";

    public static String FOOD_STAMPS;

    static {
        ConfigManager.addListener(new AwardConfig());
        init();
    }

    private AwardConfig() {

    }

    public static void init() {
        Config config = ConfigManager.getConfig(KEY);
        if (config == null) {
            LogManager.warn("no config file for : " + KEY);
            return;
        }

        NONGZHUANG_INTEGRAL = config.getString(NONGZHUANG_INTEGRAL_NAME);
        INVESTMENT_CERTIFICATE = config.getString(INVESTMENT_CERTIFICATE_NAME);
        COUPON = config.getString(COUPON_NAME);
        MALL_COUPON = config.getString(MALL_COUPON_NAME);
        FOOD_STAMPS = config.getString(FOOD_STAMPS_NAME);
    }

    public void refresh() {
        init();
    }

    public String getKey() {
        return KEY;
    }

}