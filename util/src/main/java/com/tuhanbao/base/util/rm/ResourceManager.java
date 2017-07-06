package com.tuhanbao.base.util.rm;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.config.Config;
import com.tuhanbao.base.util.config.ConfigManager;
import com.tuhanbao.base.util.config.ConfigRefreshListener;
import com.tuhanbao.base.util.exception.BaseErrorCode;
import com.tuhanbao.base.util.exception.MyException;

public class ResourceManager implements ConfigRefreshListener {
    private static final String KEY = "language";
    
    private static Config config = null;
    
    public static String getResource(String key, String ... args) {
        if (config == null) init();
        String msg = config.getString(key);
        
        return replaceArgs(msg, args);
    }
    
    public static String getResource(Enum<?> item) {
        if (config == null) init();
        String key = item.getClass().getSimpleName().toUpperCase() + Constants.UNDER_LINE + item.name();
        return config.getString(key);
    }

    public static String replaceArgs(String msg, String... args) {
        if (args == null) return msg;
        int length = args.length;
        for (int i = 0; i < length; i++) {
            msg = msg.replace("{" + i + "}", args[i]);
        }
        return msg;
    }
    
    private static final void init() {
        config = ConfigManager.getConfig(KEY);
        if (config == null) {
            throw new MyException(BaseErrorCode.INIT_CONFIGFILE_ERROR, KEY);
        }
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
