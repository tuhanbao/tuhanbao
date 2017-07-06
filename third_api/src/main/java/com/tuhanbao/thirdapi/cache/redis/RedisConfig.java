package com.tuhanbao.thirdapi.cache.redis;

import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;

import redis.clients.jedis.Protocol;

public final class RedisConfig implements ConfigRefreshListener {
    
    private static final String KEY = "redis";
    
    public static String IP;
    public static int PORT;
    public static int MAX_ACTIVE = -1;
    public static int MAX_IDLE = -1;
    public static int MAX_WAIT = -1;
    public static boolean TEST_ON_BORROW = true;
    public static int TIME_OUT = Protocol.DEFAULT_TIMEOUT;
    public static int DATABASE = Protocol.DEFAULT_DATABASE;
    public static String PASSWORD = null;
    public static String CLIENT_NAME = null;
    

    public static int RESOURCE_NUM = 10;
    public static int OVERDUE_TIME = -1;
    public static int DEFAULT_EXPIRE_TIME = 3600;
    
    private static final String IP_NAME = "ip";
    private static final String PORT_NAME = "port";
    private static final String MAX_ACTIVE_NAME = "max_active";
    private static final String MAX_ILDE_NAME = "max_ilde";
    private static final String MAX_WAIT_NAME = "max_wait";
    private static final String TEST_ON_BORROW_NAME = "test_on_borrow";
    private static final String OVERDUE_TIME_NAME = "overdue_time";
    private static final String RESOURCE_NUM_NAME = "resource_num";
    private static final String TIME_OUT_NAME = "time_out";
    private static final String DATABASE_NAME = "database";
    private static final String PASSWORD_NAME = "password";
    private static final String CLIENT_NAME_NAME = "client_name";
    
    static {
        ConfigManager.addListener(new RedisConfig());
    	init();
    }
    
    private RedisConfig() {
    }
    
    /**
     * 初始化Redis连接池
     */
    private static final void init() {
    	Config config = ConfigManager.getConfig(RedisConfig.KEY);
        if (config == null) {
            LogManager.warn("no config file for : " + KEY);
            return;
        }
    	
    	IP = config.getString(RedisConfig.IP_NAME);
    	PORT = config.getInt(RedisConfig.PORT_NAME);
    	
    	if (config.containsKey(RedisConfig.MAX_ACTIVE_NAME)) {
    		MAX_ACTIVE = config.getInt(RedisConfig.MAX_ACTIVE_NAME);
    	}
    	if (config.containsKey(RedisConfig.MAX_ILDE_NAME)) {
    		MAX_IDLE = config.getInt(RedisConfig.MAX_ILDE_NAME);
    	}
    	if (config.containsKey(RedisConfig.MAX_WAIT_NAME)) {
    		MAX_WAIT = config.getInt(RedisConfig.MAX_WAIT_NAME);
    	}
    	
    	if (config.containsKey(RedisConfig.TEST_ON_BORROW_NAME)) {
    		TEST_ON_BORROW = config.getInt(RedisConfig.TEST_ON_BORROW_NAME) == 1;
    	}

        if (config.containsKey(RedisConfig.TIME_OUT_NAME)) {
            TIME_OUT = config.getInt(RedisConfig.TIME_OUT_NAME);
        }
        if (config.containsKey(RedisConfig.DATABASE_NAME)) {
            DATABASE = config.getInt(RedisConfig.DATABASE_NAME);
        }
        if (config.containsKey(RedisConfig.PASSWORD_NAME)) {
            PASSWORD = config.getString(RedisConfig.PASSWORD_NAME);
        }
        if (config.containsKey(RedisConfig.CLIENT_NAME_NAME)) {
            CLIENT_NAME = config.getString(RedisConfig.CLIENT_NAME_NAME);
        }

    	String overdueTimeStr = config.getString(OVERDUE_TIME_NAME);
    	if (!StringUtil.isEmpty(overdueTimeStr)) {
    		OVERDUE_TIME = Integer.valueOf(overdueTimeStr);
    	}
    	String resourceNumStr = config.getString(RESOURCE_NUM_NAME);
    	if (!StringUtil.isEmpty(resourceNumStr)) {
    		RESOURCE_NUM = Integer.valueOf(resourceNumStr);
    	}
//    	String defaultExpireTimeStr = config.getString(DEFAULT_EXPIRE_TIME_NAME);
//    	if (!StringUtil.isEmpty(defaultExpireTimeStr)) {
//    	    DEFAULT_EXPIRE_TIME = Integer.valueOf(defaultExpireTimeStr);
//    	}
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