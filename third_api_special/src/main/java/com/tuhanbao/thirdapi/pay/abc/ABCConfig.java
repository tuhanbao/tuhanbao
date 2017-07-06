package com.tuhanbao.thirdapi.pay.abc;

import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;
import com.tuhanbao.base.util.log.LogManager;

public final class ABCConfig implements ConfigRefreshListener {
    
    private static final String KEY = "abc";

    private static Config config = null;

    //订单有效期，默认为2
    public static int ORDER_TIME_OUT_HOURS = 2;

    private static final String ORDER_TIME_OUT_HOURS_NAME = "OrderTimeOutHours";
    
    private static final Map<String, String> DEFAULT_VALUES = new HashMap<String, String>();

    static {
        // 如果忘记配置，以下属性会有默认值
        DEFAULT_VALUES.put(ABCConstants.PAY_TYPE_ID, "ImmediatePay");
        DEFAULT_VALUES.put(ABCConstants.CURRENCY_CODE, "156");
        DEFAULT_VALUES.put(ABCConstants.INSTALLMENT_MARK, "0");
        DEFAULT_VALUES.put(ABCConstants.PAYMENT_TYPE, "A");
        DEFAULT_VALUES.put(ABCConstants.NOTIFY_TYPE, "1");
        DEFAULT_VALUES.put(ABCConstants.IS_BREAK_ACCOUNT, "0");
        DEFAULT_VALUES.put(ABCConstants.QUERY_DETAIL, "1");
        
        DEFAULT_VALUES.put(ABCConstants.TRUSTPAY_CONNECT_METHOD, "https");
        DEFAULT_VALUES.put(ABCConstants.TRUSTPAY_SERVER_NAME, "pay.abchina.com");
        DEFAULT_VALUES.put(ABCConstants.TRUSTPAY_SERVER_PORT, "443");
        DEFAULT_VALUES.put(ABCConstants.TRUSTPAY_NEWLINE, "2");
        DEFAULT_VALUES.put(ABCConstants.TRUSTPAY_TRXURL, "/ebus/trustpay/ReceiveMerchantTrxReqServlet");
        DEFAULT_VALUES.put(ABCConstants.TRUSTPAY_IETRXURL, "https://pay.abchina.com/ebus/trustpay/ReceiveMerchantIERequestServlet");
        DEFAULT_VALUES.put(ABCConstants.MERCHANT_ERRORURL, "http://www.abc.china/ErrorPage.jsp");
        DEFAULT_VALUES.put(ABCConstants.TRUST_STORE_PASSWORD, "changeit");
        DEFAULT_VALUES.put(ABCConstants.MERCHANT_KEY_STORE_TYPE, "0");
    }
    
    static {
        ConfigManager.addListener(new ABCConfig());
    	init();
    }
    
    private ABCConfig() {
    }
    
    public static String getValue(String key) {
        String value = config.getString(key);
        // 为null证明未配置，为empty时代表是配置了的，所以必须以==null进行判断
        if (value == null) {
            value = getDefaultValue(key);
        }
        return value;
    }

    public static int getIntValue(String key) {
        return Integer.valueOf(getValue(key));
    }
    
    private static String getDefaultValue(String key) {
        return DEFAULT_VALUES.get(key);
    }
    
    /**
     * 初始化Redis连接池
     */
    private static final void init() {
    	config = ConfigManager.getConfig(ABCConfig.KEY);
    	if (config == null) {
            LogManager.warn("no config file for : " + KEY);
            return;
        }
    	
    	ORDER_TIME_OUT_HOURS = config.getInt(ORDER_TIME_OUT_HOURS_NAME);
    }

    @Override
    public void refresh() {
        init();
    }

    @Override
    public String getKey() {
        return KEY;
    }
}